package wbkj.sjapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MainActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.User;
import wbkj.sjapp.models.User_qq;
import wbkj.sjapp.pay.WixPay;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class LoginActivity extends BaseActivity {

    private TextView tvFind, tvReg;
    private EditText etAccount, etPwd;
    private Button btnOk;
    private ImageButton btnQq, btnWx;

    private String username, password;
    private SharedPreferencesUtil sp;

    private Tencent mTencent;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_login);
        if (sp.isLogin()) {
            skipActivity(this, MainActivity.class, null);
        } else {
            initView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.handleResultData(data, listener);
        }
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            username = data.getStringExtra("username");
            etAccount.setText(username);
            password = data.getStringExtra("password");
            etPwd.setText(password);
        }
    }

    private void initView() {
        etAccount = (EditText) findViewById(R.id.login_et_account);
        etPwd = (EditText) findViewById(R.id.login_et_pwd);
        tvFind = (TextView) findViewById(R.id.tv_findpwd);
        btnOk = (Button) findViewById(R.id.login_btn_ok);
        btnQq = (ImageButton) findViewById(R.id.login_by_qq);
        btnWx = (ImageButton) findViewById(R.id.login_by_wx);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etAccount.getEditableText().toString().trim();
                password = etPwd.getEditableText().toString().trim();
                if (StringUtils.isNotBlank(username, password)) {
                    loginMethod();
                } else {
                    Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipActivity(LoginActivity.this, FindPwdActivity.class, null);
            }
        });
        tvReg = (TextView) findViewById(R.id.tv_reg);
        tvReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        btnQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        btnWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WixPay wixPay = new WixPay(LoginActivity.this);
                wixPay.wxLogin();
            }
        });
    }

    private void loginMethod() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("thirdfrom", "3"),
                new OkHttpClientManager.Param("username", username),
                new OkHttpClientManager.Param("password", password)
        };
        OkHttpClientManager.postAsyn(HttpUtil.loginUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(LoginActivity.this, "网络请求失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result != 1) {
                    Toast.makeText(LoginActivity.this, "登录失败,"+json.msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    User user = json.data;
                    user.setPassword(password);
                    sp.onLoginSuccess(user);
                    skipActivity(LoginActivity.this, MainActivity.class, null);
                }
            }
        }, null);

    }

    /**
     * qq登陆
     */
    private void login() {
        mTencent = Tencent.createInstance(Contants.APPID_QQ, this);
        mTencent.login(this, "all", listener);
    }

    IUiListener listener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            JSONObject jsonObject = (JSONObject) o;
            Logger.e(jsonObject.toString());
            try {
                mTencent.setOpenId(jsonObject.getString("openid"));
                mTencent.setAccessToken(jsonObject.getString("access_token"), jsonObject.getString("expires_in"));
                getQqInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "登陆授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {

        }
    };

    private void getQqInfo() {
        userInfo = new UserInfo(this, mTencent.getQQToken());
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Logger.e(o.toString());
                User_qq user = new Gson().fromJson(o.toString(), User_qq.class);
                checkAccount(user);
            }

            @Override
            public void onError(UiError uiError) {
                Logger.e(uiError.toString());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "取消登陆", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 验证账号是否已被注册
     */
    private void checkAccount(final User_qq user) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("thirdfrom", "2"),
                new OkHttpClientManager.Param("thirdaccount", mTencent.getOpenId())
        };
        OkHttpClientManager.postAsyn(HttpUtil.loginUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(LoginActivity.this, "网络请求失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result != 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString("tag", "qq");
                    bundle.putString("thirdaccount", mTencent.getOpenId());
                    bundle.putSerializable("user", user);
                    skipActivity(LoginActivity.this, BindPhoneActivity.class, bundle);
                } else {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    User user = json.data;
                    user.setPassword("");
                    sp.onLoginSuccess(user);
                    skipActivity(LoginActivity.this, MainActivity.class, null);
                }
            }
        }, null);
    }


    @Override
    protected void onDestroy() {
        if (mTencent != null) {
            mTencent.logout(this);
        }
        super.onDestroy();
    }

}
