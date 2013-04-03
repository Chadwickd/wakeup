package com.davelabs.wakemehome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context c, Intent i) {		
		boolean entering  = i.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
		if (entering) {
	    	Intent intent = new Intent(c, AlarmActivity.class);
	    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	c.startActivity(intent);
		}
	}

}
