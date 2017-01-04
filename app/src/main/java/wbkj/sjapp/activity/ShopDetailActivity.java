package wbkj.sjapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.adapter.ProLvAdapter;
import wbkj.sjapp.models.Comment;
import wbkj.sjapp.models.Product;
import wbkj.sjapp.models.Shop;
import wbkj.sjapp.models.ShopJson;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.views.CustomListView;

public class ShopDetailActivity extends BaseActivity {

    private Toolbar toolbar;

    private CustomListView recy;
    private ProLvAdapter adatper;
    private Shop shop;
    private ImageView img;
    private TextView tvName, tvAds, tvCmts, tvTel;
    private RatingBar ratingBar;
    private List<Product> products;
    private List<Comment> comments;
    private int shopId;
    private MyApplication app;

    private String  localtionX;
    private String  localtionY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplication();
        shopId = (int) getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).get("shopId");
        setContentView(R.layout.activity_shop_detail);

        initView();
        createLoadingDialog(this, "加载中...").show();
        getDataMethod(shopId);
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

        ratingBar = (RatingBar) findViewById(R.id.shop_rating);
        img = (ImageView) findViewById(R.id.shop_img);
        tvName = (TextView) findViewById(R.id.shop_name);
        tvAds = (TextView) findViewById(R.id.shop_ads);
        tvCmts = (TextView) findViewById(R.id.shop_cmt);
        recy = (CustomListView) findViewById(R.id.pro_recy);
        tvTel = (TextView) findViewById(R.id.shop_tel);
        recy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("pro", products.get(i));
                skipActivity(ShopDetailActivity.this, ProDetailActivity.class, bundle);
            }
        });


        tvAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("localtionX", localtionX);
                bundle.putString("localtionY", localtionY);
                skipActivity(ShopDetailActivity.this, MapActivity.class,bundle);
            }
        });
    }

    private void intData() {
        Picasso.with(ShopDetailActivity.this).load(HttpUtil.IMGURL+shop.getImg()).into(img);
        tvName.setText(shop.getName());
        tvAds.setText(shop.getAddress());
        tvCmts.setText("("+comments.size()+"人评)");
        Logger.e("2-"+shop.getAvg());
        ratingBar.setRating((float) shop.getAvg());
        adatper = new ProLvAdapter(products, ShopDetailActivity.this);
        recy.setAdapter(adatper);
        tvTel.setText(shop.getPhone());
        tvTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + shop.getPhone()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void getDataMethod(final int shopId) {

        Logger.e(""+shopId);
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("id", String.valueOf(shopId))
        };
        OkHttpClientManager.postAsyn(HttpUtil.shopDetail, params, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(ShopDetailActivity.this, "网络链接失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                closeProDialog();
                ShopJson json = new Gson().fromJson(response, ShopJson.class);
                if (json.result == 1) {
                    shop = json.shop;
                    products = json.wdetailsList;
                    comments = json.commentList;

                    localtionX = shop.getLocationX();
                    localtionY = shop.getLocationY();

                    intData();
                }
            }
        }, null);
    }

}
