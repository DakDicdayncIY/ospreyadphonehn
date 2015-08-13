package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cellcom.com.cn.util.Des3;
import cellcom.com.cn.util.MD5;

/**
 * 车辆体检的activity
 * 
 * @author 周子健
 *
 */
public class CshPhysicalExaminationActivity extends ActivityFrame {
	protected static final int String = 0;
	public ProgressDialog downLoad;
	public static int progress = -1;
	public static int maxSize = -1;
	private final FinalHttp finalHttp = new FinalHttp();
	private String stringExtra2;
	public String Path = null;
	Button btnStartTJ;
	CshPhysicalExamination cpe = new CshPhysicalExamination();
	ProgressDialog dialog;
	TextView tvNumberOsCshPhysicalExamination;
	TextView tvSctjdfOsCshPhysicalExamination;
	TextView tvJtjlOsCshPhysicalExamination;
	TextView tvTimeOsCshPhysicalExamination;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_csh_physical_examination);
		AppendTitleBody8_2();
		SetTopBarTitle("车辆诊断");
		isShowSlidingMenu(false);

		dialog = new ProgressDialog(CshPhysicalExaminationActivity.this);
		// 初始化视图
		mInitView();
		// 初始化监听
		mInitLitener();

		// 初始化数据
		mInitData();
	}

	private void mInitData() {
		SharedPreferences sp = CshPhysicalExaminationActivity.this
				.getSharedPreferences("vehicleInformation",
						CshPhysicalExaminationActivity.this.MODE_PRIVATE);
		FinalHttp http = new FinalHttp();
		http.post(
				FlowConsts.CSH_LATELY_MEDICAL_EXAMINATION_REPORT_PATH
						+ sp.getString("serial_no", ""),
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						try {
							String decode = Des3.decode(t,
									"cellcom2yuying@hunan@$^*");

							JSONObject obj = new JSONObject(decode);

							JSONObject jsonObject = obj.getJSONObject("body");
							// medical_score：体检分数
							// medical_time：距离上次体检天数 单位:天
							// msg：体检结果总结
							String medical_score = jsonObject
									.getString("medical_score");
							String medical_time = jsonObject
									.getString("medical_time");
							String msg = jsonObject.getString("msg");

							tvTimeOsCshPhysicalExamination
									.setText(medical_time);
							tvNumberOsCshPhysicalExamination
									.setText(medical_score);
							tvSctjdfOsCshPhysicalExamination.setText("上次体检分数");
							tvJtjlOsCshPhysicalExamination.setText(msg);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						Toast.makeText(CshPhysicalExaminationActivity.this,
								"网络异常", Toast.LENGTH_SHORT).show();
					}
				});

	}

	private void mInitLitener() {
		btnStartTJ.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				btnStartTJ.setClickable(false);

				// 初始化检测
				mInitDetection();
			}
		});

	}

	// 初始化检测
	private void mInitDetection() {
		SharedPreferences sp = CshPhysicalExaminationActivity.this
				.getSharedPreferences("vehicleInformation",
						CshPhysicalExaminationActivity.this.MODE_PRIVATE);

		String absolutePath = sp.getString("absolutePath", null);
		String fileName = sp.getString("fileName", null);
		String SysPath = sp.getString("SysPath", null);
		String targetPath = sp.getString("targetPath", "");
		String serial_no = sp.getString("serial_no", "");
		// 解压后的车辆配置文件路径
		if (absolutePath == null || "".equals(absolutePath)) {
			AlertDialog.Builder builder = new Builder(
					CshPhysicalExaminationActivity.this);
			builder.setTitle("提示");
			builder.setMessage("车辆体检,要下载相关的配置文件,是否继续下载？");
			builder.setPositiveButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					CshPhysicalExaminationActivity.this.finish();
				}
			});
			builder.setNegativeButton("确认", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					SharedPreferences sp = CshPhysicalExaminationActivity.this
							.getSharedPreferences(
									"vehicleInformation",
									CshPhysicalExaminationActivity.this.MODE_PRIVATE);

					String user_id = sp.getString("user_id", "");
					String serial_no = sp.getString("serial_no", "");
					String time = sp.getString("time", "");
					String app_id = sp.getString("app_id", "");
					String develop_id = sp.getString("develop_id", "");
					String display_lan = sp.getString("display_lan", "");

					// 进行下载
					downloadFile(user_id, serial_no, time, app_id, develop_id,
							display_lan);
				}
			});
			builder.show();
		} else {
			// 直接体检
			if (!"".equals(targetPath) && targetPath != null) {
				cpe.startPhysical(serial_no,
						CshPhysicalExaminationActivity.this
								.getApplicationContext());
			} else {
				// 解压后在体检
				cpe.DecompressingFiles(absolutePath + fileName, absolutePath,
						serial_no, SysPath, sp,
						CshPhysicalExaminationActivity.this
								.getApplicationContext());
			}
		}
	}

	// 下载文件
	protected void downloadFile(String user_id, String serial_no, String time,
			String app_id, String develop_id, String display_lan) {
		try {

			// 获取下载配置文件
			downloadConfigurationFile(app_id, develop_id, serial_no, time,
					user_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CshPhysicalExamination cpe = new CshPhysicalExamination();

	}

	// 初始化视图
	private void mInitView() {
		downLoad = new ProgressDialog(CshPhysicalExaminationActivity.this);
		downLoad.setTitle("提示");
		downLoad.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downLoad.setMessage("正在下载车辆配置文件。。。");
		downLoad.setCancelable(false);

		btnStartTJ = (Button) findViewById(R.id.btn_start_tj_os_csh_physical_examination);
		tvNumberOsCshPhysicalExamination = (TextView) findViewById(R.id.tv_number_os_csh_physical_examination);
		tvTimeOsCshPhysicalExamination = (TextView) findViewById(R.id.tv_time_os_csh_physical_examination);

		tvSctjdfOsCshPhysicalExamination = (TextView) findViewById(R.id.tv_sctjdf_os_csh_physical_examination);
		tvJtjlOsCshPhysicalExamination = (TextView) findViewById(R.id.tv_jtjl_os_csh_physical_examination);
	}

	final Handler han = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {

			}
			if (msg.what == 3) {
				downLoad.setMax(maxSize);
				downLoad.show();
			} else if (msg.what == 4) {
				downLoad.setProgress(progress);
			}
			if (maxSize == progress) {
				maxSize = -1;
				progress = 0;
				downLoad.setMax(0);
				downLoad.setProgress(0);
				downLoad.dismiss();
			}
		};

	};

	// 获取下载配置文件
	private void downloadConfigurationFile(final String app_id,
			final String develop_id, final String serial_no, final String time,
			final String user_id) throws NoSuchAlgorithmException {
		String Sgin = "action=data_develop.get_car_eobd&app_id=" + app_id
				+ "&develop_id=" + develop_id + "&serial_no=" + serial_no
				+ "&time=" + time + "34n4y22u6a1bfe7f4774651jgf8gfc";
		String sign = MD5.MD5Encode(Sgin);

		String path1 = FlowConsts.CSH_CONFIGURATIONFILES_PATH + "app_id="
				+ app_id + "&" + "develop_id=" + develop_id + "&"
				+ "serial_no=" + serial_no + "&" + "time=" + time + "&"
				+ "sign=" + sign;
		Log.d("-=-=-=-=-=--------周子健-------------CshPhysicalExaminationActivity",
				path1);
		finalHttp.get(path1, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject obj = new JSONObject(t);
					if ("0".equals(obj.getString("code"))) {
						JSONObject jsonObject = obj.getJSONObject("data");
						String configId = jsonObject.getString("configId");
						String sign = jsonObject.getString("sign");
						String cc = jsonObject.getString("user_id");
						String obd_config = jsonObject.getString("obd_config");
						String obd_sign = jsonObject.getString("obd_sign");

						final String serial_no = jsonObject
								.getString("serial_no");
						Log.d("CshPhysicalExaminationActivtiy=====周子健=======------------------",
								"configId:" + configId + "sign" + sign + "cc"
										+ cc + "obd_config" + obd_config
										+ "obd_sign" + obd_sign + "serial_no"
										+ serial_no);
						// 下载车辆配置文件
						final Map<String, String> map = new HashMap<String, String>();
						map.put("cc", cc);
						map.put("versionDetailId", configId);
						map.put("productSerialNo", serial_no);
						map.put("sign", sign);

						final Map<String, String> map1 = new HashMap<String, String>();
						map1.put("cc", cc);
						map1.put("versionDetailId", obd_config);
						map1.put("productSerialNo", serial_no);
						map1.put("sign", obd_sign);

						// 下载sys.ini文件
						downloadSysIni(user_id, serial_no, time, app_id,
								develop_id, "cn");
						Thread thread = new Thread() {
							public void run() {
								boolean sdCardExist = Environment
										.getExternalStorageState().equals(
												Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
								// 下载后车辆配置文件的路径
								String absolutePath = null;
								if (sdCardExist) {
									absolutePath = Environment
											.getExternalStorageDirectory()
											.toString()
											+ "/" + "YuYing/zip";// 获取跟目录

								}
								if (absolutePath == null) {
									Toast.makeText(
											CshPhysicalExaminationActivity.this,
											"请插入内存卡", Toast.LENGTH_SHORT)
											.show();
									CshPhysicalExaminationActivity.this
											.finish();
								}

								CshDownload cd = new CshDownload();
								String fileName = cd
										.doPost(FlowConsts.CSH_VEHICLE_DISPOSITION_PATH,
												map, absolutePath,
												new ProgresLitener() {
													@Override
													public void getMax(int max) {
														maxSize = max;
														Message message = new Message();
														message.what = 3;
														han.sendMessage(message);
													}

													@Override
													public void getDownLoad(
															int size) {
														progress = size;
														Message me = new Message();
														me.what = 4;
														han.sendMessage(me);
													}
												});

								String sysName = cd.doPost(
										FlowConsts.CSH_EOBD_DISPOSITION_PATH,
										map1, absolutePath,
										new ProgresLitener() {

											@Override
											public void getMax(int max) {
												maxSize = max;
												Message message = new Message();
												message.what = 3;
												han.sendMessage(message);
											}

											@Override
											public void getDownLoad(int size) {
												progress = size;
												Message me = new Message();
												me.what = 4;
												han.sendMessage(me);
											}
										});

								SharedPreferences sp = CshPhysicalExaminationActivity.this
										.getSharedPreferences(
												"vehicleInformation",
												CshPhysicalExaminationActivity.this.MODE_PRIVATE);
								Editor edit = sp.edit();
								edit.putString("absolutePath", absolutePath
										+ "/");
								edit.putString("fileName", fileName);
								edit.commit();

								// /storage/emulated/0/YuYing/zip
								// EOBD2_V10_04_CN.ZIP 967790103961
								// /storage/emulated/0/YuYing/zip/967790103961
								// 解压文件

								cpe.DecompressingFiles(absolutePath + "/"
										+ fileName, absolutePath + "/",
										serial_no, absolutePath + "/"
												+ "dpusys.ini", sp,
										CshPhysicalExaminationActivity.this);

								// cpe.startPhysical(
								// "967790103961",
								// "/storage/emulated/0/YuYing/zip/967790103961",
								// CshPhysicalExaminationActivity.this.getApplicationContext());
								Message message = new Message();
								message.what = 0;
								han.sendMessage(message);
							};
						};
						thread.start();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	// 下载sys.ini文件
	private void downloadSysIni(String user_id, String serial_no, String time,
			String app_id, String develop_id, String display_lan)
			throws NoSuchAlgorithmException {

		String sign = "action=data_develop.get_config_info&" + "app_id="
				+ app_id + "&develop_id=" + develop_id + "&display_lan="
				+ display_lan + "&serial_no=" + serial_no + "&time=" + time
				+ "34n4y22u6a1bfe7f4774651jgf8gfc";
		String sign1 = MD5.MD5Encode(sign);
		String path = FlowConsts.CSH_SYSINI_PATH + "&app_id=" + app_id
				+ "&develop_id=" + develop_id + "&serial_no=" + serial_no
				+ "&time=" + time + "&sign=" + sign1;
		finalHttp.get(path, null, new AjaxCallBack<String>() {
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(String t) {
				try {
					JSONObject obj = new JSONObject(t);
					if ("0".equals(obj.getString("code"))) {
						JSONObject jsonObject = obj.getJSONObject("data");
						String string = jsonObject.getString("iniFileContent");
						// 下载后车辆配置文件的路径
						String filePath = Environment
								.getExternalStorageDirectory().toString()
								+ "/"
								+ "YuYing/zip";// 获取跟目录

						File file = new File(filePath);
						if (!file.exists()) {// 判断文件夹是否存在（不存在则创建这个文件夹）
							file.mkdirs();// 创建文件夹
						}
						File file1 = new File(filePath + "/DPUSYS.INI");
						if (!file1.exists()) {
							file1.createNewFile();
						}
						FileOutputStream fos = new FileOutputStream(file1);
						OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
								fos, "UTF-8");
						outputStreamWriter.write(string);

						outputStreamWriter.close();
						fos.close();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
}
