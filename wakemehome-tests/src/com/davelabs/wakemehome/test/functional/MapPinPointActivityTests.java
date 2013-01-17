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
		sleep(1000);
		
		assert(t.getView().isShown());
	}
}
