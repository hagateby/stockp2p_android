package com.ktsf.common.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import cn.sharesdk.onekeyshare.OneKeyShareCallback;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeDemo;

import com.ktsf.R;

public class ShareUtils {

	/**
	 * @param silent
	 *            是否直接分享（true则直接分享）
	 * @param platform
	 *            指定分享平台 如果是任意字符表示分享到所有平台
	 * @param context
	 *            context
	 * @param handler
	 *            分享后的处理
	 * @param title
	 *            分享内容
	 * @param imageUrl
	 *            分享图片url
	 * @param shareUrl
	 *            分享网址
	 * @param formFlag
	 *            是否是摇摇蛋分享
	 */
	public static void showShare(boolean silent, String platform,
			final Context context, Handler handler, String title, String text,
			String imageUrl, String shareUrl, boolean formFlag) {
		final OnekeyShare oks = new OnekeyShare();
		// 分享时Notification的图标和文字
		//oks.setNotification(R.drawable.nci_logo,
		//		context.getString(R.string.app_name));
		// address是接收人地址，仅在信息和邮件使用
		// oks.setAddress("12345678901");
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(title);
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText(text);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		if (imageUrl == null) {
//			oks.setViewToShare(((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content));
//		} else {
//			oks.setImagePath(imageUrl);
//		}
		if (imageUrl != null) {
			oks.setImagePath(imageUrl);
		}
		// imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、 微信的两个平台、Linked-In支持此字段
		// oks.setImageUrl(imageUrl);
		// 分享url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(shareUrl);
		// appPath是待分享应用程序的本地路劲，仅在微信中使用
		// oks.setAppPath(MainActivity.TEST_IMAGE);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment(getContext().getString(R.string.share));
		// site是分享此内容的网站名称，仅在QQ空间使用
		// oks.setSite(context.getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		// oks.setSiteUrl("http://sharesdk.cn");
		// venueName是分享社区名称，仅在Foursquare使用
		// oks.setVenueName("Southeast in China");
		// venueDescription是分享社区描述，仅在Foursquare使用
		// oks.setVenueDescription("This is a beautiful place!");
		// StatusRecordBiz biz = new StatusRecordBiz(context);
		// latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
		// oks.setLatitude(Float.parseFloat(biz.getLat()));
		// longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
		// oks.setLongitude(Float.parseFloat(biz.getLng()));
		// 是否直接分享（true则直接分享）
		oks.setSilent(silent);
		// 指定分享平台，和slient一起使用可以直接分享到指定的平台
		if (platform != null) {
			// 默认指定一个字符串 表示分享到所有平台
			oks.setPlatform(platform);
		}
		// 设置需要隐藏的分享平台，摇摇蛋只分享微信平台
		if (formFlag) {
			oks.addHiddenPlatform("SinaWeibo");
			oks.addHiddenPlatform("TencentWeibo");
		}
		// 去除注释，可令编辑页面显示为Dialog模式
		// oks.setDialogMode();
		// 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
		if(handler != null) {
			oks.setCallback(new OneKeyShareCallback(handler));
		}
		// 通过OneKeyShareCallback来修改不同平台分享的内容
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
		// 去除注释，演示在九宫格设置自定义的图标
		// Bitmap logo = BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.more_share);
		// String label = "更多分享";
		// final String shareText = text + shareUrl;
		// OnClickListener listener = new OnClickListener() {
		// public void onClick(View v) {
		// Intent shareInt = new Intent(Intent.ACTION_SEND);
		// shareInt.setType("text/plain");
		// shareInt.putExtra(Intent.EXTRA_SUBJECT, "分享");
		// shareInt.putExtra(Intent.EXTRA_TEXT, shareText);
		// // shareInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Intent intent = Intent.createChooser(shareInt, "请选择以下方式分享给好友");
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startActivity(intent);
		// oks.finish();
		// }
		// };
		// oks.setCustomerLogo(logo, label, listener);
		oks.show(context);
	}
}
