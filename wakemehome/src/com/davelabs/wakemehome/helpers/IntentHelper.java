package com.davelabs.wakemehome.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.davelabs.wakemehome.MapTrackingActivity;
import com.davelabs.wakemehome.R;
import com.davelabs.wakemehome.SearchedLocation;
import com.davelabs.wakemehome.persistence.SearchedLocationStore;
import com.google.android.gms.maps.model.CameraPosition;

public class IntentHelper {
	
	public SearchedLocationStore _store;
	
	public static Intent TrackingIntentFromLocation(Context context,SearchedLocation location ) {
  		CameraPosition.Builder builder = CameraPosition.builder();
  		builder.target(location.getTarget());
  		Resources r = context.getResources();
  		int defaultZoom = r.getInteger(R.integer.zoomLevel);
  		builder.zoom(defaultZoom);
  		CameraPosition position = builder.build();
  		
  		Intent intent = new Intent(context, MapTrackingActivity.class);
  		intent.putExtra("CameraPosition", position);
		return intent;
	};
	
}