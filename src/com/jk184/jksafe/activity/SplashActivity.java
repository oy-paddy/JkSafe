package com.jk184.jksafe.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jk184.jksafe.R;
import com.jk184.jksafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends BaseActivity {

	// handler msg
	protected static final int CODE_UPDATE_DIALOG = 0;

	protected static final int CODE_URL_ERROR = 1;

	protected static final int CODE_NETERWORK_ERROR = 2;

	protected static final int CODE_JSON_ERROR = 3;

	protected static final int CODE_ENTER_HOME = 4;

	// view
	private TextView tv_version;
	private TextView tv_download_progress;

	// from server data
	private String mVersionName;
	private int mVersionCode;
	private String mDescription;
	private String mDownloadUrl;

	// handler
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_URL_ERROR:
				showLongToast(SplashActivity.this,
						getString(R.string.url_error));
				enterHome();
				break;
			case CODE_NETERWORK_ERROR:
				showLongToast(SplashActivity.this,
						getString(R.string.neterwork_error));
				enterHome();
				break;
			case CODE_JSON_ERROR:
				showLongToast(SplashActivity.this,
						getString(R.string.json_error));
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		findView();
		checkVersion();

	}

	private void findView() {
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText(getString(R.string.version) + getVersionName());
		tv_download_progress = (TextView) findViewById(R.id.tv_download_progress);
	}

	/**
	 * 获取当前版本名称
	 * 
	 * @return
	 */
	private String getVersionName() {
		String version = null;
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 获取当前版本号
	 * 
	 * @return
	 */
	private int getVersionCode() {
		int versionCode = 0;
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			versionCode = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 从服务器获取版本信息，检测更新
	 */
	private void checkVersion() {
		final long startTime = System.currentTimeMillis();

		// 启动子线程，异步加载数据
		new Thread() {

			Message msg = Message.obtain();
			private HttpURLConnection conn = null;

			public void run() {
				try {
					URL url = new URL("http://192.168.1.104:8080/update.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					conn.connect();
					int responseCode = conn.getResponseCode();
					if (responseCode == 200) {
						InputStream inputStream = conn.getInputStream();
						String result = StreamUtils.readFormStream(inputStream);
						// Log.d("jk", result);

						// 解析json
						JSONObject json = new JSONObject(result);
						mVersionName = json.getString("versionName");
						mVersionCode = json.getInt("versionCode");
						mDescription = json.getString("description");
						mDownloadUrl = json.getString("downloadUrl");
						// Log.d("jk", mDownloadUrl);

						if (mVersionCode > getVersionCode()) {
							// 有更新
							msg.what = CODE_UPDATE_DIALOG;
						} else {
							// 不用更新
							msg.what = CODE_ENTER_HOME;
						}
					}
				} catch (MalformedURLException e) {
					// url地址错误异常
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
					Log.d("jk", e.getLocalizedMessage());
				} catch (IOException e) {
					// 网络错误异常
					msg.what = CODE_NETERWORK_ERROR;
					e.printStackTrace();
					Log.d("jk", e.getLocalizedMessage());
				} catch (JSONException e) {
					// json解析失败
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
					Log.d("jk", e.getLocalizedMessage());
				} finally {
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;
					if (timeUsed < 2000) {
						try {
							// 强制休眠2秒
							sleep(2000 - timeUsed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					handler.sendMessage(msg);
					if (conn != null) {
						conn.disconnect();
					}
				}

			};
		}.start();
	}

	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.new_version) + mVersionName);
		builder.setMessage(mDescription);
		builder.setPositiveButton(getString(R.string.update),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Log.d("jk", "更新");
						downloadApk();
					}
				});

		builder.setNegativeButton(getString(R.string.ignore),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						enterHome();
					}
				});
		builder.show();
	}

	/**
	 * 进入主页面
	 */
	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		this.finish();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			tv_download_progress.setVisibility(View.VISIBLE);

			String target = Environment.getExternalStorageDirectory().getPath()
					+ "/update.apk";

			// xUtils
			HttpUtils http = new HttpUtils();
			HttpHandler handler = http.download(mDownloadUrl, target,
					new RequestCallBack<File>() {

						// 文件下载进度
						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							super.onLoading(total, current, isUploading);
							// Log.d("jk", "下载进度：" + current + "--下载总大小：" +
							// total);
							tv_download_progress
									.setText(getString(R.string.download_progress)
											+ current / total * 100 + "%");
						}

						@Override
						public void onSuccess(ResponseInfo<File> arg0) {
							showLongToast(SplashActivity.this,
									getString(R.string.download_success));
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							showLongToast(SplashActivity.this,
									getString(R.string.download_failure));
						}
					});
		} else {
			showLongToast(SplashActivity.this, getString(R.string.no_sdcard));
		}
	}
}
