package wbkj.sjapp.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MainActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.User;
import wbkj.sjapp.models.User_qq;
import wbkj.sjapp.models.User_wx;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;
import wbkj.sjapp.views.CircleImageView;

public class BindPhoneActivity extends BaseActivity {

    private Toolbar toolbar;
    private CircleImageView imgUser;
    private TextView tvNname, tvSend;
    private EditText etPhone, etCode;
    private Button btnBind;

    private SharedPreferencesUtil sp;
    private User_wx wxUser;
    private User_qq qqUser;
    String tag, thirdaccount, phone, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        tag = getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getString("tag");
        if (StringUtils.isNotBlank(tag) && tag.equals("qq")) {
            qqUser = (User_qq) getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getSerializable("user");
            thirdaccount = getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getString("thirdaccount");
        } else {
            wxUser = (User_wx) getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getSerializable("user");
            Log.e("~~~~~~~~",wxUser.getNickname());
        }
        setContentView(R.layout.activity_bind_phone);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgUser = (CircleImageView) findViewById(R.id.bind_img_user);
        tvNname = (TextView) findViewById(R.id.bind_tv_nickname);
        etPhone = (EditText) findViewById(R.id.et_phone);
        tvSend = (TextView) findViewById(R.id.tv_send);
        etCode = (EditText) findViewById(R.id.et_code);
        btnBind = (Button) findViewById(R.id.btn_bind);

        if (tag.equals("qq")) {
            Picasso.with(this).load(qqUser.getFigureurl_qq_2()).into(imgUser);
            tvNname.setText(qqUser.getNickname());
        } else {
            Picasso.with(this).load(wxUser.getHeadimgurl()).into(imgUser);
            tvNname.setText(wxUser.getNickname());
        }
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = etPhone.getEditableText().toString().trim();
                if (StringUtils.isNotBlank(phone)) {
                    createLoadingDialog(BindPhoneActivity.this, "发送中...").show();
                    SMSSDK.getVerificationCode("86", phone);
                } else {
                    Toast.makeText(BindPhoneActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = etCode.getEditableText().toString().trim();
                if (StringUtils.isNotBlank(code)) {
//                    SMSSDK.submitVerificationCode("86", phone, code);
                    regMethod();
                } else {
                    Toast.makeText(BindPhoneActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    private void regMethod() {
        phone = etPhone.getEditableText().toString();
        if (!StringUtils.isNotBlank(phone)) {
            Toast.makeText(BindPhoneActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClientManager.Param[] params;
        if (tag.equals("qq")) {
            int sex;
            if (qqUser.getGender().endsWith("男")) {
                sex = 0;
            } else {
                sex = 1;
            }
            params = new OkHttpClientManager.Param[]{
                    new OkHttpClientManager.Param("thirdfrom", "2"),
                    new OkHttpClientManager.Param("phone", phone),
                    new OkHttpClientManager.Param("thirdaccount", thirdaccount),
                    new OkHttpClientManager.Param("nickname", qqUser.getNickname()),
                    new OkHttpClientManager.Param("img", qqUser.getFigureurl_qq_2()),
                    new OkHttpClientManager.Param("sex", String.valueOf(sex))
            };
        } else {
            params = new OkHttpClientManager.Param[]{
				    new OkHttpClientManager.Param("thirdfrom", "1"),
                    new OkHttpClientManager.Param("phone", phone),
				    new OkHttpClientManager.Param("thirdaccount", wxUser.getOpenid()),
				    new OkHttpClientManager.Param("nickname", wxUser.getNickname()),
				    new OkHttpClientManager.Param("img", wxUser.getHeadimgurl()),
				    new OkHttpClientManager.Param("sex", String.valueOf(wxUser.getSex()))
		};
        }
        OkHttpClientManager.postAsyn(HttpUtil.regUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(getApplication(), "网络连接失败,请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    Toast.makeText(getApplication(), "绑定成功", Toast.LENGTH_SHORT).show();
                    User user = json.data;
                    user.setPassword("");
                    sp.onLoginSuccess(user);
                    skipActivity(BindPhoneActivity.this, MainActivity.class, null);
                } else {
                    Toast.makeText(BindPhoneActivity.this, json.msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    private void initToast(int type, int event) {

        closeProDialog();
        if (type == SMSSDK.RESULT_COMPLETE) {
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                final MyCount mc = new MyCount(Contants.SEND_CODE_TIME, Contants.SEND_CODE_MIN_TIME);
                mc.start();
                Toast.makeText(this, "短信验证码发送成功", Toast.LENGTH_SHORT).show();
            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                regMethod();
            }
        } else {
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                Toast.makeText(this, "短信验证码发送失败,请重新发送", Toast.LENGTH_SHORT).show();
            } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                Toast.makeText(this, "验证码错误,请重新验证", Toast.LENGTH_SHORT).show();
            }
        }
    }
    EventHandler eh = new EventHandler(){

        @Override
        public void afterEvent(final int event, final int result, final Object data) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        initToast(SMSSDK.RESULT_COMPLETE, event);
//                //回调完成
//                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                    //提交验证码成功
//
//                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
//                    //获取验证码成功
//                }
                    }else{
                        ((Throwable)data).printStackTrace();
                        initToast(SMSSDK.RESULT_ERROR, event);
//                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                    //提交验证码失败
//
//                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
//                    //获取验证码失败
//                }
                    }
                }
            });
        }
    };

    /*	定义倒计时内部类	*/
    class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            tvSend.setText("重新发送");
            tvSend.setTextColor(getResources().getColor(R.color.white));
            tvSend.setClickable(true);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            tvSend.setText("重新发送" + millisUntilFinished / 1000);
            tvSend.setTextColor(getResources().getColor(R.color.white));
            tvSend.setClickable(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SMSSDK.registerEventHandler(eh);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SMSSDK.unregisterEventHandler(eh);
    }


}
