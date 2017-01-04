package wbkj.sjapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import wbkj.sjapp.models.Address;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.PayType;
import wbkj.sjapp.models.WxPay;
import wbkj.sjapp.pay.AliPay;
import wbkj.sjapp.pay.WixPay;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.views.CustomListView;

public class PropertyActivity extends BaseActivity {

    private Toolbar toolbar;
    private LinearLayout layoutAds;
    private CustomListView paylv;
    private ArrayAdapter<PayType> payTypeAdapter;
    private int selectedPayTypePosition = 0;
    private TextView tvName, tvPay;
    private RadioGroup rgbType;
    private Button btnPay;

    private SharedPreferencesUtil sp;
    private List<Address> adsList = new ArrayList<>();
    private Address ads;
    private int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_property);
        initView();
        getDataMethod();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ads = (Address) data.getExtras().getSerializable("ads");
            assert ads != null;
            tvName.setText(ads.getPlotnickname());
            if (type == 1) {
                tvPay.setText("￥"+(ads.getPropertyfee()*0.9));
            } else {
                tvPay.setText("￥"+(ads.getPropertyfee()*0.95));
            }
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
        paylv = (CustomListView) findViewById(R.id.pp_pay_type);
        initPay();

        tvName = (TextView) findViewById(R.id.pp_ads_name);
        tvPay = (TextView) findViewById(R.id.pp_ads_pay);
        layoutAds = (LinearLayout) findViewById(R.id.pp_address);
        rgbType = (RadioGroup) findViewById(R.id.pp_ads_rg);
        btnPay = (Button) findViewById(R.id.pp_btn_pay);

        rgbType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.pp_rb_a:
                        type = 1;
                        if (ads != null) tvPay.setText("￥"+(ads.getPropertyfee()*0.9));
                        break;
                    case R.id.pp_rb_b:
                        type = 2;
                        if (ads != null) tvPay.setText("￥"+(ads.getPropertyfee()*0.95));
                        break;
                    default: break;
                }
            }
        });
        layoutAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PropertyActivity.this, AdsListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tag", "order");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double price;
                if (ads == null) {
                    Toast.makeText(PropertyActivity.this, "请选择小区", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type == 1) {
                    price = ads.getPropertyfee()*0.9;
                } else {
                    price = ads.getPropertyfee()*0.95;
                }
                if (selectedPayTypePosition == 0) {
                    AliPay aliPay = new AliPay(PropertyActivity.this, getNo());
                    aliPay.pay("物业缴费", "app物业缴费支付", String.valueOf(price));
                } else {
                    createLoadingDialog(PropertyActivity.this, "加载中...").show();
                    getWxPay(getNo(), String.valueOf(price));
                }
            }
        });
    }

    /**
     * 初始化在线支付
     */
    private void initPay() {

        final List<PayType> list = new ArrayList<PayType>();
        PayType payType1 = new PayType("支付宝支付","推荐有支付宝账号的用户使用",R.mipmap.alipay);
        PayType payType2 = new PayType("微信支付","推荐开通微信支付的用户使用",R.mipmap.weixin);
        list.add(payType1);
        list.add(payType2);

        payTypeAdapter = new ArrayAdapter<PayType>(this, R.layout.pay_type_item, list){
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                PayType payType = list.get(position);
                @SuppressLint("ViewHolder")
                RelativeLayout view = (RelativeLayout) LayoutInflater.
                        from(getApplicationContext()).inflate(R.layout.pay_type_item, null);
                ImageView tvPayTypeLogo = (ImageView)view.findViewById(R.id.iv_pay_type_logo);
                TextView tvPayTypeName = (TextView)view.findViewById(R.id.tv_pay_type_name);
                TextView tvPayTypeSummary = (TextView)view.findViewById(R.id.tv_pay_type_summary);
                CheckBox cbPayTypeSelect = (CheckBox)view.findViewById(R.id.cb_pay_type_selected);

                tvPayTypeLogo.setImageResource(payType.getIconResId());
                tvPayTypeName.setText(payType.getName());
                tvPayTypeSummary.setText(payType.getSummary());

                cbPayTypeSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            selectedPayTypePosition = position;
                            Log.e("zzz", selectedPayTypePosition + "," + position);
                            payTypeAdapter.notifyDataSetChanged();
                        }
                    }
                });

                if(position == selectedPayTypePosition){
                    if(!cbPayTypeSelect.isChecked()){
                        cbPayTypeSelect.setChecked(true);
                    }
                }
                else{
                    if(cbPayTypeSelect.isChecked()){
                        cbPayTypeSelect.setChecked(false);
                    }
                }

                return view;
            }
        };
        paylv.setAdapter(payTypeAdapter);
    }

    private void getWxPay(String str, String price) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("orders", str),
                new OkHttpClientManager.Param("total_fee", price)
        };
        OkHttpClientManager.postAsyn(HttpUtil.wxUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(PropertyActivity.this, "网络请求失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                WxPay wxPay = new Gson().fromJson(response, WxPay.class);
                WixPay pay = new WixPay(PropertyActivity.this, wxPay);
                pay.wxPay();
            }
        }, null);
    }

    private String getNo() {
        StringBuffer sb = new StringBuffer("payno");
        sb.append(System.currentTimeMillis());
        return sb.toString();
    }

    private void getDataMethod() {
        adsList.clear();
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.adsUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(PropertyActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Address>>() {
                }.getType());
                if (json.result == 1) {
                    adsList.addAll(json.data);
                    if (adsList.size() > 0) {
                        ads = adsList.get(0);
                        tvName.setText(ads.getPlotnickname());
                        if (type == 1) {
                            tvPay.setText("￥"+(ads.getPropertyfee()*0.9));
                        } else {
                            tvPay.setText("￥"+(ads.getPropertyfee()*0.95));
                        }
                    }
                }
            }
        }, null);
    }

}
