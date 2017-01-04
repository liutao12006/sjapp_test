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
import wbkj.sjapp.models.Product;
import wbkj.sjapp.utils.HttpUtil;

public class ProGvAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private BaseActivity baseActivity;
	private List<Product> list;

	public ProGvAdapter(List<Product> list, BaseActivity baseActivity){
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
			convertView = inflater.inflate(R.layout.item_gv_pro, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.pro_d_img);
			holder.tvName = (TextView) convertView.findViewById(R.id.pro_d_name);
			holder.tvDname = (TextView) convertView.findViewById(R.id.pro_d_title);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.pro_d_price);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Product pro = list.get(position);
		String[] imgs = pro.getWare().getImg().split(",");
		Picasso.with(baseActivity).load(HttpUtil.IMGURL+imgs[0]).into(holder.img);
		holder.tvName.setText(pro.getWare().getWname());
		holder.tvDname.setText(pro.getCol1());
		holder.tvPrice.setText("￥"+pro.getWdprice());

		return convertView;
	}
	
	static class ViewHolder{
		ImageView img;
		TextView tvName;
		TextView tvDname;
		TextView tvPrice;
	}

}
