package com.davelabs.wakemehome;

import com.google.android.gms.maps.model.LatLng;

public class SearchedLocation {
	
	private long _id;
	private String _searchQuery;
	private LatLng _target;
	private boolean _isHome;
	private boolean _isPinned;

	//Creating a new searched location
	public SearchedLocation(String searchQuery, LatLng target)	{
		_searchQuery = searchQuery;
		_target = target;
		_isHome = false;
		_isPinned = false;
	}
	
	//Retrieving searched location from the DB
	public SearchedLocation(long id, String searchQuery, LatLng target, 
							boolean isHome, boolean isPinned) {
		_id = id;
		_searchQuery = searchQuery;
		_target = target;
		_isHome = isHome;
		_isPinned = isPinned;
	}
	
	public void setId(long id) {
		_id = id;
	}
	
	public long getId() {
		return _id;
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
	
	public void unsetAsHome() {
		_isHome = false;
	}
	
	public boolean isPinned() {
		return _isPinned;
	}
	
	public void pin() {
		_isPinned = true;
	}
	
	public void unpin() {
		_isPinned = false;
	}
}
