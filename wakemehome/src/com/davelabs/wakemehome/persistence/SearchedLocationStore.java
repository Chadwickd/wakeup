package com.davelabs.wakemehome.persistence;

import java.util.List;

import com.davelabs.wakemehome.SearchedLocation;

public interface SearchedLocationStore {
	SearchedLocation getHomeLocation();
	void clearHomeLocation();
	List<SearchedLocation> getPinnedLocations();
	List<SearchedLocation> getRecentLocations();
	
	void saveLocation(SearchedLocation location);
	void touchLocationDate(SearchedLocation location);
	void removeLocation(SearchedLocation location);
	void pinLocation(SearchedLocation location);
	void unpinLocation(SearchedLocation location);
	void setLocationAsHome(SearchedLocation location);
}