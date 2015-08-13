package osprey_adphone_hn.cellcom.com.cn.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CshCarDisplacementAdapter extends BaseAdapter {
	String[] mDisplacement = { "L", "T" };
	Context context;

	public CshCarDisplacementAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return mDisplacement.length;
	}
 
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mDisplacement[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		TextView tv = new TextView(context);
		tv.setText(mDisplacement[arg0]);
		tv.setTextSize(20);
		return tv;
	}

}
