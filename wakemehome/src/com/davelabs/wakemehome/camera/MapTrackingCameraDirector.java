package com.davelabs.wakemehome.camera;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MapTrackingCameraDirector extends CameraDirector {

	private long _startTime;
	private LatLng _currentTarget;
	private LatLng _homeLocation;
	private CameraPosition _homeCameraPosition;
	
	private boolean _isZoomedOnCurrentLocation;
	private boolean _isZoomingOnCurrentLocation;

	private GoogleMap _map;
	private boolean _isTracking;
	
	final private static int BOUNDS_PADDING = 100;
	

	public MapTrackingCameraDirector(CameraUpdateListener listener, GoogleMap map, CameraPosition homeCameraPosition) {
		super(listener);
		_map = map;
		_homeCameraPosition = homeCameraPosition;
		_homeLocation = homeCameraPosition.target;
	}

	public void aimForNewTarget(LatLng newPosition) {
		_currentTarget = newPosition;		
//		if (!_isZoomingOnCurrentLocation){
//			if (!_isZoomedOnCurrentLocation) {
//				 _isZoomingOnCurrentLocation = true;
//				
//			} else { 
//			     animateMapToTargetPoint(currentLocation);
//			}
//		}
	}

	@Override
	public void startDirecting() {
		_startTime = System.currentTimeMillis();
		readyForNextDirection();
	}
	
	@Override
	public void readyForNextDirection() {
		if (_starting)
		{
			aimAtHome();
		}
		if (!_pausing)
		{
			pauseForEffect();
		}
		if (!_zoomingOut)
		{
			zoomOutToShowBoth();
		}
		if (!_tracking)
		{
			startTracking();
		}
	}
	
	private void aimAtHome() {
		CameraUpdate toTargetPosition = CameraUpdateFactory.newCameraPosition(_homeCameraPosition);
		transmitUpdate(toTargetPosition);
	}

	private void pauseForEffect() {
		// TODO Auto-generated method stub
		
	}

	private void zoomOutToShowBoth() {
		 LatLngBounds cameraBounds = getCameraBounds();
	     CameraUpdate moveToBoundedCurrentLocation = CameraUpdateFactory.newLatLngBounds(cameraBounds, BOUNDS_PADDING);
	     moveMapToBoundedTargetPoint(moveToBoundedCurrentLocation);
	}

	private void startTracking() {
		// TODO Auto-generated method stub		
	}

	private LatLngBounds getCameraBounds() {
		LatLngBounds.Builder builder = LatLngBounds.builder();
		builder.include(_homeLocation);
		builder.include(_currentTarget);
		return builder.build();	
	}
	
	private CameraUpdate animateMapToTargetPoint(LatLng targetPoint) {
		 CameraUpdate moveToCurrentLocation = CameraUpdateFactory.newLatLngZoom(targetPoint,_map.getMaxZoomLevel() - 3);
		 return moveToCurrentLocation;
	}
	
	private void moveMapToBoundedTargetPoint(CameraUpdate targetPoint) {
		_map.animateCamera(targetPoint,new GoogleMap.CancelableCallback() {
			
			@Override
			public void onFinish() {
				_isZoomedOnCurrentLocation = true;
				_isZoomingOnCurrentLocation = false;
				animateMapToTargetPoint(_currentLocation);
			}
			
			@Override
			public void onCancel() {}
		});
	}

	@Override
	public void stopDirecting() {
		// TODO Auto-generated method stub
		
	}
}
