package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityBase.MyDialogInterface;
import osprey_adphone_hn.cellcom.com.cn.activity.base.FragmentBase;
import osprey_adphone_hn.cellcom.com.cn.activity.main.PreMainActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.main.WebViewActivity;
import osprey_adphone_hn.cellcom.com.cn.bean.Adv;
import osprey_adphone_hn.cellcom.com.cn.bean.AdvComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.BitMapUtil;
import osprey_adphone_hn.cellcom.com.cn.util.ContextUtil;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.Rotate3DAnimation;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.JazzyViewPager.TransitionEffect;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzyviewpager.MyJazzyPagerAdapter;
import p2p.cellcom.com.cn.bean.Message;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;
import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;
import cellcom.com.cn.util.MD5;

/**
 * 我的车
 * 
 * @author wma
 * 
 */
public class CshWdcActivity extends FragmentBase {
	private CshFragmentActivity act;
	private JazzyViewPager mJazzy;
	private List<Adv> advs;
	private LinearLayout dots_ll;// 装载点的布局
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem;
	private ArrayList<View> view_img;
	private ScheduledExecutorService scheduledExecutor;// 定时器，定时轮播广告图片
	private FinalBitmap finalBitmap;
	private FrameLayout fl_ad;

	private LinearLayout ll_cltj;
	private LinearLayout ll_gj;
	private LinearLayout ll_dzwl;
	private LinearLayout ll_dh;
	private LinearLayout ll_wyzx;
	private LinearLayout ll_jjqz;
	private LinearLayout ll_zjzd;
	private LinearLayout ll_sz;

	@Override
	public void onAttach(Activity activity) {
		act = (CshFragmentActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.os_csh_wdc_activity, container,
				false);
		initView(v, savedInstanceState);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initData();
		initListener();
	}

