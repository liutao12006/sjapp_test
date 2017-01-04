package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class ModifyPwdActivity extends BaseActivity {

    private Toolbar toolbar;
    private EditText etNewpwd, etOldpwd, etAnewpwd;
    private Button btnUpdate;

    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        sp = new SharedPreferencesUtil(this);
        initView();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etOldpwd = (EditText) findViewById(R.id.modify_et_oldpwd);
        etNewpwd = (EditText) findViewById(R.id.modify_et_newpwd);
        etAnewpwd = (EditText) findViewById(R.id.modify_et_anewpwd);

        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePwd();
            }
        });
    }

    private void validatePwd(){
        String oldPwd = etOldpwd.getEditableText().toString();
        String newPwd = etNewpwd.getEditableText().toString();
        String anewPwd = etAnewpwd.getEditableText().toString();

        if (StringUtils.isNotBlank(oldPwd) && StringUtils.isNotBlank(newPwd) && StringUtils.isNotBlank(anewPwd)){
            if (newPwd.length() >= 6){
                if (newPwd.equals(anewPwd)){
                    updataPwdMethod(newPwd);
                }else {
                    Toast.makeText(ModifyPwdActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                }
            }else {
                 Toast.makeText(ModifyPwdActivity.this, "新密码长度不够", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(ModifyPwdActivity.this, "旧密码或新密码不能为空", Toast.LENGTH_SHORT).show();
        };
    }

    private void updataPwdMethod(String newPwd){

        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("oldword", sp.getUser().getPassword()),
                new OkHttpClientManager.Param("newword", newPwd)
        };

        OkHttpClientManager.postAsyn(HttpUtil.modPwdUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ModifyPwdActivity.this, "网络链接失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {

                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1){
                    sp.logout();
                    skipActivity(ModifyPwdActivity.this, LoginActivity.class, null);
                }else {
                    Toast.makeText(ModifyPwdActivity.this, json.msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

}
