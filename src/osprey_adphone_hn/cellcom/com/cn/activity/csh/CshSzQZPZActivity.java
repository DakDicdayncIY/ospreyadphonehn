package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.SolutionScrollviewListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CshSzQZPZActivity extends ActivityFrame {
	TextView tvBrandOsCshSzQzpz;
	TextView tvTypeOsCshSzQzpz;
	TextView tvVinOsCshSzQzpz;
	EditText etPlOsCshSzQzpz;
	EditText etMjOsCshSzQzpz;
	RelativeLayout rlbRANDoScSHsZQzpz;
	RelativeLayout rlVinOsCshSzQzpz;
	SolutionScrollviewListView rlListView;
	ImageView mImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_csh_sz_qzpz);
		AppendTitleBody8();
		SetTopBarTitle("车辆设置");

		// 初始化视图
		minitView();
		// 初始化监听
		initLister();

		Intent intent2 = this.getIntent();
		String car_series_name = intent2.getStringExtra("car_series_name");
		tvTypeOsCshSzQzpz.setText(intent2.getStringExtra("car_type_id"));
		tvVinOsCshSzQzpz.setText(intent2.getStringExtra("car_brand_vin"));

		String car_gearbox_type = intent2.getStringExtra("car_gearbox_type");
		String gearbox = car_gearbox_type.substring(
				car_gearbox_type.length() - 1, car_gearbox_type.length());

		String car_displacement = intent2.getStringExtra("car_displacement");

		// intent.putExtra("car_producing_year", car_producing_year);
	}

	private void initLister() {
		rlbRANDoScSHsZQzpz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				OpenActivityForResult(CshAddDataVehicleDefault.class, 1);
			}
		});
		rlbRANDoScSHsZQzpz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ((Boolean) rlListView.getTag() == false) {
					rlListView.setVisibility(View.VISIBLE);
					rlListView.setTag(true);
					mImage.setImageResource(R.drawable.icon_arrow_down);
				} else if ((Boolean) rlListView.getTag() == true) {
					rlListView.setVisibility(View.GONE);
					rlListView.setTag(false);
					mImage.setImageResource(R.drawable.icon_arrow_right);
				}

			}
		});

		rlVinOsCshSzQzpz.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				OpenActivityForResult(CshVINCode.class, 1);
			}
		});

		rlListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String text = (String) ((TextView) arg1).getText();
				CshAutomobileMotorcycleTypeBean camt = (CshAutomobileMotorcycleTypeBean) arg0
						.getAdapter().getItem(arg2);
				tvTypeOsCshSzQzpz.setText(text);
			}
		});
	}

	private void minitView() {
		tvBrandOsCshSzQzpz = (TextView) findViewById(R.id.tv_brand_os_csh_sz_qzpz);
		tvTypeOsCshSzQzpz = (TextView) findViewById(R.id.tv_type_os_csh_sz_qzpz);
		tvVinOsCshSzQzpz = (TextView) findViewById(R.id.tv_vin_os_csh_sz_qzpz);
		etPlOsCshSzQzpz = (EditText) findViewById(R.id.et_pl_os_csh_sz_qzpz);
		etMjOsCshSzQzpz = (EditText) findViewById(R.id.et_mj_os_csh_sz_qzpz);
		rlbRANDoScSHsZQzpz = (RelativeLayout) findViewById(R.id.rl_brand_os_csh_sz_qzpz);
		rlVinOsCshSzQzpz = (RelativeLayout) findViewById(R.id.rl_vin_os_csh_sz_qzpz);
		rlListView = (SolutionScrollviewListView) findViewById(R.id.lv_brand_os_csh_sz_qzpz);
		mImage = (ImageView) findViewById(R.id.iv_type_os_csh_sz_qzpz);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.csh_sz_qzpz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
