package com.hfad.james;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.hfad.james.adapters.OrderAdapter;
import com.hfad.james.model.Items;
import com.hfad.james.model.TotalPriceEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCartFragment extends Fragment {
    @BindView(R.id.order_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.total_amount_tv)
    TextView totalPrice;
    @BindView(R.id.order_button)
    Button orderButton;
    @BindView(R.id.payment_btn)
    Button paymentButton;
    public static final String PRICE = "price";

    public ShoppingCartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        ButterKnife.bind(this, view);
        Firebase ref = new Firebase("https://james-5d3ae.firebaseio.com/");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        OrderAdapter adapter = new OrderAdapter(ref, getActivity());
        recyclerView.setAdapter(adapter);

        Items item = new Items();
        totalPrice.setText(getString(R.string.set_price, (double)item.getTotalSum()));

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getActivity(), R.string.order_sent_toast, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onPriceEvent(TotalPriceEvent event) {
        double price = event.totalPrice;
        totalPrice.setText(getString(R.string.set_price,price));

        SharedPreferences sharedPref = getActivity().getSharedPreferences(PRICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PRICE, String.valueOf(price));
        editor.commit();


        ComponentName name = new ComponentName(getActivity(), SimpleWidgetProvider.class);
        int[] ids = AppWidgetManager.getInstance(getActivity()).getAppWidgetIds(name);

        Intent widgetNotify = new Intent(getActivity(), SimpleWidgetProvider.class);
        widgetNotify.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        widgetNotify.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        getActivity().sendBroadcast(widgetNotify);
    }
}
