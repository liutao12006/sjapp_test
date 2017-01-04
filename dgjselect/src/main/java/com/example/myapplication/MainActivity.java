package com.example.myapplication;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends BaseActivity {

    private EditText  num_ed;
    private Button  submit,search_num;
    private TextView tv_id,tv_pro,tv_count,tv_mani,tv_date,tv_phone,tv_addr;

    String state,oid;
    Spinner mSpinner;
    List<String> states = new ArrayList<String>();

    JSONObject jsonobj2;
    JSONObject jsonobj6;

    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        oid = String.valueOf(jsonobj2.get("oid"));
                        tv_id.setText(String.valueOf(jsonobj2.get("oid")));
                        tv_count.setText(jsonobj2.get("tote").toString());
                        tv_mani.setText(jsonobj2.get("total").toString());
                        tv_date.setText(jsonobj2.get("odate").toString());
                        tv_phone.setText(jsonobj2.get("col2").toString());
                        tv_addr.setText(jsonobj2.get("col3").toString());

                        JSONObject jsonobj3 = jsonobj2.getJSONObject("ware");
                        tv_pro.setText(jsonobj3.get("wname").toString());
                        state = String.valueOf(jsonobj3.get("state"));
                        Log.e("^^^^^^^^^",state);

                        mSpinner.setSelection(Integer.valueOf(state),true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();//隐藏actionbar

        initView();

        initData();
    }

    private void initData() {
        states.add("");
        states.add("未付款");
        states.add("已付款");
        states.add("已完成");

        ArrayAdapter<String>  adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,states);
        //设置下拉样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this,"onItemSelected",Toast.LENGTH_LONG).show();
                //选择的时候执行
                state=String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(MainActivity.this,"onNothingSelected",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        num_ed= (EditText) this.findViewById(R.id.num_ed);
        search_num= (Button) this.findViewById(R.id.search_num);

        tv_id= (TextView) this.findViewById(R.id.order_numb);
        tv_pro= (TextView) this.findViewById(R.id.order_pro);
        tv_count= (TextView) this.findViewById(R.id.order_count);
        tv_mani= (TextView) this.findViewById(R.id.order_mani);
        tv_date= (TextView) this.findViewById(R.id.order_date);
        tv_phone= (TextView) this.findViewById(R.id.order_phone);
        tv_addr= (TextView) this.findViewById(R.id.order_addr);

        mSpinner= (Spinner) this.findViewById(R.id.spinner_sp);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_num:
                String num = num_ed.getEditableText().toString();
                if(num.isEmpty()){
                    Toast.makeText(MainActivity.this,"请输入订单号",Toast.LENGTH_LONG).show();
                }else{
                    getOrderInfo(num);
                }
                break;
            case R.id.sunmit_bt:
                if(oid!=null){
                    updateState(oid,state);
                }else{
                    Toast.makeText(MainActivity.this,"请先查询订单信息",Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    private void updateState(String oid, final String state) {

        {
            //创建okHttpClient对象
            OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
            Request request = new Request.Builder()
                    .url("http://120.55.161.215/Modifyorder.do?oid="+oid+"&state="+state)
                    .build();
//new call
            Call call = mOkHttpClient.newCall(request);
//请求加入调度
            call.enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(MainActivity.this,"修改失败，请重试",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String data = response.body().string();
                    try {
                        jsonobj6 = new JSONObject(data);
                        String result = String.valueOf(jsonobj6.get("result"));
                        if("1".equals(result)){

                            /*
                            Can't create handler inside thread that has not called Looper.prepare()
                            报这个错  需要加  Looper.prepare();， Looper.loop();
                             */
                            Looper.prepare();
                            Toast.makeText(MainActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }

    private void getOrderInfo(String num) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
         Request request = new Request.Builder()
                .url("http://120.55.161.215/Queryorderoid.do?oid="+num)
                .build();
//new call
        Call call = mOkHttpClient.newCall(request);
//请求加入调度
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("************","失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String data = response.body().string();
                try {
                    JSONObject jsonobj = new JSONObject(data);

                    jsonobj2 = jsonobj.getJSONObject("data");
//                    handler.sendEmptyMessage(1);
                    Message msg = new Message();
                    msg.what=1;
                    handler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
