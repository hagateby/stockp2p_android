package com.ktsf.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.stockp2p.R;

public class SlipButton extends View implements OnTouchListener {
	private String strName;
	private boolean enabled = true;
	public boolean flag = false;
	public boolean NowChoose = false;
	private boolean OnSlip = false;
	public float DownX = 0f, NowX = 0f;
	public float NowY = 0f;

	private Rect Btn_On, Btn_Off;

	private boolean isChgLsnOn = false;
	private SlipButtonOnChangedListener ChgLsn;
	private int bgOn = R.drawable.common_slipbutton_slip_on_btn;
	private int bgOff = R.drawable.common_slip_off_btn;
	private int slipBtn = R.drawable.common_slipbutton_slip_white_btn;

	private Bitmap bgOnImg;

	private Bitmap bgOffImg;
	private Bitmap slip_btn;

	public SlipButton(Context context) {
		super(context);
		init(this.bgOn, this.bgOff, this.slipBtn);
	}

	public SlipButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(this.bgOn, this.bgOff, this.slipBtn);
	}

	public void setBgOn(Integer bgOn) {
		bgOnImg = BitmapFactory.decodeResource(getResources(), bgOn);
	}

	public void setBgOff(Integer bgOff) {
		bgOffImg = BitmapFactory.decodeResource(getResources(), bgOff);
	}

	public void setSlipBtn(Integer slipBtn) {
		slip_btn = BitmapFactory.decodeResource(getResources(), slipBtn);
	}

	public void setChecked(boolean fl) {
		if (fl) {
			flag = true;
			NowChoose = true;
			NowX = 100;
		} else {
			flag = false;
			NowChoose = false;
			NowX = 0;
		}
	}

	public void setEnabled(boolean b) {
		if (b) {
			enabled = true;
		} else {
			enabled = false;
		}
	}

	private void init(int bgOn, int bgOff, int slipBtn) {
		bgOnImg = BitmapFactory.decodeResource(getResources(), bgOn);
		bgOffImg = BitmapFactory.decodeResource(getResources(), bgOff);
		slip_btn = BitmapFactory.decodeResource(getResources(), slipBtn);
		Btn_Off = new Rect(bgOffImg.getWidth() - slip_btn.getWidth(), 0,
				bgOffImg.getWidth(), slip_btn.getHeight());
		Btn_On = new Rect(0, 0, slip_btn.getWidth(), slip_btn.getHeight());
		setOnTouchListener(this);
	}

	public void setImage(int bgOn, int bgOff, int slipBtn) {
		init(bgOn, bgOff, slipBtn);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float x;
		{
			if (flag) {
				NowX = 100;
				flag = false;
			}
			if (NowX < (bgOnImg.getWidth() / 2))
				canvas.drawBitmap(bgOffImg, matrix, paint);
			else
				canvas.drawBitmap(bgOnImg, matrix, paint);

			if (OnSlip) {
				if (NowX >= bgOnImg.getWidth())
					x = bgOnImg.getWidth() - slip_btn.getWidth() / 2;
				else
					x = NowX - slip_btn.getWidth() / 2;
			} else {
				if (NowChoose)
					x = Btn_Off.left;
				else
					x = Btn_On.left;
			}
			if (x < 0)
				x = 0;
			else if (x > bgOnImg.getWidth() - slip_btn.getWidth())
				x = bgOnImg.getWidth() - slip_btn.getWidth();
			canvas.drawBitmap(slip_btn, x, 0, paint);
		}
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (!enabled) {
			return false;
		}

		switch (event.getAction()) {
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MASK:
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_MOVE:
			NowX = event.getX();
		default:
			Log.i("test",
					"ACTION_CANCEL[x,y]" + event.getX() + "," + event.getY());
			break;
		}
		NowY = event.getY();
		boolean LastChoose = NowChoose;
		if (NowX >= (bgOnImg.getWidth() / 2))
			NowChoose = true;
		else
			NowChoose = false;
		if (isChgLsnOn && (LastChoose != NowChoose)) {
			ChgLsn.OnChanged(strName, NowChoose);
		}
		
		invalidate();
		return true;
	}

	public void SetOnChangedListener(String name, SlipButtonOnChangedListener l) {
		strName = name;
		isChgLsnOn = true;
		ChgLsn = l;
	}
}
