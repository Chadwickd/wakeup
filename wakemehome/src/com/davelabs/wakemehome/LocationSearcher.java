package com.davelabs.wakemehome;

import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;

public class LocationSearcher implements Runnable {
	
	public interface ISearchListener {
		public void onSearchComplete(LatLng result);
		public void onSearchError(Exception e);
	}
	
	private String _query;
	private ISearchProvider _provider;
	private ISearchListener _listener;
	
	private Handler _handler;
	
	public LocationSearcher(ISearchListener listener) {
		_listener = listener;
		_handler = new Handler();
	}

	@Override
	public void run() {
		try {
			final LatLng result = _provider.getSearchResult(_query);
			_handler.post(new Runnable() {
				@Override
				public void run() {
					_listener.onSearchComplete(result);
				}
			});
		} catch (final Exception e) {
			_handler.post(new Runnable() {
				@Override
				public void run() {
					_listener.onSearchError(e);
				}
			});
		}
	}

	public void startSearch(ISearchProvider provider, String searchQuery) {
		_query = searchQuery;
		_provider = provider;
		
		Thread searchThread = new Thread(this);
		
		searchThread.start();
	}
}
