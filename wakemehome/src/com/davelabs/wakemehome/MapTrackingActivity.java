package com.davelabs.wakemehome;

import android.app.Activity;
import android.os.Bundle;

import com.davelabs.wakemehome.camera.CameraDirector;
import com.davelabs.wakemehome.camera.MapTrackingCameraDirector;
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
	private MapTrackingCameraDirector _director;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_tracking);
        
        MapFragment f = (MapFragment) this.getFragmentManager().findFragmentById(R.id.trackMap);
        _map = f.getMap();
        _map.setMyLocationEnabled(true);
        _map.getUiSettings().setMyLocationButtonEnabled(true);
        
        CameraPosition targetCameraPosition = extractTargetCameraPosition();
        showPinPointMarker(targetCameraPosition.target);
        
        CameraDirector.CameraUpdateListener cameraListener = new CameraDirector.CameraUpdateListener() {
			@Override
			public void onCameraUpdate(CameraUpdate update) {
				//might need to update this to callback to the camera director when we are ready to move again
				_map.animateCamera(update, new GoogleMap.CancelableCallback() {
					
					@Override
					public void onFinish() {
						_director.lastUpdateComplete();
					}
					
					@Override
					public void onCancel() {
						_director.stopDirecting();
					}
				});
			}
		};
		
        _director = new MapTrackingCameraDirector(cameraListener, _map, targetCameraPosition);
        _director.startDirecting();
        
        trackCurrentLocation();
    }

	

	private void trackCurrentLocation() {
		CurrentPositionTracker tracker = new CurrentPositionTracker(this,
			new CurrentPositionTracker.CurrentPositionListener() {
				@Override
				public void onCurrentPositionChanged(LatLng newPosition) {
					_director.aimForNewTarget(newPosition);
				}
			}
		);
		
		tracker.track();
	}

	private CameraPosition extractTargetCameraPosition() {
		Bundle extras = this.getIntent().getExtras();
        CameraPosition targetPosition = (CameraPosition) extras.get("CameraPosition");
		return targetPosition;
	}

	private void showPinPointMarker(LatLng targetLocation) {
		Marker m = createPinPointMarker(targetLocation);
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
