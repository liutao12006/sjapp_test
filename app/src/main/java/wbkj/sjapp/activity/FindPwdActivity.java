package wbkj.sjapp.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class FindPwdActivity extends BaseActivity {

    private Toolbar toolbar;
    private EditText etPhone, etCode, etPwd, etRpwd;
    private Button btnOk, btnSend;

    String phone, pwd, code, rpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etPhone = (EditText) findViewById(R.id.find_et_phone);
        etCode = (EditText) findViewById(R.id.find_et_code);
        etPwd = (EditText) findViewById(R.id.find_et_pwd);
        etRpwd = (EditText) findViewById(R.id.find_et_rpwd);
        btnOk = (Button) findViewById(R.id.find_btn_ok);
        btnSend = (Button) findViewById(R.id.find_btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = etPhone.getEditableText().toString().trim();
                if (StringUtils.isNotBlank(phone)) {
                    SMSSDK.getVerificationCode("86", phone);
                } else {
                    Toast.makeText(FindPwdActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                code = etCode.getEditableText().toString().trim();
                pwd = etPwd.getEditableText().toString().trim();
                rpwd = etRpwd.getEditableText().toString().trim();
                if (StringUtils.isNotBlank(pwd, rpwd)) {
                    if (!pwd.endsWith(rpwd)) {
                        Toast.makeText(FindPwdActivity.this, "两次输入密码不一致,请重新输入", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(FindPwdActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtils.isNotBlank(code)) {
//                    SMSSDK.submitVerificationCode("86", phone, code);
                    regMethod();
                } else {
                    Toast.makeText(FindPwdActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void regMethod() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("username", phone),
                new OkHttpClientManager.Param("password", pwd)
        };
        OkHttpClientManager.postAsyn(HttpUtil.findPwdUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.e("err-------", e.getMessage());
                Toast.makeText(FindPwdActivity.this, "网络连接失败,请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                Log.e("------", response.toString());
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    Toast.makeText(FindPwdActivity.this, "密码重置成功，请登录", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FindPwdActivity.this, json.msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    private void initToast(int type, int event) {

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

    /**
     * mob短信的事件接收器
     */
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
                        Log.e("err----", data.toString());
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
            btnSend.setText("重新发送");
            btnSend.setTextColor(getResources().getColor(R.color.white));
            btnSend.setClickable(true);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            btnSend.setText("重新发送" + millisUntilFinished / 1000);
            btnSend.setTextColor(getResources().getColor(R.color.white));
            btnSend.setClickable(false);
        }
    }
}
