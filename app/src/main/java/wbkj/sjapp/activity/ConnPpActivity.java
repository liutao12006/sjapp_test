package wbkj.sjapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.adapter.PpRecyAdatper;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Property;
import wbkj.sjapp.utils.DividerItemDecoration;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;
import wbkj.sjapp.utils.OkHttpClientManager;

public class ConnPpActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView recyView;
    private PpRecyAdatper adapter;
    private List<Property> lists = new ArrayList<>();

    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplication();
        setContentView(R.layout.activity_conn_pp);
        initView();
        getDataMethod();
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

        recyView = (RecyclerView) findViewById(R.id.pp_recy);
        recyView.setHasFixedSize(true);
        recyView.setLayoutManager(new LinearLayoutManager(this));
        recyView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void getDataMethod() {
        OkHttpClientManager.getAsyn(HttpUtil.ppUrl, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(ConnPpActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Property>>(){}.getType());
                if (json.result == 1) {
                    lists.addAll(json.data);
                    if (adapter == null) {
                        adapter = new PpRecyAdatper(lists, ConnPpActivity.this);
                        recyView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                    adapter.setOnItemClickListener(new MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {
                            showAlertDialog("是否拨打电话", "确定拨打该物业服务电话?", "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + lists.get(position).getWyphone()));
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }, "取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                        }
                    });
                }
            }
        }, null);
    }

}
