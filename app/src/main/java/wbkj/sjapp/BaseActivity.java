package wbkj.sjapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import wbkj.sjapp.utils.ActivityUtil;
import wbkj.sjapp.utils.Contants;


/**
 * Created by Zz on 2015/7/9 0009.
 */
public class BaseActivity extends AppCompatActivity {

    protected Dialog proDialog;
    private int mProgress = 0;
    private boolean running;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        ActivityUtil.add(this);
    }

    /**
     * 获取手机屏幕的宽度和高度
     * @return
     */
    public Map<String, Integer> getWidthHeight(Context context){
        Map<String, Integer> whMap = new HashMap<String, Integer>();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width=metrics.widthPixels;// 屏幕宽度（像素）
        int height=metrics.heightPixels;// 屏幕高度（像素）
//		float density = metrics.density;      // 屏幕密度（0.75 / 1.0 / 1.5）
//      int densityDpi = metrics.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        whMap.put("width", width);
        whMap.put("height", height);
//        whMap.put("width", px2dip(context,width));
//        whMap.put("height", px2dip(context,height));
        //System.out.println("屏幕的宽度=================="+width+",height="+height);
        //System.out.println("屏幕的宽度=================="+px2dip(context,width)+",height dp="+px2dip(context,height));
        return whMap;
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

    /** * 根据手机的分辨率从dp 的单位 转成为px(像素) */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /** * 根据手机的分辨率从px(像素) 的单位 转成为dp */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 跳转Activity
     * @param currentActivity	当前Activity
     * @param toActivityClass	目标Activity
     * @param arguments			跳转时携带的参数
     */
    public void skipActivity(Context currentActivity,Class toActivityClass,Bundle arguments){
        Intent intent=new Intent();
        intent.setClass(currentActivity, toActivityClass);
        if(arguments!=null){
            intent.putExtra(Contants.ARGUMENTS_NAME, arguments);
        }
        startActivity(intent);
    }


    /**
     * 重写getApplicationContext方法，获取到我们所需要的Application对象
     */
    @Override
    public MyApplication getApplicationContext() {
        return (MyApplication)super.getApplicationContext();
    }

    /** 含有标题、内容、两个按钮的对话框 **/
    public void showAlertDialog(String title, String message,
                                String positiveText,
                                DialogInterface.OnClickListener onClickListener,
                                String negativeText,
                                DialogInterface.OnClickListener onClickListener2) {
        new AlertDialog.Builder(this).setTitle(title).setMessage(message)
                .setPositiveButton(positiveText, onClickListener)
                .setNegativeButton(negativeText, onClickListener2)
                .show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if(proDialog!=null){
            proDialog.dismiss();
            proDialog = null;
        }
    }

    public void closeProDialog(){
        if(proDialog!=null){
            proDialog.dismiss();
            running = false;
        }
    }

    public Dialog createLoadingDialog(Context context, String msg){

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loadingdialog_layout, null);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局

        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画

        tipTextView.setText(msg);// 设置加载信息
        proDialog = new Dialog(context, R.style.customProgressDialog);// 创建自定义样式dialog

        proDialog.setCancelable(true);// 可以用“返回键”取消
        proDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return proDialog;
    }




}
