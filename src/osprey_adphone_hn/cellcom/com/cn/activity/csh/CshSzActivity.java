package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import java.security.NoSuchAlgorithmException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONObject;

import cellcom.com.cn.util.MD5;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.R.id;
import osprey_adphone_hn.cellcom.com.cn.R.layout;
import osprey_adphone_hn.cellcom.com.cn.R.menu;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CshSzActivity extends ActivityFrame {
	TextView tvDQCL;
	TextView tvCPH;
	ImageView tvCLTP;
	TextView tvQCPZ;
	RelativeLayout rl;
	FinalHttp finalHttp = new FinalHttp();

	String mine_car_plate_num;
	String car_type_id;
	String car_brand_vin;
	String car_gearbox_type;
	String car_displacement;
	String car_producing_year;
	String car_series_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_csh_sz);
		AppendTitleBody8_1();
		SetTopBarTitle("设置");
		// 初始化控件
		mInitView();
		isShowSlidingMenu(false);
		
		try {
			minitData();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	private void minitData() throws NoSuchAlgorithmException {
		SharedPreferences spp = this.getSharedPreferences("goloRegister",
				this.MODE_PRIVATE);
		String serial_no = spp.getString("serial_no", "");
		if (serial_no == null || "".equals(serial_no)) {
			return;
		}
		String time = Long.toString((System.currentTimeMillis() / 1000));
		String sgin = "action=data_develop.get_mine_car_info&develop_id=10094&serial_no="
				+ serial_no
				+ "&time="
				+ time
				+ "34n4y22u6a1bfe7f4774651jgf8gfc";
		String path = FlowConsts.CSH_GAIN_ACTIVATE_CAR_PATH
				+ "develop_id=10094&" + "serial_no=" + serial_no + "&time="
				+ time + "&sign=" + MD5.MD5Encode(sgin);

		finalHttp.get(path, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject obj = new JSONObject(t);
					JSONObject jsonObject = obj.getJSONObject("data");
					mine_car_plate_num = jsonObject
							.getString("mine_car_plate_num");
					car_type_id = jsonObject.getString("car_type_id");
					car_brand_vin = jsonObject.getString("car_brand_vin");
					car_gearbox_type = jsonObject.getString("car_gearbox_type");
					car_displacement = jsonObject.getString("car_displacement");
					car_producing_year = jsonObject
							.getString("car_producing_year");
					car_series_name = jsonObject.getString("car_series_name");

					rl.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(CshSzActivity.this,
									CshSzQZPZActivity.class);
							intent.putExtra("car_series_name", car_series_name);
							intent.putExtra("car_type_id", car_type_id);
							intent.putExtra("car_brand_vin", car_brand_vin);
							intent.putExtra("car_gearbox_type",
									car_gearbox_type);
							intent.putExtra("car_displacement",
									car_displacement);
							intent.putExtra("car_producing_year",
									car_producing_year);
							startActivity(intent);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}

	private void mInitView() {
		SharedPreferences sp = CshSzActivity.this.getSharedPreferences(
				"vehicleInformation", CshSzActivity.this.MODE_PRIVATE);
		String string = sp.getString("carId", null);

		String typeId = sp.getString("typeId", null);
		tvDQCL = (TextView) findViewById(R.id.tv_dqcl_os_csh_sz);

		tvCPH = (TextView) findViewById(R.id.tv_cph_os_csh_sz);

		tvCLTP = (ImageView) findViewById(R.id.tv_cltp_os_csh_sz_dqcl);

		tvQCPZ = (TextView) findViewById(R.id.tv_qcpz_os_csh_sz_cph);

		rl = (RelativeLayout) findViewById(R.id.rl_clpz_os_csh_sz_cph);

		if (tvCPH != null) {
			tvCPH.setText(typeId);
		}

		if (string != null) {
			tvDQCL.setText(string);
			tvQCPZ.setText(string);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.csh_sz, menu);
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
