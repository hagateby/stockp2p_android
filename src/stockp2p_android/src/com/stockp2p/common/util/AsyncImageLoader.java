package com.stockp2p.common.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.stockp2p.common.data.Constants;
import com.stockp2p.common.data.MyApplication;
import com.stockp2p.common.db.Image;

public class AsyncImageLoader {

	// private Bitmap mBitmap;
	private int THREAD_NUM = 5;
	private ExecutorService executorService = Executors
			.newFixedThreadPool(THREAD_NUM);

	private final static String IMAGE_PATH = Environment
			.getExternalStorageDirectory() + "/nci/";
	private File nci;

	private MyApplication myApplication;
	private SQLiteDatabase db;

	public interface ImageFreshCallback {
		public void refreshImage(Bitmap bitmap);
	}

	public AsyncImageLoader(Context context) {
		Log.i("AsyncImageLoader", "AsyncImageLoader开始执行了……");

		// copyAssetsImageToCard(context, "adsense2.jpg");
		// copyAssetsImageToCard(context, "main_ad_bg.png");

		myApplication = (MyApplication) context.getApplicationContext();
		db = myApplication.db;

		ArrayList<Image> images = Image.findAllImage(db);
		nci = new File(IMAGE_PATH);
		if (!nci.exists()) {
			nci.mkdirs();
		}
		String imageName = null;
		String imageURL = null;
		for (int i = 0; (null != images) && (i < images.size()); i++) {
			imageName = images.get(i).getImageName();
			// imageURL = Constants.IMGURLHEADER+imageName;
			File file = new File(IMAGE_PATH + imageName);
			if (!file.exists()) {
				imageURL = images.get(i).getRemark2();
				Log.i("打印的图片路径", "" + imageURL);
				loadBitmap(imageURL, imageName);
			}
		}

	}

	private void copyAssetsImageToCard(Context context, String fileName) {
		// 定义存放这些图片的路径
		String path = IMAGE_PATH;
		// 如果这个路径不存在则新建
		File file = new File(path);
		Bitmap image = null;
		boolean isExist = file.exists();
		if (!isExist) {
			file.mkdirs();
		}
		// 获取assets下的资源
		AssetManager am = context.getAssets();
		try {
			// 图片放在img文件夹下
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			FileOutputStream out = new FileOutputStream(IMAGE_PATH + fileName);
			// 这个方法非常赞
			image.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Bitmap loadBitmap(final String urlPath, final String imageName) {
		executorService.submit(new Runnable() {
			private Bitmap mBitmap;

			@Override
			public void run() {
				try {
					byte[] data = getImage(urlPath);
					if (data != null) {
						mBitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);
					} else {
					}
					if (mBitmap != null) {
						try {
							Log.i("测试下载的图片数量：", "&&&&&&&&&&&&&&&&&" + "一张");
							saveFile(mBitmap, imageName);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return null;
	}

	public byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return readStream(inStream);
		}
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	public void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(IMAGE_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(IMAGE_PATH + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

}
