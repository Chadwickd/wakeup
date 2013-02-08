package com.davelabs.wakemehome;

import java.util.List;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
        
        getCurrentLocation();
    }

	private LatLngBounds getCameraBounds() {
		LatLngBounds.Builder builder = LatLngBounds.builder();
		builder.include(_passedPosition);
		builder.include(_currentLocation);
		return builder.build();
		
	}

	private void getCurrentLocation() {
		LocationManager lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);
		for (String provider : providers) {
			lm.requestSingleUpdate(provider, new LocationListener() {
				@Override
				public void onLocationChanged(Location loc) {
					onLocationFound(loc);
				}
				
				@Override
				public void onProviderDisabled(String arg0) {}
				@Override
				public void onProviderEnabled(String arg0) {}
				@Override
				public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
				
			}, this.getMainLooper());
		}
	}

	protected void onLocationFound(Location currentLocation) {
		if (_currentLocation == null) {
			_currentLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
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
