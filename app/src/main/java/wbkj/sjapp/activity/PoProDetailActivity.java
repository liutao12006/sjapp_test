package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.Product;
import wbkj.sjapp.models.User;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;

public class PoProDetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView img;
    private TextView tvName, tvPrice, tvNum;
    private Button btnEx;
    private ImageButton btnAdd, btnMin;

    private SharedPreferencesUtil sp;
    private Product pro;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        pro = (Product) getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getSerializable("pro");
        Logger.e(pro.toString());
        setContentView(R.layout.activity_po_pro_detail);
        initView();
        getJifen();
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

        img = (ImageView) findViewById(R.id.popro_img);
        Picasso.with(this).load(HttpUtil.IMGURL+pro.getWare().getImg()).into(img);
        tvName = (TextView) findViewById(R.id.popro_tv_name);
        tvName.setText(pro.getWare().getWname());
        tvPrice = (TextView) findViewById(R.id.popro_tv_price);
        tvPrice.setText(pro.getWdprice()+"积分/次");
        btnEx = (Button) findViewById(R.id.popro_btn_ex);
        btnEx.setText("兑换("+pro.getWdprice()+"积分/次)");
        btnEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.e(sp.getUser().getVipid()+"");
                if (total > sp.getUser().getIntergration()) {
                    Toast.makeText(PoProDetailActivity.this, "积分不足", Toast.LENGTH_SHORT).show();
                } else {
                    createLoadingDialog(PoProDetailActivity.this, "兑换中...").show();
                    getExcMethod();
                }
            }
        });
        btnAdd = (ImageButton) findViewById(R.id.cart_item_add);
        btnMin = (ImageButton) findViewById(R.id.cart_item_min);
        tvNum = (TextView) findViewById(R.id.cart_edit_num);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(tvNum.getText().toString().trim());
                num = num + 1;
                tvNum.setText(String.valueOf(num));
                total = (int) (pro.getWdprice()*num);
                btnEx.setText("兑换("+total+"积分/次)");
            }
        });
        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = Integer.parseInt(tvNum.getText().toString().trim());
                if (num > 1) {
                    num = num - 1;
                    tvNum.setText(String.valueOf(num));
                }
                total = (int) (pro.getWdprice()*num);
                  btnEx.setText("兑换("+total+"积分/次)");
            }
        });
    }

    private void getExcMethod() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("wdid", String.valueOf(pro.getWdid())),
                new OkHttpClientManager.Param("tote", tvNum.getText().toString().trim()),
                new OkHttpClientManager.Param("total", String.valueOf(total))
        };
        OkHttpClientManager.postAsyn(HttpUtil.proExc, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(PoProDetailActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                User user = sp.getUser();
                user.setIntergration(json.intergration);
                sp.onLoginSuccess(user);
                Toast.makeText(PoProDetailActivity.this, json.msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        }, null);
    }

    private void getJifen() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.jifenUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(PoProDetailActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                User user = sp.getUser();
                user.setIntergration(json.intergration);
                sp.onLoginSuccess(user);
            }
        }, null);
    }
}
