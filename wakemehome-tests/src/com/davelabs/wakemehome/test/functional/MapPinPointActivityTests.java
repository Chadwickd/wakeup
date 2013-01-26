package com.davelabs.wakemehome.test.functional;

import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.davelabs.wakemehome.MapPinPointActivity;
import com.davelabs.wakemehome.R;
import com.davelabs.wakemehome.test.helpers.ViewHelper;

public class MapPinPointActivityTests extends ActivityInstrumentationTestCase2<MapPinPointActivity> {

	private Instrumentation _ins;
	
	public MapPinPointActivityTests() {
		super(MapPinPointActivity.class);
	}
	
	public void setUp()
	{
		_ins = getInstrumentation();
	}
	
	public void testSearchingOverlayDisplayedOnStart() {
		setIntentSearchQuery("London");
		
		MapPinPointActivity a = (MapPinPointActivity) getActivity();
		View overlay = (View) a.findViewById(R.id.progressOverlay);
		
		assertTrue(overlay.isShown());
	}
	
	public void testUnfindableAddress() {
		setIntentSearchQuery("azazazazazazazaza");
		
		MapPinPointActivity a = (MapPinPointActivity) getActivity();
	
		View overlay = (View) a.findViewById(R.id.progressOverlay);
		ViewHelper.WaitForViewToHide(overlay, 5);
			
		Dialog d = a.getSearchQueryNotFoundDialog();
		assertTrue(d.isShowing());
	}
	
	private void setIntentSearchQuery(String query) {
		Intent intent = new Intent();
		intent.putExtra("searchQuery", query);
		setActivityIntent(intent);
	}
}
