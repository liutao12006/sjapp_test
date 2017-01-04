package wbkj.sjapp.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.adapter.ShopRecyAdatper;
import wbkj.sjapp.fragment.IndexFragment;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.ProType;
import wbkj.sjapp.models.Shop;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.DividerItemDecoration;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;
import wbkj.sjapp.utils.OkHttpClientManager;

public class ShopActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView recyShops;
    private List<Shop> shops = new ArrayList<>();
    private List<ProType> types = new ArrayList<>();
    private ShopRecyAdatper adapter;
    private ImageButton imgSearch;
    private TextView tvType;
    private RadioButton tvJuli, tvTime, tvCmt;

    private String type,from;
    private MyApplication app;
    int sort = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplication();
        type = getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getString("type");
        //判断一下从哪里跳转过来的
        from = getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getString("from");
        setContentView(R.layout.activity_shop);

        initView();


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            shops.clear();
            shops.addAll((Collection<? extends Shop>) data.getExtras().getSerializable("list"));
//            Logger.e(shops.toString());
            adapter.notifyDataSetChanged();
        }
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
        recyShops = (RecyclerView) findViewById(R.id.recy_shops);
        recyShops.setHasFixedSize(true);
        recyShops.setLayoutManager(new LinearLayoutManager(this));
        recyShops.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        imgSearch = (ImageButton) findViewById(R.id.shop_search);

        tvType = (TextView) findViewById(R.id.shop_tv_type);
        tvJuli = (RadioButton) findViewById(R.id.shop_tv_juli);
        tvTime = (RadioButton) findViewById(R.id.shop_tv_time);
        tvCmt = (RadioButton) findViewById(R.id.shop_tv_cmt);

        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里的这个方法在商盟页面点击进来可以执行， 但好像在大管家超市中进来的时候 好像不行 应该是缺少什么参数  应该是没有数据的缘故

                showPopupWindow(tvType);
            }
        });
        tvJuli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort = 1;
                tvTime.setChecked(false);
                tvCmt.setChecked(false);
                createLoadingDialog(ShopActivity.this, "加载中...").show();
                getDataMethod(type, sort);
            }
        });
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort = 3;
                tvJuli.setChecked(false);
                tvCmt.setChecked(false);
                createLoadingDialog(ShopActivity.this, "加载中...").show();
                getDataMethod(type, sort);
            }
        });
        tvCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sort = 2;
                tvJuli.setChecked(false);
                tvTime.setChecked(false);
                createLoadingDialog(ShopActivity.this, "加载中...").show();
                getDataMethod(type, sort);
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopActivity.this, SearchActivity.class);
                intent.putExtra("tag", 1);
                startActivityForResult(intent, 1);
            }
        });

        createLoadingDialog(ShopActivity.this, "加载中...").show();
        if("product".equals(from)){
            getProductDataMethod(type, sort);
        }else {
            getDataMethod(type, sort);
        }
    }

    private void getDataMethod(String type, int sort) {
        shops.clear();
        types.clear();


        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("type", type),
                new OkHttpClientManager.Param("sort", String.valueOf(sort)),
//                new OkHttpClientManager.Param("x", String.valueOf(IndexFragment.Longitude)),
//                new OkHttpClientManager.Param("y", String.valueOf(IndexFragment.Latitude))
                new OkHttpClientManager.Param("x", String.valueOf(app.getaMapLocation().getLongitude())),
                new OkHttpClientManager.Param("y", String.valueOf(app.getaMapLocation().getLatitude()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.shopUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {

            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(ShopActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                Logger.e(response.toString());
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Shop>>() {
                }.getType());
                if (json.result == 1) {
                    shops.addAll(json.data);
//                    types.addAll(json.syscodes);
                    if (adapter == null) {
                        adapter = new ShopRecyAdatper(shops, ShopActivity.this);
                        recyShops.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    adapter.setOnItemClickListener(new MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("shopId", shops.get(position).getId());
                            skipActivity(ShopActivity.this, ShopDetailActivity.class, bundle);
                        }
                    });
                }
            }
        }, null);
    }

    private void showPopupWindow(View view) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pop_layout, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                tvType.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        ListView lv = (ListView) contentView.findViewById(R.id.lv_type);
        final List<String> strs = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            strs.add(types.get(i).getDmsm());
        }
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.tv_type, strs));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                popupWindow.dismiss();
                tvType.setText(strs.get(i));

                createLoadingDialog(ShopActivity.this, "加载中...").show();
                Log.e(">>>>>>>>>>><<<<<<<<<<"," "+type+"  "+sort);
                getProductDataMethod(type, sort);
//                getDataMethod(type, sort);
            }
        });

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view);

    }


    private void getProductDataMethod(String type, int sort) {
        shops.clear();
        types.clear();

//        if(IndexFragment.Longitude==0.0){//这个经纬度默认是上街地区的
//            IndexFragment.Longitude=113.296333;
//            IndexFragment.Latitude =34.80299;
//        }

        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("type", type),
                new OkHttpClientManager.Param("sort", String.valueOf(sort)),
//                new OkHttpClientManager.Param("x", String.valueOf(IndexFragment.Longitude)),
//                new OkHttpClientManager.Param("y", String.valueOf(IndexFragment.Latitude))
                new OkHttpClientManager.Param("x", String.valueOf(app.getaMapLocation().getLongitude())),
                new OkHttpClientManager.Param("y", String.valueOf(app.getaMapLocation().getLatitude()))
        };
//        OkHttpClientManager.postAsyn(HttpUtil.shopUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
        OkHttpClientManager.postAsyn(HttpUtil.shopProductUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
//                Logger.e(e.getMessage());
                Toast.makeText(ShopActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                Logger.e(response.toString());
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Shop>>() {
                }.getType());
                if (json.result == 1) {
                    shops.addAll(json.data);
                    types.addAll(json.syscodes);
                    if (adapter == null) {
                        adapter = new ShopRecyAdatper(shops, ShopActivity.this);
                        recyShops.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    adapter.setOnItemClickListener(new MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("shopId", shops.get(position).getId());
                            skipActivity(ShopActivity.this, ShopDetailActivity.class, bundle);
                        }
                    });
                }
            }
        }, null);
    }

}
