package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.util.ArrayList;
import java.util.List;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.csh.CshAutomobileBrandBean;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 车生活-汽车品牌与型号适配
 * 
 * @author 周子健
 *
 */
public class CshAddDataVehicleDefaultEXAdapter extends
		BaseExpandableListAdapter {

	public CshAddDataVehicleDefaultEXAdapter(Activity activity,
			List<CshAutomobileBrandBean> mGoroupList,
			List<List<CshAutomobileBrandBean>> mChildList) {
		super(activity);
		this.mGoroupList = mGoroupList;
		this.mChildList = mChildList;
		this.context = activity;
	}

	private Context context;
	private List<CshAutomobileBrandBean> mGoroupList = new ArrayList<CshAutomobileBrandBean>();
	private List<List<CshAutomobileBrandBean>> mChildList = new ArrayList<List<CshAutomobileBrandBean>>();

	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return mChildList.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
			ViewGroup arg4) {
		TextView tv = null;
		CshAutomobileBrandBean cshAutomobileMotorcycleTypeBean = mChildList
				.get(arg0).get(arg1);
		if (arg3 == null) {
			TextView tvView = new TextView(context);
			arg3 = tvView;
			tv = (TextView) arg3;
		} else {
			tv = (TextView) arg3;
		}
		tv.setTextSize(20);
		tv.setPadding(80, 10, 0, 10);
		tv.setText(cshAutomobileMotorcycleTypeBean.getmCarSeriesName());

		return tv;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return mChildList.get(arg0).size();
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return mGoroupList.get(arg0);
	}

	@Override
	public int getGroupCount() {
		return mGoroupList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {

		CshAutomobileBrandBean cshAutomobileMotorcycleTypeBean = mGoroupList
				.get(arg0);

		if (arg2 == null) {
			arg2 = LayoutInflater.from(context).inflate(
					R.layout.os_csh_add_data_vehicle_default_adapter, null);
		}

		TextView tv = (TextView) arg2
				.findViewById(R.id.tv_os_csh_add_data_vehicle_default_adapter);
		tv.setTextSize(20);
		tv.setPadding(80, 10, 0, 10);
		tv.setText(cshAutomobileMotorcycleTypeBean.getmCarSeriesName());
		ImageView iv = (ImageView) arg2
				.findViewById(R.id.iv_sh_add_data_vehicle_default_adapter);
		if (mChildList.get(arg0).size() > 0 && !"".equals(mChildList.get(arg0))) {
			if (arg1) {
				iv.setImageResource(R.drawable.icon_arrow_down);
			} else {
				iv.setImageResource(R.drawable.icon_arrow_right);
			}

		}

		tv.setTextSize(20);
		tv.setPadding(40, 10, 0, 10);
		tv.setText(cshAutomobileMotorcycleTypeBean.getmCarSeriesName());

		return arg2;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
