package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.R.id;
import osprey_adphone_hn.cellcom.com.cn.R.layout;
import osprey_adphone_hn.cellcom.com.cn.R.menu;
import osprey_adphone_hn.cellcom.com.cn.activity.base.ActivityFrame;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * 体检报告
 * 
 * @author 周子健
 *
 */
public class CshExaminationReportActivity extends ActivityFrame {
	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppendMainBody(R.layout.os_csh_examination_report);
		AppendTitleBody3();
		isShowSlidingMenu(false);
		SharedPreferences sp = this.getPreferences(this.MODE_PRIVATE);
		String Path = sp.getString("Path", "");
		if ("".equals(Path) || Path == null) {
			Toast.makeText(CshExaminationReportActivity.this, "请先体检！",
					Toast.LENGTH_SHORT).show();
			CshExaminationReportActivity.this.finish();
			return;
		}

		webView = (WebView) findViewById(R.id.wv_os_csh_examination_report);
		webView.loadUrl(Path);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.csh_examination_report, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
