package com.davelabs.wakemehome;

import android.location.Location;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

public class CurrentPositionTracker implements LocationListener {
	
	public interface CurrentPositionListener {
		public void onCurrentPositionChanged(LatLng newPosition);
	}

	private CurrentPositionListener _listener;
	
	public CurrentPositionTracker(CurrentPositionListener listener) {
		_listener = listener;
	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		_listener.onCurrentPositionChanged(newLatLng);		
	}
}
