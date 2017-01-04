package wbkj.sjapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author Administrator
 *	自定义ListView，用于ScrollView和ListView的嵌套,解决二者的滚动冲突
 */
public class CustomListView extends ListView {

	public CustomListView(Context context) {
		super(context);
	}
	public CustomListView(Context context, AttributeSet attrs,
						  int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
