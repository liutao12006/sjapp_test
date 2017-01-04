package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import wbkj.sjapp.adapter.ProRecyAdatper;
import wbkj.sjapp.adapter.ProRecyTypeAdapter;
import wbkj.sjapp.models.DgjProduct;
import wbkj.sjapp.models.Dmlx;
import wbkj.sjapp.models.DmlxBean;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.JsonModel3;
import wbkj.sjapp.models.Product;
import wbkj.sjapp.models.ShopDataBean;
import wbkj.sjapp.utils.DividerItemDecoration;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;
import wbkj.sjapp.utils.OkHttpClientManager;

public class DiscountProActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private Toolbar toolbar;
    private RecyclerView recy,recy_type;
    private ProRecyAdatper adatper;
    private List<Product> proList = new ArrayList<>();

    private RadioGroup mRadioGroup;

    String[] typeas ={"烟酒","蔬菜" ,"生鲜"  ,"冷藏" ,"粮油","文具","日常用品" ,"食品"};
    String[] typeas_code ={"79701","79702" ,"79702"  ,"79703" ,"79704","79705","79706" ,"79707"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_pro);
        initView();
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

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mRadioGroup.setOnCheckedChangeListener(this);

        recy = (RecyclerView) findViewById(R.id.dis_pro_recy);
        recy.setHasFixedSize(true);
        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
//        getDataMethod("38201");

        getData(797+"");


//        recy_type = (RecyclerView) findViewById(R.id.dis_pro_type);
//        recy_type.setHasFixedSize(true);
//        recy_type.setLayoutManager(new LinearLayoutManager(this));
//        recy_type.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
//        getTypeMethod();
    }

    private void getTypeMethod() {
        ProRecyTypeAdapter typeAdapter = new ProRecyTypeAdapter(typeas,DiscountProActivity.this);
        recy_type.setAdapter(typeAdapter);
        typeAdapter.setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                getDataMethod(typeas_code[position]);
            }
        });
    }

    private void getDataMethod(String smallclass) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("wsubclass", "381"),
                new OkHttpClientManager.Param("smallclass", smallclass)
        };
        OkHttpClientManager.postAsyn(HttpUtil.getDisUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(DiscountProActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {

                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Product>>(){}.getType());
                if (json.result == 1) {
                    if(proList.size()!=0)proList.clear();
                    proList.addAll(json.data);
                    if (adatper == null) {
                        adatper = new ProRecyAdatper(proList, DiscountProActivity.this);
                        recy.setAdapter(adatper);
                    } else {
                        adatper.notifyDataSetChanged();
                    }
                    adatper.setOnItemClickListener(new MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Bundle bundle = new Bundle();
   //                         Log.e("....",proList.get(position).getWare().getImgstore());
                            bundle.putSerializable("pro", proList.get(position));
                            skipActivity(DiscountProActivity.this, ProDetailActivity.class, bundle);
                        }
                    });
                }
            }
        }, null);
    }


    private void getData( String wsubclass) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
//                new OkHttpClientManager.Param("wsubclass", "381"),
                new OkHttpClientManager.Param("wsubclass", wsubclass)
        };
        OkHttpClientManager.postAsyn(HttpUtil.dgjUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(DiscountProActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel3 json = new Gson().fromJson(response, new TypeToken<JsonModel3<Product>>(){}.getType());
                if (json.result == 1) {
                    for(int j=0;j<json.wdetails.size();j++){
                        Product product =((Product)json.wdetails.get(j));
                        proList.add(product);
                    }

                    if (adatper == null) {
                        adatper = new ProRecyAdatper(proList, DiscountProActivity.this);
                        recy.setAdapter(adatper);
                    } else {
                        adatper.notifyDataSetChanged();
                    }

                    adatper.setOnItemClickListener(new MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("pro", proList.get(position));
                            skipActivity(DiscountProActivity.this, ProDetailActivity.class, bundle);
                        }
                    });

                }

            }
        }, null);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.btn_home: // 首页选中
                selectPage(0);
                getDataMethod(typeas_code[0]);
                break;
            case R.id.btn_discover: // 发现选中
                selectPage(1);
                getDataMethod(typeas_code[1]);
                break;
            case R.id.btn_shengxian: // 发现选中
                selectPage(2);
                getDataMethod(typeas_code[2]);
                break;
            case R.id.btn_lengcang: // 发现选中

                selectPage(3);
                getDataMethod(typeas_code[3]);
                break;
            case R.id.btn_liangyou: // 发现选中

                selectPage(4);
                getDataMethod(typeas_code[4]);
                break;
            case R.id.btn_wenju: // 发现选中

                selectPage(5);
                getDataMethod(typeas_code[5]);
                break;
            case R.id.btn_richang: // 发现选中

                selectPage(6);
                getDataMethod(typeas_code[6]);
                break;
            case R.id.btn_foot: // 发现选中

                selectPage(7);
                getDataMethod(typeas_code[7]);
                break;
        }
    }

    /**
     * 选择某页
     * @param position 页面的位置
     */
    private void selectPage(int position) {
        // 将所有的tab的icon变成灰色的
        for (int i = 0; i < mRadioGroup.getChildCount(); i++) {

            RadioButton child = (RadioButton) mRadioGroup.getChildAt(i);

            child.setTextColor(getResources().getColor(
                    R.color.ziti));
        }
//设置选中的字体颜色
        RadioButton select = (RadioButton) mRadioGroup.getChildAt(position);
        select.setTextColor(getResources().getColor(
                R.color.toolbar_color));

    }
}
