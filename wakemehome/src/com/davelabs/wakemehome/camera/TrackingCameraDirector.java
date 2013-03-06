package com.davelabs.wakemehome.camera;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

public class TrackingCameraDirector extends CameraDirector {

	private long _startTime;

	public TrackingCameraDirector(CameraUpdateListener listener) {
		super(listener);
		
	}

	@Override
	public void startDirector() {
		_startTime = System.currentTimeMillis();
		
	}
	
	public void setPinpointLocation();
	
	
	
	
	
	public CameraUpdate animateMapToTargetPoint(LatLng targetPoint) {
		 CameraUpdate moveToCurrentLocation = CameraUpdateFactory.newLatLngZoom(targetPoint,_map.getMaxZoomLevel() - 3);
		 return moveToCurrentLocation;
	}
	
}
