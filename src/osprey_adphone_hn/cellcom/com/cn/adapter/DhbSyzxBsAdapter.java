package osprey_adphone_hn.cellcom.com.cn.adapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.Inflater;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import osprey_adphone_hn.cellcom.com.cn.R;
import osprey_adphone_hn.cellcom.com.cn.bean.BargainGoods;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 特价商品实体
 * 
 * @author 周子健
 *
 */
public class DhbSyzxBsAdapter extends BaseAdapter {
	List<BargainGoods> mList;
	Context mContext;
	FinalBitmap bit;

	public DhbSyzxBsAdapter(List<BargainGoods> list, Context context) {
		mList = list;
		mContext = context;
		bit = FinalBitmap.create(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = LayoutInflater.from(mContext).inflate(
					R.layout.os_dhb_syzx_bs_fragment_bean, null);
		}
		BargainGoods bargainGoods = mList.get(arg0);

		final ImageView iv = (ImageView) arg1
				.findViewById(R.id.iv_dhb_syzx_bs_fragment);
		TextView tvTitle = (TextView) arg1
				.findViewById(R.id.tv_dhb_syzx_bs_fragment);
		TextView tvJifen = (TextView) arg1
				.findViewById(R.id.tv_jifen_dhb_syzx_bs_fragment);
		tvTitle.setText(bargainGoods.getTitle());
		tvJifen.setText(bargainGoods.getJifen());

		bit.display(iv, bargainGoods.getSmallpic());

		return arg1;
	}

	class Yh {
		TextView tvTitle;
		TextView tvJifen;
		ImageView iv;
	}
}