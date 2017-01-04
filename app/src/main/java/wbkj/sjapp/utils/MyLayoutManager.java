package wbkj.sjapp.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 计算recyclerView的高度
 * Created by Zz on 2015/8/12 0012.
 */

public class MyLayoutManager extends LinearLayoutManager {

    private int size;

    public MyLayoutManager(Context context, int size) {
        super(context);
        this.size = size;
        setAutoMeasureEnabled(false);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        View v = recycler.getViewForPosition(0);

        if(v != null){
            measureChild(v, widthSpec, heightSpec);
            int measuredWidth = View.MeasureSpec.getSize(widthSpec);
            int measuredHeight = 0;
            measuredHeight = v.getMeasuredHeight() * size + (size - 1) * 2;
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }
}
