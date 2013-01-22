package com.davelabs.wakemehome.test.unit;

import java.io.IOException;

import android.test.ActivityUnitTestCase;
import android.widget.Toast;

import com.davelabs.wakemehome.ISearchProvider;
import com.davelabs.wakemehome.MapPinPointActivity;
import com.davelabs.wakemehome.test.mocks.HardcodedSearchProvider;
import com.davelabs.wakemehome.test.mocks.IOExceptionSearchProvider;
import com.google.android.gms.internal.e;
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
	
	public void testSingleSearchResultIOHandled()
	{
		MapPinPointActivity activity = new MapPinPointActivity();
		ISearchProvider provider = new IOExceptionSearchProvider();
		activity.setSearchProvider(provider);
	   
		
		
		
		
		
	}
}
