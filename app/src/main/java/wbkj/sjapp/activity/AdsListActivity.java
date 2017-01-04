package wbkj.sjapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.adapter.AdsRecyAdatper;
import wbkj.sjapp.models.Address;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.utils.DividerItemDecoration;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class AdsListActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView adsRecy;
    private AdsRecyAdatper adapter;
    private List<Address> adsList = new ArrayList<>();

    private ImageButton addBtn;

    private SharedPreferencesUtil sp;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        tag = getIntent().getStringExtra("tag");
        setContentView(R.layout.activity_ads_list);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataMethod();
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

        adsRecy = (RecyclerView) findViewById(R.id.recy_recy);
        adsRecy.setHasFixedSize(true);
        adsRecy.setLayoutManager(new LinearLayoutManager(this));
        adsRecy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        addBtn = (ImageButton) findViewById(R.id.ads_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("tag", "add");
                skipActivity(AdsListActivity.this, AdsActivity.class, bundle);
            }
        });
    }

    private void getDataMethod() {
        adsList.clear();
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.adsUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(AdsListActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Address>>(){}.getType());
                if (json.result == 1) {
                    adsList.addAll(json.data);
                    if (adapter == null) {
                        adapter = new AdsRecyAdatper(adsList, AdsListActivity.this);
                        adsRecy.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    adapter.setOnItemClickListener(new MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (StringUtils.isNotBlank(tag) && tag.equals("order")) {
                                Intent intent = new Intent();
                                intent.putExtra("ads", adsList.get(position));
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putString("tag", "modify");
                                bundle.putSerializable("ads", adsList.get(position));
                                skipActivity(AdsListActivity.this, AdsActivity.class, bundle);
                            }
                        }
                    });
                }
            }
        }, null);
    }

}
