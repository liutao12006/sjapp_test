package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.Orders;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class CommentActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView img;
    private EditText etCmt;
    private RatingBar ratS, ratP, ratZ;
    private Button btnOk;

    private Orders order;
    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        sp = new SharedPreferencesUtil(this);
        order = (Orders) getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getSerializable("order");
        initView();
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

        img = (ImageView) findViewById(R.id.cmt_pro_img);
        Picasso.with(this).load(HttpUtil.IMGURL+order.getWdetails().getWare().getImg()).into(img);
        etCmt = (EditText) findViewById(R.id.cmt_et_txt);
        ratS = (RatingBar) findViewById(R.id.cmt_rating_s);
        ratP = (RatingBar) findViewById(R.id.cmt_rating_p);
        ratZ = (RatingBar) findViewById(R.id.cmt_rating_z);
        btnOk = (Button) findViewById(R.id.comment_ok);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmt = etCmt.getEditableText().toString();
                if (!StringUtils.isNotBlank(cmt)) {
                    Toast.makeText(CommentActivity.this, "请添加评论内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                int i1, i2, i3;
                i1 = (int) ratS.getRating();
                i2 = (int) ratP.getRating();
                i3 = (int) ratZ.getRating();
                if (i1 == 0 || i2 == 0 || i3 == 0) {
                    Toast.makeText(CommentActivity.this, "请添加评分信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                addCmt(i1, i2, i3, cmt);
            }
        });

    }

    private void addCmt(int i1, int i2, int i3, String content) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("wid", String.valueOf(order.getWdetails().getWid())),
                new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("cmcontent", content),
//                new OkHttpClientManager.Param("oid", order.getOid()),
                new OkHttpClientManager.Param("selevel", String.valueOf(i1)),
                new OkHttpClientManager.Param("adlevel", String.valueOf(i2)),
                new OkHttpClientManager.Param("lolevel", String.valueOf(i3))
        };
        OkHttpClientManager.postAsyn(HttpUtil.addCmt, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(CommentActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    Toast.makeText(CommentActivity.this, "发表评论成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(CommentActivity.this, "评论发表失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

}
