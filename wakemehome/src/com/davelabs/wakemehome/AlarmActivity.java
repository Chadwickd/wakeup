package com.davelabs.wakemehome;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

public class AlarmActivity extends Activity{
	
	private Vibrator _vibrator;
	private Ringtone _r;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        vibrateAlarm();
        ringAlarm();
	}

	private void ringAlarm() {
		Uri tone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		_r = RingtoneManager.getRingtone(getApplicationContext(),tone);
		_r.play();
	}

	private void vibrateAlarm() {
		_vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        _vibrator.vibrate(10000000);
	}
	
	public void imAwakeButtonClicked(View v) {
		_vibrator.cancel();
		_r.stop();
		removeProximityAlert();
	}

	private void removeProximityAlert() {
		LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Intent intent = new Intent("com.davelabs.wakemehome.RING_ALARM");
        PendingIntent proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
		lm.removeProximityAlert(proximityIntent);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	this.startActivity(intent);		
	}
}
