package wbkj.sjapp.activity;

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
import wbkj.sjapp.R;
import wbkj.sjapp.adapter.NoticeRecyAdatper;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Notice;
import wbkj.sjapp.utils.DividerItemDecoration;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;
import wbkj.sjapp.utils.OkHttpClientManager;

public class NoticeActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView recyView;
    private NoticeRecyAdatper adapter;
    private List<Notice> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
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

        recyView = (RecyclerView) findViewById(R.id.notice_recy);
        recyView.setHasFixedSize(true);
        recyView.setLayoutManager(new LinearLayoutManager(this));
        recyView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void getDataMethod() {
        OkHttpClientManager.getAsyn(HttpUtil.noticeUrl, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(NoticeActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Notice>>(){}.getType());
                if (json.result == 1) {
                    lists.addAll(json.data);
                    if (adapter == null) {
                        adapter = new NoticeRecyAdatper(lists, NoticeActivity.this);
                        recyView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                    adapter.setOnItemClickListener(new MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {

                        }
                    });
                }
            }
        }, null);
    }
}
