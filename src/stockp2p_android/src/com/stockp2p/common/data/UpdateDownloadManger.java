package com.stockp2p.common.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.stockp2p.common.db.Version;
import com.stockp2p.common.view.CommonDialog;

public class UpdateDownloadManger {

	private Context context;
	private ProgressDialog pDialog;
	private long totalLength;
	private File file;

	Handler updateHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				pDialog.setMax((int) totalLength); // 设置进度条的长度
				break;
			case 1:
				long progress = msg.getData().getLong("totalFinish");
				pDialog.setProgress((int) progress);
				break;
			case 2:
				showUpdateErrorDialog();
				break;
			}
		};
	};
	private CommonDialog dialog;

	public UpdateDownloadManger(Context context) {
		this.context = context;
	}

	public void update(final Version version) {
			Constants.isVersonUpdated = true;
			dialog = new CommonDialog(context, 2, "更新", "以后再说",
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							pDialog = new ProgressDialog(context);
							pDialog.setTitle("正在下载");
							pDialog.setMessage("请稍候……");
							pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
							pDialog.setCanceledOnTouchOutside(false);
							downFile(version.getUploadUrl());
						}
					}, null, "软件更新", "发现新版本，请更新！" + version.getVersionCode()
							+ "\n" + version.getVersionDesc());
			dialog.show();

	}

	private void downFile(final String url) {
		pDialog.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					totalLength = entity.getContentLength();

					Message msg = new Message();
					msg.what = 0;
					msg.getData().putLong("totalLength", totalLength);
					updateHandler.sendMessage(msg);

					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						file = new File(
								Environment.getExternalStorageDirectory(),
								"NCI_PHONE_FINAL.apk");
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							Message msg1 = new Message();
							msg1.what = 1;
							msg1.getData().putLong("totalFinish", count);
							updateHandler.sendMessage(msg1);
							if (totalLength > 0) {
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					complete();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					handleAccessInternetException();
				} catch (IllegalStateException e) {
					e.printStackTrace();
					handleAccessInternetException();
				}
			}
		}.start();
	}

	private void handleAccessInternetException() {
		Message msg = new Message();
		msg.what = 2;
		updateHandler.sendMessage(msg);
	}

	private void complete() {
		updateHandler.post(new Runnable() {
			public void run() {
				pDialog.cancel();
				installApk(file);
			}
		});
	}

	private void installApk(File file) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	private void showUpdateErrorDialog() {
		pDialog.dismiss();
		new CommonDialog(context, 1, "确定", null, null, null, "提示",
				"获取更新文件失败").show();
	}

}
