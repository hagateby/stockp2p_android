package com.stockp2p.common.data;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

public class Graphics {
	public static Bitmap addTxtToBitmap(Bitmap img, String txt) {
		int maxLenth = 300;
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
		p.setAlpha(0x80);
		p.setAlpha(0x30);
		p.setTextSize(30);
		p.setColor(Color.WHITE);
		p.setTextAlign(Paint.Align.CENTER);

		int txtLen = 20 * txt.length() + 20;
		int y = img.getHeight();
		int imgx = img.getWidth();

		if (txtLen < imgx) {
			txtLen = imgx;
		}

		int resultWidth = Math.min(txtLen, maxLenth);
		Bitmap result = Bitmap.createBitmap(resultWidth, y, Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		Paint p1 = new Paint();
		p1.setTextAlign(Paint.Align.CENTER);
		p1.setColor(Color.rgb(0x38, 0x38, 0x38));
		canvas.drawRect(0, 0, resultWidth, 39, p1);
		canvas.drawBitmap(img, (resultWidth - imgx) / 2, 0, p1);

		canvas.drawText(txt, resultWidth / 2, 37, p);
		Log.i("Graphic", "width:" + resultWidth + "  height:" + y);
		return result;
	}
}
