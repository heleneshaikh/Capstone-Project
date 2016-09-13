package com.hfad.james;


import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.hfad.james.adapters.OrderAdapter;
import com.hfad.james.util.IabHelper;
import com.hfad.james.util.IabResult;
import com.hfad.james.util.Inventory;
import com.hfad.james.util.Purchase;

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
//    private static final String TAG = "Billing setup";
    private static final String KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtkOB4riYLOEka1C2sJ4fYs2Wd64UF4RS12yBOgszG47iJwBCRmXQ2vpHTvdm5iIP5ZibcTcGWxQG+QLqLQi3/D1FMvbLH/X5PAwsVlcy1p5vMvkTfwwiEBMcWghD+stxCdEUcf/NE6gpsi8xGFy4I/fyKqOKNrZm3Rimk053MqlMzZqaVjPBYEs6Wd52eUyTXsP7MamElxWnRXD1dT8iuNXEaGksO2NLJyasjGBWmDW+zypZerWEGpejNnBLiEHdE5PtoAtlWmAahu6JeCylUwazdO1Pa/tErxfz2GDsqyzi1sgVY1KpAfODdv7Vbw2GJ+XaZQh8Y5kYnUEg1GYJ5wIDAQAB";
    IabHelper helper;
//    static final String ITEM_SKU = "android.test.purchased";
    static final String ITEM_SKU = "com.hfad.pay";

    public PaymentFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        helper = new IabHelper(getActivity(), KEY);
        helper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
//                    Log.v(TAG, "setup OK");
                } else {
//                    Log.v(TAG, "setup NOT OK");
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

        OrderAdapter adapter = new OrderAdapter(ref);
        recyclerView.setAdapter(adapter);

        paidButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getActivity(), "See you soon!", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        appPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001, purchaseFinishedListener, "purchaseToken" );
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
               //handle
           } else if(purchase.getSku().equals(ITEM_SKU)) {
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
                //handle
            }
        }
    };

    IabHelper.OnConsumeFinishedListener consumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                Toast toast = Toast.makeText(getActivity(), "Thanks for coming!", Toast.LENGTH_LONG);
                toast.show();
            } else {
                //handle
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
    }
}
