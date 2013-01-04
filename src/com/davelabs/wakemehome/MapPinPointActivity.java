package com.davelabs.wakemehome;

import android.os.Bundle;
import com.google.android.maps.MapActivity;

public class MapPinPointActivity extends MapActivity {

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_pinpoint);
    }
}
