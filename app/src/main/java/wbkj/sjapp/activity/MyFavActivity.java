package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import wbkj.sjapp.adapter.FavMsgRecyAdatper;
import wbkj.sjapp.adapter.FavProRecyAdatper;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Message;
import wbkj.sjapp.models.Product;
import wbkj.sjapp.utils.DividerItemDecoration;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;

public class MyFavActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView favRecy;
    private FavProRecyAdatper proAdatper;
    private FavMsgRecyAdatper msgAdapter;
    private List<Product> proList = new ArrayList<>();
    private List<Message> msgList = new ArrayList<>();
    private RadioGroup favRg;
    private RadioButton rbPro, rbMsg;

    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_my_fav);
        initView();
        createLoadingDialog(this, "加载中...").show();
        getDataMethod(1);
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

        favRg = (RadioGroup) findViewById(R.id.fav_rg);
        rbPro = (RadioButton) findViewById(R.id.fav_rb_pro);
        rbMsg = (RadioButton) findViewById(R.id.fav_rb_msg);
        favRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.fav_rb_pro:
                        getDataMethod(1);
                        break;
                    case R.id.fav_rb_msg:
                        getDataMethod(2);
                        break;
                    default: break;
                }
            }
        });

        favRecy = (RecyclerView) findViewById(R.id.fav_recy);
        favRecy.setHasFixedSize(true);
        favRecy.setLayoutManager(new LinearLayoutManager(this));
        favRecy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void getDataMethod(final int type) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("cltype", String.valueOf(type))
        };
        OkHttpClientManager.postAsyn(HttpUtil.favUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(MyFavActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                JsonModel2 json;
                if (type == 1) {
                    json = new Gson().fromJson(response, new TypeToken<JsonModel2<Product>>(){}.getType());
                    if (json.result == 1) {
                        proList.clear();
                        proList.addAll(json.data);
                        proAdatper = new FavProRecyAdatper(proList, MyFavActivity.this);
                        favRecy.setAdapter(proAdatper);
                    }
                } else {
                    json = new Gson().fromJson(response, new TypeToken<JsonModel2<Message>>(){}.getType());
                    if (json.result == 1) {
                        msgList.clear();
                        msgList.addAll(json.data);
                        msgAdapter = new FavMsgRecyAdatper(msgList, MyFavActivity.this);
                        favRecy.setAdapter(msgAdapter);
                    }
                }

            }
        }, null);
    }

}
