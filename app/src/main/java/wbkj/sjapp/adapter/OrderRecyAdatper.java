package wbkj.sjapp.adapter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.activity.CommentActivity;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.Orders;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.MyItemClickListener;
import wbkj.sjapp.utils.OkHttpClientManager;

/**
 * Created by Zz on 2016/5/16 0016.
 */
public class OrderRecyAdatper extends RecyclerView.Adapter {

    private List<Orders> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public OrderRecyAdatper(List<Orders> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView btnCal, btnCmt;
        private ImageView img;
        private TextView tvName, tvNum, tvMoney, tvDate, tvState,tvOrderNum;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.item_order_img);
            tvName = (TextView) itemView.findViewById(R.id.item_order_name);
            tvMoney = (TextView) itemView.findViewById(R.id.item_order_price);
            tvNum = (TextView) itemView.findViewById(R.id.item_order_num);
            tvDate = (TextView) itemView.findViewById(R.id.item_order_date);
            tvState = (TextView) itemView.findViewById(R.id.item_order_state);
            btnCal = (TextView) itemView.findViewById(R.id.item_btn_cal);
            btnCmt = (TextView) itemView.findViewById(R.id.item_btn_cmt);
            tvOrderNum = (TextView) itemView.findViewById(R.id.item_order_orderNum_txt);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_order, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        ViewHolder vh = (ViewHolder) holder;
        final Orders order = list.get(position);
        if (order.getWdetails().getWare().getImg() != null) {
            String [] imgStr = order.getWdetails().getWare().getImg().split(",");
            Picasso.with(baseActivity).load(HttpUtil.IMGURL+imgStr[0]).into(vh.img);
        }
        vh.tvName.setText(order.getWdetails().getWare().getWname());
        vh.tvMoney.setText("￥"+order.getTotal());
        vh.tvNum.setText("×"+order.getTote());
        vh.tvDate.setText("下单时间:"+order.getOdate());
        vh.tvOrderNum.setText("订单号:"+order.getOid());
        if (order.getState() == 1) {
            vh.tvState.setText("未支付");
            vh.btnCal.setVisibility(View.VISIBLE);
            vh.btnCmt.setVisibility(View.GONE);
            vh.btnCal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    baseActivity.showAlertDialog("确认删除", "确定取消该订单么，取消订单后将无法恢复？", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delOrder(order.getOid());
                            notifyDataSetChanged();
                        }
                    }, "取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    list.remove(list.get(position));
                }
            });
        } else if (order.getState() == 2) {
            vh.tvState.setText("已支付");
            vh.btnCal.setVisibility(View.GONE);
            vh.btnCmt.setVisibility(View.GONE);
        } else if (order.getState() == 3) {
            vh.tvState.setText("已完成");
            vh.btnCal.setVisibility(View.GONE);
            vh.btnCmt.setVisibility(View.VISIBLE);
            vh.btnCmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("order", order);
                    baseActivity.skipActivity(baseActivity, CommentActivity.class, bundle);
                }
            });
        } else if (order.getState() == 5) {
            vh.tvState.setText("已评论");
            vh.btnCal.setVisibility(View.GONE);
            vh.btnCmt.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void delOrder(String oId) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("Oid", oId)
        };
        OkHttpClientManager.postAsyn(HttpUtil.delOrder, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(baseActivity, "网络链接失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    Toast.makeText(baseActivity, "订单删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(baseActivity, "订单删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }
}
