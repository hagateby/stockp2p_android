package com.stockp2p.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.widget.EditText;

import com.stockp2p.common.view.CommonDialog;

public class CommonUtil {

	/**
	 * fastjson有此方法 map 转 jsonString
	 * 
	 * @param map
	 * @return
	 */
	// public static String toJson(Map<String, String> map) {
	// Set<String> keys = map.keySet();
	// String key = "";
	// String value = "";
	// StringBuffer jsonBuffer = new StringBuffer();
	// jsonBuffer.append("{");
	// for (Iterator<String> it = keys.iterator(); it.hasNext();) {
	// key = (String) it.next();
	// value = map.get(key);
	// jsonBuffer.append("\""+key+"\"" + ":" + "\""+value+"\"");
	// if (it.hasNext()) {
	// jsonBuffer.append(",");
	// }
	// }
	// jsonBuffer.append("}");
	// return jsonBuffer.toString();
	// }

	/**
	 * 身份证返回信息
	 * 
	 * @param id
	 *            身份证号
	 * @param i
	 *            = 0 返回性别 String值 1返回生日String值 =3返回接口的男0女1
	 * @return
	 */
	public static String idRules(String id, int i) {
		String birthday = null;
		int male = -1;
		String maleStr = null;
		if (id.length() == 15) {
			birthday = id.substring(6, 8) + "-" + id.substring(8, 10) + "-"
					+ id.substring(10, 12);
			if (!id.substring(14, 15).equals("X")) {
				male = Integer.parseInt(id.substring(14, 15));
			}
		} else if (id.length() == 18) {
			birthday = id.substring(6, 10) + "-" + id.substring(10, 12) + "-"
					+ id.substring(12, 14);
			if (!id.substring(16, 17).equals("X")) {
				male = Integer.parseInt(id.substring(16, 17));
			}
		}
		int sex = male % 2;
		if (sex == 1) {
			maleStr = "男";
		} else {
			maleStr = "女";
		}
		if (i == 3) {
			sex = (sex == 0 ? 1 : 0);// 身份证取出的值和接口的值正好相反
			return String.valueOf(sex);
		} else {
			return i == 0 ? maleStr : birthday;
		}
	}

