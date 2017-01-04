package wbkj.sjapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.MyApplication;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Cart;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.Product;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;


/**
 * Created by Zz on 2016/5/16 0016.
 */
public class CartRecyAdatper extends RecyclerView.Adapter {

    private List<Cart> list;
    private BaseActivity baseActivity;

    private static HashMap<Integer, Boolean> isSelected = new HashMap<>();
    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }
    private static HashMap<Integer, Cart> proMap = new HashMap<>();
    public static HashMap<Integer, Cart> getProMap() {
        return proMap;
    }

    private MyApplication app;

    private CallTotalPrice callListener;
    public interface CallTotalPrice{
        void setTotalPrice(Map<Integer, Cart> proSelected);
        void setSelectCb(boolean tag);
    }

    public CartRecyAdatper(List<Cart> list, BaseActivity baseActivity, CallTotalPrice listener) {
        this.list = list;
        this.baseActivity = baseActivity;
        this.callListener = listener;
        app = (MyApplication) this.baseActivity.getApplication();

        //初始化Checkbox
        for (int i = 0; i < list.size(); i++){
            getIsSelected().put(list.get(i).wdetails.getWdid(), false);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvNorms, tvNum, tvPrice, tvNums;
        private CheckBox cbPro;
        private ImageButton btnDel, btnMin, btnAdd;
        private ImageView img;
        private RelativeLayout rlInfo, rlEdit;
        public ViewHolder(View itemView) {
            super(itemView);

            rlInfo = (RelativeLayout) itemView.findViewById(R.id.cart_rl_info);
            rlEdit = (RelativeLayout) itemView.findViewById(R.id.cart_rl_edit);
            cbPro = (CheckBox) itemView.findViewById(R.id.cart_item_selected);
            img = (ImageView) itemView.findViewById(R.id.cart_item_img);
            tvName = (TextView) itemView.findViewById(R.id.cart_item_name);
            tvNorms = (TextView) itemView.findViewById(R.id.cart_item_norms);
            tvPrice = (TextView) itemView.findViewById(R.id.cart_item_price);
            tvNum = (TextView) itemView.findViewById(R.id.cart_item_num);
            btnDel = (ImageButton) itemView.findViewById(R.id.cart_item_delete);
            btnAdd = (ImageButton) itemView.findViewById(R.id.cart_item_add);
            btnMin = (ImageButton) itemView.findViewById(R.id.cart_item_min);
            tvNums = (TextView) itemView.findViewById(R.id.cart_edit_num);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_cart, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final ViewHolder vh = (ViewHolder) holder;
        final Cart cart = list.get(position);
        final Product pro = cart.wdetails;
        vh.tvNorms.setText(pro.getCol1());
        vh.cbPro.setChecked(getIsSelected().get(pro.getWdid()));
        vh.cbPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vh.cbPro.isChecked()) {
                    getIsSelected().put(pro.getWdid(), true);
                    cart.num = Integer.parseInt(vh.tvNums.getText().toString().trim());
                    getProMap().put(pro.getWdid(), cart);
                } else {
                    getIsSelected().put(pro.getWdid(), false);
                    getProMap().remove(pro.getWdid());
                }
                checkCb(getProMap());
            }
        });
        vh.cbPro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cart.num = Integer.parseInt(vh.tvNums.getText().toString().trim());
                    getProMap().put(pro.getWdid(), cart);
                } else {
                    getProMap().remove(pro.getWdid());
                }
                checkCb(getProMap());
            }
        });
        Picasso.with(baseActivity).load(HttpUtil.IMGURL+pro.getWare().getImg()).into(vh.img);
        if (MyApplication.cart_type == 0 ) {
            vh.rlInfo.setVisibility(View.VISIBLE);
            vh.rlEdit.setVisibility(View.GONE);
            vh.tvName.setText(pro.getWare().getWname());
            vh.tvPrice.setText("￥"+pro.getWdprice());
            vh.tvNum.setText("×"+cart.num);
            vh.tvNums.setText(String.valueOf(cart.num));
        } else {
            vh.rlInfo.setVisibility(View.GONE);
            vh.rlEdit.setVisibility(View.VISIBLE);
            vh.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(vh.tvNums.getText().toString().trim());
                    num = num + 1;
                    vh.tvNums.setText(String.valueOf(num));
                    cart.num = num;
                    getProMap().put(pro.getWdid(), cart);
                    checkCb(getProMap());

                }
            });
            vh.btnMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.parseInt(vh.tvNums.getText().toString().trim());
                    if (num > 1) {
                        num = num - 1;
                        vh.tvNums.setText(String.valueOf(num));
                        cart.num = num;
                        getProMap().put(pro.getWdid(), cart);
                        checkCb(getProMap());
                    }
                }
            });
            vh.btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delMethod(cart.scid, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void checkCb(HashMap<Integer, Cart> proMap){
        boolean tag = true;
        for (int key: getIsSelected().keySet()){
            if (!getIsSelected().get(key)) {
                tag = false;
            }
        }
        callListener.setTotalPrice(proMap);
        callListener.setSelectCb(tag);
    }

        private void delMethod(int scid, final int position) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("scid", String.valueOf(scid)),
        };
        OkHttpClientManager.postAsyn(HttpUtil.delCart, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                baseActivity.closeProDialog();
                Toast.makeText(baseActivity, "网络链接失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                baseActivity.closeProDialog();
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    Toast.makeText(baseActivity, "删除成功", Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(baseActivity, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

}
