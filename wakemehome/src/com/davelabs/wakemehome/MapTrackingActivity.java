package com.davelabs.wakemehome;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.davelabs.wakemehome.camera.CameraDirector;
import com.davelabs.wakemehome.camera.MapTrackingCameraDirector;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapTrackingActivity extends Activity {
	
	private static final long PROXIMITY_DURATION = 86400000;
	private static final float ALARM_RADIUS_METERS = 2000;
	private GoogleMap _map;
	private Marker _pinPointMarker;
	private MapTrackingCameraDirector _director;
	private int _defaultZoomLevel;
	private CameraPosition _targetCameraPosition;
	private LocationClient _lc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_tracking);
        
        Resources r = this.getResources();
		_defaultZoomLevel = r.getInteger(R.integer.zoomLevel);
		
        setupMap();
        setupTargetPinPoint();
        startAnimating();
        registerLocationDependentHandlers();
    }

	private void startAnimating() {
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
		
        _director = new MapTrackingCameraDirector(cameraListener, _targetCameraPosition, _defaultZoomLevel);
        _director.startDirecting();
        
        _map.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
        	@Override
			public boolean onMyLocationButtonClick() {
				_director.resumeTracking();
				return false;
			}
		});
        
        _map.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng arg0) {
				_director.pauseTracking();
			}
        });
	}

	private void setupTargetPinPoint() {
		CameraPosition targetCameraPosition = extractTargetCameraPosition();
		_targetCameraPosition = targetCameraPosition;
        showPinPointMarker();
	}

	private void setupMap() {
		MapFragment f = (MapFragment) this.getFragmentManager().findFragmentById(R.id.trackMap);
        _map = f.getMap();
        _map.setMyLocationEnabled(true);
        _map.getUiSettings().setZoomControlsEnabled(false);
	}

	private CameraPosition extractTargetCameraPosition() {
		Bundle extras = this.getIntent().getExtras();
        CameraPosition targetPosition = (CameraPosition) extras.get("CameraPosition");
		return targetPosition;
	}

	private void showPinPointMarker() {
		Marker m = createPinPointMarker(_targetCameraPosition.target);
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

	private void registerLocationDependentHandlers() {
		_lc = new LocationClient(this, 
				new GooglePlayServicesClient.ConnectionCallbacks() {

					@Override
					public void onConnected(Bundle arg0) {
						registerProximityAlert();
						registerLocationChangedHandler();
					}
				
					@Override
					public void onDisconnected() {}
			
		},
				new GooglePlayServicesClient.OnConnectionFailedListener() {

					@Override
					public void onConnectionFailed(ConnectionResult arg0) {}
		});
		
		_lc.connect();
	}
	
	private void registerLocationChangedHandler() {
		CurrentPositionTracker tracker = new CurrentPositionTracker(
			new CurrentPositionTracker.CurrentPositionListener() {
				@Override
				public void onCurrentPositionChanged(LatLng newPosition) {
					_director.aimForNewTarget(newPosition);
				}
			}
		);
		
		LocationRequest req = LocationRequest.create();
		req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		//Set this high as myLocation is requesting updates anyway and we should stay in sync
		req.setInterval(150000);
		req.setFastestInterval(1000);
		
		_lc.requestLocationUpdates(req,  tracker);
	}
	
	private void registerProximityAlert() {
		Geofence.Builder b = new Geofence.Builder();
		Geofence fence = b.setCircularRegion(
				_targetCameraPosition.target.latitude, _targetCameraPosition.target.longitude, ALARM_RADIUS_METERS)
		.setExpirationDuration(PROXIMITY_DURATION)
		.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
		.setRequestId("Dave")
		.build();
		
		final List<Geofence> fenceList = new ArrayList<Geofence>();
		fenceList.add(fence);

		Intent intent = new Intent(getApplicationContext(),AlarmActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
        final PendingIntent proximityIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		_lc.addGeofences(fenceList, proximityIntent, 
			new LocationClient.OnAddGeofencesResultListener() {
				@Override
				public void onAddGeofencesResult(int arg0,
						String[] arg1) {} 
			}
	);
	}
}
