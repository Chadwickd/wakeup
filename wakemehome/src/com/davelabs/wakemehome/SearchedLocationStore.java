package com.davelabs.wakemehome;

public interface SearchedLocationStore {
	public SearchedLocation getAllLocations();
	public SearchedLocation getHomeLocation();
	public void setHomeLocation(int id);
}
