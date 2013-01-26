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
	
		assert(t.getView().isShown());
	}
	
//	public void testNoInternetConnection() {
//		Intent intent = new Intent();
//		intent.putExtra("searchQuery", "EC1N 8NX");
//		setActivityIntent(intent);
//		
//		
//		MapPinPointActivity a = new MapPinPointActivity();
//		a.setSearchProvider(new IOExceptionSearchProvider());
//		
//		Toast t = a.getNoInternetToast();
//		assertFalse(t.getView().isShown());
//		
//		this.setActivity(a);
//		assertEquals(a, getActivity());
//		
//		a = (MapPinPointActivity) getActivity();
//		
//		assertEquals(t, a.getNoInternetToast());
//		assert(t.getView().isShown());
//	}
}
