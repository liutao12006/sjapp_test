package wbkj.sjapp;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;
import im.fir.sdk.FIR;
import wbkj.sjapp.utils.Contants;

/**
 * @author Administrator
 *	用于保存全局变量
 */
public class  MyApplication extends Application {


	public int localVersion = 0;	//当前app安装版本
	public String localVersionName = null;

	public static int cart_type = 0;

	public  static double Longitude, Latitude;//定位的经纬度

//	public  double getLatitude() {
//		return Latitude;
//	}
//
//	public  double getLongitude() {
//		return Longitude;
//	}

	public AMapLocation  myaMapLocation=null;

	public AMapLocation getaMapLocation() {
		return myaMapLocation;
	}

	public void setaMapLocation(AMapLocation myaMapLocation) {
		this.myaMapLocation = myaMapLocation;
	}

	//声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	//声明定位回调监听器
	public AMapLocationListener mLocationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(AMapLocation aMapLocation) {

			if (aMapLocation != null) {
				myaMapLocation =aMapLocation;
				if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
//					Log.e("****************",aMapLocation.getAddress());
					Longitude=aMapLocation.getLongitude();
					Latitude=aMapLocation.getLatitude();
//					Log.e("****************",aMapLocation.getCity());

					Log.e("****************",Longitude+"      baobao    "+Latitude);

				}else {
					//定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
					Log.e("···········","location Error, ErrCode:"
							+ aMapLocation.getErrorCode() + ", errInfo:"
							+ aMapLocation.getErrorInfo());


				}
			}
		}
	};

	//声明AMapLocationClientOption对象
	public AMapLocationClientOption mLocationOption = null;


	@Override
	public void onCreate() {
		super.onCreate();

		FIR.init(this);

		//mob短信初始化
		SMSSDK.initSDK(this, Contants.APP_Key, Contants.APP_Sectet);


		//初始化极光推送
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		//初始化定位
		initLocation();
//		initLocation();
		//开始定位
//		startLocation();

		//获取当前app版本
		try {
			PackageInfo packageInfo = getApplicationContext()  
                    .getPackageManager().getPackageInfo(getPackageName(), 0);  
            localVersion = packageInfo.versionCode;
			localVersionName = packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		Logger
			.init("SJAPP")                 // default PRETTYLOGGER or use just init()
			.methodCount(2)                 // default 2
			.hideThreadInfo()               // default shown
			.logLevel(LogLevel.FULL)        // default LogLevel.FULL
			.methodOffset(2);               // default 0;

	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}



	private void initLocation() {

		//初始化定位
		mLocationClient = new AMapLocationClient(getApplicationContext());
		//设置定位回调监听
		mLocationClient.setLocationListener(mLocationListener);

		//初始化AMapLocationClientOption对象
		mLocationOption = new AMapLocationClientOption();
		//设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
		mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		//设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
		mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

		//获取一次定位结果：
		//该方法默认为false。
		mLocationOption.setOnceLocation(true);

		//获取最近3s内精度最高的一次定位结果：
		//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
		mLocationOption.setOnceLocationLatest(true);

		//设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		//设置是否强制刷新WIFI，默认为true，强制刷新。
		mLocationOption.setWifiActiveScan(false);
		//设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);

		//关闭缓存机制    默认为开启
		mLocationOption.setLocationCacheEnable(false);



		//给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		//启动定位
		mLocationClient.startLocation();



	}

}
