package wbkj.sjapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Comment;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;
import wbkj.sjapp.utils.StringUtils;
import wbkj.sjapp.views.CircleImageView;

/**
 * Created by Zz on 2016/5/16 0016.
 */
public class CmtRecyAdatper extends RecyclerView.Adapter {

    private List<Comment> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public CmtRecyAdatper(List<Comment> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView img;
        RatingBar ratingBar;
        TextView tvName, tvDate, tvContent;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.cmt_img_user);
            ratingBar = (RatingBar) itemView.findViewById(R.id.cmt_rating);
            tvName = (TextView) itemView.findViewById(R.id.cmt_tv_name);
            tvDate = (TextView) itemView.findViewById(R.id.cmt_tv_date);
            tvContent = (TextView) itemView.findViewById(R.id.cmt_tv_content);
            this.mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener != null){
                mListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_cmt, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        final Comment cmt = list.get(position);
        if (StringUtils.isNotBlank(cmt.getVipuser().getImg())) {
            Picasso.with(baseActivity).load(HttpUtil.IMGURL+cmt.getVipuser().getImg()).into(vh.img);
        }
        vh.ratingBar.setRating((float) cmt.getAdlevel());
        vh.tvDate.setText(cmt.getCmdate());
        vh.tvContent.setText(cmt.getCmcontent());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
