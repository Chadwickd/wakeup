package com.davelabs.wakemehome.persistence;

import android.content.Context;

public class SearchedLocationStoreFactory {

	public static SearchedLocationStore getStore(Context c) {
		return new PreferencesFileSearchedLocationStore(c);
	}
}