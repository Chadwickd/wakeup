package com.davelabs.wakemehome.test.functional;

import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;

import com.davelabs.wakemehome.MapPinPointActivity;
import com.davelabs.wakemehome.R;
import com.davelabs.wakemehome.test.helpers.NetworkHelper;
import com.davelabs.wakemehome.test.helpers.UIHelper;
import com.google.android.gms.maps.model.GroundOverlay;

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
		setIntentSearchQuery("azazazazazazazaza");
		
		MapPinPointActivity a = (MapPinPointActivity) getActivity();
	
		View overlay = (View) a.findViewById(R.id.searchProgressOverlay);
		UIHelper.assertViewHides(overlay, 5);
			
		Dialog d = a.getSearchQueryNotFoundDialog();
		assertTrue(d.isShowing());
	}
	

	public void testNoInternetWhenLookingUpAddress() {
		setIntentSearchQuery("EC1N 8NX");
		MapPinPointActivity a = (MapPinPointActivity) getActivity();
		
		//Hopefully this runs quickly enough after the activity has been started by getActivity().
		//It doesn't seem to be possible to turn it off without creating the activity as a context
		//is required.
		NetworkHelper.turnNetworkOff(a);
		
		View overlay = (View) a.findViewById(R.id.searchProgressOverlay);
		UIHelper.assertViewHides(overlay, 5);
			
		Dialog d = a.getSearchQueryLookupFailedDialog();
		UIHelper.assertDialogShows(d, 5);
		
		//View loadingOverlay = (View) a.findViewById(R.id.searchProgressOverlay);
		//assertTrue(loadingOverlay.isShown());
		NetworkHelper.turnNetworkOn(a);
	}
	
	public void testNoInternetPolls() {
		setIntentSearchQuery("ANYTHING");
		MapPinPointActivity a = (MapPinPointActivity) getActivity();
		
		NetworkHelper.turnNetworkOff(a);
		
		Dialog d = a.getSearchQueryLookupFailedDialog();
		assertTrue(d.isShowing());
		
		//Force failure
		assertEquals(true, false);
	}

	public void testPinPointShowsForValidLocation()
	{
		setIntentSearchQuery("EC1N 8NX");
		MapPinPointActivity a = (MapPinPointActivity) getActivity();
		
		NetworkHelper.turnNetworkOn(a);
		
		GroundOverlay marker = a.getPinPointMarker();
		assertTrue(marker.isVisible());
	}
	
	private void setIntentSearchQuery(String query) {
		Intent intent = new Intent();
		intent.putExtra("searchQuery", query);
		setActivityIntent(intent);
	}
}

