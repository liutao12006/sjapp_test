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
import wbkj.sjapp.models.Product;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;

/**
 * Created by Zz on 2016/5/16 0016.
 */
public class PoProRecyAdatper extends RecyclerView.Adapter {

    private List<Product> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public PoProRecyAdatper(List<Product> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img;
        TextView tvName, tvDesc, tvPrice, tvOldPrice, tvNum;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.pro_d_img);
            tvName = (TextView) itemView.findViewById(R.id.pro_d_name);
            tvDesc = (TextView) itemView.findViewById(R.id.pro_d_title);
            tvPrice = (TextView) itemView.findViewById(R.id.pro_d_price);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gv_pro, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        final Product pro = list.get(position);
        String[] imgs = pro.getWare().getImg().split(",");
        Picasso.with(baseActivity).load(HttpUtil.IMGURL+imgs[0]).into(vh.img);
        vh.tvName.setText(pro.getWare().getWname());
        vh.tvDesc.setText(pro.getCol1());
//        if (pro.getCol4() == 1) {
//            vh.tvPrice.setText(pro.getWdprice()+"积分/次");
//        } else {
//            vh.tvPrice.setText("￥"+pro.getWdprice()+"积分/小时");
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
