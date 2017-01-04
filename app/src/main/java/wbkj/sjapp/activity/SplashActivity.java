package wbkj.sjapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import wbkj.sjapp.MainActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;

public class SplashActivity extends Activity {

	private SharedPreferencesUtil sp;

	private boolean isFirstIn=false;//是否是第一次启动程序
	private static final int GO_HOME=0;//跳转到主界面
	private static final int GO_GUIDE=1;//跳转到引导界面
	// 延迟3秒
	private static final long SPLASH_DELAY_MILLIS = 4000;


	//声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	private MyApplication app;

//	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				//goGuide();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = new SharedPreferencesUtil(this);
		app = (MyApplication) getApplication();
		setContentView(R.layout.activity_splash);
		init();

		//初始化定位
		mLocationClient = new AMapLocationClient(getApplicationContext());
		//设置定位回调监听
		mLocationClient.setLocationListener(mAMapLocationListener);
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			//申请WRITE_EXTERNAL_STORAGE权限
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
							Manifest.permission.ACCESS_FINE_LOCATION},
					2);
		}else {
			initLocation();
		}
	}
	
	private void init() {
/*		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				Contants.SPLASH_NAME, MODE_PRIVATE);

		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (!isFirstIn) {
			// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}*/
		
		mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		
	}
	
	private void goHome() {
		if (sp.isLogin()) {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		} else {
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			SplashActivity.this.startActivity(intent);
			SplashActivity.this.finish();
		}
	}

/*	private void goGuide() {
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}*/

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 2) {
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// Permission Granted
				initLocation();
			} else {
				// Permission Denied
				Toast.makeText(this, "访问被拒绝！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void initLocation() {
		//声明mLocationOption对象
		AMapLocationClientOption mLocationOption = null;
		//初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		//设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		//设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(true);

		if(mLocationOption.isOnceLocationLatest()){
			mLocationOption.setOnceLocationLatest(true);
			//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
			//如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
		}

		//设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		//设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		//设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(2000);
		//给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		//启动定位
		mLocationClient.startLocation();
	}

	AMapLocationListener mAMapLocationListener = new AMapLocationListener(){
		@SuppressLint("SetTextI18n")
		@Override
		public void onLocationChanged(AMapLocation amapLocation) {
			if (amapLocation != null) {
				if (amapLocation.getErrorCode() == 0) {
					//定位成功回调信息，设置相关消息
					amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
					amapLocation.getLatitude();//获取纬度
					amapLocation.getLongitude();//获取经度
					amapLocation.getAccuracy();//获取精度信息
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(amapLocation.getTime());
					df.format(date);//定位时间
					amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
					amapLocation.getCountry();//国家信息
					amapLocation.getProvince();//省信息
					amapLocation.getCity();//城市信息
					amapLocation.getDistrict();//城区信息
					amapLocation.getStreet();//街道信息
					amapLocation.getStreetNum();//街道门牌号信息
					amapLocation.getCityCode();//城市编码
					amapLocation.getAdCode();//地区编码
					amapLocation.getAoiName();//获取当前定位点的AOI信息

					app.setaMapLocation(amapLocation);

				} else {
					//显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
					Logger.e("AmapError","location Error, ErrCode:"
							+ amapLocation.getErrorCode() + ", errInfo:"
							+ amapLocation.getErrorInfo());
				}
			}
		}
	};
	
}
