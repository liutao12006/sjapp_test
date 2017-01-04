package wbkj.sjapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Notice;
import wbkj.sjapp.utils.MyItemClickListener;

/**
 * Created by Zz on 2016/5/16 0016.
 */
public class NoticeRecyAdatper extends RecyclerView.Adapter {

    private List<Notice> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public NoticeRecyAdatper(List<Notice> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvContent, tvDate;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.item_notice_name);
            tvContent = (TextView) itemView.findViewById(R.id.item_notice_content);
            tvDate = (TextView) itemView.findViewById(R.id.item_notice_date);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_notice, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        final Notice notice = list.get(position);
        vh.tvTitle.setText(notice.getCircular().getCititle());
        vh.tvContent.setText(notice.getCicontent());
        vh.tvDate.setText(notice.getCircular().getCidate().substring(0, 10));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
