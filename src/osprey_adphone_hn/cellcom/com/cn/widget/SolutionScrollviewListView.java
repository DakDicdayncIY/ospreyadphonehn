package osprey_adphone_hn.cellcom.com.cn.widget;

import osprey_adphone_hn.cellcom.com.cn.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

public class SolutionScrollviewListView extends ListView {
	private Context context;

	public SolutionScrollviewListView(Context context) {
		super(context);
		this.context = context;
	}

	public SolutionScrollviewListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			this.requestDisallowInterceptTouchEvent(true);
		}
		if (ev.getAction() == MotionEvent.ACTION_CANCEL) {
			this.requestDisallowInterceptTouchEvent(false);
		}
		return super.onInterceptTouchEvent(ev);
	}

	private void setParentScrollAble(boolean b) {
		View inflate = LayoutInflater.from(context).inflate(
				R.layout.os_csh_add_data, null);
		ScrollView sv = (ScrollView) inflate.findViewById(R.id.scrollView1);
		sv.requestDisallowInterceptTouchEvent(b);
	}
}
