package wbkj.sjapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Message;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;

/**
 * Created by Zz on 2016/5/16 0016.
 */
public class FavMsgRecyAdatper extends RecyclerView.Adapter {

    private List<Message> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public FavMsgRecyAdatper(List<Message> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgUser;
        private TextView tvName, tvDate, tvContent;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            imgUser = (ImageView) itemView.findViewById(R.id.msg_fav_img);
            tvName = (TextView) itemView.findViewById(R.id.msg_fav_name);
            tvDate = (TextView) itemView.findViewById(R.id.msg_fav_date);
            tvContent = (TextView) itemView.findViewById(R.id.msg_fav_content);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_favmsg, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        final Message msg = list.get(position);
        if (msg.getUser().getImg() != null) {
            Picasso.with(baseActivity).load(HttpUtil.IMGURL+msg.getUser().getImg()).into(vh.imgUser);
        }
        vh.tvName.setText(msg.getUser().getNickname());
        vh.tvDate.setText(msg.getTitle().getTdate().substring(0, 10));
        vh.tvContent.setText(msg.getTdcontent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
