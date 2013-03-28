package com.davelabs.wakemehome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		
    			
    	Intent intent = new Intent(arg0, AlarmActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	arg0.startActivity(intent);
		
	}

}
