package com.davelabs.wakemehome.camera;

import com.google.android.gms.maps.CameraUpdate;

public abstract class CameraDirector {
	
	public interface CameraUpdateListener {
		public void onCameraUpdate(CameraUpdate update);
	}

	private final CameraUpdateListener _listener;
	protected boolean _readyForNextDirection = true;
	
	public CameraDirector(CameraUpdateListener listener) {
		_listener = listener;
	}
	
	protected void transmitUpdate(CameraUpdate update) {
		_readyForNextDirection = false;
		_listener.onCameraUpdate(update);
	}
	
	public void lastUpdateComplete() {
		_readyForNextDirection = true;
	}
	
	public abstract void startDirecting();
	public abstract void stopDirecting(); 
}
