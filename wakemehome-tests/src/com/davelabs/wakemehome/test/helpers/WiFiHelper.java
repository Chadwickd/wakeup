package com.davelabs.wakemehome.test.helpers;

import android.content.Context;
import android.net.wifi.WifiManager;

public class WiFiHelper {
	
	private WifiManager wifiManager;
	
	public WiFiHelper(Context c) {
		wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
	}
	
	public void setWiFiOff() {
        wifiManager.setWifiEnabled(false);
    }
    public void setWiFiOn() {
        wifiManager.setWifiEnabled(true);
    }
}
