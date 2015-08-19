package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class DhbSyzxFragmentPagerAdapter extends FragmentPagerAdapter {
	List<Fragment> mList;

	public DhbSyzxFragmentPagerAdapter(FragmentManager fm, List<Fragment> mList) {
		super(fm);
		this.mList = mList;
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = mList.get(arg0);
		
		return fragment;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

}
