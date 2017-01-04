package wbkj.sjapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.adapter.OrderProLvAdapter;
import wbkj.sjapp.models.Address;
import wbkj.sjapp.models.Cart;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.OrdersStr;
import wbkj.sjapp.models.PayType;
import wbkj.sjapp.models.User;
import wbkj.sjapp.models.WxPay;
import wbkj.sjapp.pay.AliPay;
import wbkj.sjapp.pay.WixPay;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;
import wbkj.sjapp.views.CustomListView;

public class ConfirmOrdersActivity extends BaseActivity {

    private Toolbar toolbar;
    private List<Cart> proList;
    private CustomListView proLv;
    private OrderProLvAdapter adapter;
    private TextView tvAds, adsName, adsPhone, adsDetail, tvTotal, tvJifen;
    private RelativeLayout adsLayout;
    private Button btnOK;
    private CustomListView paylv;
    private ArrayAdapter<PayType> payTypeAdapter;
    private int selectedPayTypePosition = 0;
    private EditText etTxt, etPoint;

    private SharedPreferencesUtil sp;
    private Address ads;
    private double total;
    int integral = 0;
    int rate;
    double discount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        proList = (List<Cart>) getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).get("cart");
        total = (double) getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).get("total");
        setContentView(R.layout.activity_confirm_orders);
        initView();
        getDataMethod();
        getVip();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            tvAds.setVisibility(View.GONE);
            adsLayout.setVisibility(View.VISIBLE);
            ads = (Address) data.getExtras().getSerializable("ads");
            adsName.setText(ads.getPlotnickname());
            adsPhone.setText(ads.getTel());
            adsDetail.setText(ads.getProvince()+ads.getCity()+ads.getCounty()+ads.getAddress());
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

        tvTotal = (TextView) findViewById(R.id.confirm_order_total);
        tvTotal.setText("￥"+total);
        proLv = (CustomListView) findViewById(R.id.confirm_order_pro);
        adapter = new OrderProLvAdapter(proList, this);
        proLv.setAdapter(adapter);
        adsLayout = (RelativeLayout) findViewById(R.id.ads_layout);
        tvAds = (TextView) findViewById(R.id.ads_tv);
        adsName = (TextView) findViewById(R.id.item_ads_name);
        adsPhone = (TextView) findViewById(R.id.item_ads_phone);
        adsDetail = (TextView) findViewById(R.id.item_ads_detail);
        btnOK = (Button) findViewById(R.id.confirm_order_btn);
        etTxt = (EditText) findViewById(R.id.confirm_order_ettxt);
        etPoint = (EditText) findViewById(R.id.confirm_order_point);
        tvJifen = (TextView) findViewById(R.id.confirm_order_tv);
        paylv = (CustomListView) findViewById(R.id.pp_pay_type);
        initPay();

        etPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
               if (StringUtils.isNotBlank(editable.toString())) {
                   int p = Integer.parseInt(editable.toString());
                   if (p > sp.getUser().getIntergration()) {
                       etPoint.setHint("积分不足");
                       etPoint.setHintTextColor(getResources().getColor(R.color.font_red));
                   } else if (p <= Integer.parseInt(editable.toString())) {
                       integral = p;
                       total = total - integral/rate;
                       if (total <= 0) total = 0;
                       tvTotal.setText("￥"+total);
                   }
               }
            }
        });
        tvAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ConfirmOrdersActivity.this, AdsListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tag", "order");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        adsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ConfirmOrdersActivity.this, AdsListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tag", "order");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOrder();
            }
        });
    }


    private void setOrder() {
        List<OrdersStr> lists = new ArrayList<>();
        if (ads == null) {
            Toast.makeText(ConfirmOrdersActivity.this, "请选择需要服务的小区地址", Toast.LENGTH_SHORT).show();
            return;
        }
        String notes = etTxt.getEditableText().toString();
        for (int i = 0; i < proList.size(); i++) {
            Cart cart = proList.get(i);
            OrdersStr order = new OrdersStr();
            order.setUserid(String.valueOf(sp.getUser().getVipid()));
            order.setWdid(String.valueOf(cart.wdetails.getWdid()));
            order.setTote(String.valueOf(cart.num));
            order.setTotal(String.valueOf(cart.num*cart.wdetails.getWdprice()));
            order.setCol1(String.valueOf(ads.getUdid()));
            order.setIntegral(String.valueOf(integral/rate));
            if (StringUtils.isNotBlank(notes)) order.setCol5(notes);
            order.setCid(String.valueOf(cart.scid));
            Log.e("**************",sp.getUser().getVipid()+"**"+cart.wdetails.getWdid()+"**"+cart.num+"**"+cart.num*cart.wdetails.getWdprice()+"**"+ads.getUdid()+""+integral/rate+"**"+cart.scid);
            lists.add(order);
        }

        if (integral > 0 ) {
            total = total - integral/rate;
            if (total <= 0) {
                total = 0;
            } else {
                total = total * discount;
            }
        }

        createLoadingDialog(this, "下单中...").show();
        addMethod(lists);
    }

    private void addMethod(final List<OrdersStr> list) {
        Gson gson = new Gson();
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("orders", gson.toJson(list))
        };
        Log.e(">>>>>>>>>>",gson.toJson(list));
        OkHttpClientManager.postAsyn(HttpUtil.addOrder, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(ConfirmOrdersActivity.this, "网络请求失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    Toast.makeText(ConfirmOrdersActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    if (selectedPayTypePosition == 0) {
                        /*
                        阿里支付
                         */
                        AliPay aliPay = new AliPay(ConfirmOrdersActivity.this, json.payno);
                        aliPay.pay("上街管家", "订单产品支付", String.valueOf(total));

                    } else {
                        getWxPay(json.payno);
                    }
                } else {
                    Toast.makeText(ConfirmOrdersActivity.this, "提交失败,请重新提交", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    private void getWxPay(String str) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("orders", str),
                new OkHttpClientManager.Param("total_fee", String.valueOf(total))
        };
        OkHttpClientManager.postAsyn(HttpUtil.wxUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ConfirmOrdersActivity.this, "网络请求失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                Logger.e(response.toString());
                WxPay wxPay = new Gson().fromJson(response, WxPay.class);
                WixPay pay = new WixPay(ConfirmOrdersActivity.this, wxPay);
                pay.wxPay();
            }
        }, null);
    }

    private void getDataMethod() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.adsUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ConfirmOrdersActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Address>>(){}.getType());
                if (json.result == 1) {
                    if (StringUtils.isNotBlank(json.data.toString())&&json.data.size()>0) {
                        ads = (Address) json.data.get(0);
                        tvAds.setVisibility(View.GONE);
                        adsLayout.setVisibility(View.VISIBLE);
                        adsName.setText(ads.getPlotnickname());
                        adsPhone.setText(ads.getTel());
                        adsDetail.setText(ads.getProvince()+ads.getCity()+ads.getCounty()+ads.getAddress());
                    }
                }
            }
        }, null);
    }

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

    private void getVip() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.jifenUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ConfirmOrdersActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    User user = sp.getUser();
                    user.setGrade(json.grade);
                    user.setIntergration(json.intergration);
                    sp.onLoginSuccess(user);
                    rate = json.pay;
                    discount = json.discount;

                    tvJifen.setText("当前可用积分:"+json.intergration);
                }
            }
        }, null);
    }



    /*
 通知商家的短信接口
  */
    public void notifyShoping(){

        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
//                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
//                new OkHttpClientManager.Param("state", String.valueOf(type))
        };
        OkHttpClientManager.postAsyn(HttpUtil.smsUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.e(">>>>",e.toString());

            }

            @Override
            public void onResponse(JsonElement response) {

            }
        }, null);
    }

}
