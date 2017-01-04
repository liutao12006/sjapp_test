package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;

public class BigImageActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView img;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        url = getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getString("img");
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
        img = (ImageView) findViewById(R.id.big_img);
        Picasso.with(this).load(HttpUtil.IMGURL+url).into(img);
    }
}
