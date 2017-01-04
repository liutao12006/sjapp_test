package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.fragment.IndexFragment;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Shop;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;

public class MapActivity extends BaseActivity implements AMapLocationListener, LocationSource {

    private Toolbar toolbar;

    private MyApplication app;
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Circle circle;

    private List<Shop> shops = new ArrayList<>();

    private String  localtionX;
    private String  localtionY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApplication) getApplication();
        setContentView(R.layout.activity_map);
        if(getIntent().getBundleExtra(Contants.ARGUMENTS_NAME)!=null){
            localtionX = getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getString("localtionX");
            localtionY = getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getString("localtionY");
        }
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        initView();

        getDataMethod();
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

        if (aMap == null) {
            aMap = mapView.getMap();
//			mUiSettings = aMap.getUiSettings();
        }
//        aMap.moveCamera(CameraUpdateFactory
//                .newLatLngZoom(new LatLng(app.getaMapLocation().getLatitude(),
//                        app.getaMapLocation().getLongitude()), 14));
//        aMap.addMarker(new MarkerOptions().position(new LatLng(app.getaMapLocation().getLatitude(), app.getaMapLocation().getLongitude())));
//上面的定位有问题  我来这样试试


//        if(IndexFragment.Longitude==0.0&&localtionX==null){
        if(app.Longitude==0.0&&localtionX==null){
            IndexFragment.Longitude=113.296333;
            IndexFragment.Latitude =34.80299;
        }else {
//            IndexFragment.Longitude=Double.valueOf(localtionX);
//            IndexFragment.Latitude =Double.valueOf(localtionY);
            IndexFragment.Longitude=app.Longitude;
            IndexFragment.Latitude =app.Latitude;
        }

        aMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(IndexFragment.Latitude,
                        IndexFragment.Longitude), 14));
        aMap.addMarker(new MarkerOptions().position(new LatLng(IndexFragment.Latitude,IndexFragment.Longitude)));


//        MyLocationStyle locationStyle = new MyLocationStyle();
//        locationStyle.strokeColor(Color.BLACK);
//        locationStyle.strokeWidth(0);
//        locationStyle.radiusFillColor(Color.TRANSPARENT);
//        locationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_loc));
//        aMap.setMyLocationStyle(locationStyle);//设置定位点样式
//        aMap.setLocationSource(this);//设置监听
//		aMap.setMyLocationEnabled(true);//启动定位图层

//        LatLng latLng = new LatLng(34.807394, 113.301866);
//
//        aMap.addMarker(new MarkerOptions().title("111")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_loc))
//                .position(latLng));
//        aMap.addMarker(new MarkerOptions().title("222")
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_loc))
//                .position(new LatLng(34.808098, 113.304821)));
//        circle = aMap.addCircle(new CircleOptions().
//                center(latLng).
//                radius(1000).
//                fillColor(Color.argb(50, 1, 1, 1)).
//                strokeColor(Color.argb(50, 1, 1, 1)).
//                strokeWidth(5));

        for (int i = 0; i < shops.size(); i++) {
            Shop shop = shops.get(i);
            Logger.e(shop.toString());
            aMap.addMarker(new MarkerOptions()
                    .title(shop.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_loc))
                    .position(new LatLng(Double.parseDouble(shop.getLocationY()), Double.parseDouble(shop.getLocationX()))))
                    .setObject(shop);
        }
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Shop shop = (Shop) marker.getObject();
                Bundle bundle = new Bundle();
                bundle.putInt("shopId", shop.getId());
                skipActivity(MapActivity.this, ShopDetailActivity.class, bundle);
            }
        });

    }

    private void getDataMethod() {
//        Logger.e(app.getaMapLocation().getLatitude()+","+app.getaMapLocation().getLongitude());
//        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
//                new OkHttpClientManager.Param("x", String.valueOf(app.getaMapLocation().getLongitude())),
//                new OkHttpClientManager.Param("y", String.valueOf(app.getaMapLocation().getLatitude()))
//        };

        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("x", String.valueOf(IndexFragment.Longitude)),
                new OkHttpClientManager.Param("y", String.valueOf(IndexFragment.Latitude))
        };

        OkHttpClientManager.postAsyn(HttpUtil.nearByshopUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(MapActivity.this, "网络连接失败,请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Shop>>(){}.getType());
                if (json.result == 1) {
                    shops.addAll(json.data);
                    initView();
                }
            }
        }, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mapView.onPause();
        deactivate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

}
