package wbkj.sjapp.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.adapter.CmtLvAdapter;
import wbkj.sjapp.adapter.ProGvAdapter;
import wbkj.sjapp.adapter.ProImgAdapter;
import wbkj.sjapp.models.Cart;
import wbkj.sjapp.models.Comment;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.ProJsonModel;
import wbkj.sjapp.models.Product;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;
import wbkj.sjapp.views.CustomGridView;
import wbkj.sjapp.views.CustomListView;

public class ProDetailActivity extends BaseActivity {

    private Toolbar toolbar;

    private ImageButton imgFav, imgCart;
    private TextView tvName, tvNum, tvPrice, tvDprice, tvStime, tvDesc, tvTotal, tvWeb;
    private CustomGridView proGv1, proGv2;
    private CustomListView cmtLv;
    private ProGvAdapter adapter1, adapter2;
    private Button btnAdd, btnNow;
    private ViewPager viewPager;
    private LinearLayout linearPoint;
    private ProImgAdapter imgAdapter;
    private List<View> pointList = new ArrayList<>();
    private int currentItem = 0;
    String[] imgs;
    private List<Product> proList1 = new ArrayList<>();
    private List<Product> proList2 = new ArrayList<>();
    private List<Comment> cmtlist = new ArrayList<>();
    private CmtLvAdapter cmtAdapter;

    private boolean isFav;
    private Product pro;
    private int clid;       //收藏商品的id
    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        pro = (Product) getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getSerializable("pro");
        setContentView(R.layout.activity_pro_detail);
        initView();
        getFav();
        createLoadingDialog(this, "加载中...").show();
        getCmt(pro.getWdid());

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

