package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.adapter.CshAddDataVehicleDefaultEXAdapter;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.TabHost;
import cellcom.com.cn.util.MD5;

/**
 * 车生活-汽车体检-添加车辆-车辆品牌
 * 
 * @author 周子健
 * 
 */
public class CshAddDataVehicleDefault extends ActivityFrame {

	private ExpandableListView elAllOSCshAddDataVehicleDefault;
	private final FinalHttp finalHttp = new FinalHttp();
	public final List<CshAutomobileBrandBean> mGoroupList = new ArrayList<CshAutomobileBrandBean>();
	public List<List<CshAutomobileBrandBean>> mChildList = new ArrayList<List<CshAutomobileBrandBean>>();
	ProgressDialog pdShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_csh_add_data_vehicle_default);
		AppendTitleBody8_3();
		isShowSlidingMenu(false);
		SetTopBarTitle("车辆品牌");
		pdShow = new ProgressDialog(CshAddDataVehicleDefault.this);
		pdShow.setCancelable(false);
		pdShow.setTitle("提示");
		pdShow.setMessage("数据加载中。。。");
		pdShow.show();
		// 初始化视图
		mInitView();
		try {
			// 注册golo接口
			mInitRegister();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}

	// 注册golo接口
	private void mInitRegister() throws NoSuchAlgorithmException {
		AjaxParams ajaxParams = new AjaxParams();
		ajaxParams.put("develop_id", "10094");
		ajaxParams.put("app_id", "2013120200000002");
		String time = Long.toString((System.currentTimeMillis() / 1000));
		String times = (time + "34n4y22u6a1bfe7f4774651jgf8gfc");
		ajaxParams.put("time", time);
		String md5Encode = MD5
				.MD5Encode("action=develop.reg_user&app_id=2013120200000002&develop_id=10094&time="
						+ times);
		ajaxParams.put("sign", md5Encode);

		finalHttp.get(
				FlowConsts.REGISTER_GOLO_PATH + ajaxParams.getParamString(),
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						try {
							JSONObject object = new JSONObject(t);
							JSONObject jsonObject = object
									.getJSONObject("data");

							String string = jsonObject.getString("access_id");
							String string2 = jsonObject
									.getString("access_token");

							SharedPreferences sp = CshAddDataVehicleDefault.this
									.getSharedPreferences(
											"goloRegister",
											CshAddDataVehicleDefault.this.MODE_PRIVATE);
							SharedPreferences.Editor editor = sp.edit();
							
							// 用putString的方法保存数据
							editor.putString("access_id", string);
							editor.putString("access_token", string2);
							editor.commit();

							// 初始化数据
							mInitData();
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						mOnReStartConnect();
					}
				});

	}

	// 重新连接服务器
	protected void mOnReStartConnect() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("网络连接异常！是否重新连接？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setCancelable(false);
		builder.show();
		return;
	}

	// 获得车辆相关数据的接口
	private void mInitData() throws NoSuchAlgorithmException {

		SharedPreferences sp = CshAddDataVehicleDefault.this
				.getSharedPreferences("goloRegister",
						CshAddDataVehicleDefault.this.MODE_PRIVATE);
		String string = sp.getString("access_id", "");
		String string2 = sp.getString("access_token", "");
		String time = Long.toString((System.currentTimeMillis() / 1000));
		String times = (time + "34n4y22u6a1bfe7f4774651jgf8gfc");

		// 生成签名
		String md5Encode = MD5.MD5Encode(("access_id=" + string
				+ "&access_token=" + string2 + "&action=forjth.getbrandid&"
				+ "app_id=2013120200000002&develop_id=10094&time=" + times));
		// 参数路径
		String path = "develop_id=10094&app_id=2013120200000002&access_id="
				+ string + "&" + "access_token=" + string2 + "&time=" + time
				+ "&sign=" + md5Encode;

		finalHttp.get(FlowConsts.CSH_VEHICLE_ALL_PATH + path,
				new AjaxCallBack<String>() {
					@Override
					public void onSuccess(String t) {
						try {

							JSONArray jsonArray = new JSONArray(t);
							for (int i = 0; i < jsonArray.length(); i++) {
								CshAutomobileBrandBean csh = new CshAutomobileBrandBean();

								JSONObject jsonObject = jsonArray
										.getJSONObject(i);
								String carSeriedsId = jsonObject
										.getString("carSeriesId");
								csh.setmCarSeriesId(carSeriedsId);

								csh.setmParentId(jsonObject
										.getString("parentId"));
								csh.setmUrl(jsonObject.getString("url"));
								csh.setmCarSeriesName(jsonObject
										.getString("carSeriesName"));
								mGoroupList.add(csh);
								
								List<CshAutomobileBrandBean> child = new ArrayList<CshAutomobileBrandBean>();
								if (jsonObject.getString("subList") != null
										&& !"".equals(jsonObject
												.getString("subList"))) {

									JSONArray jsonArray2 = jsonObject
											.getJSONArray("subList");
									for (int j = 0; j < jsonArray2.length(); j++) {
										CshAutomobileBrandBean cb = new CshAutomobileBrandBean();
										JSONObject jsonObject2 = jsonArray2
												.getJSONObject(j);
										cb.setmCarSeriesName(jsonObject2
												.getString("carSeriesName"));
										cb.setmCarSeriesId(jsonObject2
												.getString("carSeriesId"));
									
										child.add(cb);
									}
								}
								mChildList.add(child);

								elAllOSCshAddDataVehicleDefault
										.setAdapter(new CshAddDataVehicleDefaultEXAdapter(
												CshAddDataVehicleDefault.this,
												mGoroupList, mChildList));
								pdShow.dismiss();

							}
							elAllOSCshAddDataVehicleDefault
									.setOnGroupClickListener(new OnGroupClickListener() {

										@Override
										public boolean onGroupClick(
												ExpandableListView arg0,
												View arg1, int arg2, long arg3) {
											Log.d("----", mChildList.get(arg2)
													.size()+"");
											
											if (0 == (mChildList.get(arg2)
													.size())) {
												CshAutomobileBrandBean cbb = mGoroupList
														.get(arg2);
												String getmCarSeriesId = cbb
														.getmCarSeriesId();
												Intent intent = new Intent(); //
												// 把返回数据存入Intent

												intent.putExtra("child", getmCarSeriesId);

												// 设置返回数据
												CshAddDataVehicleDefault.this
														.setResult(RESULT_OK,
																intent); //
												// 关闭Activity
												CshAddDataVehicleDefault.this
														.finish();
												return true;
											}
											return false;
										}
									});

							// 点击品牌型号返回
							elAllOSCshAddDataVehicleDefault
									.setOnChildClickListener(new OnChildClickListener() {
										@Override
										public boolean onChildClick(
												ExpandableListView arg0,
												View arg1, int arg2, int arg3,
												long arg4) {

											Intent intent = new Intent(); //
											// 把返回数据存入Intent
											CshAutomobileBrandBean cshAutomobileBrandBean = mChildList.get(arg2).get(arg3);
											intent.putExtra("child", cshAutomobileBrandBean.getmCarSeriesId());

											// 设置返回数据
											CshAddDataVehicleDefault.this
													.setResult(RESULT_OK,
															intent); //
											// 关闭Activity
											CshAddDataVehicleDefault.this
													.finish();

											return true;
										}
									});

						} catch (JSONException e) {
							e.printStackTrace();
							Log.e("CshAddDataVehicleDefault", "服务器json有问题!");
						}
					}

					@Override
					public void onFailure(Throwable t, int errorNo,
							String strMsg) {
						mOnReStartConnect();
					}
				});

	}

	// 初始化视图
	private void mInitView() {
		// 所有品牌
		elAllOSCshAddDataVehicleDefault = (ExpandableListView) findViewById(R.id.el_all_os_csh_add_data_vehicle_default);
		// 去掉箭头
		elAllOSCshAddDataVehicleDefault.setGroupIndicator(null);
	}

}
