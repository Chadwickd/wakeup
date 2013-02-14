package com.davelabs.wakemehome;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTrackingActivity extends Activity {
	
	private GoogleMap _map;
	private Marker _pinPointMarker;
	private LatLng _currentLocation;
	private LatLng _passedPosition;
	final private static int BOUNDS_PADDING = 100;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_tracking);
        
        MapFragment f = (MapFragment) this.getFragmentManager().findFragmentById(R.id.trackMap);
        _map = f.getMap();
        _map.setMyLocationEnabled(true);
        _map.getUiSettings().setMyLocationButtonEnabled(true);
        
        _passedPosition = extractPassedPosition();
        showPinPointMarker(_passedPosition);
        
        trackCurrentLocation();
    }

	private LatLngBounds getCameraBounds() {
		LatLngBounds.Builder builder = LatLngBounds.builder();
		builder.include(_passedPosition);
		builder.include(_currentLocation);
		return builder.build();
		
	}

	private void trackCurrentLocation() {
		CurrentPositionTracker tracker = new CurrentPositionTracker(
			new CurrentPositionTracker.CurrentPositionListener() {

				@Override
				public void currentPositionChanged(LatLng newPosition) {
					onCurrentPositionChanged(newPosition);
				}
				
			}
		);
		
		tracker.track(this);
	}

	protected void onCurrentPositionChanged(LatLng currentPosition) {
		if (_currentLocation == null) {
			_currentLocation = currentPosition;
			 LatLngBounds cameraBounds = getCameraBounds();
		     CameraUpdate moveToCurrentLocation = CameraUpdateFactory.newLatLngBounds(cameraBounds, BOUNDS_PADDING);
		     moveMapToTargetPoint(moveToCurrentLocation);
		}
	}

	private void moveMapToTargetPoint(CameraUpdate targetPoint) {
		_map.animateCamera(targetPoint);
	}

	private LatLng extractPassedPosition() {
        Bundle extras = this.getIntent().getExtras();
        LatLng passedPosition = (LatLng) extras.get("LocationCoords");
		return passedPosition;
	}

	private void showPinPointMarker(LatLng result) {
		Marker m = createPinPointMarker(result);
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
