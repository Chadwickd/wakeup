package com.davelabs.wakemehome;

import java.io.IOException;

import com.google.android.gms.maps.model.LatLng;

public interface ISearchProvider {
	
	public LatLng getSearchResult(String searchQuery) throws IOException;

}
