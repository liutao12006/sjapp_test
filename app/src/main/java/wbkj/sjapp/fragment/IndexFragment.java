package wbkj.sjapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import wbkj.sjapp.MainActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.activity.DiscountProActivity;
import wbkj.sjapp.activity.LifeHelpActivity;
import wbkj.sjapp.activity.PaymentActivity;
import wbkj.sjapp.activity.PointsShopActivity;
import wbkj.sjapp.activity.SecondActivity;
import wbkj.sjapp.activity.qrscan.MipcaActivityCapture;
import wbkj.sjapp.adapter.AdImageAdapter;
import wbkj.sjapp.models.Banner;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;

/**
 *	首页
 */
public class IndexFragment extends Fragment {

	private View mView;
	private MainActivity baseActivity;
	private ViewPager viewPager;
	private LinearLayout linearPoint;
	private AdImageAdapter viewAdapter;
	private ArrayList<Banner> adList = new ArrayList<>();
	private List<View> pointList = new ArrayList<>();
	private int currentItem = 0; // 当前视图的索引号
	private ImageView imgDis, imgPoint;
	private TextView tvPP, tvTw, tvLf;
//	private ImageButton imgScan;
	private TextView tvFSL;

	private MyApplication app;
	private SharedPreferencesUtil sp;

	public  static String[] data;

	public  static  double Longitude, Latitude;//定位的经纬度

//	private AMapLocationClient locationClient = null;
//	private AMapLocationClientOption locationOption = new AMapLocationClientOption();

	//声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	//声明定位回调监听器
	public AMapLocationListener mLocationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(AMapLocation aMapLocation) {

			if (aMapLocation != null) {
				if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
//					Log.e("****************",aMapLocation.getAddress());
//
//					Log.e("****************",aMapLocation.getCity());
//					Toast.makeText(getActivity(),aMapLocation.getCity()+"",Toast.LENGTH_LONG).show();

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.baseActivity=(MainActivity) getActivity();
		app = (MyApplication) this.baseActivity.getApplication();
		sp = new SharedPreferencesUtil(this.baseActivity);


		//初始化定位
//		initLocation();
//		initLocation();
       //开始定位
//		startLocation();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		mView = inflater.inflate(R.layout.fragment_index, null, false);
		initView(mView);
		getPicMethod();

		getCountMethod();

		return mView;
	}

	private void initView(View v) {
		viewPager = (ViewPager) v.findViewById(R.id.index_vp);
		linearPoint = (LinearLayout) v.findViewById(R.id.linear_point);
		tvPP = (TextView) v.findViewById(R.id.index_tv_agent);
		tvTw = (TextView) v.findViewById(R.id.index_tv_tw);
		tvLf = (TextView) v.findViewById(R.id.index_tv_life);

		tvFSL = (TextView) v.findViewById(R.id.index_scan);
		tvFSL.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				baseActivity.skipActivity(baseActivity, MipcaActivityCapture.class, null);
				Toast.makeText(getActivity(),"大管家当前的用户数为;"+data[1].substring(0,data[1].length()-1),Toast.LENGTH_LONG).show();
			}
		});
		imgDis = (ImageView) v.findViewById(R.id.index_img_dis);
		imgDis.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				baseActivity.skipActivity(baseActivity, DiscountProActivity.class, null);
			}
		});
		imgPoint = (ImageView) v.findViewById(R.id.index_img_point);
		imgPoint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				baseActivity.skipActivity(baseActivity, PointsShopActivity.class, null);
			}
		});
		tvTw.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				baseActivity.skipActivity(baseActivity, SecondActivity.class, null);
			}
		});
		tvLf.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				baseActivity.skipActivity(baseActivity, LifeHelpActivity.class, null);
			}
		});
		tvPP.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				baseActivity.skipActivity(baseActivity, PaymentActivity.class, null);
			}
		});

	}

	private void getPicMethod() {
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
				new OkHttpClientManager.Param("type", "1")
		};
		OkHttpClientManager.postAsyn(HttpUtil.getImgUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
			@Override
			public void onError(Request request, Exception e) {
				Toast.makeText(baseActivity, "网络请求失败,请重新操作", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(JsonElement response) {
				JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Banner>>(){}.getType());
				if (json.result == 1) {
					adList.addAll(json.data);
				}
				if (adList.size() > 0) {
					initViewPager();
					initPointImg();
					handler.postDelayed(switchImageThread, Contants.PIC_SWITCH_TIME);
				}
			}
		}, null);
	}

	private void getCountMethod() {

		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
		};

		OkHttpClientManager.postAsyn(HttpUtil.fslUrl, params, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {
				Toast.makeText(baseActivity, "网络请求失败,请重新操作", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(String response) {
				data = response.split(":");
				tvFSL.setText("粉丝量:"+data[1].substring(0,data[1].length()-1));

			}
		}, null);
	}


	/**
	 * 初始化导航
	 */
	private void initViewPager() {
		viewAdapter = new AdImageAdapter(baseActivity, adList);
		viewPager.setAdapter(viewAdapter);
		viewPager.setCurrentItem(0);
		/**
		 * 1-1.为ViewPager添加事件
		 */
		if(adList.size() > 1){
			viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

				private int oldPosition = 0;//保存旧的指示图片的位置
				@Override
				public void onPageSelected(int position) {
					currentItem = position;
					pointList.get(position % adList.size()).setBackgroundResource(R.mipmap.feature_point_cur);
					pointList.get(oldPosition % adList.size()).setBackgroundResource(R.mipmap.feature_point);
					oldPosition = position;
				}
				@Override
				public void onPageScrolled(int position, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
		}
	}
	/**
	 * 初始化指示图片
	 */
	private void initPointImg(){
		if(pointList.size()>0){
			pointList.clear();
		}
		if(linearPoint.getChildCount()>0){
			linearPoint.removeAllViews();
		}
		for (int i = 0; i < adList.size(); i++) {
			ImageView imView=new ImageView(getActivity());
			if(i==0){
				imView.setBackgroundResource(R.mipmap.feature_point_cur);
			}else{
				imView.setBackgroundResource(R.mipmap.feature_point);
			}
			pointList.add(imView);
			linearPoint.addView(imView);
		}
	}
	/**
	 * 执行图片切换任务的线程类
	 */
	Runnable switchImageThread = new Runnable() {

		@Override
		public void run() {
			currentItem++;
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
			handler.postDelayed(this, Contants.PIC_SWITCH_TIME);
		}
	};
	/**
	 * 用于处理当前需要显示图片
	 */
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
		};
	};

	@Override
	public void onPause() {
		super.onPause();
		handler.removeCallbacks(switchImageThread);
	}
