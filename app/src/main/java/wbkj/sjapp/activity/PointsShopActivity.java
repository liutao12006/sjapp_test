package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
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
import wbkj.sjapp.adapter.PoProRecyAdatper;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Product;
import wbkj.sjapp.utils.DividerItemDecoration;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;
import wbkj.sjapp.utils.OkHttpClientManager;

public class PointsShopActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView recy;
    private PoProRecyAdatper adapter;
    private List<Product> lists = new ArrayList<>();

    private SharedPreferencesUtil sp;
    private TextView myjf_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_shop);
        sp = new SharedPreferencesUtil(this);
        initView();
        createLoadingDialog(this, "加载中...").show();
        getData();
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

        myjf_tv= (TextView) findViewById(R.id.myjf_tv);
        myjf_tv.setText("我的积分："+sp.getUser().getIntergration());

        recy = (RecyclerView) findViewById(R.id.recy_popro);
        recy.setHasFixedSize(true);
        recy.setLayoutManager(new GridLayoutManager(this, 2));
        recy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void getData() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("Wsubclass", "3810"),
                new OkHttpClientManager.Param("smallclass", "38191")
        };
        OkHttpClientManager.postAsyn(HttpUtil.sProUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(PointsShopActivity.this, "网络连接失败,请重新连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                lists.clear();
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Product>>(){}.getType());
                if (json.result == 1) {
                    lists.addAll(json.data);
                    if (adapter == null) {
                        adapter = new PoProRecyAdatper(lists, PointsShopActivity.this);
                        recy.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    adapter.setOnItemClickListener(new MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("pro", lists.get(position));
                            skipActivity(PointsShopActivity.this, PoProDetailActivity.class, bundle);
                        }
                    });
                }
            }
        }, null);
    }

}