        viewPager = (ViewPager) findViewById(R.id.index_vp);
        linearPoint = (LinearLayout) findViewById(R.id.linear_point);
        imgFav = (ImageButton) findViewById(R.id.pro_detail_fav);
        imgCart = (ImageButton) findViewById(R.id.pro_detail_cart);
        tvName = (TextView) findViewById(R.id.pro_detail_name);
        tvName.setText(pro.getWare().getWname());
        tvNum = (TextView) findViewById(R.id.pro_detail_num);
        tvNum.setText(pro.getWare().getLaud()+"");
        tvPrice = (TextView) findViewById(R.id.pro_detail_price);
        tvPrice.setText("￥"+pro.getWdprice());
        tvDprice = (TextView) findViewById(R.id.pro_detail_oprice);
        tvDprice.setText("￥"+pro.getOidprice());
        tvDprice.getPaint().setAntiAlias(true);//抗锯齿
        tvDprice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        tvStime = (TextView) findViewById(R.id.pro_detail_service);
        tvStime.setText(pro.getCol3());
        tvDesc = (TextView) findViewById(R.id.pro_detail_desc);
        tvDesc.setText(pro.getCol1());
        tvWeb = (TextView) findViewById(R.id.pro_detail_web);
        tvWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isNotBlank(pro.getCol7())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", HttpUtil.IMGURL+pro.getCol7());
                    skipActivity(ProDetailActivity.this, WebviewActivity.class, bundle);
                } else {
                    Toast.makeText(ProDetailActivity.this, "产品未添加图文信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvTotal = (TextView) findViewById(R.id.pro_detail_total);
        tvTotal.setText("￥"+pro.getWdprice());
        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFav) {
                    getCalFav();
                } else {
                    getAddFav();
                }
            }
        });
        cmtLv = (CustomListView) findViewById(R.id.pro_detail_lv);
        proGv1 = (CustomGridView) findViewById(R.id.pro_detail_gv1);
        proGv2 = (CustomGridView) findViewById(R.id.pro_detail_gv2);
        btnAdd = (Button) findViewById(R.id.pro_detail_add);

        proGv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("pro", proList1.get(i));
                skipActivity(ProDetailActivity.this, ProDetailActivity.class, bundle);
            }
        });
        proGv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("pro", proList2.get(i));
                skipActivity(ProDetailActivity.this, ProDetailActivity.class, bundle);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCart();
            }
        });
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipActivity(ProDetailActivity.this, CartActivity.class, null);
            }
        });
        btnNow = (Button) findViewById(R.id.pro_detail_now);
        btnNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                List<Cart> lists = new ArrayList<Cart>();
                Cart cart = new Cart();
                cart.num = 1;
                cart.wdetails = pro;
                lists.add(cart);
                bundle.putSerializable("cart", (Serializable) lists);
                bundle.putDouble("total", pro.getWdprice());
                skipActivity(ProDetailActivity.this, ConfirmOrdersActivity.class, bundle);
            }
        });
        imgs = pro.getWare().getImgstore().split(",");
        Log.e(">",imgs.length+"");
        if (imgs.length > 0) {
            initViewPager();
            initPointImg();
        }
    }

    private void initViewPager() {
        imgAdapter = new ProImgAdapter(this, imgs);
        viewPager.setAdapter(imgAdapter);
        viewPager.setCurrentItem(0);
        /**
         * 1-1.为ViewPager添加事件
         */
        if(imgs.length > 1){
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                private int oldPosition = 0;//保存旧的指示图片的位置
                @Override
                public void onPageSelected(int position) {
                    currentItem = position;
                    pointList.get(position % imgs.length).setBackgroundResource(R.mipmap.feature_point_cur);
                    pointList.get(oldPosition % imgs.length).setBackgroundResource(R.mipmap.feature_point);
                    oldPosition = position;
                }
                @Override
                public void onPageScrolled(int position, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
        }
    }
    /**
     * 初始化指示图片
     */
    private void initPointImg(){
        if(pointList.size()>0){
            pointList.clear();
        }
        if(linearPoint.getChildCount()>0){
            linearPoint.removeAllViews();
        }
        for (int i = 0; i < imgs.length; i++) {
            ImageView imView=new ImageView(this);
            if(i==0){
                imView.setBackgroundResource(R.mipmap.feature_point_cur);
            }else{
                imView.setBackgroundResource(R.mipmap.feature_point);
            }
            pointList.add(imView);
            linearPoint.addView(imView);
        }
    }

    private void addCart() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),//86
                new OkHttpClientManager.Param("wdid", String.valueOf(pro.getWdid())),//212
                new OkHttpClientManager.Param("num", "1")
        };
        OkHttpClientManager.postAsyn(HttpUtil.addCart, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ProDetailActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    Toast.makeText(ProDetailActivity.this, "添加购物车成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProDetailActivity.this, "添加购物车失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    /*
    获取页面数据
     */
    private void getCmt(int proId) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("wid", String.valueOf(pro.getWid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.cmtUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ProDetailActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
//                Log.e(">",response.toString());
                ProJsonModel json = new Gson().fromJson(response, ProJsonModel.class);
                if (json.result == 1) {
                    pro = json.data;
                    cmtlist.addAll(json.comment);
                    cmtAdapter = new CmtLvAdapter(cmtlist, ProDetailActivity.this);
                    cmtLv.setAdapter(cmtAdapter);

                    getPro(pro.getWare().getShopid(), pro.getWid());
                }
            }
        }, null);
    }

    private void getPro(int shopId, int proId) {
        proList1.clear();
        proList2.clear();

        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("shopid", String.valueOf(shopId)),//0
                new OkHttpClientManager.Param("wid", String.valueOf(proId))//241
        };
        OkHttpClientManager.postAsyn(HttpUtil.recProUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(ProDetailActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                Log.e(">",response.toString());
                Logger.e("11"+response.toString());
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Product>>(){}.getType());
                if (json.result == 1) {
                    proList1.addAll(json.shopdata);
                    adapter1 = new ProGvAdapter(proList1, ProDetailActivity.this);
                    proGv1.setAdapter(adapter1);
                    adapter2 = new ProGvAdapter(proList2, ProDetailActivity.this);
                    proGv2.setAdapter(adapter2);
                }
            }
        }, null);
    }

    /*
    获取是否收藏
     */
    private void getFav() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("cltype", "1"),
                new OkHttpClientManager.Param("quiltid", String.valueOf(pro.getWdid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.favStateUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ProDetailActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    if (json.is == 1) {
                        isFav = true;
                        imgFav.setImageResource(R.mipmap.fav_on);
                        clid = json.collect.clid;
                    } else {
                        isFav = false;
                        imgFav.setImageResource(R.mipmap.fav_off);
                    }
                }
            }
        }, null);
    }

    private void getAddFav() {

        Log.e(String.valueOf(sp.getUser().getVipid()),"___");
        Log.e(String.valueOf(pro.getWdid()),"___");
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("cltype", "1"),
                new OkHttpClientManager.Param("quiltid", String.valueOf(pro.getWdid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.addFavUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ProDetailActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                Log.e("---------", response.toString());
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    isFav = true;
                    imgFav.setImageResource(R.mipmap.fav_on);
                    clid = json.collect.clid;
                    Toast.makeText(ProDetailActivity.this, "添加收藏成功", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    private void getCalFav() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("Clid", String.valueOf(clid))
        };
        OkHttpClientManager.postAsyn(HttpUtil.calFavUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ProDetailActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                Log.e("---------", response.toString());
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    isFav = false;
                    imgFav.setImageResource(R.mipmap.fav_off);
                    Toast.makeText(ProDetailActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

}
