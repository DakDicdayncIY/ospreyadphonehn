package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import osprey_adphone_hn.cellcom.com.cn.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class DhbSyzxBShenFragment extends Fragment {
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.os_dhb_syzx_bs_fragment,
				container, false);
		GridView gv = (GridView) inflate
				.findViewById(R.id.gv_bshen_os_dhb_syzx_fragment);
		
		return inflate;// 加载期间，展开的layout是否应当附着到ViewGroup上
	}
}
