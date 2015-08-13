package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import java.security.NoSuchAlgorithmException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.util.Base64;
import cellcom.com.cn.util.MD5;

public class CshSFActivtiy extends ActivityFrame {
	Button btnStarte;
	TextView tvOs3;
	TextView tvOs6;
	TextView tvSf;
	ImageView ivOs3;
	public static int CarState = 0;
	FinalHttp http;

	SharedPreferences sp;
	String serial_no;
	String id;
	Button btnRemove;
	Button btnLookCar;
	String times;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_csh_sfactivtiy);
		AppendTitleBody8_4();
		SetTopBarTitle("设防");
		isShowSlidingMenu(false);

		http = new FinalHttp();
		minit();
		times = Long.toString((System.currentTimeMillis() / 1000));
		onFindAllDZWL();
		// 初始化视图
		minitView();
		// 初始化监听
		minitLitener();
		registerReceiver(br, new IntentFilter(
				"osprey_adphone_hn.cellcom.com.cn.activity.csh.CshSFActivtiy"));
	}

	private void minit() {
		sp = CshSFActivtiy.this.getSharedPreferences("vehicleInformation",
				CshSFActivtiy.this.MODE_PRIVATE);
		serial_no = "967790128161";// sp.getString("serial_no", "");
	}

	BroadcastReceiver br = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String state = arg1.getStringExtra("state");
			// 通知用户车跑了
			if ("tongzi".equals(state)) {
				onShiftOutView();
				startMap();
			} else {
				startMap();
			}
		}
	};

	/**
	 * 打开地图
	 */
	private void startMap() {
		mInitData(new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				Intent intent = null;
				try {
					JSONObject obj = new JSONObject(t);
					if (!"0".equals(obj.getString("code"))) {
						Toast.makeText(CshSFActivtiy.this, "坐标获取失败!",
								Toast.LENGTH_SHORT).show();
						return;
					}

					JSONObject jsonObject = obj.getJSONObject("data");
					String latitude = jsonObject.getString("latitude");
					String longitude = jsonObject.getString("longitude");

					GoogleZBaidu zb = new GoogleZBaidu();
					zb.getJWD(Double.valueOf(longitude),
							Double.valueOf(latitude),
							new AjaxCallBack<String>() {
								@Override
								public void onSuccess(String t) {
									try {
										JSONObject obj = new JSONObject(t);
										if ("0".equals(obj.getString("error"))) {

											Double latitude = Double.valueOf(new String(
													Base64.decode(obj
															.getString("x")),
													"utf-8"));
											Double longitude = Double.valueOf(new String(
													Base64.decode(obj
															.getString("y")),
													"utf-8"));
											Intent intent2 = new Intent(
													CshSFActivtiy.this,
													CshElectronicFenceActivity.class);
											intent2.putExtra("latitude",
													latitude);
											intent2.putExtra("longitude",
													longitude);
											intent2.putExtra("serial_no",
													serial_no);
											startActivity(intent2);
										}
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									// TODO Auto-generated method stub
									super.onFailure(t, errorNo, strMsg);
								}
							});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				Log.d("-=-=-=--=-", errorNo + "");
				super.onFailure(t, errorNo, strMsg);
			}
		});
	}

	// 初始化数据
	private void mInitData(AjaxCallBack<String> ajax) {
		if (!"".equals(serial_no)) {
			String time = Long.toString((System.currentTimeMillis() / 1000));
			String sign = "action=gps_service.get_current_gps_info&develop_id=10094&devicesn="
					+ serial_no
					+ "&time="
					+ time
					+ "34n4y22u6a1bfe7f4774651jgf8gfc";
			String path = null;
			try {
				path = "develop_id=10094&devicesn=" + serial_no + "&time="
						+ time + "&sign=" + MD5.MD5Encode(sign);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 得到鱼鹰盒子的经纬度（车子的经纬度）
			http.get(FlowConsts.CSH_GOLOG_PLACE_PATH + path, ajax);
		} else {
			Toast.makeText(CshSFActivtiy.this, "请先激活鱼鹰盒子", Toast.LENGTH_SHORT)
					.show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	};

	private void minitLitener() {
		btnRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onRemoveDZWL();
			}
		});
		btnLookCar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startMap();
			}
		});
		btnStarte.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!"".equals(id) && id != null) {
					onRemoveDZWL();
				} else {
					mInitData(new AjaxCallBack<String>() {
						@Override
						public void onSuccess(String t) {
							try {
								JSONObject obj = new JSONObject(t);
								if (obj.getString("code").equals("0")) {
									JSONObject jsonObject = obj
											.getJSONObject("data");
									Double latitude = Double.valueOf(jsonObject
											.getString("latitude"));
									Double longitude = Double
											.valueOf(jsonObject
													.getString("longitude"));

									onStartDZWL(latitude, longitude);

								} else {
									Toast.makeText(CshSFActivtiy.this,
											"未查询到坐标", Toast.LENGTH_SHORT)
											.show();
								}
								// onStartDZWL(112.971692, 28.271923);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							Toast.makeText(CshSFActivtiy.this, "网络异常",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			}

		});
	}

	// 移除电子围栏
	private void onRemoveDZWL() {
		String path = "action=bounds_info_service.delete_setting&develop_id=10094&id="
				+ id
				+ "&serial_no="
				+ serial_no
				+ "&time="
				+ Long.toString((System.currentTimeMillis() / 1000));
		try {
			String md5Encode = MD5.MD5Encode(path
					+ "34n4y22u6a1bfe7f4774651jgf8gfc");
			http.get(FlowConsts.CSH_DZWL_PATH + path + "&sign=" + md5Encode,
					new AjaxCallBack<String>() {
						@Override
						public void onSuccess(String t) {
							try {
								Log.d("Csf-----------", t);
								JSONObject obj = new JSONObject(t);
								String code = obj.getString("code");
								if ("0".equals(code)) {
									onRemoveView();

									Toast.makeText(CshSFActivtiy.this,
											"电子围栏移除成功！", Toast.LENGTH_SHORT)
											.show();
									Editor edit = sp.edit();
									edit.remove("CshSf_id");
									edit.commit();
									id = null;
								} else {
									Toast.makeText(CshSFActivtiy.this,
											"电子围栏移除失败！", Toast.LENGTH_SHORT)
											.show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}

					});
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 超出电子围栏后的视图
	private void onShiftOutView() {
		btnStarte.setVisibility(View.GONE);
		tvOs6.setTextColor(Color.parseColor("#F26060"));
		btnRemove.setVisibility(View.VISIBLE);
		btnLookCar.setVisibility(View.VISIBLE);
		ivOs3.setBackgroundColor(R.drawable.img_csh_sf_sos);
	}

	// 移除电子围栏后的视图
	private void onRemoveView() {
		btnStarte.setVisibility(View.VISIBLE);
		btnStarte.setBackgroundResource(R.drawable.btn_bg_open_down);
		btnStarte.setText("开启围栏");
		tvOs6.setVisibility(View.GONE);
		tvOs3.setText("设防未开启");
		tvOs6.setTextColor(Color.parseColor("#CCCCCC"));
		ivOs3.setImageResource(R.drawable.img_csh_sf_off);
	}

	/**
	 * 开启围栏后的位置
	 * 
	 * @param wd
	 *            纬度
	 * @param jd
	 *            经度
	 */
	private void onStartView(String wd, String jd) {
		onGetDZDefault(wd, jd);

		btnStarte.setVisibility(View.VISIBLE);
		btnStarte.setBackgroundResource(R.drawable.btn_cancel_up);
		btnStarte.setText("关闭围栏");
		tvOs6.setVisibility(View.VISIBLE);
		tvOs3.setText("设防已开启");
		tvOs6.setTextColor(Color.parseColor("#2BB770"));
		ivOs3.setImageResource(R.drawable.img_csh_sf_on);
	}

	/**
	 * 经纬度得到详细地址信息
	 * 
	 * @param wd
	 *            纬度
	 * @param jd
	 *            经度
	 */
	public void onGetDZDefault(String wd, String jd) {
		http.get(
				"http://api.map.baidu.com/geocoder/v2/?ak=61f8bd72d68aef3a7b66537761d29d82&callback=renderReverse&location="
						+ wd + "," + jd + "&output=json&pois=0",
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						String replace = t.replace("renderReverse", "")
								.replace("&", "").replace("(", "")
								.replace(")", "");
						try {
							JSONObject obj = new JSONObject(replace);
							if ("0".equals(obj.getString("status"))) {
								JSONObject jsonobj = obj
										.getJSONObject("result");
								String formatted_address = jsonobj
										.getString("formatted_address");
								tvOs6.setText("当前位置:" + formatted_address);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	// 查询所有的电子围栏
	private void onFindAllDZWL() {
		String path = "action=bounds_info_service.get_bounds_setting_data&develop_id=10094&"
				+ "serial_no=" + serial_no + "&time=" + times;
		try {
			String md5Encode = MD5.MD5Encode(path
					+ "34n4y22u6a1bfe7f4774651jgf8gfc");
			String z_path = FlowConsts.CSH_DZWL_PATH + path + "&sign="
					+ md5Encode;
			http.get(z_path, new AjaxCallBack<String>() {
				public void onSuccess(String t) {
					try {
						JSONObject obj = new JSONObject(t);
						String data = obj.getString("data");
						if ("[]".equals(data)) {
							onRemoveView();
						} else {
							JSONArray jsonArray = obj.getJSONArray("data");
							JSONObject jsonObject = jsonArray.getJSONObject(0);
							double longitude = jsonObject
									.getDouble("center_longitude");
							double latitude = jsonObject
									.getDouble("center_latitude");
							id = jsonObject.getString("bounds_id");
							GoogleZBaidu gb = new GoogleZBaidu();
							gb.getJWD(longitude, latitude,
									new AjaxCallBack<String>() {
										public void onSuccess(String t) {
											try {
												JSONObject obj = new JSONObject(
														t);
												if ("0".equals(obj
														.getString("error"))) {

													String latitude = new String(
															Base64.decode(obj
																	.getString("x")),
															"utf-8");
													String longitude = new String(
															Base64.decode(obj
																	.getString("y")),
															"utf-8");
													onStartView(longitude,
															latitude);
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										};
									});

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 开启围栏
	private void onStartDZWL(final Double latitude, final Double longitude)
			throws NoSuchAlgorithmException {
		String path = "action=bounds_info_service.add_setting&desc=12&develop_id=10094&max_latitude="
				+ (latitude + 0.0002)
				+ "&max_longitude="
				+ (longitude + 0.0002)
				+ "&min_latitude="
				+ (latitude - 0.0002)
				+ "&min_longitude="
				+ (longitude - 0.0002)
				+ "&name=test&serial_no="
				+ serial_no
				+ "&time="
				+ Long.toString((System.currentTimeMillis() / 1000));
		String sign = MD5.MD5Encode(path + "34n4y22u6a1bfe7f4774651jgf8gfc");
		String z_path = FlowConsts.CSH_DZWL_PATH + path + "&sign=" + sign;
		http.get(z_path, new AjaxCallBack<String>() {
			public void onSuccess(String t) {
				Log.d("日啊法撒旦", t);
				if (!t.contains("<br/>")) {
					Toast.makeText(CshSFActivtiy.this, "电子围栏开启失败！同一坐标无法开启",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Editor edit2 = sp.edit();
				edit2.putString("latitude", latitude + "");
				edit2.putString("longitude", longitude + "");
				edit2.commit();

				String[] strs = t.split("[<br/>]");
				String data = strs[5];
				try {
					JSONObject obj = new JSONObject(data.replace("test", ""));
					String code = obj.getString("code");
					if ("0".equals(code)) {
						CarState = 1;
						Editor edit = sp.edit();
						id = obj.getString("data");
						edit.putString("CshSf_id", id);
						edit.commit();
						String wd = sp.getString("latitude", "");
						String jd = sp.getString("longitude", "");
						onStartView(wd, jd);
						Toast.makeText(CshSFActivtiy.this, "电子围栏开启成功!",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(CshSFActivtiy.this, "电子围栏开启失败!",
								Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			};
		});

	}

	private void minitView() {
		btnStarte = (Button) findViewById(R.id.btn_start_os_csh_sfactivity);
		btnStarte.setVisibility(View.GONE);
		tvOs3 = (TextView) findViewById(R.id.tv_os_csh_sfactivity3);
		tvOs6 = (TextView) findViewById(R.id.tv_os_csh_sfactivity6);
		ivOs3 = (ImageView) findViewById(R.id.iv_os_csh_sfactivity3);
		btnRemove = (Button) findViewById(R.id.btn_remove_os_csh_sfactivity);
		btnRemove.setVisibility(View.GONE);
		btnLookCar = (Button) findViewById(R.id.btn_look_car_os_csh_sfactivity);
		btnLookCar.setVisibility(View.GONE);

	}

}
