package wbkj.sjapp.utils;

import android.app.Activity;

import java.util.ArrayList;

public class ActivityUtil {

	static ArrayList<Activity> mActivities = new ArrayList<Activity>();

	public static void exitAll() {

		for (Activity activity : mActivities) {
			activity.finish();
		}

	}

	public static void add(Activity activity) {

		if (!mActivities.contains(activity)) {

			mActivities.add(activity);

		}
	}
}
