package wbkj.sjapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Address;
import wbkj.sjapp.utils.MyItemClickListener;

/**
 * Created by Zz on 2016/5/16 0016.
 */
public class AdsRecyAdatper extends RecyclerView.Adapter {

    private List<Address> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public AdsRecyAdatper(List<Address> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName, tvDetail, tvDefault;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_ads_name);
            tvDetail = (TextView) itemView.findViewById(R.id.item_ads_detail);
            tvDefault = (TextView) itemView.findViewById(R.id.item_ads_default);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_ads, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        final Address ads = list.get(position);
        vh.tvName.setText(ads.getPlotnickname());
        vh.tvDetail.setText(ads.getProvince()+ads.getCity()+ads.getCounty()+ads.getAddress());
        if (ads.getIsdefult() == 1) {
            vh.tvDefault.setVisibility(View.VISIBLE);
        }else {
            vh.tvDefault.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
