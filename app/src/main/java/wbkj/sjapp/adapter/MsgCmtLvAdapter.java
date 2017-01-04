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
import wbkj.sjapp.models.Message;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.StringUtils;


public class MsgCmtLvAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private BaseActivity baseActivity;
	private List<Message.Comment> list;

	public MsgCmtLvAdapter(List<Message.Comment> list, BaseActivity baseActivity){
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
			convertView = inflater.inflate(R.layout.item_lv_cmt, null);
			holder = new ViewHolder();
			holder.imgUser = (ImageView) convertView.findViewById(R.id.msg_cmt_img);
			holder.tvName = (TextView) convertView.findViewById(R.id.msg_cmt_name);
			holder.tvDate = (TextView) convertView.findViewById(R.id.msg_cmt_date);
			holder.tvContent = (TextView) convertView.findViewById(R.id.msg_cmt_content);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Message.Comment cmt = list.get(position);
		if (StringUtils.isNotBlank(cmt.getVipuser().getImg())) {
			Picasso.with(baseActivity).load(HttpUtil.IMGURL+cmt.getVipuser().getImg()).into(holder.imgUser);
		}
		holder.tvName.setText(cmt.getVipuser().getNickname());
		holder.tvDate.setText(cmt.getCmdate().substring(0, 10));
		holder.tvContent.setText(cmt.getCmcontent());
		return convertView;
	}
	
	static class ViewHolder{
		ImageView imgUser;
		TextView tvName;
		TextView tvDate;
		TextView tvContent;
	}

}
