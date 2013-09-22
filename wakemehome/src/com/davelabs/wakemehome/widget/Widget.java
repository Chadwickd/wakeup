package com.davelabs.wakemehome.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.RemoteViews;

import com.davelabs.wakemehome.MapTrackingActivity;
import com.davelabs.wakemehome.R;
import com.davelabs.wakemehome.SearchedLocation;
import com.davelabs.wakemehome.persistence.SearchedLocationStore;
import com.davelabs.wakemehome.persistence.SearchedLocationStoreFactory;
import com.google.android.gms.maps.model.CameraPosition;

public class Widget extends AppWidgetProvider {
	
	private SearchedLocationStore _store;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            
            _store = SearchedLocationStoreFactory.getStore(context);

            // Create an Intent to launch ExampleActivity
            SearchedLocation home = _store.getHomeLocation();
    		CameraPosition.Builder builder = CameraPosition.builder();
    		builder.target(home.getTarget());
    		Resources r = context.getResources();
    		int defaultZoom = r.getInteger(R.integer.zoomLevel);
    		builder.zoom(defaultZoom);
    		CameraPosition position = builder.build();
    		
    		Intent intent = new Intent(context, MapTrackingActivity.class);
    		intent.putExtra("CameraPosition", position);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}