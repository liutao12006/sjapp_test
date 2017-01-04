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
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class FeedbackActivity extends BaseActivity {

    private Toolbar toolbar;
    private EditText etText;
    private Button btnOk;

    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_feedback);
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
        etText = (EditText) findViewById(R.id.et_feedback);
        btnOk = (Button) findViewById(R.id.btn_feedback);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etText.getEditableText().toString();
                if (StringUtils.isNotBlank(str)) {
                    addMethod(str);
                } else {
                    Toast.makeText(FeedbackActivity.this, "提交内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void addMethod(String content) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("ctype", "0"),
                new OkHttpClientManager.Param("content", content)
        };
        OkHttpClientManager.postAsyn(HttpUtil.feedbackUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(FeedbackActivity.this, "网络请求失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1){
                    Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FeedbackActivity.this, "提交失败,请重新提交", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

}
