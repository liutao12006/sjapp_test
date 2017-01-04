package wbkj.sjapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.activity.MapActivity;
import wbkj.sjapp.activity.ShopActivity;


public class ProductFragment extends Fragment {

	private View myView;
	private BaseActivity baseActivity;

	private ImageButton btnMap;
	private RelativeLayout rlClo;
	private TextView tvEdu, tvHap, tvFood, tvJzjc, tvJydq, tvHotel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.baseActivity = (BaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		myView = inflater.inflate(R.layout.fragment_pro, null, false);
		initView(myView);

		return myView;
	}

	private void initView(View v) {

		btnMap = (ImageButton) v.findViewById(R.id.btn_map);
		btnMap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				baseActivity.skipActivity(baseActivity, MapActivity.class, null);
			}
		});

		MyClick click = new MyClick();
		rlClo = (RelativeLayout) v.findViewById(R.id.shop_layout_clo);
		rlClo.setOnClickListener(click);
		tvEdu = (TextView) v.findViewById(R.id.shop_tv_edu);
		tvEdu.setOnClickListener(click);
		tvHap = (TextView) v.findViewById(R.id.shop_tv_hap);
		tvHap.setOnClickListener(click);
		tvFood = (TextView) v.findViewById(R.id.shop_tv_food);
		tvFood.setOnClickListener(click);
		tvJzjc = (TextView) v.findViewById(R.id.shop_tv_jzjc);
		tvJzjc.setOnClickListener(click);
		tvJydq = (TextView) v.findViewById(R.id.shop_tv_jydq);
		tvJydq.setOnClickListener(click);
		tvHotel = (TextView) v.findViewById(R.id.shop_tv_hotel);
		tvHotel.setOnClickListener(click);

	}

	class MyClick implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			Bundle bundle = new Bundle();
			switch (view.getId()) {
				case R.id.shop_layout_clo:
					bundle.putString("type", "7951");
					bundle.putString("from", "product");
					baseActivity.skipActivity(baseActivity, ShopActivity.class, bundle);
					break;
				case R.id.shop_tv_edu:
					bundle.putString("type", "7952");
					bundle.putString("from", "product");
					baseActivity.skipActivity(baseActivity, ShopActivity.class, bundle);
					break;
				case R.id.shop_tv_hap:
					bundle.putString("type", "7953");
					bundle.putString("from", "product");
					baseActivity.skipActivity(baseActivity, ShopActivity.class, bundle);
					break;
				case R.id.shop_tv_food:
					bundle.putString("type", "7954");
					bundle.putString("from", "product");
					baseActivity.skipActivity(baseActivity, ShopActivity.class, bundle);
					break;
				case R.id.shop_tv_jzjc:
					bundle.putString("type", "7955");
					bundle.putString("from", "product");
					baseActivity.skipActivity(baseActivity, ShopActivity.class, bundle);
					break;
				case R.id.shop_tv_jydq:
					bundle.putString("type", "7956");
					bundle.putString("from", "product");
					baseActivity.skipActivity(baseActivity, ShopActivity.class, bundle);
					break;
				case R.id.shop_tv_hotel:
					bundle.putString("type", "7957");
					bundle.putString("from", "product");
					baseActivity.skipActivity(baseActivity, ShopActivity.class, bundle);
					break;
				default:break;
			}
		}
	}

}
