package wbkj.sjapp.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.util.Calendar;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.Address;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Message;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class AdsActivity extends BaseActivity {

    private Toolbar toolbar;
    private EditText etNname, etName, etDetail, etPc, etDic, etHname, etPhone, etArea, etPpFree,etProvince,etCity;
    private Button btnOk;
    private TextView tvDate;
    private RelativeLayout rlDate;
    private Switch swBtn;

    private SharedPreferencesUtil sp;
    private MyApplication app;
    private Address ads;
    String tag;
    private String province, city, country;
    int isDefault = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        app = (MyApplication) getApplication();
        tag = getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getString("tag");
        assert tag != null;
        if (tag.equals("modify")) {
            ads = (Address) getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getSerializable("ads");
        }
        setContentView(R.layout.activity_ads);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etNname = (EditText) findViewById(R.id.et_ads_nname);
        //etProvince,etCity
        etProvince = (EditText) findViewById(R.id.et_ads_pr);
        etCity = (EditText) findViewById(R.id.et_ads_dic);

        etPc = (EditText) findViewById(R.id.et_ads_pr);
        etDic = (EditText) findViewById(R.id.et_ads_dic);
        etName = (EditText) findViewById(R.id.et_ads_name);
        etDetail = (EditText) findViewById(R.id.et_ads_detail);
        etHname = (EditText) findViewById(R.id.et_ads_household);
        etPhone = (EditText) findViewById(R.id.et_ads_phone);
        etArea = (EditText) findViewById(R.id.et_ads_houserarea);
        etPpFree = (EditText) findViewById(R.id.et_ads_propertyfee);
        tvDate = (TextView) findViewById(R.id.et_ads_date);
        rlDate = (RelativeLayout) findViewById(R.id.rl_layout_date);
        btnOk = (Button) findViewById(R.id.ads_btn_ok);
        swBtn = (Switch) findViewById(R.id.ads_switch);
        swBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isDefault = 1;
                } else {
                    isDefault = 0;
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tag.equals("modify")) {
                    btnOk.setText("修改");
                    modAdsMethod();
                } else {
                    addAdsMethod();
                }
            }
        });
        final Calendar calendar = Calendar.getInstance();
        rlDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AdsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        //西方的日期的月数是0 -- 11
                        tvDate.setText(i+"-"+(i1+1)+"-"+i2);
                    }
                }, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        if (tag.equals("modify")) {
            etNname.setText(ads.getPlotnickname());
            etPc.setText(ads.getProvince()+ads.getCity());
            etDic.setText(ads.getCounty());
            etName.setText(ads.getPlot());
            etDetail.setText(ads.getAddress());
            etHname.setText(ads.getHousehold());
            etPhone.setText(ads.getTel());
            etArea.setText(ads.getHouserarea());
            etPpFree.setText(ads.getPowerfee()+"");
            tvDate.setText(ads.getExpirationtime());
            if (ads.getIsdefult() == 1) swBtn.setChecked(true);
        } else {
            province = app.getaMapLocation().getProvince();
            city = app.getaMapLocation().getCity();
            country = app.getaMapLocation().getDistrict();
            //这里是我加上的   因为这个country = ""
            if(country==""&country==null){
                country = "China";
            }
            etPc.setText(province+city);
            etDic.setText(country);
        }
    }

    private void addAdsMethod(){
        String nname = etNname.getEditableText().toString();
        String name = etName.getEditableText().toString();
        String detail = etDetail.getEditableText().toString();
        String hname = etHname.getEditableText().toString();
        String phone = etPhone.getEditableText().toString();
        String area = etArea.getEditableText().toString();
        String ppFree = etPpFree.getEditableText().toString();
        String date = tvDate.getText().toString();

        String city = etCity.getEditableText().toString();
        String province = etProvince.getEditableText().toString();

//        if (!StringUtils.isNotBlank(nname, name, province, city, country, detail, phone, area, hname, ppFree, date)) {
//            Toast.makeText(AdsActivity.this, "小区信息请填写完整", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Log.e("************",nname+" 1 "+province+" 2 "+city+" 3 "+country+" 4 "+name+" 5 "+detail+"  6 "+hname+"  7 "+phone+"  8 "+area+"  9 "+ppFree+" 10 "+date+"  11  "+isDefault);
       // 贝多芬 1 河南省郑州市 2 上街区 3 上街区 4 看看 5 阿鲁  6 拉  7 15136251528  8 19  9 15 10 2016-12-31  11  1
        if (!StringUtils.isNotBlank(nname, name, city, detail, phone)) {
            Toast.makeText(AdsActivity.this, "小区信息请填写完整", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("plotnickname", nname),
                new OkHttpClientManager.Param("province", province),
                new OkHttpClientManager.Param("city", city),
                new OkHttpClientManager.Param("county", country),
                new OkHttpClientManager.Param("plot", name),
                new OkHttpClientManager.Param("address", detail),
                new OkHttpClientManager.Param("household", hname),
                new OkHttpClientManager.Param("tel", phone),
                new OkHttpClientManager.Param("houserarea", area),
                new OkHttpClientManager.Param("propertyfee", ppFree),
                new OkHttpClientManager.Param("expirationtime", date),
                new OkHttpClientManager.Param("isDefault", String.valueOf(isDefault))

        };

        Log.e("************",nname+" 1 "+province+" 2 "+city+" 3 "+country+" 4 "+name+" 5 "+detail+"  6 "+hname+"  7 "+phone+"  8 "+area+"  9 "+ppFree+" 10 "+date+"  11  "+isDefault);

        OkHttpClientManager.postAsyn(HttpUtil.addAdsUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Log.e("err------", e.getMessage());
                Toast.makeText(AdsActivity.this, "网络链接失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Message.Comment>>(){}.getType());
                if (json.result == 1) {
                    Toast.makeText(AdsActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdsActivity.this, json.msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);


//        OkHttpClientManager.postAsyn(HttpUtil.addAdsUrl, params, new OkHttpClientManager.ResultCallback<String>() {
//            @Override
//            public void onError(Request request, Exception e) {
//                closeProDialog();
//                Log.e("err------", e.getMessage());
//                Toast.makeText(AdsActivity.this, "网络链接失败，请重新操作", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onResponse(String response) {
//                closeProDialog();
//
//                Log.e("..................",response);
//
//            }
//        }, null);

    }

    private void modAdsMethod(){
        String nname = etNname.getEditableText().toString();
        String name = etName.getEditableText().toString();
        String detail = etDetail.getEditableText().toString();
        String hname = etHname.getEditableText().toString();
        String phone = etPhone.getEditableText().toString();
        String area = etArea.getEditableText().toString();
        String ppFree = etPpFree.getEditableText().toString();
        String date = tvDate.getText().toString();
        province = ads.getProvince();
        city = ads.getCity();
        country = ads.getCounty();
        if (!StringUtils.isNotBlank(nname, name, detail, phone, area, hname, ppFree, date)) {
            Toast.makeText(AdsActivity.this, "小区信息请填写完整", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("udid", String.valueOf(ads.getUdid())),
                new OkHttpClientManager.Param("plotnickname", nname),
                new OkHttpClientManager.Param("province", province),
                new OkHttpClientManager.Param("city", city),
                new OkHttpClientManager.Param("county", country),
                new OkHttpClientManager.Param("plot", name),
                new OkHttpClientManager.Param("address", detail),
                new OkHttpClientManager.Param("household", hname),
                new OkHttpClientManager.Param("tel", phone),
                new OkHttpClientManager.Param("houserarea", area),
                new OkHttpClientManager.Param("propertyfee", ppFree),
                new OkHttpClientManager.Param("expirationtime", date),
//                new OkHttpClientManager.Param("isDefault", String.valueOf(isDefault))

        };

        Log.e("************",nname+" 1 "+province+" 2 "+city+" 3 "+country+" 4 "+name+" 5 "+detail+"  6 "+hname+"  7 "+phone+"  8 "+area+"  9 "+ppFree+" 10 "+date+"  11  "+isDefault);

        OkHttpClientManager.postAsyn(HttpUtil.modAdsUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Log.e("********",e.getMessage());
                Toast.makeText(AdsActivity.this, "网络链接失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Message.Comment>>(){}.getType());
                if (json.result == 1) {
                    Toast.makeText(AdsActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AdsActivity.this, json.msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }


}
