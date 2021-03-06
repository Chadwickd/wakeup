package com.davelabs.wakemehome.camera;

import android.os.Handler;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class MapTrackingCameraDirector extends CameraDirector {

	private static final int WAIT_SECONDS = 6;
	
	private int _defaultZoomLevel;
	
	private LatLng _currentTarget;
	private LatLng _homeLocation;
	private CameraPosition _homeCameraPosition;

	private boolean _hasAimedAtHome;
	private boolean _waitComplete;
	private boolean _zooming;
	private boolean _hasZoomed;
	private boolean _trackingPaused;
	
	final private static int BOUNDS_PADDING = 100;
	

	public MapTrackingCameraDirector(CameraUpdateListener listener, CameraPosition homeCameraPosition, int defaultZoomLevel) {
		super(listener);
		_homeCameraPosition = homeCameraPosition;
		_homeLocation = homeCameraPosition.target;
		_defaultZoomLevel = defaultZoomLevel;
		_trackingPaused = false;
	}

	public void aimForNewTarget(LatLng newPosition) {
		_currentTarget = newPosition;
		getNextDirection();
	}
	
	public void pauseTracking() {
		_trackingPaused = true;
	}
	
	public void resumeTracking() {
		_trackingPaused = false;
	}

	@Override
	protected void initialize() {
		_hasAimedAtHome = false;
		_waitComplete = false;
		_zooming = false;
		_hasZoomed = false;
	}
	
	
	@Override
	protected void getNextDirection() {
		if (shouldTrack()) {
			if(!_trackingPaused) {
				track();
			}
		} else {
			getPreTrackAnimationDirection();
		}
	}
	
	@Override
	protected void updateStateAfterUpdateCompletion() {
		if (_zooming) {
			_zooming = false;
			_hasZoomed = true;
			
		}
	}

	private void getPreTrackAnimationDirection() {
		if (shouldZoom()) {
			zoomOutToShowBoth();
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
	

	private boolean shouldTrack() {
		return (_waitComplete && _hasZoomed && !_zooming);
	}

	private boolean shouldZoom() {
		return (_waitComplete && _currentTarget != null);
	}
	
	private void aimAtHome() {
		CameraUpdate toTargetPosition = CameraUpdateFactory.newCameraPosition(_homeCameraPosition);
		transmitUpdate(toTargetPosition);
		_hasAimedAtHome = true;
	}

	private void zoomOutToShowBoth() {
		LatLngBounds cameraBounds = getCameraBounds();
		CameraUpdate moveToBoundedCurrentLocation = CameraUpdateFactory.newLatLngBounds(cameraBounds, BOUNDS_PADDING);
		transmitUpdate(moveToBoundedCurrentLocation);
		_zooming = true;
	}

	private void track() {
		CameraUpdate moveToCurrentLocation = CameraUpdateFactory.newLatLngZoom(_currentTarget, _defaultZoomLevel);
		transmitUpdate(moveToCurrentLocation);
	}

	private LatLngBounds getCameraBounds() {
		LatLngBounds.Builder builder = LatLngBounds.builder();
		builder.include(_homeLocation);
		builder.include(_currentTarget);
		return builder.build();	
	}
	
}
