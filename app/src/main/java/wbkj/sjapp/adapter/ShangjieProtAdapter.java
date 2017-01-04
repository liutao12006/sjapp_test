package wbkj.sjapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.utils.MyItemClickListener;

/**
 * Created by Administrator on 2016/12/19.
 */

public class ShangjieProtAdapter extends RecyclerView.Adapter{

    private int[] images;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public ShangjieProtAdapter(int[] images, BaseActivity baseActivity,MyItemClickListener mItemClickListener) {
        this.images = images;
        this.baseActivity = baseActivity;
        this.mItemClickListener = mItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgPro;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            imgPro = (ImageView) itemView.findViewById(R.id.spro_img);
            this.mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //这里mListener 为null  下面的方法没有执行
            if(mListener != null){
//                Toast.makeText(baseActivity,"test",Toast.LENGTH_SHORT).show();
                mListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_shangjieport, parent, false);
        return new ShangjieProtAdapter.ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ShangjieProtAdapter.ViewHolder) holder;

        vh.imgPro.setBackgroundResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }
}
