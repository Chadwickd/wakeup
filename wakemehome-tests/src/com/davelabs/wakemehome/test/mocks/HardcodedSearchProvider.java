package com.davelabs.wakemehome.test.mocks;

import com.davelabs.wakemehome.ISearchProvider;
import com.google.android.gms.maps.model.LatLng;

public class HardcodedSearchProvider implements ISearchProvider {

	private LatLng _hardcodedValue = new LatLng(100, 100);
	
	@Override
	public LatLng getSearchResult(String searchQuery) {
		return _hardcodedValue;
	}

	public LatLng getHardcodedValue()
	{
		return _hardcodedValue;
	}
}
