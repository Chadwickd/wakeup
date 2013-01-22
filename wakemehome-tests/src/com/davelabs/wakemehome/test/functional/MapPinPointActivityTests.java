package com.davelabs.wakemehome.test.functional;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Toast;

import com.davelabs.wakemehome.MapPinPointActivity;

public class MapPinPointActivityTests extends ActivityInstrumentationTestCase2<MapPinPointActivity> {

	private Instrumentation _ins;
	
	public MapPinPointActivityTests() {
		super(MapPinPointActivity.class);
	}
	
	public void setUp()
	{
		_ins = getInstrumentation();
	}
	
	public void testUnfindableAddress() {
		Intent intent = new Intent();
		intent.putExtra("searchQuery", "skjfhskjdfh");
		setActivityIntent(intent);
		
		MapPinPointActivity a = (MapPinPointActivity)getActivity();
		Toast t = a.getSearchErrorToast();
		
		//try to add some kind of sleep
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assert(t.getView().isShown());
	}
	public void testNoInternetConnection() {
		Intent intent = new Intent();
		intent.putExtra("searchQuery", "EC1N 8NX");
		setActivityIntent(intent);
		MapPinPointActivity a = (MapPinPointActivity)getActivity();
		
	}
}
