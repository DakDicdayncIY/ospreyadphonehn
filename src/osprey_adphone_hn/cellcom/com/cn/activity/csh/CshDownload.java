package osprey_adphone_hn.cellcom.com.cn.activity.csh;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RecoverySystem.ProgressListener;
import android.util.Log;

/**
 * post下载文件
 * 
 * @author 周子健
 *
 */
public class CshDownload {
	int contentLength = 0;

	public String doPost(String mpath, Map<String, String> map,
			String filePath, ProgresLitener listerner) {
		String params = "cc=" + map.get("cc") + "&versionDetailId="
				+ map.get("versionDetailId") + "&productSerialNo="
				+ map.get("productSerialNo"); // downloadDiagSoftWs

		URL url = null;
		String fileName = null;
		try {
			url = new URL(mpath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");// 提交模式 conn.setDoOutput(true);
											// 是否输入参数
			conn.setUseCaches(false);// Post 请求不能使用缓存
			conn.setConnectTimeout(10000);// 连接超时 单位毫秒
			conn.setReadTimeout(600000);// 读取超时 单位毫秒
			conn.addRequestProperty("cc", map.get("cc"));// 901295590000052
			conn.addRequestProperty("sign", map.get("sign"));
			byte[] bypes = params.toString().getBytes("UTF-8");
			conn.getOutputStream().write(bypes);
			String fileNames = conn.getHeaderField("Content-Disposition");
			
			fileName = fileNames.substring(fileNames.indexOf("=")+2,fileNames.indexOf("P")+1); 
			if (conn.getResponseCode() == 200) {
				InputStream inputStream = conn.getInputStream();
				listerner.getMax(conn.getContentLength());
				File mlfile = new File(filePath);
				if(!mlfile.exists()){
					mlfile.mkdirs();
				}
				File file = new File(filePath +"/"+ fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[2048];
				int len = -1;
				while ((len = inputStream.read(buffer)) != -1) {

					contentLength += len;
					listerner.getDownLoad(contentLength);

					fos.write(buffer, 0, len);
				}
				contentLength = 0;
				fos.flush();
				fos.close();
				inputStream.close();
			}
		} catch (Exception e) {
			Date end = new Date();
			System.out.println(e);
		}
		return fileName ;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();// 网页的二进制数据
		outStream.close();
		inStream.close();
		return data;
	}
}
