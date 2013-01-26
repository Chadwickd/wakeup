package com.davelabs.wakemehome;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

public class GeocoderSearchProvider implements ISearchProvider {

	private Context _context;
	
	public GeocoderSearchProvider(Context c) {
		_context = c;
	}
	
	@Override
	public LatLng getSearchResult(String searchQuery) throws IOException {
		Geocoder g = new Geocoder(_context);
		List<Address> addresses = g.getFromLocationName(searchQuery, 1);
		if (addresses.isEmpty()) {
			return null;
		} else {
			Address a = addresses.get(0);
			return new LatLng(a.getLatitude(), a.getLongitude());
		}
	}

}
