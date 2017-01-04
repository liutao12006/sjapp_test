package wbkj.sjapp.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.activity.MsgDetailActivity;
import wbkj.sjapp.activity.PubMsgActivity;
import wbkj.sjapp.adapter.AdImageAdapter;
import wbkj.sjapp.adapter.MsgLvAdapter;
import wbkj.sjapp.models.Banner;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.models.Message;
import wbkj.sjapp.utils.Contants;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.views.CustomListView;

public class NearFragment extends Fragment {

	private View myView;
	private BaseActivity baseActivity;
	private ViewPager viewPager;
	private LinearLayout linearPoint;
	private AdImageAdapter viewAdapter;
	private ArrayList<Banner> adList = new ArrayList<>();
	private List<View> pointList = new ArrayList<>();
	private int currentItem = 0; // 当前视图的索引号

	private ImageButton pubBtn;
	private RadioGroup rgBtn;
	private CustomListView msgLv;
	private MsgLvAdapter msgAdapter;
	private List<Message> msgList = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseActivity = (BaseActivity) getActivity();

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		myView = inflater.inflate(R.layout.fragment_near, null, false);
		initView(myView);
		getPicMethod();
		getDataMethod(1);
		return myView;
	}

	@Override
	public void onResume() {
		super.onResume();
		getDataMethod(1);
	}

	public void initView(View v){
		viewPager = (ViewPager) v.findViewById(R.id.index_vp);
		linearPoint = (LinearLayout) v.findViewById(R.id.linear_point);
		pubBtn = (ImageButton) v.findViewById(R.id.pub_msg_btn);
		pubBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				baseActivity.skipActivity(baseActivity, PubMsgActivity.class, null);
			}
		});

		rgBtn = (RadioGroup) v.findViewById(R.id.msg_rb);
		msgLv = (CustomListView) v.findViewById(R.id.near_list_data);
		msgLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("msg", msgList.get(i));
				baseActivity.skipActivity(baseActivity, MsgDetailActivity.class, bundle);
			}
		});
		rgBtn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				switch (i) {
					case R.id.msg_rb_a:
						getDataMethod(1);
						break;
					case R.id.msg_rb_b:
						getDataMethod(2);
						break;
					case R.id.msg_rb_c:
						getDataMethod(3);
						break;
					default: break;
				}
			}
		});
	}

	private void getDataMethod(int tid) {
		msgList.clear();
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
				new OkHttpClientManager.Param("ttype", String.valueOf(tid))
		};
		OkHttpClientManager.postAsyn(HttpUtil.getMsgUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
			@Override
			public void onError(Request request, Exception e) {
				Toast.makeText(baseActivity, "网络请求失败,请重新操作", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(JsonElement response) {
				JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Message>>(){}.getType());
				if (json.result == 1) {
					msgList.addAll(json.data);
					if (msgAdapter == null) {
						msgAdapter = new MsgLvAdapter(msgList, baseActivity);
						msgLv.setAdapter(msgAdapter);
					} else {
						msgAdapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(baseActivity, json.msg, Toast.LENGTH_SHORT).show();
				}
			}
		}, null);
	}

	private void getPicMethod() {
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
				new OkHttpClientManager.Param("type", "2")
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

}
