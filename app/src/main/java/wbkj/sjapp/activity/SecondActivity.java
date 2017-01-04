package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LoggingMXBean;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.adapter.ShangjieProtAdapter;
import wbkj.sjapp.adapter.SproRecyAdatper;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Product;
import wbkj.sjapp.utils.DividerItemDecoration;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;
import wbkj.sjapp.utils.OkHttpClientManager;

public class SecondActivity extends BaseActivity implements MyItemClickListener {

    private Toolbar toolbar;
    private ImageButton imgBtn;
//    private RecyclerView recy;
    private List<Product> products = new ArrayList<>();
    private SproRecyAdatper adapter;


    private ShangjieProtAdapter s_protAdapter;
    private RecyclerView port_recy;
    private int[] images = {R.drawable.shangjie_tianqi,R.drawable.shangjie_xinzuo,R.drawable.shangjie_kuaidi,R.drawable.shangjie_weizhang,
            R.drawable.shangjie_shucheng, R.drawable.shangjie_gongjiao,R.drawable.shangjie_congzhi, R.drawable.shangjie_suanming};

    private String[] urls={
            "http://m.weather.com.cn/mweather/101180108.shtml",
            "http://m.12xingzuo.com.cn/?f=sm",
            "http://wap.guoguo-app.com/?togoList=1",
            "http://m.weizhang8.cn/",
            "http://book.uc.cn/wap?tl=ucbs_index&from=read_jingpin_ad&cm=M3080048&uc_param_str=dnfrvecppfnteisipr",
            "http://zuoche.com/touch/",
            "http://h5.m.taobao.com/app/cz/cost.html?clientSource=uccz_1",
            "http://m.lnka.cn/"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getDataMethod();
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
        imgBtn = (ImageButton) findViewById(R.id.edit_pub);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipActivity(SecondActivity.this, EditSProActivity.class, null);
            }
        });


        port_recy = (RecyclerView) findViewById(R.id.port_recy);
        port_recy.setHasFixedSize(true);
        port_recy.setLayoutManager(new GridLayoutManager(this,4));
//        port_recy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        s_protAdapter = new ShangjieProtAdapter(images,this,this);

        port_recy.setAdapter(s_protAdapter);


//        recy = (RecyclerView) findViewById(R.id.spro_recy);
//        recy.setHasFixedSize(true);
//        recy.setLayoutManager(new LinearLayoutManager(this));
//        recy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void getDataMethod() {
        products.clear();
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("Wsubclass", "0"),
                new OkHttpClientManager.Param("smallclass", "8861")
        };
        OkHttpClientManager.postAsyn(HttpUtil.sProUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(SecondActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Product>>(){}.getType());
                if (json.result == 1) {
                    products.addAll(json.data);
                    if (adapter == null) {
                        adapter = new SproRecyAdatper(products, SecondActivity.this);
//                        recy.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }, null);
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("url", urls[position]);
        skipActivity(SecondActivity.this, WebviewActivity.class, bundle);
    }

    public  void  onClick(View view){
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case  R.id.port_car:
                bundle.putString("url", "http://sempage.guazi.com/zz/sell?ca_s=dh_ucllq&ca_n=ucllq2&platform=2&scode=10104039222");
                skipActivity(SecondActivity.this, WebviewActivity.class, bundle);
              break;

            case  R.id.port_house:
                bundle.putString("url", "http://m.fang.com/main.d?m=index&city=default&sf_source=ucbrowser01");
                skipActivity(SecondActivity.this, WebviewActivity.class, bundle);
                break;

            case  R.id.port_xiaohua:
                bundle.putString("url", "http://www.qiushibaike.com/");
                skipActivity(SecondActivity.this, WebviewActivity.class, bundle);
                break;

            case  R.id.port_search:
                bundle.putString("url", "https://m.baidu.com/?from=2001a");
                skipActivity(SecondActivity.this, WebviewActivity.class, bundle);
                break;

            case  R.id.shangjie_newscenter:
                bundle.putString("url", "http://sports.sina.cn/?wm=4007_9001");
                skipActivity(SecondActivity.this, WebviewActivity.class, bundle);
                break;
        }
    }
}
