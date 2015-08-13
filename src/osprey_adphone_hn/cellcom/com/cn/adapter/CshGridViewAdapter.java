package osprey_adphone_hn.cellcom.com.cn.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CshGridViewAdapter extends BaseAdapter {

	private String[] list;
	private Context context;

	public CshGridViewAdapter(String[] list, Context context) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		String name = list[arg0];
		if (arg1 == null) {
			arg1 = new TextView(context);
		}
		TextView tv = (TextView) arg1;
		tv.setText(name);
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		tv.setBackgroundColor(Color.WHITE);
		tv.setPadding(5, 5, 5, 5);
		return tv;
	}

}
