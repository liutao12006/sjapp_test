package wbkj.sjapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.models.Notes;
import wbkj.sjapp.utils.MyItemClickListener;

/**
 * Created by Zz on 2016/5/16 0016.
 */
public class NotesRecyAdatper extends RecyclerView.Adapter {

    private List<Notes> list;
    private BaseActivity baseActivity;
    private MyItemClickListener mItemClickListener;

    public NotesRecyAdatper(List<Notes> list, BaseActivity baseActivity) {
        this.list = list;
        this.baseActivity = baseActivity;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName, tvDate, tvMoney;
        private MyItemClickListener mListener;
        public ViewHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.item_note_name);
            tvDate = (TextView) itemView.findViewById(R.id.item_note_date);
            tvMoney = (TextView) itemView.findViewById(R.id.item_note_money);

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recy_note, parent, false);
        return new ViewHolder(v, mItemClickListener);
    }

    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder vh = (ViewHolder) holder;
        final Notes note = list.get(position);
        vh.tvName.setText(note.getTcontent());
        vh.tvDate.setText(note.getTdate());
        vh.tvMoney.setText("￥"+note.getTmoney());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
