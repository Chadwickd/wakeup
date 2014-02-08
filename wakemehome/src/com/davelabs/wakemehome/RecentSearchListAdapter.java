package com.davelabs.wakemehome;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.davelabs.wakemehome.helpers.IntentHelper;
import com.davelabs.wakemehome.persistence.SearchedLocationStore;
import com.davelabs.wakemehome.persistence.SearchedLocationStoreFactory;

public class RecentSearchListAdapter implements ListAdapter {

	private SearchedLocationStore _store;
	
	public RecentSearchListAdapter(Context c) {
		_store = SearchedLocationStoreFactory.getStore(c);
	}
	
	@Override
	public int getCount() {
		return _store.getRecentLocations().size();
	}

	@Override
	public Object getItem(int i) {
		return _store.getRecentLocations().get(i);
	}

	@Override
	public long getItemId(int i) {
		return _store.getRecentLocations().get(i).getId();
	}

	@Override
	public int getItemViewType(int i) {
		return 0;
	}

	@Override
	public View getView(int i, View old, final ViewGroup parent) {
		final Context context = parent.getContext();
		final SearchedLocation l = _store.getRecentLocations().get(i);
		
		TextView v = new TextView(context);
		v.setText(l.getSearchQuery());
		v.setTextAppearance(context, R.style.StandardText);
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = IntentHelper.TrackingIntentFromLocation(context, l);
				context.startActivity(intent);
			}
		});
		return v;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return _store.getRecentLocations().size() == 0;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

}
