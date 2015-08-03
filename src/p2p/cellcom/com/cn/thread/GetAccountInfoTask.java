package p2p.cellcom.com.cn.thread;

import org.json.JSONObject;

import p2p.cellcom.com.cn.bean.Account;
import p2p.cellcom.com.cn.global.AccountPersist;
import p2p.cellcom.com.cn.global.NpcCommon;
import p2p.cellcom.com.cn.utils.Utils;
import u.aly.ac;
import android.content.Context;
import android.os.AsyncTask;

import com.p2p.core.network.GetAccountInfoResult;
import com.p2p.core.network.NetManager;

public class GetAccountInfoTask extends AsyncTask {
	private Context mContext;
	private GetAccountInfoCallBack callBack;
	private final static int UPDATE_FAILURE = 3;
    private final static int UPDATE_SUCCESS = 4;

	public GetAccountInfoTask(Context context, GetAccountInfoCallBack callBack) {
		this.mContext = context;
		this.callBack = callBack;
	}

	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		Utils.sleepThread(1000);
		Account account = AccountPersist.getInstance().getActiveAccountInfo(
				mContext);
		if(account!=null){
		  publishProgress(UPDATE_SUCCESS,NetManager.getInstance(mContext).getAccountInfo(
            NpcCommon.mThreeNum, account.sessionId));
		}else{
		  publishProgress(UPDATE_FAILURE);
		}
		return null;
	}

	public static void startTask(Context context,
			GetAccountInfoCallBack callBack) {
		new GetAccountInfoTask(context, callBack).execute();
	}

	@Override
	protected void onProgressUpdate(Object... values) {
	// TODO Auto-generated method stub
	int update = Integer.valueOf(String.valueOf(values[0]));
	switch (update) {
      case UPDATE_FAILURE:
          break;
      case UPDATE_SUCCESS:
          if(callBack!=null){            
            GetAccountInfoResult result = NetManager
                .createGetAccountInfoResult((JSONObject) values[1]);
            callBack.onPostExecute(result);
          }
          break;
      default:
          break;
      }
      super.onProgressUpdate(values);
	}
	@Override
	protected void onPostExecute(Object object) {
		// TODO Auto-generated method stub
//		GetAccountInfoResult result = NetManager
//				.createGetAccountInfoResult((JSONObject) object);
//		callBack.onPostExecute(result);
	}

	public interface GetAccountInfoCallBack {
		public abstract void onPostExecute(GetAccountInfoResult result);
	}
}