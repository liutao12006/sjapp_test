package wbkj.sjapp.adapter;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Product;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.StringUtils;

/**
 * Created by jianghan on 16/9/23.
 */
public class ProLvAdapter extends BaseAdapter {

    private List<Product> list;
    private BaseActivity baseActivity;
    private LayoutInflater inflater;

    public ProLvAdapter(List<Product> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
        inflater = this.baseActivity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null ) {
            convertView = inflater.inflate(R.layout.item_recy_pro, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.item_pro_img);
            holder.tvName = (TextView) convertView.findViewById(R.id.item_pro_name);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.item_pro_desc);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.item_pro_price);
            holder.tvOldPrice = (TextView) convertView.findViewById(R.id.item_pro_oldp);
            holder.tvNum = (TextView) convertView.findViewById(R.id.item_pro_num);
            holder.tvCount = (TextView) convertView.findViewById(R.id.item_pro_count);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Product pro = list.get(position);
        if (StringUtils.isNotBlank(pro.getWare().getImg())) {
//            String[] imgs = pro.getWare().getImg().split(",");
            Picasso.with(baseActivity).load(HttpUtil.IMGURL+pro.getWare().getImg()).into(holder.img);
        }
        holder.tvName.setText(pro.getWare().getWname());
        holder.tvCount.setText("已服务"+pro.getWare().getLaud()+"次");
        holder.tvDesc.setText(pro.getCol1());
//        if (pro.getCol4() == 1) {
//            holder.tvPrice.setText("￥"+pro.getWdprice()+"/次");
//        } else if (pro.getCol4() == 2){
//            holder.tvPrice.setText("￥"+pro.getWdprice()+"/小时");
//        } else if (pro.getCol4() == 3){
//            holder.tvPrice.setText("￥"+pro.getWdprice()+"/积分");
//        } else if (pro.getCol4() == 4){
            holder.tvPrice.setText("￥"+pro.getWdprice());
//        }
        holder.tvOldPrice.setText("￥"+pro.getOidprice());
        holder.tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
//        holder.tvNum.setText("已经提供服务"+pro.getWare().getLaud()+"次");
        return convertView;
    }

    static class ViewHolder{
        ImageView img;
        TextView tvName, tvDesc, tvPrice, tvOldPrice, tvNum,tvCount;
    }

}
