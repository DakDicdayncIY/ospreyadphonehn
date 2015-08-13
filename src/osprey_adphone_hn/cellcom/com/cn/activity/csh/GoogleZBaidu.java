package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import cellcom.com.cn.util.Base64;

public class GoogleZBaidu {
	FinalHttp http = new FinalHttp();

	// 得到经纬度
	public void getJWD(Double longitude, Double latitude, AjaxCallBack back) {
		http.get("http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x="
				+ longitude + "&y=" + latitude, back);
	}
}
