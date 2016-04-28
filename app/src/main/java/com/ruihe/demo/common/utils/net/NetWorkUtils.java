package com.ruihe.demo.common.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 检验网络的公用工具包
 */
public class NetWorkUtils {

	/**
	 * 检验网络是否可以使用
	 * 
	 * @param context
	 *            上下文
	 * @return 网络是否可以使用的布尔值、
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager conManger = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (conManger != null) {
				if (conManger.getActiveNetworkInfo() != null) {
					return (conManger.getActiveNetworkInfo().isAvailable())
							&& (conManger.getActiveNetworkInfo().isConnected());
				}
			}
		}
		return false;
	}

	/**
	 * 检验网络是否是wifi连接
	 * 
	 * @param context
	 *            上下文
	 * @return wifi网络对应的布尔值
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager conManger = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = conManger
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return (mWiFiNetworkInfo.isAvailable())
						&& (mWiFiNetworkInfo.isConnected());
			}
		}
		return false;
	}

	/**
	 * 检验是否是蜂窝网络的即移动数据
	 * 
	 * @param context
	 *            上下文
	 * @return 移动网络的连接状态布尔值
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager conManger = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = conManger
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return (mMobileNetworkInfo.isAvailable())
						&& (mMobileNetworkInfo.isConnected());
			}
		}
		return false;
	}

    /**
     * 得到当前网络情况 none无网络，wifi，2G，3G，4G
     */
    public static String getNetWorkType(Context context) {
		String strNetworkType = "none";

		NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				strNetworkType = "WIFI";
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				String _strSubTypeName = networkInfo.getSubtypeName();

				Log.e("NetWorkUtils", "Network getSubtypeName : " + _strSubTypeName);

				// TD-SCDMA   networkType is 17
				int networkType = networkInfo.getSubtype();
				switch (networkType) {
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
						strNetworkType = "2G";
						break;
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
					case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
					case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
						strNetworkType = "3G";
						break;
					case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
						strNetworkType = "4G";
						break;
					default:
						// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
						if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
							strNetworkType = "3G";
						} else {
							//strNetworkType = _strSubTypeName;
							strNetworkType = "2G";
						}

						break;
				}

				Log.e("NetWorkUtils", "Network getSubtype : " + Integer.valueOf(networkType).toString());
			}
		}

		Log.e("NetWorkUtils", "Network Type : " + strNetworkType);

		return strNetworkType;
	}


}
