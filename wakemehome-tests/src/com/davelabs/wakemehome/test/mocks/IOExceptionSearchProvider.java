package com.davelabs.wakemehome.test.mocks;

import java.io.IOException;

import com.davelabs.wakemehome.ISearchProvider;
import com.google.android.gms.maps.model.LatLng;

public class IOExceptionSearchProvider implements ISearchProvider {

	
	
	@Override
	public LatLng getSearchResult(String searchQuery) throws IOException {
		throw new IOException();
	}
}
