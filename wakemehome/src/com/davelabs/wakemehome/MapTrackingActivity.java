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
	private LocationClient _lc;


	
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
		Geofence.Builder b = new Geofence.Builder();
		Geofence fence = b.setCircularRegion(_targetCameraLocation.latitude,_targetCameraLocation.longitude, 2000)
		.setExpirationDuration(Geofence.NEVER_EXPIRE)
		.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
		.setRequestId("Dave")
		.build();
		final List<Geofence> fenceList = new ArrayList<Geofence>();
		fenceList.add(fence);
		Intent intent = new Intent(getApplicationContext(),AlarmActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        final PendingIntent proximityIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		
		_lc = new LocationClient(this, 
				new GooglePlayServicesClient.ConnectionCallbacks() {

					@Override
					public void onConnected(Bundle arg0) {
						addFence(fenceList, proximityIntent);
					}					
					@Override
					public void onDisconnected() {}
			
		},
				new GooglePlayServicesClient.OnConnectionFailedListener() {

					@Override
					public void onConnectionFailed(ConnectionResult arg0) {}
			
		}
		);
		_lc.connect();
	}

	private void addFence(final List<Geofence> fenceList,
		final PendingIntent proximityIntent) {
		_lc.addGeofences(fenceList, proximityIntent, 
			new LocationClient.OnAddGeofencesResultListener() {
				@Override
				public void onAddGeofencesResult(int arg0,
						String[] arg1) {} 
			}
	);
	}
}
