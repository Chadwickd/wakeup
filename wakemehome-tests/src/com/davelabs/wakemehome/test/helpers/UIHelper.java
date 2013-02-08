package com.davelabs.wakemehome.test.helpers;

import junit.framework.Assert;
import android.app.Dialog;
import android.view.View;

public class UIHelper {

	public static void assertViewHides(View v, long timeout) {
		long startTime = System.currentTimeMillis();
		long waitFor = timeout * 1000;
		
		while (v.isShown() && (System.currentTimeMillis() < startTime + waitFor));
		
		Assert.assertFalse("View did not hide before the given timeout", v.isShown());
			
		return;
	}

	public static void assertViewShows(View v, long timeout) {
		long startTime = System.currentTimeMillis();
		long waitFor = timeout * 1000;
		
		while ((v == null || !v.isShown()) && (System.currentTimeMillis() < startTime + waitFor));
		
		Assert.assertTrue("View did not display before the given timeout", v.isShown());
			
		return;
	}
	
	public static void assertDialogHides(Dialog d, long timeout) {
		long startTime = System.currentTimeMillis();
		long waitFor = timeout * 1000;
		
		while (d.isShowing() && (System.currentTimeMillis() < startTime + waitFor));
		
		Assert.assertFalse("Dialog did not hide before the given timeout", d.isShowing());
			
		return;
	}
	
	public static void assertDialogShows(Dialog d, long timeout) {
		long startTime = System.currentTimeMillis();
		long waitFor = timeout * 1000;
		
		while ((d == null || !d.isShowing()) && (System.currentTimeMillis() < startTime + waitFor));
		
		Assert.assertTrue("Dialog did not show before the given timeout", d.isShowing());
			
		return;
	}
	
}
