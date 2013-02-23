package com.davelabs.wakemehome;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTrackingActivity extends Activity {
	
	private GoogleMap _map;
	private Marker _pinPointMarker;
	private boolean _isZoomedOnCurrentLocation;
	private boolean _isZoomingOnCurrentLocation;
	private LatLng _currentLocation;
	private LatLng _targetLocation;
	final private static int BOUNDS_PADDING = 100;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_tracking);
        
        _isZoomedOnCurrentLocation = false;
        _isZoomingOnCurrentLocation = false;
        
        MapFragment f = (MapFragment) this.getFragmentManager().findFragmentById(R.id.trackMap);
        _map = f.getMap();
        _map.setMyLocationEnabled(true);
        _map.getUiSettings().setMyLocationButtonEnabled(true);
        
        CameraPosition targetCameraPosition = extractTargetCameraPosition();
        showPinPointMarker();
        
        CameraUpdate toTargetPosition = CameraUpdateFactory.newCameraPosition(targetCameraPosition);
        _map.moveCamera(toTargetPosition);
        trackCurrentLocation();
    }

	private LatLngBounds getCameraBounds(LatLng currentLocation, LatLng targetPosition) {
		LatLngBounds.Builder builder = LatLngBounds.builder();
		builder.include(targetPosition);
		builder.include(currentLocation);
		return builder.build();
		
	}

	private void trackCurrentLocation() {
		CurrentPositionTracker tracker = new CurrentPositionTracker(this,
			new CurrentPositionTracker.CurrentPositionListener() {

				@Override
				public void currentPositionChanged(LatLng newPosition) {
					onCurrentPositionChanged(newPosition);
				}
				
			}
		);
		
		tracker.track();
	}

	protected void onCurrentPositionChanged(LatLng currentLocation) {
		_currentLocation = currentLocation;
		if (!_isZoomingOnCurrentLocation){
			if (!_isZoomedOnCurrentLocation) {
				 _isZoomingOnCurrentLocation = true;
				 LatLngBounds cameraBounds = getCameraBounds(currentLocation, _targetLocation);
			     CameraUpdate moveToBoundedCurrentLocation = CameraUpdateFactory.newLatLngBounds(cameraBounds, BOUNDS_PADDING);
			     moveMapToBoundedTargetPoint(moveToBoundedCurrentLocation);
			} else { 
			     animateMapToTargetPoint(currentLocation);
			}
		}
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
	
	private void animateMapToTargetPoint(LatLng targetPoint) {
		 CameraUpdate moveToCurrentLocation = CameraUpdateFactory.newLatLngZoom(targetPoint,_map.getMaxZoomLevel() - 3);
		_map.animateCamera(moveToCurrentLocation);
	}
	
	

	private CameraPosition extractTargetCameraPosition() {
        Bundle extras = this.getIntent().getExtras();
        CameraPosition targetPosition = (CameraPosition) extras.get("CameraPosition");
        _targetLocation = targetPosition.target;
		return targetPosition;
	}

	private void showPinPointMarker() {
		Marker m = createPinPointMarker(_targetLocation);
		m.setVisible(true);
	}
	
	private Marker createPinPointMarker(LatLng position) {
		if (_pinPointMarker == null) {
			BitmapDescriptor marker = BitmapDescriptorFactory.defaultMarker();
			_pinPointMarker = _map.addMarker(new MarkerOptions()
		      .icon(marker)
		      .position(position)
		      .draggable(false)
		    );
		}

		return _pinPointMarker;
	}
	
}
