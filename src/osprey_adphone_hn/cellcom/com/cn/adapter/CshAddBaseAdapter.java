package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.activity.csh.CshAutomobileMotorcycleTypeBean;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CshAddBaseAdapter extends BaseAdapter {
	List<CshAutomobileMotorcycleTypeBean> mList; // android上下文
	private Context context;

	public CshAddBaseAdapter(Context context,
			List<CshAutomobileMotorcycleTypeBean> customers) {
		this.context = context;
		this.mList = customers;
	}

	/**
	 * 获取总数
	 */
	public int getCount() {
		return mList.size();
	}

	/**
	 * 根据索引获得item
	 */
	public Object getItem(int position) {
		return this.mList.get(position);
	}

	/**
	 * 根据索引获得item的id,customer没有id,我们把索引当做id
	 */
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new TextView(context);
		}
		TextView tv = (TextView) convertView;
		CshAutomobileMotorcycleTypeBean cshAutomobileMotorcycleTypeBean = mList
				.get(position);
		cshAutomobileMotorcycleTypeBean.getmCarType();

		tv.setText(cshAutomobileMotorcycleTypeBean.getmCarType());
		tv.setPadding(100, 10, 0, 10);
		return tv;
	}
}
