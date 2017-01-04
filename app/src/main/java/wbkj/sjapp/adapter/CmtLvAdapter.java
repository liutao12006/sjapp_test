package wbkj.sjapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Comment;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.StringUtils;
import wbkj.sjapp.views.CircleImageView;

/**
 * Created by jianghan on 16/9/23.
 */
public class CmtLvAdapter extends BaseAdapter {

    private List<Comment> list;
    private BaseActivity baseActivity;
    private LayoutInflater inflater;

    public CmtLvAdapter(List<Comment> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
        inflater = this.baseActivity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null ) {
            convertView = inflater.inflate(R.layout.item_recy_cmt, null);
            holder = new ViewHolder();
            holder.img = (CircleImageView) convertView.findViewById(R.id.cmt_img_user);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.cmt_rating);
            holder.tvName = (TextView) convertView.findViewById(R.id.cmt_tv_name);
            holder.tvDate = (TextView) convertView.findViewById(R.id.cmt_tv_date);
            holder.tvContent = (TextView) convertView.findViewById(R.id.cmt_tv_content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Comment cmt = list.get(position);
        if (StringUtils.isNotBlank(cmt.getVipuser().getImg())) {
            Picasso.with(baseActivity).load(HttpUtil.IMGURL+cmt.getVipuser().getImg()).into(holder.img);
        }
        holder.ratingBar.setRating((float) cmt.getAdlevel());
        holder.tvDate.setText(cmt.getCmdate());
        holder.tvContent.setText(cmt.getCmcontent());
        holder.tvName.setText(cmt.getVipuser().getNickname());

        return convertView;
    }

    static class ViewHolder{
        CircleImageView img;
        RatingBar ratingBar;
        TextView tvName, tvDate, tvContent;
    }

}
