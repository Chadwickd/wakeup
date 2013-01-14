package com.davelabs.wakemehome.test;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Instrumentation.ActivityMonitor;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;

import com.davelabs.wakemehome.MainActivity;

public class MainActivityTests extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity _a;
	private EditText _queryInputBox;
	private Button _searchButton;
	private Instrumentation _ins;
	
	public MainActivityTests() {
		super(MainActivity.class);
	}
	
	public void setUp()
	{
		_a = getActivity();
		_queryInputBox = (EditText) _a.findViewById(com.davelabs.wakemehome.R.id.searchLocationInput);
		_searchButton = (Button) _a.findViewById(com.davelabs.wakemehome.R.id.performSearchButton);
		_ins = getInstrumentation();
	}
	
	public void testSearchButtonStartsMapSearch()
	{
		ActivityMonitor monitor = _ins.addMonitor(
				com.davelabs.wakemehome.MapPinPointActivity.class.getName(),
				null, true);
		
		String testString = "TestSearchQuery";
		TouchUtils.clickView(this, _queryInputBox);
		_ins.sendStringSync(testString);
		TouchUtils.clickView(this, _searchButton);
		
		Activity a = _ins.waitForMonitorWithTimeout(monitor, 5000);
		assertNotNull(a);
		
		Intent i = a.getIntent();
		Bundle b = i.getExtras();
		
		assertEquals(testString, b.get("searchQuery"));
	}
}
