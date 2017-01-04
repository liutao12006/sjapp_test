package wbkj.sjapp.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import wbkj.sjapp.utils.StringUtils;

/**
 * Created by Zz on 2016/5/16 0016.
 */
public class ProRecyAdatper extends RecyclerView.Adapter {

    private List<Product> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public ProRecyAdatper(List<Product> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img;
        TextView tvName, tvDesc, tvPrice, tvOldPrice, tvNum;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_pro_img);
            tvName = (TextView) itemView.findViewById(R.id.item_pro_name);
            tvDesc = (TextView) itemView.findViewById(R.id.item_pro_desc);
            tvPrice = (TextView) itemView.findViewById(R.id.item_pro_price);
            tvOldPrice = (TextView) itemView.findViewById(R.id.item_pro_oldp);
            tvNum = (TextView) itemView.findViewById(R.id.item_pro_num);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_pro, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        final Product pro = list.get(position);
        if(pro!=null){
            Log.e("^^^^^^^^^^^^^^^^",StringUtils.isNotBlank(pro.getWare().getImg())+"");
        }
        if (StringUtils.isNotBlank(pro.getWare().getImg())) {
            String[] imgs = pro.getWare().getImg().split(",");
            Picasso.with(baseActivity).load(HttpUtil.IMGURL+imgs[0]).into(vh.img);
        }
        vh.tvName.setText(pro.getWare().getWname());
        vh.tvDesc.setText(pro.getCol1());
        if (pro.getCol4() == 1) {
            vh.tvPrice.setText("￥"+pro.getWdprice()+"/次");
        } else if (pro.getCol4() == 2){
            vh.tvPrice.setText("￥"+pro.getWdprice()+"/小时");
        } else if (pro.getCol4() == 3){
            vh.tvPrice.setText("￥"+pro.getWdprice()+"/积分");
        } else if (pro.getCol4() == 4){
            vh.tvPrice.setText("￥"+pro.getWdprice()+"/元");
        }
        vh.tvOldPrice.setText("￥"+pro.getOidprice());
        vh.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        vh.tvNum.setText("已经提供服务"+pro.getWare().getLaud()+"次");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
