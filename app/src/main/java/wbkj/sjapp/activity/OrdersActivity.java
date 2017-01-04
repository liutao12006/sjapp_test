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
import wbkj.sjapp.adapter.OrderRecyAdatper;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Orders;
import wbkj.sjapp.utils.DividerItemDecoration;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;
import wbkj.sjapp.utils.OkHttpClientManager;
import android.view.ViewGroup.LayoutParams;

public class OrdersActivity extends BaseActivity {

    private Toolbar toolbar;
    private RadioButton rbCur, rbHis;
    private RecyclerView recy;
    private OrderRecyAdatper adapter;
    private List<Orders> lists = new ArrayList<>();
    private RadioGroup rgbType;

    private SharedPreferencesUtil sp;
    int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_orders);
        initView();
        getDataMethod(type);
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

        rgbType = (RadioGroup) findViewById(R.id.order_rg_type);
        rbCur = (RadioButton) findViewById(R.id.order_rb_cur);
        rbHis = (RadioButton) findViewById(R.id.order_rb_his);
        recy = (RecyclerView) findViewById(R.id.order_recy);
        rbCur.setSelected(true);
        recy.setHasFixedSize(true);
        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        rgbType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.order_rb_cur:
                        type = 1;
                        if(rbHis.isSelected()){
                            rbHis.setSelected(false);
                        }
                        rbCur.setSelected(true);
                        getDataMethod(type);
                        break;
                    case R.id.order_rb_his:
                        type = 2;
                        if(rbCur.isSelected()){
                            rbCur.setSelected(false);
                        }
                        rbHis.setSelected(true);
                        getDataMethod(type);
                        break;
                    default: break;
                }
            }
        });
    }

    private void getDataMethod(int type) {
        lists.clear();
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("state", String.valueOf(type))
        };
        OkHttpClientManager.postAsyn(HttpUtil.orderUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(OrdersActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Orders>>(){}.getType());
                if (json.result == 1) {
                    lists.addAll(json.data);
                    if (adapter == null) {
                        adapter = new OrderRecyAdatper(lists, OrdersActivity.this);
                        recy.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                    adapter.setOnItemClickListener(new MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {

                        }
                    });
                }else if(json.result == 2){
                    //这里好像不符合逻辑  但在手机端没有问题 ？？？？
                    if (adapter == null) {
                        adapter = new OrderRecyAdatper(lists, OrdersActivity.this);
                        recy.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }, null);
    }


}