	/**
	 * 创建bitmap
	 * 
	 * @param context
	 * @return
	 */
	public static Bitmap getBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
	/**
	 * 创建bitmap
	 * 
	 * @param context
	 * @return
	 */
	public static Bitmap getBitMap2(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inSampleSize = 2;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 创建bitmap
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap getBitmap(String path) {
		Bitmap bm = null;
		try {
			InputStream is = new FileInputStream(path);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inTempStorage = new byte[100 * 1024];
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inPurgeable = true;
			opts.inSampleSize = 2;
			opts.inInputShareable = true;
			bm = BitmapFactory.decodeStream(is, null, opts);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bm;
	}

	private static long lastClickTime;

	/**
	 * 正则校验输入的数字合法性
	 * 
	 * @param number
	 *            要检验的数字
	 * @param zhengshu
	 *            整数位数
	 * @param xiaoshu
	 *            小数位数
	 * @return 返回是否满足条件。满足true
	 */
	public static boolean checkNumber(String number, int zhengshu, int xiaoshu) {

		boolean re = true;
		if (number != null && !"".equals(number)) {
			String str = "\\d{1," + zhengshu + "}";
			String str2 = "^\\d{1," + zhengshu + "}\\.\\d{1," + xiaoshu + "}$";
			Pattern p = Pattern.compile(str);
			Pattern p2 = Pattern.compile(str2);
			Matcher m = p.matcher(number);
			Matcher m2 = p2.matcher(number);
			return m.matches() | m2.matches();
		}
		return re;
	}

	public static boolean doCheck(EditText ed, int maxVal) {
		String text = ed.getText().toString().trim();
		if (text == null || text.length() == 0) {
			// 允许为空
			return true;
		}
		if (text.length() > 0) {
			// float val = Float.parseFloat(text);
			BigDecimal bd = new BigDecimal(text);
			// bd = bd.setScale(SCALE,ACCURACY);
			// val = bd.floatValue();
			BigDecimal maxDec = new BigDecimal(maxVal);
			if (bd.compareTo(maxDec) == -1) {
				return true;
			} else {
				// ed.setText(String.valueOf(val));
				return false;
			}
		}
		return false;
	}

	/**
	 * 
	 * 判断时间是否小于输入参数，如果时间小于输入参数则返回trun。 以此方法来防止按钮短时间内被再次点击。
	 * 
	 * @param ms
	 *            毫秒
	 * @return
	 */
	public static boolean isFastDoubleClick(int ms) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (timeD > 0 && timeD < ms) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	/**
	 * 用户名格式校验
	 * 
	 * @param userName
	 * @return 正确返回 true
	 */
	public static boolean checkUserName(String userName) {
		if (userName == null || !userName.matches("[a-zA-Z0-9]{5,20}")) {
			return false;
		}
		return true;
	}

	/**
	 * 邮编验证
	 * 
	 * @param zipcode
	 *            邮编
	 * @return 正确返回 true
	 */
	public static boolean isZipCode(String zipcode) {
		if (zipcode == null || !zipcode.matches("\\b[0-9]{6}(?:-[0-9]{4})?\\b")) {
			return false;
		}
		return true;
	}

	/**
	 * email验证
	 * 
	 * @param email
	 * @return 正确返回 true
	 */
	public static boolean isEmail(String email) {
		if (email == null
				|| !email
						.matches("\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$")) {
			return false;
		}
		return true;
	}

	/**
	 * qq验证
	 * 
	 * @param qq
	 * @return 正确返回 true
	 */
	public static boolean isQQ(String QQ) {
		if (QQ != null && !QQ.matches("^[0-9]{5,12}")) {
			return false;
		}
		return true;
	}

	// // check the email format. wrong return false
	// public static boolean isEmail(String email) {
	// String str = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	// Pattern p = Pattern.compile(str);
	// Matcher m = p.matcher(email);
	// return m.matches();
	// }
	/**
	 * 密码校验。（不能全为数字或者全为密码）
	 * 
	 * @param password
	 * @return 正确返回 true
	 */
	public static boolean checkPassword(String password) {
		if (password == null
				|| !password
						.matches("[\\w|[\\\\\"!#\\$%\\&'\\(\\)\\*\\+,-./:;<=>\\?@\\[\\]\\^`\\{\\}~\\|]]{6,16}")) {
			return false;
		}
		return true;
	}

	/**
	 * 电话号码简单验证
	 * 
	 * @param mobiles
	 * @return 正确返回 true
	 */
	public static boolean isMobileNO(String mobiles) {
		if (mobiles == null || !mobiles.matches("1[3-9]\\d{9}$")) {
			return false;
		}
		return true;
	}

	/**
	 * 固定电话、住宅电话、传真，简单验证
	 * 
	 * @param mobiles
	 * @return 正确返回 true
	 */
	public static boolean isFax(String mobiles) {
		if (mobiles == null
				|| !mobiles
						.matches("(0[0-9]{2,5}\\-[0-9]{7,11}\\-[0-9]{1,5}$)|(^[0-9]{7,11}\\-[0-9]{1,5}$)|(^0[0-9]{2,5}\\-[0-9]{7,11}$)|(^[0-9]{7,20}$)")) {
			return false;
		}
		return true;
	}

	/**
	 * 银行账号校验
	 * 
	 * @param accNum
	 *            银行帐号
	 * @return 正确返回 true
	 */
	public static boolean isAccount(String accNum) {
		if (accNum == null || !accNum.matches("[0-9][0-9\\-]{11,50}$")) {
			return false;
		}
		return true;
	}

	/**
	 * 身份证验证
	 * 
	 * @param idCardNo
	 * @return 正确返回 true
	 */
	public static boolean isIDCardNo(String idCardNo) {
		Pattern p = Pattern
				.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
		Pattern p1 = Pattern
				.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$");
		Matcher m = p.matcher(idCardNo);
		Matcher m1 = p1.matcher(idCardNo);
		return m.matches() || m1.matches();
	}

	public static long age(String birthday) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = new Date();
		try {
			// 出生年月
			beginDate = format.parse(birthday);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date endDate = null;
		try {
			// 现在时间
			Date date = new Date(System.currentTimeMillis());
			String string = format.format(date);
			endDate = format.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long day = (endDate.getTime() - beginDate.getTime()) / 1000;
		long age = day / (60 * 60 * 24 * 365);
		TipUitls.Log("CommonUtil", "你的年龄是：" + age);
		return age;
	}

	private static DateFormat input = new SimpleDateFormat(
			"yyyy-MM-dd'T'hh:mm:ss.SSS'+08:00'");
	private static DateFormat output = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat rqUIdPut = new SimpleDateFormat("yyyyMMddhhmmss");
	private static DateFormat rqDtPut = new SimpleDateFormat(
			"yyyy-MM-dd'T'hh:mm:ss'Z'"); // 2012-12-25T16:55:17Z
	private static DateFormat shortCutFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm");

	public static Date getDateFromFormatedString(String str) {
		try {
			if (str != null && str.length() > 0) {
				return input.parse(str);
			} else {
				return null;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new Date();
		}
	}

	public static String getDateStyle1FromFormatedString(Date date) {
		if (date == null) {
			return "";
		} else {
			return rqDtPut.format(date);
		}
	}

	public static String getRqUIdString(Date date) {
		return rqUIdPut.format(date);
	}

	public static Date getDateFromStandardString(String str, boolean nullable) {
		try {
			return output.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (nullable) {
				return null;
			} else {
				return new Date();
			}
		}
	}

	public static String getDateString(Date date) {
		if (date == null) {
			return "";
		} else {
			return output.format(date);
		}
	}

	// yyyy-MM-DD hh:mm
	public static String getShortCutFormat(Date date) {
		if (date == null) {
			return "";
		} else {
			return shortCutFormat.format(date);
		}
	}

	// convert the dip to the pix
	public static int dip2px(Context context, float dpValue) {

		final float scale = context.getResources().getDisplayMetrics().density;

		return (int) (dpValue * scale + 0.5f);

	}

	// convert the pix to the dip
	public static int px2dip(Context context, float pxValue) {

		final float scale = context.getResources().getDisplayMetrics().density;

		return (int) (pxValue / scale + 0.5f);

	}

	public static boolean isSameDay(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
						.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Convert the given code and the dropdown values return the code mapped
	 * value. e.g: given 1 and gender, return 男.
	 * 
	 * @param hostCode
	 * @param id
	 * @return
	 */
	public static String getValueForHostCode(Context context, String hostCode,
			int id) {

		// default the value to be the same as the hostCode.
		String valueMapped = hostCode;

		if (hostCode != null && context != null) {
			// id:R.array.education
			String[] mItems = context.getResources().getStringArray(id);
			for (String value : mItems) {
				if (value.contains(hostCode)) {
					valueMapped = value;
				}
			}
		}
		return valueMapped;
	}

//	public static String md5(String s) {
//		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//				'a', 'b', 'c', 'd', 'e', 'f' };
//		s = "http://vanceinfo.com" + s;
//		try {
//			byte[] btInput = s.getBytes();
//			// 获得MD5摘要算法的 MessageDigest 对象
//			MessageDigest mdInst = MessageDigest.getInstance("MD5");
//			// 使用指定的字节更新摘要
//			mdInst.update(btInput);
//			// 获得密文
//			byte[] md = mdInst.digest();
//			// 把密文转换成十六进制的字符串形式
//			int j = md.length;
//			char str[] = new char[j * 2];
//			int k = 0;
//			for (int i = 0; i < j; i++) {
//				byte byte0 = md[i];
//				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//				str[k++] = hexDigits[byte0 & 0xf];
//			}
//			return new String(str);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

	/**
	 * 数字格式化
	 * 
	 * @param scale
	 * @return
	 */
	public static String numberFormat(int scale) {
		String m_strFormat = "#,##0";

		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		if (scale > 0) {
			m_strFormat += ".";
			for (int i = 0; i < scale; i++) {
				m_strFormat += "0";
			}
		}
		return m_strFormat;
	}

	/**
	 * 保存日志到sdcard
	 * 
	 * @param activityName
	 * @param message
	 */
	public static void SaveLog(String activityName, String message) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// sdcard存在并且有权限操作
			String basePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/SFA";
			System.out.println("filePath=== " + basePath);
			File directory = new File(basePath);
			if (!directory.exists())
				directory.mkdirs();
			File file = new File(basePath + "/sfa_log.txt");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			FileOutputStream outStream;
			try {
				outStream = new FileOutputStream(file);
				SimpleDateFormat sf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				sf.format(new Date());
				outStream.write((sf.format(new Date()) + " " + activityName
						+ ":" + message + "\n").getBytes());
				outStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存日志到sdcard
	 * 
	 * @param activityName
	 * @param str
	 *            错误描述
	 * @param ex
	 */
	public static void SaveLog(String activityName, String str, Exception ex) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {// sdcard存在并且有权限操作
			String basePath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/SFA";
			System.out.println("filePath=== " + basePath);
			File directory = new File(basePath);
			if (!directory.exists())
				directory.mkdirs();
			File file = new File(basePath + "/sfa_log.txt");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				SimpleDateFormat sf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				sf.format(new Date());
				StackTraceElement[] stack = ex.getStackTrace();
				String message = ex.getMessage() + "\n";
				FileOutputStream fos = new FileOutputStream(file, true);
				if (str != null && !"".equals(str))
					fos.write((sf.format(new Date()) + " " + activityName + ":"
							+ str + "\n").getBytes());
				fos.write((sf.format(new Date()) + " " + activityName + ":"
						+ message + "\n").getBytes());
				for (int i = 0; i < stack.length; i++) {
					fos.write((stack[i].toString() + "\n").getBytes());
				}
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// String转成java.sql.Date
	public static java.sql.Date StringToDate(String string) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date timeDate = null;
		try {
			timeDate = sdf.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (timeDate != null)
			return new java.sql.Date(timeDate.getTime());// sql类型
		else
			return null;
	}

	// String转成java.sql.Date
	public static java.sql.Timestamp StringToDate(String string, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		java.util.Date timeDate = null;
		try {
			timeDate = sdf.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (timeDate != null)
			return new java.sql.Timestamp(timeDate.getTime());// sql类型
		else
			return null;
	}

	public void alert(Activity activity, String string) {
		new AlertDialog.Builder(activity).setTitle("提示").setMessage(string)
				.setPositiveButton("确定", null).create().show();
	}

	/**
	 * 通过url获取网络数据
	 */
	public static String getJSONArray(String path) throws Exception {
		String json = null;
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 利用HttpURLConnection对象,我们可以从网络中获取网页数据.
		conn.setConnectTimeout(5 * 1000); // 单位是毫秒，设置超时时间为5秒
		conn.setRequestMethod("GET"); // HttpURLConnection是通过HTTP协议请求path路径的，所以需要设置请求方式,可以不设置，因为默认为GET
		if (conn.getResponseCode() == 200) {// 判断请求码是否是200码，否则失败
			InputStream is = conn.getInputStream(); // 获取输入流
			byte[] data = readStream(is); // 把输入流转换成字符数组
			json = new String(data); // 把字符数组转换成字符串

		}
		return json;
	}

	public static byte[] readStream(InputStream inputStream) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			bout.write(buffer, 0, len);
		}
		bout.close();
		inputStream.close();
		return bout.toByteArray();
	}

	// 过滤特殊字符
	public static boolean blockSpecialChar(String str)
			throws PatternSyntaxException {
		String regEx = "[^`~!@#$%%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]{1,}";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static long timeDistance(String start, String end) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		long days = 0;
		try {
			Date d1 = df.parse(start);
			Date d2 = df.parse(end);
			long diff = d2.getTime() - d1.getTime();
			days = diff / (1000 * 60 * 60 * 24);
		} catch (Exception e) {
		}
		return days;
	}

	/**
	 * 获取应用版本号
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getVersionName(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		return packInfo.versionName;
	}

	/**
	 * 获取系统版本号
	 * 
	 * @return
	 */
	public static String getClientVer() {
		String clientver = android.os.Build.VERSION.SDK;
		return clientver;

	}

	/**
	 * Role:Telecom service providers获取手机服务商信息 <BR>
	 * 需要加入权限<uses-permission
	 * android:name="android.permission.READ_PHONE_STATE"/> <BR>
	 * Date:2012-3-12 <BR>
	 * 
	 * @author CODYY)peijiangping
	 */
	public static String getProvidersName(Context context) {
		String HOST = null;
		// 返回唯一的用户ID;就是这张卡的编号神马的
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String IMSI = telephonyManager.getSubscriberId();
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (IMSI == null) {
			HOST = "123.127.246.38";
		} else if (IMSI.startsWith("46001")) {
			// HOST = "中国联通";
			HOST = "123.127.246.38";
		} else if (IMSI.startsWith("46003")) {
			// HOST = "中国电信";
			HOST = "219.143.202.143";
			// } else if (IMSI.startsWith("46000") || IMSI.startsWith("46002"))
			// {
		} else {
			// HOST = "中国移动";
			HOST = "123.127.246.38";
		}
		return HOST;
	}

	/**
	 * // 检查网络状况
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnectingToInternet(Context context) {// 检查网络状况
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
		}
		return false;
	}

	/**
	 * 认证限额
	 */
	public static boolean isLimitMoney(Context context, double inputMoney) {
		
		return true;
	}
}
