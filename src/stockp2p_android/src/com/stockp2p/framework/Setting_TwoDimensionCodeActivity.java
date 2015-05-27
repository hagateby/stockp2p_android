package com.stockp2p.framework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.stockp2p.R;
import com.stockp2p.common.util.CommonUtil;
import com.stockp2p.framework.baseframe.BaseFragment;

public class Setting_TwoDimensionCodeActivity extends BaseFragment {

	String text = "掌上新华是新华保险为android平台量身打造的保险服务展示平台，提供全面且完整的客户服务解决方案。让您更方便快捷的了解新华保险客户服务的内容";
	String version;
	String time;
	private Bitmap bmp;
	private TextView twoDimensionCode, Tv;
	private View thisView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.set_two_dimension_code,
				null);
		init(thisView, "掌上新华");
		return thisView;
	}

	@Override
	public void init(View view, String title) {
		// TODO Auto-generated method stub
		super.init(view, title);
		try {
			version = CommonUtil.getVersionName(context);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getActivity(), "获取当前版本信息失败", Toast.LENGTH_SHORT).show();
		}
		Tv = (TextView) thisView.findViewById(R.id.tv);
		Tv.setText("掌上新华" + version);
		twoDimensionCode = (TextView) thisView.findViewById(R.id.tv_two);

		// rightDevideLine.setVisibility(View.VISIBLE);
		// buttonRight_two.setVisibility(View.VISIBLE);
		twoDimensionCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.set_qrcode);
				try {
					saveBitmapToFile(bmp,
							Environment.getExternalStorageDirectory()
									+ "/DCIM/camera");

				} catch (Exception e) {
					Log.e("ssssssssssssssssssssssssssssssssssssss",
							e.getMessage(), e);
				}
			}
		});

	}

	public void saveBitmapToFile(Bitmap bitmap, String _file)
			throws IOException {
		FileOutputStream fill = null;
		try {
			File file = new File(_file);
			if (!file.exists()) {
				file.mkdirs();
			}
			String fileName = Environment.getExternalStorageDirectory()
					+ "/DCIM/camera/1111.png";
			fill = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fill);
			Toast.makeText(context, "二维码已保存在DCIM/camera文件中", Toast.LENGTH_LONG)
					.show();
		} finally {
			if (fill != null) {
				try {
					fill.close();
				} catch (IOException e) {
					Log.e("8080080808080808080808", e.getMessage(), e);
				}
			}
		}
	}

}
