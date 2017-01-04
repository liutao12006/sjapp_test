package wbkj.sjapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.StringUtils;


public class MsgImgGvAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private BaseActivity baseActivity;
	private List<String> imgStr;

	public MsgImgGvAdapter(List<String> img, BaseActivity baseActivity){
		this.imgStr = img;
		this.baseActivity = baseActivity;
		inflater = this.baseActivity.getLayoutInflater();
	}

	@Override
	public int getCount() {
		return imgStr.size();
	}

	@Override
	public Object getItem(int position) {
		return imgStr.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.item_gv_msg, null);
		ImageView img = (ImageView) convertView.findViewById(R.id.msg_item_img);
		if (StringUtils.isNotBlank(imgStr.get(position))) {
			Picasso.with(baseActivity).load(HttpUtil.IMGURL+imgStr.get(position)).into(img);
		}
		return convertView;
	}

}
