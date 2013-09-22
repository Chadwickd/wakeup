package com.davelabs.wakemehome.persistence;

import com.davelabs.wakemehome.persistence.db.DBSearchedLocationStore;

import android.content.Context;

public class SearchedLocationStoreFactory {

	public static SearchedLocationStore getStore(Context c) {
		return new DBSearchedLocationStore(c);
	}
}