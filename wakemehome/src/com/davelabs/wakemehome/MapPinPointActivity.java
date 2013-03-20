package com.davelabs.wakemehome;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.davelabs.wakemehome.persistence.SearchedLocationStore;
import com.davelabs.wakemehome.persistence.SearchedLocationStoreFactory;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPinPointActivity extends Activity {

	private GoogleMap _map;
	
	private Dialog _searchQueryNotFoundDialog;
	private Dialog _searchQueryLookupFailedDialog;

	private Marker _pinPointMarker;

	private String _searchQuery;
	
	private int _defaultZoomLevel;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_pinpoint);
        setInstanceVariables();
        
        _searchQuery = getPassedSearchQuery();
        tryToLookupSearchQuery(_searchQuery);
    }

	private void setInstanceVariables() {        
        MapFragment f = (MapFragment) this.getFragmentManager().findFragmentById(R.id.searchMap);
        _map = f.getMap();
        
        Resources r = this.getResources();
		_defaultZoomLevel = r.getInteger(R.integer.zoomLevel);
	}
	
	private void tryToLookupSearchQuery(String searchQuery) {
		
		LocationSearcher.ISearchListener listener = new LocationSearcher.ISearchListener() {
			@Override
			public void onSearchError(Exception e) {
				searchQueryLookupFailed(e);
			}
			
			@Override
			public void onSearchComplete(LatLng result) {
				searchQueryLookupComplete(result);
			}
		};
		
		LocationSearcher searcher = new LocationSearcher(listener);
		showOverlay();
		searcher.startSearch(new GeocoderSearchProvider(this), searchQuery);
	}
	
	private void searchQueryLookupFailed(Exception e) {
		hideOverlay();
		if (e instanceof IOException) {
			getSearchQueryLookupFailedDialog().show();
		} else {
			//this should never be hit
			Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show();
		}
	}
	
	private void searchQueryLookupComplete(LatLng result) {
		hideOverlay();
		if (result != null) {
			moveMapToTargetPoint(result);
			showPinPointMarker(result);
			showConfirmDestinationButton();
		} else {
			Dialog d = getSearchQueryNotFoundDialog();
			d.show();
		}
	}

	private void showPinPointMarker(LatLng result) {
		Marker m = createPinPointMarker(result);
		m.setVisible(true);
	}

	private void hideOverlay() {
		View v = this.findViewById(R.id.searchProgressOverlay);
		v.setVisibility(View.INVISIBLE);
	}
	
	private void showOverlay() {
		View v = this.findViewById(R.id.searchProgressOverlay);
		v.setVisibility(View.VISIBLE);
	}

	private void showConfirmDestinationButton() {
		View b = this.findViewById(R.id.confirmDestinationButton);
		b.setVisibility(View.VISIBLE);
	}

	private void moveMapToTargetPoint(LatLng targetPoint) {
		
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(targetPoint, _defaultZoomLevel);
		_map.animateCamera(cameraUpdate);
	}

	private String getPassedSearchQuery() {
        Bundle extras = this.getIntent().getExtras();
		String passedSearchType = extras.getString("searchQuery");
		return passedSearchType;
	}
	
	public void onLocationOkButtonClicked(View v)  {
    	Intent intent = new Intent(this, MapTrackingActivity.class);
    	CameraPosition.Builder b = CameraPosition.builder(_map.getCameraPosition());
    	b.target(_pinPointMarker.getPosition());
    	
    	storeLocationAsHome(_pinPointMarker);
    	
    	intent.putExtra("CameraPosition", b.build());
    	this.startActivity(intent);
    }
	
	private void storeLocationAsHome(Marker m) {
		SearchedLocationStore store = SearchedLocationStoreFactory.getStore(this);
		SearchedLocation location = new SearchedLocation(_searchQuery, m.getPosition());
		store.saveLocation(location);
	}

	public Dialog getSearchQueryNotFoundDialog() {
		if (_searchQueryNotFoundDialog == null) {
			final Activity a = this;
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Unable to find the address, please try another search query.");
			builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					a.finish();
				}
			});
			
			_searchQueryNotFoundDialog = builder.create();
		}
		
		return _searchQueryNotFoundDialog;
	}

	private Dialog getSearchQueryLookupFailedDialog() {
	
		if (_searchQueryLookupFailedDialog == null)
		{
			final Activity a = this;
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("No internet available");
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					a.finish();
				}
			});
			
			builder.setPositiveButton("Retry now", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					tryToLookupSearchQuery(_searchQuery);
				}
			});
			
			_searchQueryLookupFailedDialog = builder.create();
		}
		
		return _searchQueryLookupFailedDialog;
	}
	
	private Marker createPinPointMarker(LatLng position) {
		
		if (_pinPointMarker == null) {
			BitmapDescriptor marker = BitmapDescriptorFactory.defaultMarker();
			_pinPointMarker = _map.addMarker(new MarkerOptions()
		      .icon(marker)
		      .position(position)
		      .draggable(true)
		    );
		}

		return _pinPointMarker;
	}
}
