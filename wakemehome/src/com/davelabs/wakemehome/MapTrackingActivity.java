package com.davelabs.wakemehome;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;

public class MapTrackingActivity extends Activity {
	
	private GoogleMap _map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_tracking);
        MapFragment f = (MapFragment) this.getFragmentManager().findFragmentById(R.id.trackMap);
        _map = f.getMap();
        CameraPosition passedPosition = getPassedCameraPosition();
        
        CameraUpdate moveToPassedPosition = CameraUpdateFactory.newCameraPosition(passedPosition);
        moveMapToTargetPoint(moveToPassedPosition);
		
    }

	private void moveMapToTargetPoint(CameraUpdate targetPoint) {
		
		_map.animateCamera(targetPoint);
	}

	

	private CameraPosition getPassedCameraPosition() {
        Bundle extras = this.getIntent().getExtras();
        CameraPosition passedCameraPosition = (CameraPosition) extras.get("LocationCoords");
		return passedCameraPosition;
	}
	
	
}
