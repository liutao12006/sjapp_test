package wbkj.sjapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class ModifySexActivity extends BaseActivity {

    private Toolbar toolbar;
    private ListView sexLv;

    private static final String[] sexs = {"女","男"};
    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_modify_sex);
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

        sexLv = (ListView) findViewById(R.id.minfo_sex_list);
        sexLv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sexs));
        sexLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateSexMethod(position);
            }
        });
    }

    private void updateSexMethod(final int sex){
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("sex", String.valueOf(sex))
        };
        OkHttpClientManager.postAsyn(HttpUtil.modSexUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ModifySexActivity.this, "网络链接失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    User user = sp.getUser();
                    user.setSex(sex);
                    sp.onLoginSuccess(user);
                    Toast.makeText(ModifySexActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("sex", sexs[sex]);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(ModifySexActivity.this, "修改失败，请重新修改", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