	/**
	 * 初始化控件
	 */
	private void initView(View v, Bundle savedInstanceState) {
		mJazzy = (JazzyViewPager) v.findViewById(R.id.viewpager);
		dots_ll = (LinearLayout) v.findViewById(R.id.ll_dot);
		fl_ad = (FrameLayout) v.findViewById(R.id.fl_ad);

		ll_cltj = (LinearLayout) v.findViewById(R.id.ll_cltj);
		ll_gj = (LinearLayout) v.findViewById(R.id.ll_gj);
		ll_dzwl = (LinearLayout) v.findViewById(R.id.ll_dzwl);
		ll_dh = (LinearLayout) v.findViewById(R.id.ll_dh);
		ll_wyzx = (LinearLayout) v.findViewById(R.id.ll_wyzx);
		ll_jjqz = (LinearLayout) v.findViewById(R.id.ll_jjqz);
		ll_zjzd = (LinearLayout) v.findViewById(R.id.ll_zjzd);
		ll_sz = (LinearLayout) v.findViewById(R.id.ll_sz);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		ll_cltj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences sp = CshWdcActivity.this.getActivity()
						.getSharedPreferences("vehicleInformation",
								Context.MODE_PRIVATE);

				String serial_no = sp.getString("serial_no", "");
				if (!"".equals(serial_no)) {
					OpenActivity(CshPhysicalExaminationActivity.class);
				} else {
					ShowAlertDialog("温馨提示", "此功能需要添加车辆数据支持，是否立即添加",
							new MyDialogInterface() {

								@Override
								public void onClick(DialogInterface dialog) {
									// TODO Auto-generated method stub
									OpenActivity(CshAddData.class);

									dialog.dismiss();
								}
							});
				}
			}
		});
		ll_gj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowMsg(R.string.app_base_ts);
			}
		});
		ll_dzwl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(CshWdcActivity.this.getActivity(),
						CshSFActivtiy.class));
			}
		});

		ll_dh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isAvilible(CshWdcActivity.this.getActivity(),
						"com.baidu.BaiduMap")) {// 传入指定应用包名
					SharedPreferences sp = CshWdcActivity.this
							.getActivity()
							.getSharedPreferences(
									"vehicleInformation",
									CshWdcActivity.this.getActivity().MODE_PRIVATE);
					// String serial_no = sp.getString("serial_no", "");
					String serial_no = "967790128161";
					if (!"".equals(serial_no)) {
						String time = Long.toString((System.currentTimeMillis() / 1000));
						String sign = "action=gps_service.get_current_gps_info&develop_id=10094&devicesn="
								+ serial_no
								+ "&time="
								+ time
								+ "34n4y22u6a1bfe7f4774651jgf8gfc";

						String path = null;
						try {
							path = "develop_id=10094&devicesn=" + serial_no
									+ "&time=" + time + "&sign="
									+ MD5.MD5Encode(sign);
						} catch (NoSuchAlgorithmException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						FinalHttp http = new FinalHttp();
						// 得到鱼鹰盒子的经纬度（车子的经纬度）
						http.get(FlowConsts.CSH_GOLOG_PLACE_PATH + path,
								new AjaxCallBack<String>() {
									@Override
									public void onSuccess(String t) {
										Intent intent = null;
										try {
											JSONObject obj = new JSONObject(t);

											JSONObject jsonObject = obj
													.getJSONObject("data");
											String latitude = jsonObject
													.getString("latitude");
											String longitude = jsonObject
													.getString("longitude");
											Intent intent1 = Intent
													.getIntent("intent://map/marker?location="
															+ latitude
															+ ","
															+ longitude
															+ "&title=我的位置&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
											startActivity(intent1);
										} catch (Exception e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
								});

					} else {
						Toast.makeText(CshWdcActivity.this.getActivity(),
								"请先将鱼鹰盒子激活", Toast.LENGTH_SHORT).show();
					}
				} else {// 未安装
					Toast.makeText(CshWdcActivity.this.getActivity(),
							"请先安装百度地图,在使用本功能", Toast.LENGTH_SHORT).show();
				}
			}
		});

		ll_wyzx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowMsg(R.string.app_base_ts);
			}
		});
		ll_jjqz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowMsg(R.string.app_base_ts);
			}
		});
		ll_zjzd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowMsg(R.string.app_base_ts);
			}
		});
		ll_sz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				OpenActivity(CshSzActivity.class);
			}
		});
	}

	/**
	 * 判断是否安装 某某包
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	private boolean isAvilible(Context context, String packageName) {
		// 获取packagemanager
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		// 用于存储所有已安装程序的包名
		List<String> packageNames = new ArrayList<String>();
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (packageInfos != null) {
			for (int i = 0; i < packageInfos.size(); i++) {
				String packName = packageInfos.get(i).packageName;
				packageNames.add(packName);
			}
		}
		// 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
		return packageNames.contains(packageName);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		finalBitmap = FinalBitmap.create(act);
		// if (ContextUtil.getHeith(act) <= 480) {
		// LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)
		// fl_ad
		// .getLayoutParams();
		// linearParams.height = ContextUtil.dip2px(act, 60);
		// fl_ad.setLayoutParams(linearParams);
		// } else if (ContextUtil.getHeith(act) <= 800) {
		// // if(ContextUtil.getWidth(this)<=480)
		// LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)
		// fl_ad
		// .getLayoutParams();
		// linearParams.height = ContextUtil.dip2px(act, 140);
		// fl_ad.setLayoutParams(linearParams);
		// } else if (ContextUtil.getHeith(act) <= 860) {
		// // if(ContextUtil.getWidth(this)<=480)
		// LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)
		// fl_ad
		// .getLayoutParams();
		// linearParams.height = ContextUtil.dip2px(act, 150);
		// fl_ad.setLayoutParams(linearParams);
		// } else if (ContextUtil.getHeith(act) <= 960) {
		// // if(ContextUtil.getWidth(this)<=480)
		// LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)
		// fl_ad
		// .getLayoutParams();
		// linearParams.height = ContextUtil.dip2px(act, 180);
		// fl_ad.setLayoutParams(linearParams);
		// } else if (ContextUtil.getHeith(act) <= 1280) {
		// LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)
		// fl_ad
		// .getLayoutParams();
		// linearParams.height = ContextUtil.dip2px(act, 200);
		// fl_ad.setLayoutParams(linearParams);
		// } else {
		// LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams)
		// fl_ad
		// .getLayoutParams();
		// linearParams.height = ContextUtil.dip2px(act, 210);
		// fl_ad.setLayoutParams(linearParams);
		// }
		LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) fl_ad
				.getLayoutParams();
		linearParams.width = ContextUtil.getWidth(act);
		linearParams.height = linearParams.width / 2;
		fl_ad.setLayoutParams(linearParams);
		BitMapUtil.getImgOpt(act, finalBitmap, mJazzy,
				R.drawable.os_login_topicon);
		getAdv();
		ll_cltj.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Rotate3DAnimation rotation_zhcz = new Rotate3DAnimation(-90, 0,
						ll_cltj.getWidth() / 2, ll_cltj.getHeight() / 2, 0.0f,
						false);
				rotation_zhcz.setDuration(500);
				rotation_zhcz.setFillAfter(true);
				ll_cltj.startAnimation(rotation_zhcz);
				ll_cltj.setVisibility(View.VISIBLE);

				Rotate3DAnimation rotation_hfk = new Rotate3DAnimation(-90, 0,
						ll_gj.getWidth() / 2, ll_gj.getHeight() / 2, 0.0f,
						false);
				rotation_hfk.setDuration(500);
				rotation_hfk.setFillAfter(true);
				ll_gj.startAnimation(rotation_hfk);
				ll_gj.setVisibility(View.VISIBLE);

				Rotate3DAnimation rotation_yyk = new Rotate3DAnimation(-90, 0,
						ll_dzwl.getWidth() / 2, ll_dzwl.getHeight() / 2, 0.0f,
						false);
				rotation_yyk.setDuration(500);
				rotation_yyk.setFillAfter(true);
				ll_dzwl.startAnimation(rotation_yyk);
				ll_dzwl.setVisibility(View.VISIBLE);

				Rotate3DAnimation rotation_jfk = new Rotate3DAnimation(-90, 0,
						ll_dh.getWidth() / 2, ll_dh.getHeight() / 2, 0.0f,
						false);
				rotation_jfk.setDuration(500);
				rotation_jfk.setFillAfter(true);
				ll_dh.startAnimation(rotation_jfk);
				ll_dh.setVisibility(View.VISIBLE);

				rotation_jfk.setInterpolator(new AccelerateInterpolator());
				// 设置监听
				rotation_jfk.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub

						Rotate3DAnimation rotation_fgg = new Rotate3DAnimation(
								-90, 0, ll_wyzx.getWidth() / 2, ll_wyzx
										.getHeight() / 2, 0.0f, false);
						rotation_fgg.setDuration(500);
						rotation_fgg.setFillAfter(true);
						ll_wyzx.startAnimation(rotation_fgg);
						ll_wyzx.setVisibility(View.VISIBLE);

						Rotate3DAnimation rotation_cxzs = new Rotate3DAnimation(
								-90, 0, ll_jjqz.getWidth() / 2, ll_jjqz
										.getHeight() / 2, 0.0f, false);
						rotation_cxzs.setDuration(500);
						rotation_cxzs.setFillAfter(true);
						ll_jjqz.startAnimation(rotation_cxzs);
						ll_jjqz.setVisibility(View.VISIBLE);

						Rotate3DAnimation rotation_zbsh = new Rotate3DAnimation(
								-90, 0, ll_zjzd.getWidth() / 2, ll_zjzd
										.getHeight() / 2, 0.0f, false);
						rotation_zbsh.setDuration(500);
						rotation_zbsh.setFillAfter(true);
						ll_zjzd.startAnimation(rotation_zbsh);
						ll_zjzd.setVisibility(View.VISIBLE);

						Rotate3DAnimation rotation_shjf = new Rotate3DAnimation(
								-90, 0, ll_sz.getWidth() / 2,
								ll_sz.getHeight() / 2, 0.0f, false);
						rotation_shjf.setDuration(500);
						rotation_shjf.setFillAfter(true);
						ll_sz.startAnimation(rotation_shjf);
						ll_sz.setVisibility(View.VISIBLE);

					}
				});
			}
		});
	}

	private void getAdv() {
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(act,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("pos", "6");
		HttpHelper.getInstances(act).send(FlowConsts.YYW_GETGG,
				cellComAjaxParams, CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onSuccess(CellComAjaxResult cellComAjaxResult) {
						// TODO Auto-generated method stub
						AdvComm advComm = cellComAjaxResult.read(AdvComm.class,
								CellComAjaxResult.ParseType.GSON);
						if (!FlowConsts.STATUE_1.equals(advComm.getReturnCode())) {
							Toast.makeText(act, advComm.getReturnMessage(),
									Toast.LENGTH_SHORT).show();
							return;
						}
						advs = advComm.getBody();
						initJazzViewPager();
					}
				});
	}

	/**
	 * 初始化JazzViewPager开源库
	 */
	private void initJazzViewPager() {
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				ContextUtil.dip2px(act, 8), ContextUtil.dip2px(act, 8));
		ll.setMargins(ContextUtil.dip2px(act, 1.5f), 0,
				ContextUtil.dip2px(act, 1.5f), 0);
		dots = new ArrayList<View>();
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		view_img = new ArrayList<View>();
		if (advs.size() > 0) {
			for (int i = 0; i < advs.size(); i++) {
				View view = act.getLayoutInflater().inflate(
						R.layout.os_main_ad_item, null);
				ImageView img = (ImageView) view.findViewById(R.id.img);
				img.setAdjustViewBounds(true);
				img.setScaleType(ScaleType.FIT_XY);
				finalBitmap.display(img, advs.get(i).getMeitiurl());
				final int tempPos = i;
				img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (!TextUtils.isEmpty(advs.get(tempPos).getLinkurl())) {
							Intent intent = new Intent(act,
									WebViewActivity.class);
							intent.putExtra("url", advs.get(tempPos)
									.getLinkurl());
							startActivity(intent);
						}
					}
				});
				view_img.add(view);
				if (i == 0) {
					ImageView dot = new ImageView(act);
					dot.setLayoutParams(ll);
					dot.setBackgroundResource(R.drawable.app_dot_focused);
					dot.setPadding(ContextUtil.dip2px(act, 1.5f), 0,
							ContextUtil.dip2px(act, 1.5f), 0);
					dots_ll.addView(dot);
					dots.add(dot);
				} else {
					ImageView dot = new ImageView(act);
					dot.setLayoutParams(ll);
					dot.setPadding(ContextUtil.dip2px(act, 1.5f), 0,
							ContextUtil.dip2px(act, 1.5f), 0);
					dot.setBackgroundResource(R.drawable.app_dot_normal);
					dots_ll.addView(dot);
					dots.add(dot);
				}
			}
		}
		if (view_img.size() > 0) {
			mJazzy.setAdapter(new MyJazzyPagerAdapter(view_img, mJazzy));
			mJazzy.setCurrentItem(0);
		}

		mJazzy.setOnPageChangeListener(new MyJazzyViewPager());
		// mJazzy.setPageMargin(30);
		// 创建定时器
		scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
		// 首次启动时3秒后开始执行，接下来3秒执行一次
		scheduledExecutor.scheduleAtFixedRate(new ViewpagerTask(), 3, 5,
				TimeUnit.SECONDS);
	}

	/**
	 * 
	 * 创建自动滚动广告线程
	 * 
	 */
	class ViewpagerTask implements Runnable {
		@Override
		public void run() {
			synchronized (mJazzy) {
				currentItem = (currentItem + 1) % view_img.size();
				handler.sendEmptyMessage(0);
			}
		}
	}

	/**
	 * 更新广告图片
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mJazzy.setCurrentItem(currentItem);
		};
	};

	/**
	 * 
	 * 路口截图改变ViewPager监听
	 * 
	 */
	public class MyJazzyViewPager implements OnPageChangeListener {
		private int oldPosition = 0;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			// mJazzy.setCurrentItem(position);
			dots.get(oldPosition).setBackgroundResource(
					R.drawable.app_dot_normal);
			dots.get(position)
					.setBackgroundResource(R.drawable.app_dot_focused);
			oldPosition = position;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		act.onKeyDown(keyCode, event);
		return false;
	}
}
