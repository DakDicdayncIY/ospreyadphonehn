package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.List;

import cellcom.com.cn.net.CellComAjaxHttp;
import cellcom.com.cn.net.CellComAjaxParams;
import cellcom.com.cn.net.CellComAjaxResult;
import cellcom.com.cn.net.base.CellComHttpInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import osprey_adphone_hn.cellcom.com.cn.activity.setting.DhzxActivity;
import osprey_adphone_hn.cellcom.com.cn.activity.welcome.LoginActivity;
import osprey_adphone_hn.cellcom.com.cn.adapter.GrzxHfkAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.GrzxCaichan;
import osprey_adphone_hn.cellcom.com.cn.bean.GrzxCaichanComm;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import osprey_adphone_hn.cellcom.com.cn.net.HttpHelper;
import osprey_adphone_hn.cellcom.com.cn.util.CommonUtils;
import osprey_adphone_hn.cellcom.com.cn.util.SharepreferenceUtil;
import osprey_adphone_hn.cellcom.com.cn.widget.fbutton.FButton;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyHelper;
import osprey_adphone_hn.cellcom.com.cn.widget.jazzylistview.JazzyListView;

/**
 * 个人中心金币库界面
 * 
 * @author Administrator
 * 
 */
public class DhbGrzxYykActivity extends ActivityFrame {
	private TextView tv_kyyy, tv_yyzs, tv_dhcs;
	private FButton btn_dhzx;
	private JazzyListView mJazzy;
	private GrzxHfkAdapter adapter;
	private List<GrzxCaichan> grzxCaichan = new ArrayList<GrzxCaichan>();
	private TextView tv_empty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_dhb_grzx_yyk_activity);
		AppendTitleBody1();
		SetTopBarTitle(getResources().getString(R.string.os_dhb_grzx_yyk));
		initView();
		initListener();
		initData();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		tv_kyyy = (TextView) findViewById(R.id.tv_kyyy);
		tv_yyzs = (TextView) findViewById(R.id.tv_yyzs);
		tv_dhcs = (TextView) findViewById(R.id.tv_dhcs);
		btn_dhzx = (FButton) findViewById(R.id.btn_dhzx);

		tv_empty = (TextView) findViewById(R.id.tv_empty);
		mJazzy = (JazzyListView) findViewById(R.id.listview);
		mJazzy.setTransitionEffect(JazzyHelper.SLIDE_IN);
		adapter = new GrzxHfkAdapter(this, grzxCaichan);
		mJazzy.setAdapter(adapter);
	}

	/**
	 * 初始化监听
	 */
	private void initListener() {
		mJazzy.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});

		tv_empty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initData();
			}
		});

		// 兑换中心
		btn_dhzx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommonUtils.setCurrentChildMenuActivity("dhzx");
				Intent intent = new Intent(DhbGrzxYykActivity.this,
						DhzxActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		grzxCaichan.clear();
		getYingyuanInfos();
	}

	/**
	 * 获取金币库数据
	 */
	private void getYingyuanInfos() {
		if (SharepreferenceUtil.readString(this, SharepreferenceUtil.fileName,
				"uid", "").equals("")) {
			Intent loginintent = new Intent(DhbGrzxYykActivity.this,
					LoginActivity.class);
			startActivity(loginintent);
			return;
		}
		CellComAjaxParams cellComAjaxParams = new CellComAjaxParams();
		cellComAjaxParams.put("uid", SharepreferenceUtil.readString(this,
				SharepreferenceUtil.fileName, "uid", ""));
		cellComAjaxParams.put("moneytype", "3");
		HttpHelper.getInstances(DhbGrzxYykActivity.this).send(
				FlowConsts.YYW_GETCAICHAN, cellComAjaxParams,
				CellComAjaxHttp.HttpWayMode.POST,
				new CellComHttpInterface.NetCallBack<CellComAjaxResult>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						ShowProgressDialog(R.string.hsc_progress);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						DismissProgressDialog();
						tv_empty.setVisibility(View.VISIBLE);
						mJazzy.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(CellComAjaxResult arg0) {
						// TODO Auto-generated method stub
						DismissProgressDialog();
						GrzxCaichanComm grzxCaichanComm = arg0.read(
								GrzxCaichanComm.class,
								CellComAjaxResult.ParseType.GSON);
						String state = grzxCaichanComm.getReturnCode();
						String msg = grzxCaichanComm.getReturnMessage();
						if (!FlowConsts.STATUE_1.equals(state)) {
							ShowMsg(msg);
							tv_empty.setVisibility(View.VISIBLE);
							mJazzy.setVisibility(View.GONE);
							return;
						}
						try {
							tv_empty.setVisibility(View.GONE);
							mJazzy.setVisibility(View.VISIBLE);
							if (grzxCaichanComm.getBody().getMoney() != null) {
								tv_kyyy.setText(grzxCaichanComm.getBody()
										.getMoney());
								SharepreferenceUtil.write(
										DhbGrzxYykActivity.this,
										SharepreferenceUtil.fileName,
										"yinyuan", grzxCaichanComm.getBody()
												.getMoney());
							} else {
								tv_kyyy.setText("0");
							}
							if (grzxCaichanComm.getBody().getLeijimoney() != null) {
								tv_yyzs.setText(grzxCaichanComm.getBody()
										.getLeijimoney() + "金币");
							} else {
								tv_yyzs.setText(0 + "金币");
							}
							if (grzxCaichanComm.getBody().getLeijimoney() != null) {
								tv_dhcs.setText(grzxCaichanComm.getBody()
										.getDuihuantimes());
							} else {
								tv_dhcs.setText("0");
							}
							grzxCaichan.addAll(grzxCaichanComm.getBody()
									.getData());
							adapter.notifyDataSetChanged();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}
}
