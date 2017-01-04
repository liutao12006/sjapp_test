package wbkj.sjapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Property;
import wbkj.sjapp.utils.MyItemClickListener;

/**
 * Created by Zz on 2016/5/16 0016.
 */
public class PpRecyAdatper extends RecyclerView.Adapter {

    private List<Property> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public PpRecyAdatper(List<Property> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvName, tvDesc, tvPhone;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_pp_name);
            tvDesc = (TextView) itemView.findViewById(R.id.item_pp_txt);
            tvPhone = (TextView) itemView.findViewById(R.id.item_pp_phone);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_pp, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        final Property pp = list.get(position);
        vh.tvName.setText(pp.getWyintro());
        vh.tvDesc.setText(pp.getWyprincipal());
        vh.tvPhone.setText("服务电话:"+pp.getWyphone());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
