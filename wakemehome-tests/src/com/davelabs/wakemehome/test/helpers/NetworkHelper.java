package com.davelabs.wakemehome.test.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class NetworkHelper {
	
	public static boolean networkConnected(Context context) {
        ConnectivityManager cm =
            (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }
	
	public static void turnNetworkOn(Context c) {
		turnMobileDataOn(c);
		turnWifiOn(c);
		while (!networkConnected(c));
	}
	
	public static void turnNetworkOff(Context c) {
		turnMobileDataOff(c);
		turnWifiOff(c);
		while (networkConnected(c));
	}
	
	private static void setMobileData(Context context, boolean enabled) {
		try {
		    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    final Class conmanClass = Class.forName(conman.getClass().getName());
		    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		    iConnectivityManagerField.setAccessible(true);
		    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
		    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
		    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		    setMobileDataEnabledMethod.setAccessible(true);
	
		    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void turnMobileDataOn (Context context) {
		setMobileData(context, true);
	}
	
	private static void turnMobileDataOff (Context context) {
		setMobileData(context, false);
	}
	
	private static WifiManager getWifiManager(Context c) {
		return (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
	}
	
	private static void turnWifiOff(Context c) {
		getWifiManager(c).setWifiEnabled(false);
    }
	private static void turnWifiOn(Context c) {
    	getWifiManager(c).setWifiEnabled(true);
    }
	
}
