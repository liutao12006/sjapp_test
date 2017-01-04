package wbkj.sjapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Message;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.StringUtils;
import wbkj.sjapp.views.CustomGridView;


public class MsgLvAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private BaseActivity baseActivity;
	private List<Message> list;

	public MsgLvAdapter(List<Message> list, BaseActivity baseActivity){
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
			convertView = inflater.inflate(R.layout.item_lv_msg, null);
			holder = new ViewHolder();
			holder.imgUser = (ImageView) convertView.findViewById(R.id.msg_user_img);
			holder.tvName = (TextView) convertView.findViewById(R.id.msg_user_name);
			holder.tvDate = (TextView) convertView.findViewById(R.id.msg_user_date);
			holder.tvNum = (TextView) convertView.findViewById(R.id.msg_user_num);
			holder.tvContent = (TextView) convertView.findViewById(R.id.msg_content);
			holder.imgGv = (CustomGridView) convertView.findViewById(R.id.msg_imgs);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Message msg = list.get(position);
		if (msg.getUser().getThirdfrom() == 3) {
			if (StringUtils.isNotBlank(msg.getUser().getImg())) {
				Picasso.with(baseActivity).load(HttpUtil.IMGURL + msg.getUser().getImg()).into(holder.imgUser);
			} else {
				holder.imgUser.setImageResource(R.mipmap.user_head);
			}
		} else {
			if (StringUtils.isNotBlank(msg.getUser().getImg())) {
				Picasso.with(baseActivity).load(msg.getUser().getImg()).into(holder.imgUser);
			} else {
				holder.imgUser.setImageResource(R.mipmap.user_head);
			}
		}
		holder.tvName.setText(msg.getUser().getNickname());
		holder.tvDate.setText(msg.getTitle().getTdate().substring(0, 10));
		holder.tvNum.setText(""+msg.getCommentList().size());
		holder.tvContent.setText(msg.getTdcontent());
		List<String> imgStr = new ArrayList<>();
		if (StringUtils.isNotBlank(msg.getImg2())) imgStr.add(msg.getImg2());
		if (StringUtils.isNotBlank(msg.getImg3())) imgStr.add(msg.getImg2());
		if (StringUtils.isNotBlank(msg.getImg4())) imgStr.add(msg.getImg2());
		if (StringUtils.isNotBlank(msg.getImg5())) imgStr.add(msg.getImg2());
		if (StringUtils.isNotBlank(msg.getImg6())) imgStr.add(msg.getImg2());
		if (StringUtils.isNotBlank(msg.getImg7())) imgStr.add(msg.getImg2());
		holder.imgGv.setAdapter(new MsgImgGvAdapter(imgStr, baseActivity));

		return convertView;
	}
	
	static class ViewHolder{
		ImageView imgUser;
		TextView tvName;
		TextView tvDate;
		TextView tvNum;
		TextView tvContent;
		CustomGridView imgGv;
	}

}
