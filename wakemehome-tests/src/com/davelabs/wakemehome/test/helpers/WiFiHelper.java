package com.davelabs.wakemehome.test.helpers;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WifiHelper {
	
	private WifiManager wifiManager;
	
	public WifiHelper(Context c) {
		wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
	}
	
	public void setWifiOff() {
        wifiManager.setWifiEnabled(false);
    }
    public void setWifiOn() {
        wifiManager.setWifiEnabled(true);
    }
    public boolean isWiFiConnected() {
    	return wifiManager.isWifiEnabled();
    }
}
