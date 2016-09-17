package com.hfad.james;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
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
    double price;
    public static final String PRICE = "price";

    //TOTAL PRICE
    @Override
    public void onReceive(Context context, Intent intent) {
        if (SET_TOTAL.equals(intent.getAction())) {
            price = intent.getDoubleExtra(PRICE, 0);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.simple_widget);
            //set image
            remoteViews.setImageViewResource(R.id.piggy_bank, R.drawable.piggy_bank);

            //TOTALPRICE
            remoteViews.setTextViewText(R.id.total_amount, String.valueOf(price));

            Intent intent = new Intent(context, SimpleWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //set refresh button
            remoteViews.setOnClickPendingIntent(R.id.refresh_btn, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

//    @Override
//    public void onEnabled(Context context) {
//        EventBus.getDefault().register(this);
//    }
//
//    //set total price
//    @Subscribe
//    public void onPriceEvent(TotalPriceEvent event) {
//       price = event.totalPrice;
////        remoteViews.setTextViewText(R.id.total_amount, String.valueOf(price));
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        EventBus.getDefault().unregister(this);
//    }
}
