package com.davelabs.wakemehome.camera;

import android.os.Handler;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MapTrackingCameraDirector extends CameraDirector {

	private static final int WAIT_SECONDS = 10;
	
	private long _startTime;
	private LatLng _currentTarget;
	private LatLng _homeLocation;
	private CameraPosition _homeCameraPosition;

	private GoogleMap _map;

	private boolean _hasAimedAtHome;
	private boolean _waitComplete;
	private boolean _zooming;
	
	final private static int BOUNDS_PADDING = 100;
	

	public MapTrackingCameraDirector(CameraUpdateListener listener, GoogleMap map, CameraPosition homeCameraPosition) {
		super(listener);
		_map = map;
		_homeCameraPosition = homeCameraPosition;
		_homeLocation = homeCameraPosition.target;
	}

	public void aimForNewTarget(LatLng newPosition) {
		_currentTarget = newPosition;		
		getNextDirection();
	}

	@Override
	protected void initialize() {
		_startTime = System.currentTimeMillis();
		_hasAimedAtHome = false;
		_waitComplete = false;
		_zooming = false;
	}
	
	@Override
	protected void getNextDirection() {
		if (shouldTrack())	{
			track();
		} else {
			getPreTrackAnimationDirection();
		}
	}

	private void getPreTrackAnimationDirection() {
		if (shouldZoom()) {
			zoomOutToShowBoth();
			zoomCompleted();
		} else if (shouldAimAtHome()){
			aimAtHome();
			startWait();
		}
	}

	private boolean shouldAimAtHome() {
		return (!_hasAimedAtHome);
	}

	private void startWait() {
		Handler h = new Handler();
		h.postDelayed(new Runnable() {

			@Override
			public void run() {
				waitCompleted();
			}
			
		}, WAIT_SECONDS * 1000);
	}

	private void waitCompleted() {
		_waitComplete = true;
		getNextDirection();
	}
	
	private void zoomCompleted() {
		_zooming = false;
	}

	private boolean shouldTrack() {
		return (_waitComplete && !_zooming);
	}

	private boolean shouldZoom() {
		return (_waitComplete && _currentTarget != null);
	}
	
	private void aimAtHome() {
		CameraUpdate toTargetPosition = CameraUpdateFactory.newCameraPosition(_homeCameraPosition);
		transmitUpdate(toTargetPosition);
		_hasAimedAtHome = true;
	}

	private void pauseForEffect() {
		while (System.currentTimeMillis() < _startTime +10000 );
	}

	private void zoomOutToShowBoth() {
		 LatLngBounds cameraBounds = getCameraBounds();
	     CameraUpdate moveToBoundedCurrentLocation = CameraUpdateFactory.newLatLngBounds(cameraBounds, BOUNDS_PADDING);
	     moveMapToBoundedTargetPoint(moveToBoundedCurrentLocation);
	}

	private void track() {
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
//				_isZoomedOnCurrentLocation = true;
//				_isZoomingOnCurrentLocation = false;
				animateMapToTargetPoint(_currentTarget);
			}
			
			@Override
			public void onCancel() {}
		});
	}
}
