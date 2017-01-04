package wbkj.sjapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Shop;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;

/**
 * Created by Zz on 2016/5/16 0016.
 */
public class ShopRecyAdatper extends RecyclerView.Adapter {

    private List<Shop> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public ShopRecyAdatper(List<Shop> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img;
        RatingBar ratingBar;
        TextView tvName, tvAds, tvLoc;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_shop_img);
            tvName = (TextView) itemView.findViewById(R.id.item_shop_name);
            tvAds = (TextView) itemView.findViewById(R.id.shop_item_ads);
            tvLoc = (TextView) itemView.findViewById(R.id.shop_item_loc);
            ratingBar = (RatingBar) itemView.findViewById(R.id.item_shop_rating);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_shop, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        final Shop shop = list.get(position);
        Logger.e(shop.getImg());
        Picasso.with(baseActivity).load(HttpUtil.IMGURL+shop.getImg()).into(vh.img);
        vh.tvName.setText(shop.getName());
        vh.tvAds.setText(shop.getAddress());
        vh.tvLoc.setText(new DecimalFormat("###.00").format(Double.parseDouble(shop.getDistance())/1000)+"千米");
        Logger.e("1-"+shop.getAvg());
        vh.ratingBar.setRating((float) shop.getAvg());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
