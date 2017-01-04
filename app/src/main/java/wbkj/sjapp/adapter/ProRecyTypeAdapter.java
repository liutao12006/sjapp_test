package wbkj.sjapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Product;
import wbkj.sjapp.utils.MyItemClickListener;

/**
 * Created by Administrator on 2016/11/19.
 */

public class ProRecyTypeAdapter extends RecyclerView.Adapter {

    private String[] list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;
    int ps;

    public ProRecyTypeAdapter(String[] list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvType;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView,MyItemClickListener listener) {
            super(itemView);
            tvType=(TextView)itemView.findViewById(R.id.item_pro_type);
            this.mListener = listener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(mListener != null){
//                view.setSelected(true);//这句话主要是让字体颜色变化

                mListener.onItemClick(view, getAdapterPosition());

            }
        }
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_pro_type, parent, false);
        return new ProRecyTypeAdapter.ViewHolder(v,mItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProRecyTypeAdapter.ViewHolder vh = (ProRecyTypeAdapter.ViewHolder) holder;

        vh.tvType.setText(list[position]);

    }

    @Override
    public int getItemCount() {
        return list.length;
    }
}
