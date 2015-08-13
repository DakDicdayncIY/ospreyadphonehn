package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.SolutionScrollviewListView;
import osprey_adphone_hn.cellcom.com.cn.adapter.CshAddBaseAdapter;
import osprey_adphone_hn.cellcom.com.cn.adapter.CshCarDisplacementAdapter;
import osprey_adphone_hn.cellcom.com.cn.adapter.CshGridViewAdapter;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.util.MD5;

/**
 * 车生活-车辆体检-添加车辆数据
 * 
 * @author 周子健
 *
 */
public class CshAddData extends ActivityFrame {
	private TextView tvBrandOsCshAddData;
	private RelativeLayout rlBrandOsCshAddData;
	private ExpandableListView elvOsCshAddData;
	private RelativeLayout rlShowListView;
	private SolutionScrollviewListView rlListView;
	private TextView mText;
	private ImageView mImage;
	private RelativeLayout mRlVindOsCshAddData;
	private final String[] mPlateNumber = { "京", "沪", "津", "渝", "冀", "蒙", "辽",
			"吉", "黑", "苏", "皖", "闽", "赣", "鲁", "豫", "鄂", "湘", "粤", "桂", "琼",
			"川", "贵", "云", "藏", "藏", "甘", "青", "宁", "新" };
	private TextView mtvVinOsCshAddData;
	private Spinner spPlOsCshAddData;
	private final FinalHttp finalHttp = new FinalHttp();
	Dialog dialog;
	private List<CshAutomobileMotorcycleTypeBean> mChildList = new ArrayList<CshAutomobileMotorcycleTypeBean>();
	private TextView mTvTypeOsCshAddData;
	private TextView mTvTypeIdOsCshAddData;
	private RadioGroup mtg;
	private TextView tvBrandIsOsCshAddData;
	public static final int CSH_ADD_DATA_VEHICLEE_DEFAULT_RESULT = 1;
	private String cshReadioTag = "1";
	public static final int CSH_CSH_VIN_CODE = 1;
	private CshPhysicalExamination cpe = new CshPhysicalExamination();
	public ProgressDialog downLoad;
	public static int progress = -1;
	public static int maxSize = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_csh_add_data);
		AppendTitleBody8();
		SetTopBarTitle("车辆信息");
		isShowSlidingMenu(false);

		// 初始化数据
		initView();

		// 初始化监听
		initLister();

		// 初始化适配
		initAdapter();

		// 注册广播
		registerReceiver(saveReceiver, new IntentFilter(
				"osprey_adphone_hn.cellcom.com.cn.activity.csh.CshAddData"));
	}

	// 激活车辆广播
	BroadcastReceiver saveReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {

			if (intent.getAction().equals(
					"osprey_adphone_hn.cellcom.com.cn.activity.csh.CshAddData")) {
				// 车牌号码
				EditText etImport = (EditText) CshAddData.this
						.findViewById(R.id.et_import_os_csh_add_data);
				TextView tvCshAddData = (TextView) CshAddData.this
						.findViewById(R.id.tv_os_csh_add_data);

				// 车辆品牌
				TextView tvBrand = (TextView) CshAddData.this
						.findViewById(R.id.tv_brand_id_os_csh_add_data);
				// 车辆型号
				TextView tvType = (TextView) CshAddData.this
						.findViewById(R.id.tv_type_os_csh_add_data);

				TextView tvVin = (TextView) CshAddData.this
						.findViewById(R.id.tv_vin_os_csh_add_data);
				// 变速箱
				RadioGroup rg = (RadioGroup) CshAddData.this
						.findViewById(R.id.rg_id);
				rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						RadioButton rb = (RadioButton) CshAddData.this
								.findViewById(arg0.getCheckedRadioButtonId());
						cshReadioTag = (String) rb.getTag();
					}
				});

				// 排量
				final EditText etPl = (EditText) CshAddData.this
						.findViewById(R.id.et_pl_os_csh_add_data);
				etPl.setTag("L");
				// 排量的单位
				Spinner sp = (Spinner) CshAddData.this
						.findViewById(R.id.sp_pl_os_csh_add_data);
				sp.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						TextView tv = (TextView) arg1;
						String pls = tv.getText().toString();
						etPl.setTag(pls);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
				// 年款
				EditText etMj = (EditText) CshAddData.this
						.findViewById(R.id.et_mj_os_csh_add_data_automatic);
				// golo数列号
				EditText etSerial = (EditText) CshAddData.this
						.findViewById(R.id.et_serial_number_os_csh_add_data_automatic);
				// 密码
				EditText etPassword = (EditText) CshAddData.this
						.findViewById(R.id.et_password);

				// 获得数据值并且进行md5加密
				try {
					// 车牌号码
					String num = tvCshAddData.getText().toString()
							+ etImport.getText().toString();

					String code = tvBrand.getText().toString();
					String serial = etSerial.getText().toString();
					String carcase = tvVin.getText().toString();

					String appId = "2013120200000002";
					String display = "cn";
					String type = tvType.getText().toString();
					String year = etMj.getText().toString();

					String displacement = etPl.getText().toString()
							+ etPl.getTag();
					String boxId = cshReadioTag;
					String passord = etPassword.getText().toString();
					SharedPreferences spp = CshAddData.this
							.getSharedPreferences("goloRegister",
									CshAddData.this.MODE_PRIVATE);
					String access_id = spp.getString("access_id", "");
					String access_token = spp.getString("access_token", "");
					String time = Long
							.toString((System.currentTimeMillis() / 1000));
					String times = MD5.MD5Encode(time);
					String qm = ("access_id="
							+ access_id
							+ "&access_token="
							+ access_token
							+ "&action=forjth.save_car_config&app_id=2013120200000002&car_carcase_num="
							+ carcase
							+ "&car_displacement="
							+ displacement
							+ "&car_gearbox_type="
							+ boxId
							+ "&car_producing_year="
							+ year
							+ "&car_type_id="
							+ type
							+ "&code_id="
							+ code
							+ "&develop_id=10094&display_lan=cn&mine_car_plate_num="
							+ num + "&password=" + passord + "&serial_no="
							+ serial + "&time=" + time + "34n4y22u6a1bfe7f4774651jgf8gfc");
					// 生成签名
					String md5Encode = MD5.MD5Encode(qm);
					activateVehicle(access_id, num, code, serial, carcase,
							appId, display, type, year, displacement, boxId,
							passord, access_id, access_token, time, md5Encode);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				CshAddData.this.finish();
			}

		}

	};

	// 激活车辆
	private void activateVehicle(String access_id, final String num,
			final String cdoe, final String serial, String carcase,
			final String appId, final String display, final String typeId,
			String producingYear, final String displacement,
			final String boxId, String passwrod, String accessId, String token,
			final String time, String sign) {
		String path = FlowConsts.CSH_ACTIVATE_PATH + "mine_car_plate_num="
				+ num + "&code_id=" + cdoe + "&serial_no=" + serial
				+ "&car_carcase_num=" + carcase + "&app_id=" + appId
				+ "&display_lan=" + display + "&car_type_id=" + typeId
				+ "&car_producing_year=" + producingYear + "&car_displacement="
				+ displacement + "&car_gearbox_type=" + boxId + "&password="
				+ passwrod + "&access_id=" + accessId + "&access_token="
				+ token + "&time=" + time + "&develop_id=10094&sign=" + sign;
		FinalHttp finalHttp = new FinalHttp();
		finalHttp.get(path, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject obj = new JSONObject(t);

					Log.d("CshAddData_激活", t);

					if ("0".equals(obj.getString("code"))) {
						Toast.makeText(CshAddData.this, "激活成功",
								Toast.LENGTH_SHORT).show();
						SharedPreferences sp = CshAddData.this
								.getSharedPreferences("vehicleInformation",
										CshAddData.this.MODE_PRIVATE);
						SharedPreferences.Editor editor = sp.edit();

						// 用putString的方法保存数据
						editor.putString("displacement", displacement);
						editor.putString("carId", num);
						editor.putString("typeId", typeId);
						editor.putString("code", cdoe);
						editor.putString("serial_no", serial);
						editor.putString("user_id", obj.getString("user_id"));
						editor.putString("serial_no", serial);
						editor.putString("time", time);
						editor.putString("app_id", appId);
						editor.putString("develop_id", "10094");
						editor.putString("display_lan", "cn");
						editor.putString("boxId", boxId);
						editor.commit();

						startActivity(new Intent(CshAddData.this,
								CshPhysicalExaminationActivity.class));

					} else {
						Toast.makeText(CshAddData.this, "激活失败!序列号或者密码错误。",
								Toast.LENGTH_LONG).show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	// 开始体检
	public void startPhysicalExamination() {

	}

	private void initAdapter() {
		spPlOsCshAddData.setAdapter(new CshCarDisplacementAdapter(
				CshAddData.this));
	}

	private void showDialog() {
		View view = getLayoutInflater().inflate(
				R.layout.os_csh_add_data_license_tag, null);
		GridView gv = (GridView) view
				.findViewById(R.id.gv_os_csh_add_data_license_tag);
		gv.setHorizontalSpacing(10);
		gv.setVerticalSpacing(10);
		gv.setAdapter(new CshGridViewAdapter(mPlateNumber, CshAddData.this));
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mText.setText(mPlateNumber[arg2]);
				dialog.dismiss();
			}
		});

		dialog = new Dialog(CshAddData.this, R.style.CshDialogTheme);

		dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		Window window = dialog.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.CshDialogAnimation);
		WindowManager wm = getWindowManager();
		Display d = wm.getDefaultDisplay();// 获得宽和高

		WindowManager.LayoutParams wl = window.getAttributes();
		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();
		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = d.getWidth();
		wl.height = (int) (d.getHeight() * 0.3);

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);
		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	// 监听
	private void initLister() {
		rlBrandOsCshAddData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				OpenActivityForResult(CshAddDataVehicleDefault.class,
						CSH_ADD_DATA_VEHICLEE_DEFAULT_RESULT);
			}
		});
		rlShowListView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 显示集合
				if ((Boolean) rlListView.getTag() == false) {
					rlListView.setVisibility(View.VISIBLE);
					rlListView.setTag(true);
					mImage.setImageResource(R.drawable.icon_arrow_down);
				} else if ((Boolean) rlListView.getTag() == true) {// 隐藏集合
					rlListView.setVisibility(View.GONE);
					rlListView.setTag(false);
					mImage.setImageResource(R.drawable.icon_arrow_right);
				}

			}
		});

		mText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDialog();
			}
		});

		mRlVindOsCshAddData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				OpenActivityForResult(CshVINCode.class, CSH_CSH_VIN_CODE);
			}
		});

		rlListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String text = (String) ((TextView) arg1).getText();
				CshAutomobileMotorcycleTypeBean camt = (CshAutomobileMotorcycleTypeBean) arg0
						.getAdapter().getItem(arg2);
				mTvTypeOsCshAddData.setText(text);

				if ((Boolean) rlListView.getTag() == true) {// 隐藏集合
					rlListView.setVisibility(View.GONE);
					rlListView.setTag(false);
					mImage.setImageResource(R.drawable.icon_arrow_right);
				}
			}
		});
	}

	// 关闭上一个activity的回调函数
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0 == CSH_ADD_DATA_VEHICLEE_DEFAULT_RESULT && arg1 == RESULT_OK) {
			String child = arg2.getStringExtra("child");
			tvBrandIsOsCshAddData.setText(child);

			sendHttp(child);

		} else if (arg0 == CSH_CSH_VIN_CODE && arg1 == CSH_CSH_VIN_CODE) {
			String stringExtra = arg2.getStringExtra("code");
			if (!"".equals(stringExtra) && stringExtra != null) {
				mtvVinOsCshAddData.setText(stringExtra);
			}
		}
	}

	// 根据详细车子defaultId查询
	private void sendHttp(String child) {
		finalHttp.get(FlowConsts.CSH_VEHICLE_DEFAULT_PATH + child,
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						try {
							mChildList.removeAll(mChildList);

							JSONObject obj = new JSONObject(t);
							JSONArray jsonArray = obj.getJSONArray("data");
							RemoveSpacing rs = new RemoveSpacing();
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonObject = jsonArray
										.getJSONObject(i);
								CshAutomobileMotorcycleTypeBean cmtb = new CshAutomobileMotorcycleTypeBean();
								cmtb.setmCarType(rs.remove(
										jsonObject.getString("carType"), ' '));
								cmtb.setmDetailId(jsonObject
										.getString("detailId"));
								cmtb.setmDetailName(jsonObject
										.getString("detailName"));
								cmtb.setmDiagCarModel(jsonObject
										.getString("diagCarModel"));
								cmtb.setmId(jsonObject.getString("id"));
								cmtb.setmLanId(jsonObject.getString("lanId"));
								mChildList.add(cmtb);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						rlListView.setAdapter(new CshAddBaseAdapter(
								CshAddData.this, mChildList));
						// 设置车辆品牌名
						tvBrandOsCshAddData.setText(mChildList.get(0)
								.getmDetailName());
					}
				});
	}

	// 初始化数据
	private void initView() {
		downLoad = new ProgressDialog(CshAddData.this);
		downLoad.setTitle("提示");
		downLoad.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downLoad.setMessage("正在下载车辆配置文件。。。");
		downLoad.setCancelable(false);

		tvBrandOsCshAddData = (TextView) findViewById(R.id.tv_brand_os_csh_add_data);

		tvBrandIsOsCshAddData = (TextView) findViewById(R.id.tv_brand_id_os_csh_add_data);

		rlBrandOsCshAddData = (RelativeLayout) findViewById(R.id.rl_brand_os_csh_add_data);

		rlShowListView = (RelativeLayout) findViewById(R.id.rl_brand_os_csh_pull_down);

		rlListView = (SolutionScrollviewListView) findViewById(R.id.lv_brand_os_csh_add_data);

		rlListView.setTag(false);

		mText = (TextView) findViewById(R.id.tv_os_csh_add_data);

		mImage = (ImageView) findViewById(R.id.iv_type_os_csh_add_data);

		mRlVindOsCshAddData = (RelativeLayout) findViewById(R.id.rl_vin_os_csh_add_data);

		mtvVinOsCshAddData = (TextView) findViewById(R.id.tv_vin_os_csh_add_data);

		spPlOsCshAddData = (Spinner) findViewById(R.id.sp_pl_os_csh_add_data);

		mTvTypeOsCshAddData = (TextView) findViewById(R.id.tv_type_os_csh_add_data);

		mtg = (RadioGroup) findViewById(R.id.rg_id);
	}

}
