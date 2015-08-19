package osprey_adphone_hn.cellcom.com.cn.activity.dhb;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.widget.GridView;
import osprey_adphone_hn.cellcom.com.cn.adapter.DhbSyzxBsAdapter;
import osprey_adphone_hn.cellcom.com.cn.bean.BargainGoods;
import osprey_adphone_hn.cellcom.com.cn.net.FlowConsts;
import cellcom.com.cn.util.Des3;

/**
 * 获得特价商品
 * 
 * @author 周子健
 *
 */
public class DhbGetTJSP {
	public void getData(String path, FinalHttp mHttp, final GridView gv,
			final Context context) {
		mHttp.get(path, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				try {
					String decode = Des3.decode(t, FlowConsts.DES3_KEY);
					JSONObject obj = new JSONObject(decode);
					List<BargainGoods> list = new ArrayList<BargainGoods>();

					if ("1".equals(obj.getString("returnCode"))) {
						JSONObject jsonObject = obj.getJSONObject("body");
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject2 = jsonArray.getJSONObject(i);
							String cid = jsonObject2.getString("cid");
							String ImagePath = jsonObject2
									.getString("smallpic");
							String jifen = jsonObject2.getString("jifen");
							String type = jsonObject2.getString("type");
							String title = jsonObject2.getString("title");
							String leftnum = jsonObject2.getString("leftnum");
							String simpleinfo = jsonObject2
									.getString("simpleinfo");

							BargainGoods bg = new BargainGoods();
							bg.setCid(cid);
							bg.setSmallpic(ImagePath);
							bg.setJifen(jifen);
							bg.setType(type);
							bg.setTitle(title);
							bg.setLeftnum(leftnum);
							bg.setSimpleinfo(simpleinfo);
							list.add(bg);
						}
						gv.setAdapter(new DhbSyzxBsAdapter(list, context));
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
