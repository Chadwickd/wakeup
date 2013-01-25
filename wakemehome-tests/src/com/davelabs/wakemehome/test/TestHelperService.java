package com.davelabs.wakemehome.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class TestHelperService extends Service {
    
    @Override
    public void onCreate() {
    	super.onCreate();
    	context = getApplicationContext();
    }

	private static Context context;

    public static Context getContext() {
        return TestHelperService.context;
    }

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
