package wbkj.sjapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import wbkj.sjapp.fragment.FragmentContainer;
import wbkj.sjapp.fragment.IndexFragment;
import wbkj.sjapp.fragment.MineFragment;
import wbkj.sjapp.fragment.NearFragment;
import wbkj.sjapp.fragment.ProductFragment;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.utils.ActivityUtil;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;

public class MainActivity extends BaseActivity {

    protected Stack<FragmentContainer> fragmentStack = new Stack<FragmentContainer>();
    protected FragmentContainer currentContainer;
    protected static Map<String, FragmentContainer> fragmentClassMap = new HashMap<String, FragmentContainer>();

    private SharedPreferencesUtil sp;
    private MyApplication app;

    static{
        fragmentClassMap.put("index", new FragmentContainer(R.id.home_tab_index, IndexFragment.class) );
        fragmentClassMap.put("pro", new FragmentContainer(R.id.home_tab_product, ProductFragment.class));
        fragmentClassMap.put("near", new FragmentContainer(R.id.home_tab_near, NearFragment.class));
        fragmentClassMap.put("mine", new FragmentContainer(R.id.home_tab_mine, MineFragment.class));
    }

    protected FragmentManager fragmentManager;

    private RadioGroup radioGroup;
    private RadioButton rbIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame);
        checkMethod();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sp = new SharedPreferencesUtil(this);
        app = getApplicationContext();
        fragmentManager = getSupportFragmentManager();

        rbIndex = (RadioButton) findViewById(R.id.home_tab_index);
        rbIndex.setSelected(true);
        radioGroup = (RadioGroup)findViewById(R.id.bottomRg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton currentRB = (RadioButton) findViewById(currentContainer.getId());

//                if(checkedId == R.id.home_tab_order && !sp.isLogin()){
//                    RadioButton orderButton = (RadioButton)findViewById(checkedId);
//                    orderButton.setSelected(false);
//                    orderButton.setChecked(false);
//                    currentRB.setSelected(true);
//                    currentRB.setChecked(true);
//                    skipActivity(MainActivity.this, LoginActivity.class, null);
//                    return;
//                }

                currentRB.setTextColor(Color.BLACK);
                RadioButton rb = (RadioButton) findViewById(checkedId);
                boolean buttonChecked = rb.isChecked();
                if (buttonChecked) {//用户点击触发,或后退键出发

                    if (checkedId != currentContainer.getId()) {
                        unSelectCurrentTab();
                        rb.setTextColor(getResources().getColor(R.color.black));
                        switchTab(checkedId);
                    }
                    if (checkedId != R.id.home_tab_index) {
                        RadioButton tabRB = (RadioButton) findViewById(R.id.home_tab_index);
                        tabRB.setSelected(false);
                        tabRB.setChecked(false);
                    }
                    rb.setTextColor(getResources().getColor(R.color.home_font));
                }
            }
        });
        if(savedInstanceState == null){
            rbIndex.setTextColor(getResources().getColor(R.color.home_font));
            currentContainer = new FragmentContainer();
            currentContainer.setId(R.id.home_tab_index);
            switchTab(R.id.home_tab_index);
        }
    }

    /**
     * @param to 需要显示的fragment
     */
    public void switchContent(Fragment to, boolean addToStack) {

        if (currentContainer.getFragment() != to) {
            FragmentTransaction transaction = fragmentManager.beginTransaction().setCustomAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out);
            if(currentContainer.getFragment() != null){
                transaction.hide(currentContainer.getFragment());
                if(addToStack){
                    FragmentContainer container = new FragmentContainer(currentContainer.getId(),currentContainer.getFragment());
                    fragmentStack.add(container);
                }
            }
            for(FragmentContainer f:fragmentStack){
                transaction.hide(f.getFragment());
            }
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.add(R.id.content_frame, to); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.show(to); // 隐藏当前的fragment，显示下一个
            }
            transaction.commit();
            currentContainer.setFragment(to);
        }
    }
    /**
     * 取消当前tab的选中状态
     */
    public void unSelectCurrentTab(){
        RadioButton button = (RadioButton)findViewById(currentContainer.getId());
        button.setTextColor(Color.BLACK);
        button.setSelected(false);
        button.setChecked(false);
    }

    /**
     * 选中目标tab
     * @param id
     */
    public void selectTab(int id){
        RadioButton button = (RadioButton)findViewById(id);
        button.setTextColor(getResources().getColor(R.color.home_font));
        button.setSelected(true);
        button.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                break;
        }
        return true;
    }

    private void showExitDialog(){
        showAlertDialog("退出程序", "确定退出？", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fragmentStack.clear();
                ActivityUtil.exitAll();
                System.exit(0);
            }
        }, "取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public void switchTab(int checkedId) {
        switch (checkedId){
            case R.id.home_tab_index:
                fragmentStack.clear();
                this.switchFragment("index");
                break;
            case R.id.home_tab_product:
                this.switchFragment("pro");
                break;
            case R.id.home_tab_mine:
                this.switchFragment("mine");
                break;
            case R.id.home_tab_near:
                this.switchFragment("near");
                break;

            default:break;
        }
    }

    public void  switchFragment(String tag){
        FragmentContainer container = fragmentClassMap.get(tag);
        try {
            Fragment fragment = container.getFragment();
            if(fragment == null){
                fragment = (Fragment)(container.getFragmentClass().newInstance());
            }
            switchContent(fragment, true);
            container.setFragment(fragment);
            currentContainer.setFragment(fragment);
            currentContainer.setId(container.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkMethod() {
        OkHttpClientManager.getAsyn(HttpUtil.appUrl, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(MainActivity.this, "网络连接失败,请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                final JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    if (json.version <= app.localVersion) {
//                        Toast.makeText(MainActivity.this, "当前版本已经是最新版本", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, json.msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
