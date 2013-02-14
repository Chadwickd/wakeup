package com.davelabs.wakemehome;

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

public class CurrentPositionTracker implements LocationListener {
	
	public interface CurrentPositionListener {
		public void currentPositionChanged(LatLng newPosition);
	}

	private CurrentPositionListener _listener;
	
	private Location _currentLocation;
	
	public CurrentPositionTracker(CurrentPositionListener listener) {
		_listener = listener;
	}
	
	@Override
	public void onLocationChanged(Location newLocation) {		
		if (_currentLocation == null) {
			betterLocationFound(newLocation);
		} else {
			if (isAtLeastAsAccurate(newLocation)) {
				betterLocationFound(newLocation);
			} else if (doesntIntersectWithCurrentPosition(newLocation)) {
				betterLocationFound(newLocation);
			}
		}
	}

	private void betterLocationFound(Location location) {
		_currentLocation = location;
		
		LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
		_listener.currentPositionChanged(newLatLng);
	}

	private boolean isAtLeastAsAccurate(Location newLocation) {
		return (newLocation.getAccuracy() <= _currentLocation.getAccuracy());
	}
	
	private boolean doesntIntersectWithCurrentPosition(Location newLocation) {
		float pointDelta = _currentLocation.distanceTo(newLocation);
		double radiusSum = newLocation.getAccuracy() + _currentLocation.getAccuracy();
		
		return (pointDelta > radiusSum);
	}
//	
//	private double getLocationDeltaInMetres(Location newLocation, Location currentLocation) {
//		double earthRadius = 6372.8;
//		double newLat = newLocation.getLatitude();
//		double currentLat = newLocation.getLatitude();
//		double deltaLat =  newLat - currentLat; 
//		double deltaLong = newLocation.getLongitude() - currentLocation.getLongitude();
//		
//		double dLatRad = Math.toRadians(deltaLat);
//		double dLonRad = Math.toRadians(deltaLong);
//		
//		double a = (
//				    Math.sin(dLatRad / 2) * Math.sin(dLatRad / 2)
//				   ) + 
//				   (
//					Math.sin(dLonRad / 2) * Math.sin(dLonRad / 2) * Math.cos(currentLat) * Math.cos(newLat)
//				   );
//		
//		double c = 2 * Math.asin(Math.sqrt(a));
//		return earthRadius * c;
//	}

	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	public void track(Context c)	{
		LocationManager lm = (LocationManager) c.getSystemService(c.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		lm.requestLocationUpdates(1000, 0, criteria, this, c.getMainLooper());
	}
}