//
//	/**
//	 * 初始化定位
//	 *
//	 * @since 2.8.0
//	 * @author hongming.wang
//	 *
//	 */
//	private void initLocation(){
//		//初始化client
//		locationClient = new AMapLocationClient(getActivity().getApplicationContext());
//		//设置定位参数
//		locationClient.setLocationOption(getDefaultOption());
//		// 设置定位监听
//		locationClient.setLocationListener(locationListener);
//	}
//
//	/**
//	 * 默认的定位参数
//	 * @since 2.8.0
//	 * @author hongming.wang
//	 *
//	 */
//	private AMapLocationClientOption getDefaultOption(){
//		AMapLocationClientOption mOption = new AMapLocationClientOption();
//		mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
//		mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
//		mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
//		mOption.setInterval(200);//可选，设置定位间隔。默认为2秒
//		mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
//		mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
//		mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
//
//		return mOption;
//	}
//
//	/**
//	 * 定位监听
//	 */
//	AMapLocationListener locationListener = new AMapLocationListener() {
//		@Override
//		public void onLocationChanged(AMapLocation aMapLocation) {
//			if (aMapLocation != null) {
//				if (aMapLocation.getErrorCode() == 0) {
////可在其中解析amapLocation获取相应内容。
//					Log.e("****************",aMapLocation.getAddress());
//
//					Log.e("****************",aMapLocation.getCity());
//					Toast.makeText(getActivity(),aMapLocation.getCity()+"",Toast.LENGTH_LONG).show();
////					tv.setText(aMapLocation.getAddress()+"         "+aMapLocation.getLatitude()+"    "+aMapLocation.getLongitude());
//				}else {
//					//定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//					Log.e("···········","location Error, ErrCode:"
//							+ aMapLocation.getErrorCode() + ", errInfo:"
//							+ aMapLocation.getErrorInfo());
//
//
//				}
//			}
////			if (null != loc) {
////				//解析定位结果    这个定位结果需要手机打开位置信息  否则是定位失败的 还有就是需要一些时间 速度比较慢
////
////				 Longitude=loc.getLongitude();
////				 Latitude = loc.getLatitude();
//////				Toast.makeText(getActivity().getApplicationContext(),Longitude+"...."+Latitude,Toast.LENGTH_LONG).show();
////
////			} else {
////				Toast.makeText(getActivity().getApplicationContext(),"定位失败",Toast.LENGTH_LONG).show();
////			}
//		}
//	};
//
//	/**
//	 * 开始定位
//	 *
//	 * @since 2.8.0
//	 * @author hongming.wang
//	 *
//	 */
//	private void startLocation(){
//		//根据控件的选择，重新设置定位参数
//		// resetOption();
//		// 设置定位参数
//		locationClient.setLocationOption(locationOption);
//		// 启动定位
//		locationClient.startLocation();
//	}

	private void initLocation() {

		//初始化定位
		mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
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

		//单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
		mLocationOption.setHttpTimeOut(20000);

		//关闭缓存机制    默认为开启
		mLocationOption.setLocationCacheEnable(false);



		//给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		//启动定位
		mLocationClient.startLocation();



	}


}
