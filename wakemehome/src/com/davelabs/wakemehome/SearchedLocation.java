package com.davelabs.wakemehome;

import com.google.android.gms.maps.model.LatLng;

public interface SearchedLocation {
	public LatLng target();
	public boolean isHome();
	public boolean isFavourite();
}
