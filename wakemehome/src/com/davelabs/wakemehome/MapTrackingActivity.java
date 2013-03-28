package com.davelabs.wakemehome;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;

import com.davelabs.wakemehome.camera.CameraDirector;
import com.davelabs.wakemehome.camera.MapTrackingCameraDirector;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTrackingActivity extends Activity {
	
	private GoogleMap _map;
	private Marker _pinPointMarker;
	private MapTrackingCameraDirector _director;
	private int _defaultZoomLevel;
	private LatLng _targetCameraLocation;


	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_tracking);
        
        Resources r = this.getResources();
		_defaultZoomLevel = r.getInteger(R.integer.zoomLevel);
		
        MapFragment f = (MapFragment) this.getFragmentManager().findFragmentById(R.id.trackMap);
        _map = f.getMap();
        _map.setMyLocationEnabled(true);
        _map.getUiSettings().setMyLocationButtonEnabled(true);
        
        CameraPosition targetCameraPosition = extractTargetCameraPosition();
        _targetCameraLocation = targetCameraPosition.target;
        showPinPointMarker();
        
        CameraDirector.CameraUpdateListener cameraListener = new CameraDirector.CameraUpdateListener() {
			@Override
			public void onCameraUpdate(CameraUpdate update, final CameraDirector.UpdateCompleteListener listener) {
				_map.animateCamera(update, new GoogleMap.CancelableCallback() {
					
					@Override
					public void onFinish() {
						listener.onUpdateComplete();
					}
					
					@Override
					public void onCancel() {
						_director.stopDirecting();
					}
				});
			}
		};
		
        _director = new MapTrackingCameraDirector(cameraListener, targetCameraPosition, _defaultZoomLevel);
        _director.startDirecting();
        
        trackCurrentLocation();
        addProximityAlert();
    }

	

	private void trackCurrentLocation() {
		CurrentPositionTracker tracker = new CurrentPositionTracker(
			new CurrentPositionTracker.CurrentPositionListener() {
				@Override
				public void onCurrentPositionChanged(LatLng newPosition) {
					_director.aimForNewTarget(newPosition);
				}
			}
		);
		
        _map.setOnMyLocationChangeListener(tracker);
	}

	private CameraPosition extractTargetCameraPosition() {
		Bundle extras = this.getIntent().getExtras();
        CameraPosition targetPosition = (CameraPosition) extras.get("CameraPosition");
		return targetPosition;
	}

	private void showPinPointMarker() {
		Marker m = createPinPointMarker(_targetCameraLocation);
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



	private void addProximityAlert() {
		LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		 Intent intent = new Intent("com.davelabs.wakemehome.RING_ALARM");
         PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		lm.addProximityAlert(_targetCameraLocation.latitude,_targetCameraLocation.longitude, 200, -1, proximityIntent);
		
	}
}
