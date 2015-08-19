package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import net.tsz.afinal.FinalHttp;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class DhbSyzxQGFragment extends Fragment {
	View mMainView;
	FinalHttp mHttp;

	@Override
	public void onPause() {
		super.onPause();
	}

	public DhbSyzxQGFragment(FinalHttp http) {
		this.mHttp = http;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.os_dhb_syzx_qg_fragment,
				container, false);
		GridView gv = (GridView) inflate
				.findViewById(R.id.gv_qg_os_dhb_syzx_fragment);
		String u_id = SharepreferenceUtil.readString(
				DhbSyzxQGFragment.this.getActivity(),
				SharepreferenceUtil.fileName, "uid", "");
		// uid：app用户编号
		// pageid：当前页面，每页2个商品
		// area：区域 1 本市 2 全省 3 全国
		String path = FlowConsts.DHB_SYZX_TJSP_PATH + "uid=" + u_id
				+ "&pageid=1&area=3";

		DhbGetTJSP dgtj = new DhbGetTJSP();
		dgtj.getData(path, mHttp, gv, DhbSyzxQGFragment.this.getActivity());
		return inflate;// 加载期间，展开的layout是否应当附着到ViewGroup上
	}
}
