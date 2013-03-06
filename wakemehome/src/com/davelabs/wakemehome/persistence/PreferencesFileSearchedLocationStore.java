package com.davelabs.wakemehome.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.davelabs.wakemehome.SearchedLocation;
import com.google.android.gms.maps.model.LatLng;

//Simple SearchedLocationStore implementation that saves a single location only
public class PreferencesFileSearchedLocationStore implements SearchedLocationStore {

	private static final String PREF_HOME_QUERY = "homeLocationQuery";
	private static final String PREF_HOME_LAT = "homeLocationLat";
	private static final String PREF_HOME_LNG = "homeLocationLng";
	
	private SharedPreferences _preferences;

	public PreferencesFileSearchedLocationStore(Context context)
	{
		_preferences = context.getSharedPreferences("preferences", context.MODE_PRIVATE);
	}
	
	@Override
	public List<SearchedLocation> getAllLocations() {
		List<SearchedLocation> locations = new ArrayList<SearchedLocation>();
		
		SearchedLocation homeLocation = getHomeLocationFromPreferences();
		
		if (homeLocation != null) {
			locations.add(homeLocation);
		}
		
		return locations;
	}

	@Override
	public SearchedLocation getHomeLocation() {
		return getHomeLocationFromPreferences();
	}

	@Override
	public void saveLocation(SearchedLocation location) {
		Editor e = _preferences.edit();
		
		e.putString(PREF_HOME_QUERY, location.getSearchQuery());
		
		LatLng latLng = location.getTarget();
		e.putFloat(PREF_HOME_LAT, (float)latLng.latitude);
		e.putFloat(PREF_HOME_LNG, (float)latLng.longitude);
		
		e.commit();
	}	

	private SearchedLocation getHomeLocationFromPreferences() {
		SearchedLocation homeLocation = null;
		
		String homeLocationQuery = _preferences.getString(PREF_HOME_QUERY, null);
		
		if (homeLocationQuery != null) {
			float lat = _preferences.getFloat(PREF_HOME_LAT, 0);
			float lng = _preferences.getFloat(PREF_HOME_LNG, 0);

			LatLng latLng = new LatLng(lat, lng);
			
			homeLocation = new SearchedLocation(0, homeLocationQuery, latLng, true, false);
		}
		
		return homeLocation;
	}
}
