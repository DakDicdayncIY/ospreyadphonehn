package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import cellcom.com.cn.util.Base64;
import cellcom.com.cn.util.MD5;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.GroundOverlayOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

/**
 * 电子围栏
 * 
 * @author 周子健
 *
 */
public class CshElectronicFenceActivity extends ActivityFrame {
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerA;
	private Marker mMarkerB;
	private Marker mMarkerC;
	private Marker mMarkerD;
	private InfoWindow mInfoWindow;
	BitmapDescriptor bdA;
	// 定位相关
	LocationClient mLocClient;
	Double latitude = null;
	Double longitude = null;
	private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	String serial_no;
	private LocationMode mCurrentMode = LocationMode.NORMAL;
	BitmapDescriptor mCurrentMarker;
	public Boolean isFirstLoc = true;

	Timer time;
	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdGround;
	FinalHttp http = new FinalHttp();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_csh_electronic_fence);
		AppendTitleBody8();
		isShowSlidingMenu(false);
		final Intent intent2 = this.getIntent();
		SetTopBarTitle("电子围栏");

		bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
		bdGround = BitmapDescriptorFactory
				.fromResource(R.drawable.ground_overlay);
		longitude = intent2.getDoubleExtra("longitude", -1);

		latitude = intent2.getDoubleExtra("latitude", -1);

		serial_no = intent2.getStringExtra("serial_no");
		isShowSlidingMenu(false);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();

		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18);
		mBaiduMap.setMapStatus(msu);

		mBaiduMap.setMyLocationEnabled(true);

		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));

		initOverlay();
	}

	@Override
	protected void onStart() {
		onStartGetData();

		super.onStart();
	}

	@Override
	protected void onStop() {
		if (time != null) {
			time.cancel();
		}
		time = null;
		http = null;
		super.onStop();
	}

	/**
	 * 开启获得经纬度 5s一次
	 */
	private void onStartGetData() {
		time = new Timer(true);
		TimerTask task = new TimerTask() {
			public void run() {
				String time = Long
						.toString((System.currentTimeMillis() / 1000));
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
				http.get(FlowConsts.CSH_GOLOG_PLACE_PATH + path,
						new AjaxCallBack<String>() {
							@SuppressLint("NewApi")
							@Override
							public void onSuccess(String t) {
								try {
									JSONObject obj = new JSONObject(t);
									if (obj.getString("code").equals("0")) {

										JSONObject jsonObject = obj
												.getJSONObject("data");
										Double latitude1 = Double
												.valueOf(jsonObject
														.getString("latitude"));
										Double longitude1 = Double.valueOf(jsonObject
												.getString("longitude"));
										initOverlay();
										onGoogleZBaidu(longitude1, latitude1);
									} else {
										Toast.makeText(
												CshElectronicFenceActivity.this,
												"未查询到坐标", Toast.LENGTH_SHORT)
												.show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
			}
		};
		time.schedule(task, 5000, 1000); // 延时1000ms后执行，1000ms执行一次
	}

	// 得到google的经纬度转成百度的
	private void onGoogleZBaidu(Double longitude1, Double latitude1) {

		http.get("http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x="
				+ longitude1 + "&y=" + latitude1, new AjaxCallBack<String>() {
			public void onSuccess(String t) {
				try {
					JSONObject obj = new JSONObject(t);
					Double latitude = Double.valueOf(new String(Base64
							.decode(obj.getString("x")), "utf-8"));
					Double longitude = Double.valueOf(new String(Base64
							.decode(obj.getString("y")), "utf-8"));
					if ("0".equals(obj.getString("error"))) {

						onSetMap(latitude, longitude);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			};
		});
	}

	// 设置
	private void onSetMap(Double latitude, Double longitude) {
		try {
			// MyLocationData locData = new
			// MyLocationData.Builder().accuracy(60)
			// // 此处设置开发者获取到的方向信息，顺时针0-360
			// .direction(100).latitude(latitude).longitude(longitude)
			// .build();
			// mBaiduMap.setMyLocationData(locData);
			//
			// if (isFirstLoc) {
			// isFirstLoc = false;
			// LatLng ll = new LatLng(latitude, longitude);
			// MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			// mBaiduMap.animateMapStatus(u);
			// }
			LatLng llA = new LatLng(longitude, latitude);
			OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
					.zIndex(9).draggable(true);

			clearOverlay(null);
			mBaiduMap.addOverlay(ooA);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置覆盖图片的位置
	public void initOverlay() {
		onSetMap(latitude, longitude);
		// add ground overlay
		LatLng southwest = new LatLng(longitude + 0.0001, latitude + 0.0001);
		LatLng northeast = new LatLng(longitude - 0.0001, latitude - 0.0001);
		LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
				.include(southwest).build();

		OverlayOptions ooGround = new GroundOverlayOptions()
				.positionFromBounds(bounds).image(bdGround).transparency(0.8f);
		mBaiduMap.addOverlay(ooGround);

		MapStatusUpdate u = MapStatusUpdateFactory
				.newLatLng(bounds.getCenter());
		mBaiduMap.setMapStatus(u);

	}

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
		onStartGetData();
	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
		mMapView.onDestroy();

		super.onDestroy();
		// 回收 bitmap 资源
		bdGround.recycle();
	}
}
