package com.stockp2p.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.stockp2p.R;

/**
 * 公共对话框
 * 
 * @author yilong.zhang
 * 
 */
public class CommonDialog extends Dialog {
	private Context context;
	private String title, content;// 输入交易密码dialog的标题等
	private String sureText, cancelText;// 确定按钮的文字,取消按钮的文字
	private View.OnClickListener surelistener, cancellistener;// 确定和取消的点击监听
	private int btnNums;// 按钮的数量

	/**
	 * 
	 * @param context
	 *            上下文
	 * @param btnNums
	 *            按钮的数量,1只有确定按钮,2有确定和取消按钮
	 * @param sureText
	 *            按钮1的文字
	 * @param cancelText
	 *            按钮2的文字
	 * @param surelistener
	 *            确定按钮的监听,传null时默认dismiss
	 * @param cancellistener
	 *            取消按钮的监听,传null时默认dismiss
	 * @param title
	 *            标题,传null时默认"提示"
	 * @param content
	 *            内容,传null时默认"错误"
	 */
	public CommonDialog(Context context, int btnNums, String sureText,
			String cancelText, View.OnClickListener surelistener,
			View.OnClickListener cancellistener, String title, String content) {
		super(context, R.style.MyCustomDialog);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.btnNums = btnNums;
		this.sureText = sureText;
		this.cancelText = cancelText;
		this.surelistener = surelistener;
		this.cancellistener = cancellistener;
		this.title = title;
		this.content = content;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.common_dialog);
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		params.width = (int) (display.getWidth() * 0.85);
		// params.height = display.getHeight();
		this.getWindow().setAttributes(params);
		TextView dialog_title = (TextView) this
				.findViewById(R.id.common_dialog_title);// 对话框标题
		TextView dialog_content = (TextView) this
				.findViewById(R.id.common_dialog_content);// 对话框内容
		Button btn_sure = (Button) this
				.findViewById(R.id.common_dialog_btn_sure);// 确定按钮
		Button btn_cancel = (Button) this
				.findViewById(R.id.common_dialog_btn_cancel);// 取消按钮
		if (btnNums == 1) {// 只有一个按钮时,隐藏掉取消按钮
			btn_sure.setText(sureText);
			btn_cancel.setVisibility(View.GONE);
		} else if (btnNums == 2) {// 两个按钮时都显示
			btn_sure.setText(sureText);
			btn_cancel.setText(cancelText);
			btn_cancel.setVisibility(View.VISIBLE);
		}
		// 有确定按钮的监听
		if (surelistener != null) {
			btn_sure.setOnClickListener(surelistener);
		} else {// 没有确定按钮的监听
			btn_sure.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
		}
		// 有取消按钮的监听
		if (btnNums == 2 && cancellistener != null) {
			btn_cancel.setOnClickListener(cancellistener);
		} else if (btnNums == 2 && cancellistener == null) {// 没有取消按钮的监听
			btn_cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
		}
		// title不为空
		if (title != null) {
			dialog_title.setText(title);
		} else {
			dialog_title.setText("提示");
		}
		// content不为空
		if (content != null && !content.equals("")) {
			dialog_content.setText(content);
		} else {
			dialog_content.setText("出错!");
		}
		setCancelable(false);
	}
}
