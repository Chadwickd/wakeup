package com.davelabs.wakemehome.test.helpers;

import junit.framework.Assert;
import android.view.View;

public class ViewHelper {

	public static void WaitForViewToHide(View v, long timeout) {
		long startTime = System.currentTimeMillis();
		long waitFor = timeout * 1000;
		
		while (v.isShown() && (System.currentTimeMillis() < startTime + waitFor));
		
		Assert.assertFalse("View did not hide before the given timeout", v.isShown());
			
		return;
	}
}
