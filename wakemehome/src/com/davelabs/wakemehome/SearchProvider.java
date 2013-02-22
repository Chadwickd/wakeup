package com.davelabs.wakemehome;

import java.io.IOException;

import com.google.android.gms.maps.model.LatLng;

public interface SearchProvider {
	
	public LatLng getSearchResult(String searchQuery) throws IOException;

}
