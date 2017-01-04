package wbkj.sjapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.User;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class ModifyNnameActivity extends BaseActivity {

    private Toolbar toolbar;
    private EditText etName;
    private Button btnOk;

    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_modify_nname);
        initView();
    }

    private void initView() {
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
        etName = (EditText) findViewById(R.id.mod_nick_et);
        btnOk = (Button) findViewById(R.id.mod_nick_btn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = etName.getEditableText().toString();
                if (StringUtils.isNotBlank(nick)) {
                    upNickMethod(nick);
                } else {
                    Toast.makeText(ModifyNnameActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void upNickMethod(final String nick) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("nickname", nick)
        };
        OkHttpClientManager.postAsyn(HttpUtil.modNickUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ModifyNnameActivity.this, "网络链接超时，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                Log.e("suc----", response.toString());
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    User user = sp.getUser();
                    user.setNickname(nick);
                    sp.onLoginSuccess(user);
                    Toast.makeText(ModifyNnameActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("nick", nick);
                    setResult(RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(ModifyNnameActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }
}
