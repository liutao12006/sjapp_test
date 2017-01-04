package wbkj.sjapp.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
public class SproRecyAdatper extends RecyclerView.Adapter {

    private List<Product> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public SproRecyAdatper(List<Product> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgPro;
        TextView tvName, tvDesc, tvPrice, tvTel;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            imgPro = (ImageView) itemView.findViewById(R.id.spro_img);
            tvName = (TextView) itemView.findViewById(R.id.spro_name);
            tvDesc = (TextView) itemView.findViewById(R.id.spro_desc);
            tvPrice = (TextView) itemView.findViewById(R.id.spro_price);
            tvTel = (TextView) itemView.findViewById(R.id.spro_tel);
            this.mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(baseActivity,"test",Toast.LENGTH_SHORT).show();
            //这里mListener 为null  下面的方法没有执行
            if(mListener != null){
                mListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_spro, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        final Product pp = list.get(position);
        Picasso.with(baseActivity).load(HttpUtil.IMGURL+pp.getWare().getImg()).into(vh.imgPro);
        vh.tvName.setText(pp.getWare().getWname());
        vh.tvDesc.setText(pp.getCol1());
        vh.tvPrice.setText("￥"+pp.getWdprice());
        vh.tvTel.setText(pp.getCol2());

        vh.tvTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(baseActivity,"拨号",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "13838383838"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                baseActivity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
