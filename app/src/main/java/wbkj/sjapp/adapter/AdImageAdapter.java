package wbkj.sjapp.adapter;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.activity.ShopDetailActivity;
import wbkj.sjapp.models.Banner;
import wbkj.sjapp.utils.HttpUtil;

public class AdImageAdapter extends PagerAdapter {

	private BaseActivity activity;
	private List<Banner> list;
	
	public AdImageAdapter(BaseActivity activity, List<Banner> list) {
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		if(position > list.size()-1){
			position %= list.size();
		}
		final ImageView imageView = new ImageView(activity);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        final Banner img = list.get(position);
		Picasso.with(activity).load(HttpUtil.IMGURL+img.getImg()).into(imageView);
		ViewParent vp = imageView.getParent();
		if (vp != null) {
			ViewGroup parent = (ViewGroup) vp;
			parent.removeView(imageView);
		}
		container.addView(imageView);
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putInt("shopId", img.getCol1());
				activity.skipActivity(activity, ShopDetailActivity.class, bundle);
			}
		});

		return imageView;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
