package wbkj.sjapp.adapter;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.activity.BigImageActivity;
import wbkj.sjapp.utils.HttpUtil;

public class ProImgAdapter extends PagerAdapter {

	private BaseActivity activity;
	private String[] imgs;

	public ProImgAdapter(BaseActivity activity, String[] imgs) {
		this.activity = activity;
		this.imgs = imgs;
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

		if(position > imgs.length-1){
			position %= imgs.length;
		}
		final ImageView imageView = new ImageView(activity);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        final String img = imgs[position];
		Picasso.with(activity).load(HttpUtil.IMGURL+img).into(imageView);
		imageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("img", img);
				activity.skipActivity(activity, BigImageActivity.class, bundle);
			}
		});
		ViewParent vp = imageView.getParent();
		if (vp != null) {
			ViewGroup parent = (ViewGroup) vp;
			parent.removeView(imageView);
		}
		container.addView(imageView);

		return imageView;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
