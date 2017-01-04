package wbkj.sjapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.adapter.MsgCmtLvAdapter;
import wbkj.sjapp.adapter.MsgImgGvAdapter;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Message;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;
import wbkj.sjapp.views.CustomGridView;
import wbkj.sjapp.views.CustomListView;

public class MsgDetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView imgUser;
    private TextView tvName, tvDate, tvContent;
    private CustomGridView imgGv;
    private CustomListView cmtLv;
    private ImageButton imgBtn;

    private EditText etCmt;
    private Button btnAdd;

    private SharedPreferencesUtil sp;
    private Message msg;
    private boolean isFav;
    private int clid;       //收藏id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        msg = (Message) getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getSerializable("msg");
        setContentView(R.layout.activity_msg_detail);
        initView();
        getFav();
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

        imgUser = (ImageView) findViewById(R.id.msg_user_img);
        if (StringUtils.isNotBlank(msg.getUser().getImg())) {
            Picasso.with(this).load(HttpUtil.IMGURL+msg.getUser().getImg()).into(imgUser);
        }
        tvName = (TextView) findViewById(R.id.msg_user_name);
        tvName.setText(msg.getUser().getNickname());
        tvDate = (TextView) findViewById(R.id.msg_user_date);
        tvDate.setText(msg.getTitle().getTdate().substring(0, 10));
        tvContent = (TextView) findViewById(R.id.msg_content);
        tvContent.setText(msg.getTdcontent());
        imgGv = (CustomGridView) findViewById(R.id.msg_imgs);
        List<String> imgStr = new ArrayList<>();
        if (StringUtils.isNotBlank(msg.getImg2())) imgStr.add(msg.getImg2());
        if (StringUtils.isNotBlank(msg.getImg3())) imgStr.add(msg.getImg2());
        if (StringUtils.isNotBlank(msg.getImg4())) imgStr.add(msg.getImg2());
        if (StringUtils.isNotBlank(msg.getImg5())) imgStr.add(msg.getImg2());
        if (StringUtils.isNotBlank(msg.getImg6())) imgStr.add(msg.getImg2());
        if (StringUtils.isNotBlank(msg.getImg7())) imgStr.add(msg.getImg2());
        imgGv.setAdapter(new MsgImgGvAdapter(imgStr, MsgDetailActivity.this));
        cmtLv = (CustomListView) findViewById(R.id.msg_detail_cmlv);
        cmtLv.setAdapter(new MsgCmtLvAdapter(msg.getCommentList(), MsgDetailActivity.this));

        etCmt = (EditText) findViewById(R.id.msg_et_cmt);
        btnAdd = (Button) findViewById(R.id.msg_btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etCmt.getEditableText().toString();
                if (StringUtils.isNotBlank(content)) {
                    createLoadingDialog(MsgDetailActivity.this, "添加中...").show();
                    addCmtMethod(content);
                }
            }
        });
        imgBtn = (ImageButton) findViewById(R.id.msg_detail_fav);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFav) {
                    getCalFav();
                } else {
                    getAddFav();
                }
            }
        });
    }

    private void addCmtMethod(String content){
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("tid", String.valueOf(msg.getTid())),
                new OkHttpClientManager.Param("cmcontent", content)
        };
        OkHttpClientManager.postAsyn(HttpUtil.addCmtUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                closeProDialog();
                Toast.makeText(MsgDetailActivity.this, "网络链接失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                closeProDialog();
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Message.Comment>>(){}.getType());
                if (json.result == 1) {
                    cmtLv.setAdapter(new MsgCmtLvAdapter(json.data, MsgDetailActivity.this));
                    etCmt.setText("");
                    hintKbTwo();
                } else {
                    Toast.makeText(MsgDetailActivity.this, json.msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    private void getFav() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("cltype", "2"),
                new OkHttpClientManager.Param("quiltid", String.valueOf(msg.getTid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.favStateUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(MsgDetailActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    if (json.is == 1) {
                        isFav = true;
                        imgBtn.setImageResource(R.mipmap.fav_on);
                        clid = json.collect.clid;
                    } else {
                        isFav = false;
                        imgBtn.setImageResource(R.mipmap.fav_off);
                    }
                }
            }
        }, null);
    }

    private void getAddFav() {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("cltype", "2"),
                new OkHttpClientManager.Param("quiltid", String.valueOf(msg.getTid()))
        };
        OkHttpClientManager.postAsyn(HttpUtil.addFavUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(MsgDetailActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    isFav = true;
                    imgBtn.setImageResource(R.mipmap.fav_on);
                    clid = json.collect.clid;
                    Toast.makeText(MsgDetailActivity.this, "添加收藏成功", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MsgDetailActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    isFav = false;
                    imgBtn.setImageResource(R.mipmap.fav_off);
                    Toast.makeText(MsgDetailActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    //此方法只是关闭软键盘
    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
