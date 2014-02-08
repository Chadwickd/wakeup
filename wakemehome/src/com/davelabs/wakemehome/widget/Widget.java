package com.davelabs.wakemehome.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.davelabs.wakemehome.R;
import com.davelabs.wakemehome.SearchedLocation;
import com.davelabs.wakemehome.helpers.IntentHelper;
import com.davelabs.wakemehome.persistence.SearchedLocationStore;
import com.davelabs.wakemehome.persistence.SearchedLocationStoreFactory;

public class Widget extends AppWidgetProvider {
	
	private SearchedLocationStore _store;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            
            _store = SearchedLocationStoreFactory.getStore(context);

            SearchedLocation home = _store.getHomeLocation();
    		Intent intent = IntentHelper.TrackingIntentFromLocation(context, home);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}