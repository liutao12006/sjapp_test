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
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.adapter.NotesRecyAdatper;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Notes;
import wbkj.sjapp.utils.DividerItemDecoration;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;

public class PayNotesActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView recyView;
    private NotesRecyAdatper adapter;
    private List<Notes> lists = new ArrayList<>();

    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_pay_notes);
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

        recyView = (RecyclerView) findViewById(R.id.notes_recy);
        recyView.setHasFixedSize(true);
        recyView.setLayoutManager(new LinearLayoutManager(this));
        recyView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }

    private void getDataMethod() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
        };
        OkHttpClientManager.postAsyn(HttpUtil.payNotesUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(PayNotesActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Notes>>(){}.getType());
                if (json.result == 1) {
                    lists.addAll(json.data);
                    if (adapter == null) {
                        adapter = new NotesRecyAdatper(lists, PayNotesActivity.this);
                        recyView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }, null);
    }
}
