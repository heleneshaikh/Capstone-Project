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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.hfad.james.adapters.OrderAdapter;
import com.hfad.james.model.TotalPriceEvent;
import com.hfad.james.util.IabHelper;
import com.hfad.james.util.IabResult;
import com.hfad.james.util.Inventory;
import com.hfad.james.util.Purchase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {

    @BindView(R.id.payment_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.total_amount_tv)
    TextView totalPrice;
    @BindView(R.id.mark_paid_btn)
    Button paidButton;
    @BindView(R.id.app_payment_btn)
    Button appPayment;
    @BindView(R.id.resto_list_btn)
    Button goToComments;
    private static final String TAG = "Billing setup";
    IabHelper helper;
    //    static final String ITEM_SKU = "android.test.purchased";
    static final String ITEM_SKU = "com.hfad.payment";
    double price;
    public static final String PRICE = "price";

    public PaymentFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        helper = new IabHelper(getActivity(), getString(R.string.my_api_key));
        helper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    Log.v(TAG, "setup OK");
                } else {
                    Log.v(TAG, "setup NOT OK");
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout relativeLayout = (LinearLayout) inflater.inflate(R.layout.fragment_payment, container, false);
        ButterKnife.bind(this, relativeLayout);

        Firebase ref = new Firebase("https://james-5d3ae.firebaseio.com/");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        OrderAdapter adapter = new OrderAdapter(ref, getActivity());
        recyclerView.setAdapter(adapter);

        paidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getActivity(), R.string.see_you_toast, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        appPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001, purchaseFinishedListener, "purchaseToken");
            }
        });

        goToComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TodosOverviewActivity.class);
                startActivity(intent);
            }
        });
        return relativeLayout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!helper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                purchaseFailed();
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
            }
        }
    };

    private void consumeItem() {
        helper.queryInventoryAsync(receivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener receivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
            if (result.isSuccess()) {
                helper.consumeAsync(inv.getPurchase(ITEM_SKU), consumeFinishedListener);
            } else {
                purchaseFailed();
            }
        }
    };

    IabHelper.OnConsumeFinishedListener consumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                Toast toast = Toast.makeText(getActivity(), R.string.thanks_toast, Toast.LENGTH_LONG);
                toast.show();
            } else {
               purchaseFailed();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (helper != null) {
            helper.dispose();
            helper = null;
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onPriceEvent(TotalPriceEvent event) {
        price = event.totalPrice;
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

    void purchaseFailed() {
        Toast toast = Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_LONG);
        toast.show();
    }
}
