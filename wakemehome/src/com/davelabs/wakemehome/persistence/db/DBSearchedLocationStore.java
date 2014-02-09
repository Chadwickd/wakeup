package com.davelabs.wakemehome.persistence.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.davelabs.wakemehome.SearchedLocation;
import com.davelabs.wakemehome.persistence.SearchedLocationStore;
import com.davelabs.wakemehome.persistence.db.DBSearchedLocationStoreContract.SearchedLocationContract;
import com.google.android.gms.maps.model.LatLng;

public class DBSearchedLocationStore implements SearchedLocationStore {

	private DBSearchedLocationStoreDBHelper _helper;
	
	public DBSearchedLocationStore(Context c) {
		_helper = new DBSearchedLocationStoreDBHelper(c);
	}
	
	private static final String[] COLUMNS = new String[]{ 
		SearchedLocationContract._ID,
		SearchedLocationContract.COLUMN_NAME_SEARCH_QUERY,
		SearchedLocationContract.COLUMN_NAME_LAT,
		SearchedLocationContract.COLUMN_NAME_LNG,
		SearchedLocationContract.COLUMN_NAME_IS_HOME,
		SearchedLocationContract.COLUMN_NAME_IS_PINNED,
		SearchedLocationContract.COLUMN_NAME_LAST_UPDATED
	};
	
	@Override
	public SearchedLocation getHomeLocation() {
		SQLiteDatabase db = _helper.getReadableDatabase();
		Cursor c = db.query(SearchedLocationContract.TABLE_NAME, COLUMNS, "IS_HOME = 1", null, null, null, null);
		c.moveToFirst();
		
		SearchedLocation location = null;
		if (c.getCount() > 0) {
			location = parseCurrentRow(c);
		}
		
		return location;
	}

	
	@Override
	public List<SearchedLocation> getPinnedLocations() {
		SQLiteDatabase db = _helper.getReadableDatabase();
		Cursor c = db.query(SearchedLocationContract.TABLE_NAME, COLUMNS, "IS_HOME = 0 AND IS_PINNED = 1", null, null, null, 
				SearchedLocationContract._ID + " DESC");
		
		return parseRows(c);
	}

	@Override
	public List<SearchedLocation> getRecentLocations() {
		SQLiteDatabase db = _helper.getReadableDatabase();
		Cursor c = db.query(SearchedLocationContract.TABLE_NAME, COLUMNS, "IS_HOME = 0 AND IS_PINNED = 0", null, null, null, 
				SearchedLocationContract.COLUMN_NAME_LAST_UPDATED + " DESC");
		
		return parseRows(c);
	}

	@Override
	public void saveLocation(SearchedLocation location) {
		SQLiteDatabase db = _helper.getWritableDatabase();
		ContentValues values = valuesFromLocation(location);
		
		String[] upperCaseSearch = {location.getSearchQuery().toUpperCase()};
		db.delete(SearchedLocationContract.TABLE_NAME, "IS_HOME = 0 AND upper(SEARCH_QUERY) = ?", upperCaseSearch);

		long newRowId = db.insert(SearchedLocationContract.TABLE_NAME, null, values);
		location.setId(newRowId);
	}

	@Override
	public void removeLocation(SearchedLocation location) {
		SQLiteDatabase db = _helper.getWritableDatabase();
		
		String selection = SearchedLocationContract._ID + " = ?";
		String[] selectionArgs = { String.valueOf(location.getId()) };
		
		db.delete(SearchedLocationContract.TABLE_NAME, selection, selectionArgs);
	}

	@Override
	public void pinLocation(SearchedLocation location) {
		location.pin();
		updateLocationRow(location);
	}

	@Override
	public void unpinLocation(SearchedLocation location) {
		location.unpin();
		updateLocationRow(location);
	}
	
	@Override
	public void setLocationAsHome(SearchedLocation location) {
		unsetHomeOnAllRows();
		location.setAsHome();
		updateLocationRow(location);
	}
	
	private int unsetHomeOnAllRows() {
		SQLiteDatabase db = _helper.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SearchedLocationContract.COLUMN_NAME_IS_HOME, "0");

		int count = db.update(SearchedLocationContract.TABLE_NAME, values, null, null);
		
		return count;
	}

	@Override
	public void clearHomeLocation() {
		SearchedLocation location = getHomeLocation();
		if (location != null) {
			location.unsetAsHome();
			updateLocationRow(location);
		};
	}
	
	@Override
	public void touchLocationDate(SearchedLocation location) {
		location.setLastUpdated(new Date());
		updateLocationRow(location);
	}

	private List<SearchedLocation> parseRows(Cursor c) {
		c.moveToFirst();
		List<SearchedLocation> l = new ArrayList<SearchedLocation>();
		
		for (int i = 0; i < c.getCount(); i++) {
			l.add(parseCurrentRow(c));
			c.moveToNext();
		}
		
		return l;
	}

	private SearchedLocation parseCurrentRow(Cursor c) {
		long id = c.getLong(c.getColumnIndex(SearchedLocationContract._ID));
		String searchQuery = c.getString(c.getColumnIndex(SearchedLocationContract.COLUMN_NAME_SEARCH_QUERY));
		double lat = c.getDouble(c.getColumnIndex(SearchedLocationContract.COLUMN_NAME_LAT));
		double lng = c.getDouble(c.getColumnIndex(SearchedLocationContract.COLUMN_NAME_LNG));
		boolean isHome = c.getInt(c.getColumnIndex(SearchedLocationContract.COLUMN_NAME_IS_HOME)) == 1 ? true : false;
		boolean isPinned = c.getInt(c.getColumnIndex(SearchedLocationContract.COLUMN_NAME_IS_PINNED)) == 1 ? true : false;
		Date lastUpdated = new Date (c.getLong(c.getColumnIndex(SearchedLocationContract.COLUMN_NAME_LAST_UPDATED)));
		
		return new SearchedLocation(id, searchQuery, new LatLng(lat, lng), isHome, isPinned,lastUpdated);
	}
	
	private int updateLocationRow(SearchedLocation location) {
		SQLiteDatabase db = _helper.getWritableDatabase();

		ContentValues values = valuesFromLocation(location);

		// Which row to update, based on the ID
		String selection = SearchedLocationContract._ID + " = ?";
		String[] selectionArgs = { String.valueOf(location.getId()) };

		int count = db.update(
		    SearchedLocationContract.TABLE_NAME,
		    values,
		    selection,
		    selectionArgs);
		
		return count;
	}
	
	private ContentValues valuesFromLocation(SearchedLocation location) {
		ContentValues values = new ContentValues();
		values.put(SearchedLocationContract.COLUMN_NAME_SEARCH_QUERY, location.getSearchQuery());
		values.put(SearchedLocationContract.COLUMN_NAME_LAT, location.getTarget().latitude);
		values.put(SearchedLocationContract.COLUMN_NAME_LNG, location.getTarget().longitude);
		values.put(SearchedLocationContract.COLUMN_NAME_IS_HOME, location.isHome());
		values.put(SearchedLocationContract.COLUMN_NAME_IS_PINNED, location.isPinned());
		values.put(SearchedLocationContract.COLUMN_NAME_LAST_UPDATED, location.getLastUpdated().getTime());
		
		return values;
	}

}
