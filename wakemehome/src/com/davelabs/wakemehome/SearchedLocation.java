package com.davelabs.wakemehome;

import com.google.android.gms.maps.model.LatLng;

public class SearchedLocation {
	
	private int _id;
	private String _searchQuery;
	private LatLng _target;
	private boolean _isHome;
	private boolean _isFavourite;

	//Creating a new searched location
	public SearchedLocation(String searchQuery, LatLng target)	{
		_searchQuery = searchQuery;
		_target = target;
		_isHome = false;
		_isFavourite = false;
	}
	
	//Retrieving searched location from the DB
	public SearchedLocation(int id, String searchQuery, LatLng target, 
							boolean isHome, boolean isFavourite) {
		_id = id;
		_searchQuery = searchQuery;
		_target = target;
		_isHome = isHome;
		_isFavourite = isFavourite;
	}
	
	public String getSearchQuery() {
		return _searchQuery;
	}

	public LatLng getTarget() {
		return _target;
	}
	
	public boolean isHome()	{
		return _isHome;
	}
	
	public void setAsHome() {
		_isHome = true;
	}
	
	public boolean isFavourite() {
		return _isFavourite;
	}
	
	public void toggleFavourite() {
		_isFavourite = !_isFavourite;
	}
}
