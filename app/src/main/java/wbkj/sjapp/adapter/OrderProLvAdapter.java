package wbkj.sjapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Cart;
import wbkj.sjapp.utils.HttpUtil;


public class OrderProLvAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private BaseActivity baseActivity;
	private List<Cart> list;

	public OrderProLvAdapter(List<Cart> list, BaseActivity baseActivity){
		this.list = list;
		this.baseActivity = baseActivity;
		inflater = this.baseActivity.getLayoutInflater();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null ) {
			convertView = inflater.inflate(R.layout.item_lv_pro, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.item_pro_img);
			holder.tvName = (TextView) convertView.findViewById(R.id.item_pro_name);
			holder.tvNum = (TextView) convertView.findViewById(R.id.item_pro_num);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.item_pro_oldp);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Cart cart = list.get(position);
		String[] imgs = cart.wdetails.getWare().getImg().split(",");
		Picasso.with(baseActivity).load(HttpUtil.IMGURL+imgs[0]).into(holder.img);
		holder.tvName.setText(cart.wdetails.getWare().getWname());
		holder.tvPrice.setText("￥"+cart.wdetails.getWdprice());
		holder.tvNum.setText("×"+cart.num);

		return convertView;
	}
	
	static class ViewHolder{
		ImageView img;
		TextView tvName;
		TextView tvPrice;
		TextView tvNum;
	}

}
