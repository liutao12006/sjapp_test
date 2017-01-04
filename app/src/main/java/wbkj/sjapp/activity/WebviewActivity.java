package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.utils.Contants;

public class WebviewActivity extends BaseActivity {

    private Toolbar toolbar;
    private WebView wbview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getString("url");
        boolean isfromlift = getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getBoolean("fromlift");
        setContentView(R.layout.activity_webview);

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
        if(!isfromlift){
            TextView title = (TextView) findViewById(R.id.toolbar_title);
            title.setText("上街之窗");
        }
        wbview = (WebView) findViewById(R.id.webview);
        wbview.loadUrl(url);
    }
}
