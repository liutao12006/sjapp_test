package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.adapter.CartRecyAdatper;
import wbkj.sjapp.models.Cart;
import wbkj.sjapp.models.JsonModel2;
import wbkj.sjapp.utils.DividerItemDecoration;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;

public class CartActivity extends BaseActivity implements CartRecyAdatper.CallTotalPrice {

	private Toolbar toolbar;
	private RecyclerView cartRecy;
	private CartRecyAdatper adapter;
	private List<Cart> proLists = new ArrayList<>();
	private ImageButton btnEdit;
	private CheckBox cbAll;
	private TextView tvPrice;
	private Button btnFin;

	double totoal = 0.0;
	private List<Cart> selectPro = new ArrayList<>();
	private Boolean isLoad = false;
	private SharedPreferencesUtil sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
		sp = new SharedPreferencesUtil(this);
		initView();
		createLoadingDialog(this, "加载中...").show();
		getDataMethod();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	public void initView(){
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
		cbAll = (CheckBox) findViewById(R.id.cart_all_selected);
		tvPrice = (TextView) findViewById(R.id.cart_total_price);
		btnEdit = (ImageButton) findViewById(R.id.cart_btn_edit);
		cartRecy = (RecyclerView) findViewById(R.id.cart_recy_pro);
		cartRecy.setHasFixedSize(true);
		cartRecy.setLayoutManager(new LinearLayoutManager(this));
		cartRecy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		btnFin = (Button) findViewById(R.id.cart_btn_fin);

		btnEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MyApplication.cart_type == 0) {
					MyApplication.cart_type = 1;
					adapter.notifyDataSetChanged();
					btnEdit.setImageResource(R.mipmap.check);
				} else {
					MyApplication.cart_type = 0;
					adapter.notifyDataSetChanged();
					btnEdit.setImageResource(R.mipmap.edit_cart);
				}
			}
		});
		cbAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cbAll.isChecked()) {
					for (int i = 0; i < proLists.size(); i ++) {
						CartRecyAdatper.getIsSelected().put(proLists.get(i).wdetails.getWdid(), true);
					}
				} else {
					for (int i = 0; i < proLists.size(); i ++) {
						CartRecyAdatper.getIsSelected().put(proLists.get(i).wdetails.getWdid(), false);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
		btnFin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("cart", (Serializable) selectPro);
				bundle.putDouble("total", totoal);
				skipActivity(CartActivity.this, ConfirmOrdersActivity.class, bundle);
			}
		});
	}

	@Override
	public void setTotalPrice(Map<Integer, Cart> proSelected) {
		selectPro.clear();
		totoal = 0;
		for (int key : proSelected.keySet()) {
			Cart cart = proSelected.get(key);
			totoal = totoal + (cart.wdetails.getWdprice() * cart.num);
			selectPro.add(cart);
		}
		tvPrice.setText("￥"+totoal);
	}

	@Override
	public void setSelectCb(boolean tag) {
		cbAll.setChecked(tag);
	}

	private void getDataMethod() {
		OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
				new OkHttpClientManager.Param("userid", String.valueOf(sp.getUser().getVipid()))
		};
		OkHttpClientManager.postAsyn(HttpUtil.cartUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
			@Override
			public void onError(Request request, Exception e) {
				closeProDialog();
				Toast.makeText(CartActivity.this, "网络链接失败，请重新操作", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onResponse(JsonElement response) {
				isLoad = true;
				proLists.clear();
				closeProDialog();
				JsonModel2 json = new Gson().fromJson(response, new TypeToken<JsonModel2<Cart>>(){}.getType());
				if (json.result == 1) {
					if (json.data != null && json.data.size() != 0) {
						proLists.addAll(json.data);
						if (adapter == null) {
							adapter = new CartRecyAdatper(proLists, CartActivity.this, CartActivity.this);
							cartRecy.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
					}

				}
			}
		}, null);
	}


}
