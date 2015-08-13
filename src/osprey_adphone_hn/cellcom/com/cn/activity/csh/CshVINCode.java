package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.R.id;
import osprey_adphone_hn.cellcom.com.cn.R.layout;
import osprey_adphone_hn.cellcom.com.cn.R.menu;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class CshVINCode extends ActivityFrame {
	/**
	 * 车辆vin码识别
	 * 
	 * @author 周子健
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_csh_vin_code);
		AppendTitleBody7();
		SetTopBarTitle("车辆识别码");
		isShowSlidingMenu(false);
	}
}
