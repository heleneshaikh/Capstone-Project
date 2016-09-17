package com.hfad.james;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.hfad.james.model.TotalPriceEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heleneshaikh on 17/09/16.
 */
public class SimpleWidgetProvider extends AppWidgetProvider {
    RemoteViews remoteViews;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.simple_widget);
            //set image
            remoteViews.setImageViewResource(R.id.piggy_bank, R.drawable.piggy_bank);

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

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        EventBus.getDefault().register(this);
    }

    //set total price
    @Subscribe
    public void onPriceEvent(TotalPriceEvent event) {
        double price = event.totalPrice;
        remoteViews.setTextViewText(R.id.total_amount, String.valueOf(price));
    }

    @Override
    public void onDisabled(Context context) {
        EventBus.getDefault().unregister(this);
        super.onDisabled(context);
    }
}
