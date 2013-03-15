package com.davelabs.wakemehome.camera;

import com.google.android.gms.maps.CameraUpdate;

public abstract class CameraDirector {
	
	public interface CameraUpdateListener {
		public void onCameraUpdate(CameraUpdate update, UpdateCompleteListener listener);
	}
	
	public interface UpdateCompleteListener {
		public void onUpdateComplete();
	}

	private final CameraUpdateListener _listener;
	
	public CameraDirector(CameraUpdateListener listener) {
		_listener = listener;
	}
	
	protected void transmitUpdate(CameraUpdate update) {
		_listener.onCameraUpdate(update, new UpdateCompleteListener() {
			@Override
			public void onUpdateComplete() {
				getNextDirection();
			}
		});
	}
	
	public void startDirecting() {
		initialize();
		getNextDirection();
	}
	
	public void stopDirecting() {
		
	}
	
	protected abstract void initialize();
	protected abstract void getNextDirection();
}
