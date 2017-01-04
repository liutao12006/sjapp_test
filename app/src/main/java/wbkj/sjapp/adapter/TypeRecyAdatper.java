package wbkj.sjapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.ProType;
import wbkj.sjapp.utils.MyItemClickListener;

/**
 * Created by Zz on 2016/5/16 0016.
 */
public class TypeRecyAdatper extends RecyclerView.Adapter {

    private List<ProType> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    private int clickItem = -1;

    public TypeRecyAdatper(List<ProType> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    public void setSeclection(int position) {
        clickItem = position;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.item_tv_type);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_type, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        final ProType type = list.get(position);
        vh.tvTitle.setText(type.getDmsm());
        if (clickItem == position) {
            vh.tvTitle.setTextColor(baseActivity.getResources().getColor(R.color.toolbar_color));
        } else {
            vh.tvTitle.setTextColor(baseActivity.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
