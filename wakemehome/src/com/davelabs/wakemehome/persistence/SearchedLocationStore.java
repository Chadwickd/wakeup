package com.davelabs.wakemehome.persistence;

import java.util.List;

import com.davelabs.wakemehome.SearchedLocation;

public interface SearchedLocationStore {
	public List<SearchedLocation> getAllLocations();
	public SearchedLocation getHomeLocation();
	public void saveLocation(SearchedLocation location);
}
