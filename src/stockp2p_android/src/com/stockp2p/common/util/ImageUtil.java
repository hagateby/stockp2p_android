package com.stockp2p.common.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class ImageUtil {
	private final static String IMAGE_PATH = Environment.getExternalStorageDirectory() + "/nci/";
	private Bitmap mBitmap;
	public void saveImage(String urlPath,String imageName) throws Exception {
			byte[] data = getImage(urlPath);
			if (data != null) {
				mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			} else {
			}
			if (mBitmap != null) {
				try {
					save(mBitmap, imageName);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
	public byte[] readStream(InputStream inStream) throws Exception {
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
	public void save(Bitmap bm, String fileName) throws IOException {  
        File dirFile = new File(IMAGE_PATH);  
        if(!dirFile.exists()){  
            dirFile.mkdir();  
        }  
        File myCaptureFile = new File(IMAGE_PATH+ fileName);  
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);  
        bos.flush();  
        bos.close();  
    }
}
