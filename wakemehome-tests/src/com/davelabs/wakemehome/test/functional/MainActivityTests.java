package com.davelabs.wakemehome.test.functional;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.davelabs.wakemehome.MainActivity;
import com.davelabs.wakemehome.test.helpers.WifiHelper;

public class MainActivityTests extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity _a;
	private EditText _queryInputBox;
	private Button _searchButton;
	private Instrumentation _ins;
	private WifiHelper _wirelessControl;
	
	public MainActivityTests() {
		super(MainActivity.class);
	}
	
	public void setUp()
	{
		_a = getActivity();
		_wirelessControl = new WifiHelper(_a);
		_wirelessControl.setWifiOn();
		while (!_wirelessControl.isWiFiConnected());
		_queryInputBox = (EditText) _a.findViewById(com.davelabs.wakemehome.R.id.searchLocationInput);
		_searchButton = (Button) _a.findViewById(com.davelabs.wakemehome.R.id.performSearchButton);
		_ins = getInstrumentation();
	}
	
	public void testSearchButtonStartsMapSearch()
	{
		ActivityMonitor monitor = _ins.addMonitor(
				com.davelabs.wakemehome.MapPinPointActivity.class.getName(),
				new ActivityResult(Activity.RESULT_OK, null), true);

		assertEquals(monitor.getHits(), 0);
		
		TouchUtils.clickView(this, _searchButton);
		
		assertEquals(monitor.getHits(), 1);
		
		_ins.removeMonitor(monitor);
	}
	
	public void testSearchActionSendsSearchQuery() {
		ActivityMonitor monitor = _ins.addMonitor(
				com.davelabs.wakemehome.MapPinPointActivity.class.getName(),
				null, false);
		
		String testString = "EC1N 8NX";
		TouchUtils.clickView(this, _queryInputBox);
		_ins.sendStringSync(testString);
		TouchUtils.clickView(this, _searchButton);
		
		Activity activity = _ins.waitForMonitorWithTimeout(monitor, 10000);
		assertNotNull(activity);
		
		Intent launchIntent = activity.getIntent();
		Bundle bundle = launchIntent.getExtras();
		
		assertEquals(testString, bundle.getString("searchQuery"));
		
		activity.finish();
	}
	
	public void testNoInternetLaunchesPopup() {	
		_wirelessControl.setWifiOff();
		
		TouchUtils.clickView(this, _searchButton);
		AlertDialog popup = _a.getPopup();
		assertTrue(popup.isShowing());
		
		ActivityMonitor monitor = _ins.addMonitor(
				new IntentFilter(Settings.ACTION_SETTINGS),
				new ActivityResult(Activity.RESULT_OK, null), true);
		assertEquals(monitor.getHits(), 0);
		
		View positiveButton = popup.getButton(AlertDialog.BUTTON_POSITIVE);
		TouchUtils.clickView(this, positiveButton);
		assertEquals(monitor.getHits(), 1);
		
		_ins.removeMonitor(monitor);
		_wirelessControl.setWifiOn();
	}
}
