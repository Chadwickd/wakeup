package com.davelabs.wakemehome.test.unit;

import android.test.ActivityUnitTestCase;

import com.davelabs.wakemehome.MapPinPointActivity;
import com.davelabs.wakemehome.test.mocks.HardcodedSearchProvider;
import com.google.android.gms.maps.model.LatLng;

public class UnitMapPinPointActivityTests extends ActivityUnitTestCase<MapPinPointActivity> {

	

	public UnitMapPinPointActivityTests() {
		super(MapPinPointActivity.class);
		
	}

	public void testSingleSearchResultReturned()
	{
		MapPinPointActivity activity = new MapPinPointActivity();
		HardcodedSearchProvider provider = new HardcodedSearchProvider();
		activity.setSearchProvider(provider);
		LatLng l = activity.convertSearchQueryToPoint("EC1N 8NX");
		assertEquals(provider.getHardcodedValue().latitude, l.latitude);
		assertEquals(provider.getHardcodedValue().longitude, l.longitude);
	}
	
//	public void testSingleSearchResultIOHandled()
//	{
//		ISearchProvider provider = new IOExceptionSearchProvider();
//		_a.setSearchProvider(provider);
//		
//		try {
//			LatLng l = _a.convertSearchQueryToPoint("EC1N 8NX");
//		} catch (Exception e) {
//			assertEquals(IOException.class, e.class);
//		}
//	}
}
