package wbkj.sjapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.utils.ActivityUtil;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;

public class SetupActivity extends BaseActivity {

    private Toolbar toolbar;
    private RelativeLayout rlAds, rlCache, rlTel, rlVersion;
    private TextView tvUs, tvIdea, tvCache, tvTel, tvVersion;
    private Button btnLgout;

    private SharedPreferencesUtil sp;
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        app = (MyApplication) getApplication();
        setContentView(R.layout.activity_setup);
        initView();
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

        rlCache = (RelativeLayout) findViewById(R.id.rl_set_cache);
        rlTel = (RelativeLayout) findViewById(R.id.rl_set_tel);
        tvUs = (TextView) findViewById(R.id.tv_set_us);
        tvIdea = (TextView) findViewById(R.id.tv_set_idea);
        tvCache = (TextView) findViewById(R.id.tv_set_cache);
        tvTel = (TextView) findViewById(R.id.tv_set_tel);
        btnLgout = (Button) findViewById(R.id.btn_logout);
        tvVersion = (TextView) findViewById(R.id.tv_set_version);
        rlVersion = (RelativeLayout) findViewById(R.id.rl_set_version);
        tvVersion.setText(app.localVersionName);

        rlCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog("清楚缓存", "确定清楚应用的缓存信息?", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cleanInternalCache(SetupActivity.this);
                        tvCache.setText("0K");
                    }
                }, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            }
        });
        rlVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regMethod();
            }
        });
        tvUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipActivity(SetupActivity.this, AboutUsActivity.class, null);
            }
        });
        tvIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipActivity(SetupActivity.this, FeedbackActivity.class, null);
            }
        });
        tvTel.setText(Contants.Tel);
        rlTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Contants.Tel));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        btnLgout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.logout();
                skipActivity(SetupActivity.this, LoginActivity.class, null);
                ActivityUtil.exitAll();
            }
        });
    }

    /*
     *  更新版本
     */
    private void regMethod() {
        OkHttpClientManager.getAsyn(HttpUtil.appUrl, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(SetupActivity.this, "网络连接失败,请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                //得到的数据是 {result: 1,description: "1.更新了什么什么内容",name: "1.1.2",url: "apk/sjgj.apk",version: "4"}
                final JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    if (json.version <= app.localVersion) {
                        Toast.makeText(SetupActivity.this, "当前版本已经是最新版本", Toast.LENGTH_SHORT).show();
                    } else {
                        showAlertDialog("版本更新", "更新应用最新的版本", "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri uri = Uri.parse(HttpUtil.URL+json.url);
                                startActivity(new Intent(Intent.ACTION_VIEW, uri));
                            }
                        }, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                    }
                } else {
                    Toast.makeText(SetupActivity.this, json.msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        getCacheSize(getPackageName());

    }

    /**
     * 获取缓存大小
     */
    private void getCacheSize(final String mPackageName) {
        try {
            Method getPackageSizeInfo = getPackageManager().getClass().getMethod(
                    "getPackageSizeInfo", String.class, IPackageStatsObserver.class);

            getPackageSizeInfo.invoke(getPackageManager(), mPackageName, new IPackageStatsObserver.Stub() {
                @Override
                public void onGetStatsCompleted(final PackageStats pStats, boolean succeeded)
                        throws RemoteException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvCache.setText(Formatter.formatFileSize(getApplication(), pStats.cacheSize));
                            Log.e("content", pStats.toString());
                        }
                    });
                }
            });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     *
     * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
