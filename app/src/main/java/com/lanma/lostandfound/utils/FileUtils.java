package com.lanma.lostandfound.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该类的作用:文件工具类 包含获取sd卡状态,缓存等信息
 * <p/>
 * 作者 :任强强 创建时间:2016/1/24
 */
@SuppressWarnings("ALL")
public class FileUtils {

	/**
	 * 获取指定url的文件名
	 *
	 * @param url
	 *            文件地址
	 * @return 文件名
	 */
	public static String getRealFileNameFromUrl(String url) {
		String name = null;
		try {
			if (TextUtils.isEmpty(url)) {
				return null;
			}

			URL mUrl = new URL(url);
			HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
			mHttpURLConnection.setConnectTimeout(5 * 1000);
			mHttpURLConnection.setRequestMethod("GET");
			mHttpURLConnection.setRequestProperty("Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
			mHttpURLConnection.setRequestProperty("Referer", url);
			mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
			mHttpURLConnection.setRequestProperty("User-Agent", "");
			mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			mHttpURLConnection.connect();
			if (mHttpURLConnection.getResponseCode() == 200) {
				for (int i = 0;; i++) {
					String mine = mHttpURLConnection.getHeaderField(i);
					if (mine == null) {
						break;
					}
					if ("content-disposition".equals(mHttpURLConnection.getHeaderFieldKey(i).toLowerCase())) {
						Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
						if (m.find())
							return m.group(1).replace("\"", "");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 获取真实文件名（xx.后缀），通过网络获取.
	 *
	 * @param connection
	 *            连接
	 * @return 文件名
	 */
	public static String getRealFileName(HttpURLConnection connection) {
		String name = null;
		try {
			if (connection == null) {
				return null;
			}
			if (connection.getResponseCode() == 200) {
				for (int i = 0;; i++) {
					String mime = connection.getHeaderField(i);
					if (mime == null) {
						break;
					}
					// "Content-Disposition","attachment; filename=1.txt"
					// Content-Length
					if ("content-disposition".equals(connection.getHeaderFieldKey(i).toLowerCase())) {
						Matcher m = Pattern.compile(".*filename=(.*)").matcher(mime.toLowerCase());
						if (m.find()) {
							return m.group(1).replace("\"", "");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取真实文件名（xx.后缀），通过网络获取.
	 *
	 * @param response
	 *            the response
	 * @return 文件名
	 */
	// public static String getRealFileName(HttpResponse response){
	// String name = null;
	// try {
	// if(response == null){
	// return name;
	// }
	// //获取文件名
	// Header[] headers = response.getHeaders("content-disposition");
	// for(int i=0;i<headers.length;i++){
	// Matcher m =
	// Pattern.compile(".*filename=(.*)").matcher(headers[i].getValue());
	// if (m.find()){
	// name = m.group(1).replace("\"", "");
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// AbLogUtil.e(AbFileUtil.class, "网络上获取文件名失败");
	// }
	// return name;
	// }

	/**
	 * 获取文件名（不含后缀）.
	 *
	 * @param url
	 *            文件地址
	 * @return 文件名
	 */
	public static String getCacheFileNameFromUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		String name = null;
		try {
			name = MD5(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取文件名（.后缀），外链模式和通过网络获取.
	 *
	 * @param url
	 *            文件地址
	 * @param response
	 *            the response
	 * @return 文件名
	 */
	// public static String getCacheFileNameFromUrl(String url,HttpResponse
	// response){
	// if(TextUtils.isEmpty(url)){
	// return null;
	// }
	// String name = null;
	// try {
	// //获取后缀
	// String suffix = getMIMEFromUrl(url,response);
	// if(TextUtils.isEmpty(suffix)){
	// suffix = ".ab";
	// }
	// name = AbMd5.MD5(url)+suffix;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return name;
	// }

	/**
	 * 获取文件名（.后缀），外链模式和通过网络获取.
	 *
	 * @param url
	 *            文件地址
	 * @param connection
	 *            the connection
	 * @return 文件名
	 */
	public static String getCacheFileNameFromUrl(String url, HttpURLConnection connection) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		String name = null;
		try {
			// 获取后缀
			String suffix = getMIMEFromUrl(url, connection);
			if (TextUtils.isEmpty(suffix)) {
				suffix = ".ab";
			}
			name = MD5(url) + suffix;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取文件后缀，本地.
	 *
	 * @param url
	 *            文件地址
	 * @param connection
	 *            the connection
	 * @return 文件后缀
	 */
	public static String getMIMEFromUrl(String url, HttpURLConnection connection) {

		if (TextUtils.isEmpty(url)) {
			return null;
		}
		String suffix = null;
		try {
			// 获取后缀
			if (url.lastIndexOf(".") != -1) {
				suffix = url.substring(url.lastIndexOf("."));
				if (suffix.contains("/") || suffix.contains("?") || suffix.contains("&")) {
					suffix = null;
				}
			}
			if (TextUtils.isEmpty(suffix)) {
				// 获取文件名 这个效率不高
				String fileName = getRealFileName(connection);
				if (fileName != null && fileName.lastIndexOf(".") != -1) {
					suffix = fileName.substring(fileName.lastIndexOf("."));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return suffix;
	}

	/**
	 * 获取文件后缀，本地和网络.
	 *
	 * @param url
	 *            文件地址
	 * @param response
	 *            the response
	 * @return 文件后缀
	 */
	// public static String getMIMEFromUrl(String url,HttpResponse response){
	//
	// if(AbStrUtil.isEmpty(url)){
	// return null;
	// }
	// String mime = null;
	// try {
	// //获取后缀
	// if(url.lastIndexOf(".")!=-1){
	// mime = url.substring(url.lastIndexOf("."));
	// if(mime.indexOf("/")!=-1 || mime.indexOf("?")!=-1 ||
	// mime.indexOf("&")!=-1){
	// mime = null;
	// }
	// }
	// if(AbStrUtil.isEmpty(mime)){
	// //获取文件名 这个效率不高
	// String fileName = getRealFileName(response);
	// if(fileName!=null && fileName.lastIndexOf(".")!=-1){
	// mime = fileName.substring(fileName.lastIndexOf("."));
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return mime;
	// }

	/**
	 * 描述：MD5加密.
	 *
	 * @param str
	 *            要加密的字符串
	 * @return String 返回加密后的MD5字符串
	 */
	public final static String MD5(String str) {
		char hexDigits[] = { // 用来将字节转换成 16 进制表示的字符
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = str.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte tmp[] = mdTemp.digest(); // MD5 的计算结果是一个 128 位的长整数，
			// 用字节表示就是 16 个字节
			char strs[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
			// 所以表示成 16 进制需要 32 个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
				// 转换成 16 进制字符的转换
				byte byte0 = tmp[i]; // 取第 i 个字节
				strs[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
				// >>> 为逻辑右移，将符号位一起右移
				strs[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			return new String(strs).toUpperCase(); // 换后的结果转换为字符串
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 计算sdCard上的剩余空间.
	 *
	 * @return the int
	 */
	public static int freeSpaceOnSDCard() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / 1024 * 1024;
		return (int) sdFreeMB;
	}

	/**
	 * 根据文件的最后修改时间进行排序.
	 */
	public static class SortWithFileLastModify implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}


	/**
	 * 获取sd卡缓存目录
	 * 
	 * @param context
	 *            上下文
	 * @param uniqueName
	 *            缓存要保存的目录,如sd卡下自定义一个image目录用来存放图片缓存
	 * @return 返回sd卡缓存目录
	 */
	public File getDiskCacheDir(Context context, String uniqueName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/****
	 * 通过目录和文件名来获取Uri
	 * 
	 * @param strFileDir
	 *            目录
	 * @param strFileName
	 *            文件名
	 * @return Uri
	 * @throws IOException
	 *             IO异常
	 */
	public static Uri getUriByFileDirAndFileName(String strFileDir, String strFileName) throws IOException {
		Uri uri = null;
		File fileDir = new File(Environment.getExternalStorageDirectory(), strFileDir); // 定义目录
		if (!fileDir.exists()) { // 判断目录是否存在
			fileDir.mkdirs(); // 如果不存在则先创建目录
		}
		File file = new File(fileDir, strFileName); // 定义文件
		if (!file.exists()) { // 判断文件是否存在
			file.createNewFile(); // 如果不存在则先创建文件
		}
		uri = Uri.fromFile(file); // 获取Uri
		return uri;
	}

	/**
	 * 检查是否存在SD卡
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 通过Uri返回File文件 注意：通过相机的是类似content://media/external/images/media/97596
	 * 通过相册选择的：file:///storage/sdcard0/DCIM/Camera/IMG_20150423_161955.jpg
	 * 通过查询获取实际的地址
	 * 
	 * @param uri
	 *            Uri
	 * @param context
	 *            上下文对象 Activity
	 * @return File
	 */
	public static File getFileByUri(Activity context, Uri uri) {
		String path = null;
		if ("file".equals(uri.getScheme())) {
			path = uri.getEncodedPath();
			if (path != null) {
				path = Uri.decode(path);
				ContentResolver cr = context.getContentResolver();
				Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA },
						"(" + MediaStore.Images.ImageColumns.DATA + "=" + "'" + path + "'" + ")", null, null);
				int index = 0;
				int dataIdx = 0;
				assert cur != null;
				for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
					index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
					index = cur.getInt(index);
					dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
					path = cur.getString(dataIdx);
				}
				cur.close();
				if (index == 0) {
				} else {
					Uri u = Uri.parse("content://media/external/images/media/" + index);
					System.out.println("temp uri is :" + u);
				}
			}
			if (path != null) {
				return new File(path);
			}
		} else if ("content".equals(uri.getScheme())) {
			// 4.2.2以后
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				path = cursor.getString(columnIndex);
			}
			cursor.close();

			return new File(path);
		}
		return null;
	}
}
