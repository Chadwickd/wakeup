package com.davelabs.wakemehome.test.functional;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;

import com.davelabs.wakemehome.MainActivity;
import com.davelabs.wakemehome.test.TestHelperService;
import com.davelabs.wakemehome.test.helpers.WiFiHelper;

public class MainActivityTests extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity _a;
	private EditText _queryInputBox;
	private Button _searchButton;
	private Instrumentation _ins;
	private WiFiHelper _wirelessControl;
	
	public MainActivityTests() {
		super(MainActivity.class);
	}
	
	public void setUp()
	{
		_a = getActivity();
		_a.startService(new Intent(_a, com.davelabs.wakemehome.test.TestHelperService.class));
		_wirelessControl = new WiFiHelper(TestHelperService.getContext());
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
	
	public void testSearchActionSendsSearchQuery()
	{
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
	
	public void testNoInternetLaunchesPopup()
	{	
		_wirelessControl.setWiFiOff();
		TouchUtils.clickView(this, _searchButton);
		AlertDialog popup = _a.getPopup();
		assert(popup.isShowing());
		_wirelessControl.setWiFiOn();
	}
	
	
	
}
