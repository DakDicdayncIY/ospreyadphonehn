package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;
import cellcom.com.cn.util.Des3;

import com.cnlaunch.golo.inspection.interfaces.DiagCallBack;
import com.cnlaunch.golo.inspection.interfaces.UnZipFileCallBack;
import com.cnlaunch.golo.inspection.main.DiagEnter;
import com.cnlaunch.golo.inspection.main.UnZipFileUtils;

@SuppressLint("ShowToast")
public class CshPhysicalExamination {
	public void start() {

	}

	/**
	 * 解压文件
	 * 
	 * @param sZipFile
	 *            需要解压的文件路径
	 * @param targetPath
	 *            需要解压的到的文件路径
	 * @param snKey
	 *            序列号
	 * 
	 * @param sysPath
	 *            dpusys.ini文件路径
	 *
	 * @param sp
	 *            保存路径
	 *
	 * @param context
	 *            环境
	 */
	public void DecompressingFiles(String sZipFile, String targetPath,
			final String snKey, final String sysPath,
			final SharedPreferences sp, final Context cotext) {

		UnZipFileUtils.unZipSingleFiles(sZipFile, targetPath, snKey, cotext,
				new UnZipFileCallBack() {
					@Override
					public void onUnZipFileFinish(String zipFilePath,
							String targetPath) {
						// TODO Auto-generated method stub
						Log.d("CshPhysicalExamination", "解压成功");
						Editor edit = sp.edit();
						UnZipFileUtils.copyFile(sysPath, targetPath
								+ "/dpusys.ini");
						// 解压后的路径保存起来
						edit.putString("targetPath", targetPath);
						edit.putString("SysPath", targetPath + "/dpusys.ini");
						edit.commit();
						startPhysical(snKey, cotext);
					}

					@Override
					public void onUnZipFileFailed() {
						Log.e("weige", "解压缩失败");
					}

					@Override
					public void onUnZipFileing() {
						// TODO Auto-generated method stub
						Log.e("weige", "正在解压中");
					}
				});
	}

	/**
	 * 开始体检
	 * 
	 * @param snKey
	 *            golo盒子序列号
	 * 
	 * @param filePath
	 *            诊断软件路径
	 * 
	 * @param mContext
	 *            上下文环境
	 */

	public void startPhysical(String snKey, final Context mContext) {
		
		final ProgressDialog dialog = new ProgressDialog(mContext);
		dialog.setTitle("提示");
		dialog.setMessage("正在体检中。。。");
		dialog.show();
		DiagEnter.getInstance().startDiag(snKey, new DiagCallBack() {
			// 体检返回成功
			@Override
			public void diagSuccess(int mode, String json) {
				// TODO Auto-generated method
				Log.e("weige", "体检成功====diagSuccess(): " + json);

				SharedPreferences sp = mContext.getSharedPreferences(
						"vehicleInformation", mContext.MODE_PRIVATE);

				String mine_car_plate_num = sp.getString("carId", "");
				String car_type_id = sp.getString("typeId", "");
				String car_sub_type_name = sp.getString("code", "");
				String car_displacement = sp.getString("displacement", "");
				String car_producing_year = sp.getString("car_producing_year",
						"");
				String car_gearbox_type = sp.getString("boxId", "");
				String serial_no = sp.getString("serial_no", "");
				String appid = "2";
				Date date = new Date();
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String medical_time = format.format(date);
				String user_id = sp.getString("user_id", "");
				Editor edit = sp.edit();
				String path = sp.getString("Path", "");
				if (!"".equals(path) || path != null) {
					edit.remove("Path");
					edit.commit();
				}

				getEedicalExaminationReport(json, appid, mine_car_plate_num,
						car_type_id, car_sub_type_name, car_displacement,
						car_producing_year, car_gearbox_type, medical_time,
						serial_no, user_id, mContext);

				dialog.dismiss();
			}

			// 体检进度
			public void diagProgress(int progress, String title,
					String content, int index) {
				Log.e("weige", "体检进度====diagProgress(): " + progress
						+ " ,title: " + title + " ,content: " + content
						+ " index: " + index);
			}

			// 体检弹框消失
			@Override
			public void diagDialogDismiss() {
				// TODO Auto-generated method stub n
				Log.e("weige", "弹框消失====diagDialogDismiss()");
			}

			// 体检返回失败
			@Override
			public void diagFailed(String msg, int error) {
				Toast.makeText(mContext, "体检失败，请检查是否连接上golg盒子",
						Toast.LENGTH_SHORT).show();
			}

			// 体检过程提示内容弹框1
			@Override
			public void diagDialogShow(String msg, int flag) {
				// TODO Auto-generated method stub
				Log.e("weige", "提示内容====diagDialogShow(): " + msg);
			}

			// 后台
			@Override
			public void diagBackPressed() {
				// TODO Auto-generated method stub
				Log.e("weige", "后台====diagBackPressed(): " + "keyBack pressed");
			}
		}, mContext);
	}

	// 体检报告
	private void getEedicalExaminationReport(String json, String appid,
			String mine_car_plate_num, String car_type_id,
			String car_sub_type_name, String car_displacement,
			String car_producing_year, String car_gearbox_type,
			String medical_time, String serial_no, String user_id,
			final Context context) {

		FinalHttp http = new FinalHttp();
		AjaxParams ap = new AjaxParams();
		ap.put("report", json);
		ap.put("mine_car_plate_num", mine_car_plate_num);
		ap.put("car_type_id", car_type_id);
		ap.put("car_sub_type_name", car_sub_type_name);
		ap.put("car_displacement", car_displacement);
		ap.put("car_producing_year", car_producing_year);
		ap.put("car_gearbox_type", car_gearbox_type);
		ap.put("serial_no", serial_no);
		ap.put("medical_time", medical_time);
		ap.put("user_id", user_id);
		ap.put("appid", appid);

		http.post(FlowConsts.CSH_MEDICAL_EXAMINATION_REPORT_PATH, ap,
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						try {
							String decode = Des3.decode(t,
									"cellcom2yuying@hunan@$^*");

							JSONObject obj = new JSONObject(decode);

							JSONObject jsonObject = obj.getJSONObject("body");
							String y = jsonObject.getString("state");
							if ("Y".equals(y)) {
								String report = jsonObject
										.getString("report_id");
								SharedPreferences sp = context
										.getSharedPreferences(
												"vehicleInformation",
												context.MODE_PRIVATE);

								String Path = "http://183.6.172.138:8081/yywapp/yyw_query_medical_report.flow?reprot_id="
										+ report;
								Editor edit = sp.edit();
								edit.putString("Path", Path);
								edit.commit();
								
								context.startActivity(new Intent(context,
										CshExaminationReportActivity.class));
							}
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
						Toast.makeText(context, "网络异常", Toast.LENGTH_SHORT)
								.show();
					}
				});
		
	}
}
