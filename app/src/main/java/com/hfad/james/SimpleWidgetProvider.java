package com.hfad.james;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import com.hfad.james.model.TotalPriceEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


/**
 * Created by heleneshaikh on 17/09/16.
 */
public class SimpleWidgetProvider extends AppWidgetProvider {
    RemoteViews remoteViews;
    public static final String SET_TOTAL = "com.hfad.james.SET_TOTAL";
    public static final String PRICE = "price";


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        SharedPreferences sharedPref = context.getSharedPreferences(PRICE, Context.MODE_PRIVATE);
        String price = sharedPref.getString(PRICE,"0.0");

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.simple_widget);

            //TOTALPRICE
            remoteViews.setTextViewText(R.id.total_amount, price);

            //set image
            remoteViews.setImageViewResource(R.id.piggy_bank, R.drawable.piggy_bank);

            Intent intent = new Intent(context, SimpleWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.refresh_btn, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

}
