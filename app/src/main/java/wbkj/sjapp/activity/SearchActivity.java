package wbkj.sjapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.fragment.IndexFragment;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Product;
import wbkj.sjapp.models.Shop;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class SearchActivity extends BaseActivity {

    private Toolbar toolbar;

    private EditText etSearch;
    private ListView hisLv;
    private TextView tvDelete, tvSearch;
    private ArrayAdapter<String> hisAdapter;
    private List<String> hisList = new ArrayList<>();

    private SharedPreferencesUtil sp;
    private MyApplication app;
    private String hisStr;
    int type, shopId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        app = (MyApplication) getApplication();
        type = getIntent().getIntExtra("tag", 0);
        if (type == 2) {
            shopId = getIntent().getExtras().getInt("shopId");
        }
        setContentView(R.layout.activity_search);

        hisStr = sp.get("history", "");
        if (StringUtils.isNotBlank(hisStr)){
            String his[] = hisStr.split(",");
            for (int i = 0; i < his.length; i++) {
                hisList.add(his[i]);
            }
        }
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etSearch = (EditText) findViewById(R.id.search_et_str);

        hisLv = (ListView) findViewById(R.id.search_his_lv);
        hisLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (type == 1) {
                    searchShops(hisList.get(position));
                } else {
                    searchPros();
                }
            }
        });

        hisAdapter = new ArrayAdapter<>(this, R.layout.textview_history_item, hisList);
        hisLv.setAdapter(hisAdapter);
        tvSearch = (TextView) findViewById(R.id.search_btn);
        tvDelete = (TextView) findViewById(R.id.search_btn_del);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchStr = etSearch.getEditableText().toString().trim();
                if (StringUtils.isNotBlank(searchStr)){
                    tvDelete.setVisibility(View.VISIBLE);
                    if(StringUtils.isNotBlank(hisStr)){
                        if (!hisList.contains(searchStr)) {
                            hisList.add(searchStr);
                            StringBuffer hisSB = new StringBuffer(hisStr);
                            hisSB.append("," + searchStr);
                            sp.put("history", hisSB.toString());
                        }
                        hisAdapter.notifyDataSetChanged();
                    }else{
                        hisList.add(searchStr);
                        hisAdapter.notifyDataSetChanged();
                        sp.put("history", searchStr);
                    }
                    if (type == 1) {
                        searchShops(searchStr);
                    } else {
                        searchPros();
                    }
                }else{
                    Toast.makeText(SearchActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (hisList.size() > 0) tvDelete.setVisibility(View.VISIBLE);
        tvDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sp.delete();
                hisList.clear();
                if(hisAdapter != null){
                    hisAdapter.notifyDataSetChanged();
                }
                tvDelete.setVisibility(View.GONE);
            }
        });
    }

    private void searchShops(String str) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("name", str),
                new OkHttpClientManager.Param("x", String.valueOf(IndexFragment.Longitude)),
                new OkHttpClientManager.Param("y", String.valueOf(IndexFragment.Latitude))
//                new OkHttpClientManager.Param("x", String.valueOf(app.getaMapLocation().getLongitude())),
//                new OkHttpClientManager.Param("y", String.valueOf(app.getaMapLocation().getLatitude()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.searchShops, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(SearchActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                Logger.e(response.toString());
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Shop>>(){}.getType());
                if (json.result == 1) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) json.data);
                    intent.putExtras(bundle);
                    setResult(1, intent);
                    finish();
                }
            }
        }, null);
    }

    private void searchPros() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("name", etSearch.getEditableText().toString().trim()),
                new OkHttpClientManager.Param("shopid", String.valueOf(shopId))
        };
        OkHttpClientManager.postAsyn(HttpUtil.searchPros, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(SearchActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
//                Logger.e(response.toString());
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Product>>(){}.getType());
                if (json.result == 1) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) json.data);
                    intent.putExtras(bundle);
                    setResult(1, intent);
                    finish();
                }
            }
        }, null);
    }

}
