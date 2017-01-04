package wbkj.sjapp.adapter;

import android.graphics.Paint;
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
public class FavProRecyAdatper extends RecyclerView.Adapter {

    private List<Product> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public FavProRecyAdatper(List<Product> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvPrice, tvOprice;
        private ImageView imgFav;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            imgFav = (ImageView) itemView.findViewById(R.id.pro_fav_img);
            tvTitle = (TextView) itemView.findViewById(R.id.pro_fav_name);
            tvPrice = (TextView) itemView.findViewById(R.id.pro_fav_price);
            tvOprice = (TextView) itemView.findViewById(R.id.pro_fav_oprice);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_favpro, parent, false);
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
        Picasso.with(baseActivity).load(HttpUtil.IMGURL+imgs[0]).into(vh.imgFav);
        vh.tvTitle.setText(pro.getWare().getWname());
        vh.tvPrice.setText("￥"+pro.getWdprice());
        vh.tvOprice.setText("￥"+pro.getOidprice());
        vh.tvOprice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
