package wbkj.sjapp.fragment;

import android.support.v4.app.Fragment;

public class FragmentContainer {
    private int id;
    private Fragment fragment;
    private Class fragmentClass;

    public FragmentContainer() {
    }

    public FragmentContainer(int id, Class fragmentClass) {
        this.id = id;
        this.fragmentClass = fragmentClass;
    }

    public FragmentContainer(int id, Fragment fragment) {
        this.id = id;
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Class getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	/*public boolean isNeedRefresh(){
        return false;
		//return fragment!=null&& fragment.getClass()==WZShoppingCartFragment.class;
	}*/
}
