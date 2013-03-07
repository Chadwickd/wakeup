package com.davelabs.wakemehome.camera;

import com.google.android.gms.maps.CameraUpdate;

public abstract class CameraDirector {
	
	public interface CameraUpdateListener {
		public void onCameraUpdate(CameraUpdate update);
	}

	private final CameraUpdateListener _listener;
	
	public CameraDirector(CameraUpdateListener listener) {
		_listener = listener;
	}
	
	protected void transmitUpdate(CameraUpdate update) {
		_listener.onCameraUpdate(update);
	}
	
	public abstract void startDirecting();
}
