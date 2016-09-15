package com.hfad.james;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.hfad.james.util.IabHelper;
import com.hfad.james.util.IabResult;
import com.hfad.james.util.Inventory;
import com.hfad.james.util.Purchase;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    @BindView(R.id.food_button)
    Button foodButton;
    @BindView(R.id.drinks_btn)
    Button drinkButton;
    @BindView(R.id.remove_ad_btn)
    Button removeAdButton;
    //    static final String ITEM_SKU = "android.test.purchased";
    static final String ITEM_SKU = "com.hfad.pay";
    private static final String KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtkOB4riYLOEka1C2sJ4fYs2Wd64UF4RS12yBOgszG47iJwBCRmXQ2vpHTvdm5iIP5ZibcTcGWxQG+QLqLQi3/D1FMvbLH/X5PAwsVlcy1p5vMvkTfwwiEBMcWghD+stxCdEUcf/NE6gpsi8xGFy4I/fyKqOKNrZm3Rimk053MqlMzZqaVjPBYEs6Wd52eUyTXsP7MamElxWnRXD1dT8iuNXEaGksO2NLJyasjGBWmDW+zypZerWEGpejNnBLiEHdE5PtoAtlWmAahu6JeCylUwazdO1Pa/tErxfz2GDsqyzi1sgVY1KpAfODdv7Vbw2GJ+XaZQh8Y5kYnUEg1GYJ5wIDAQAB";
    IabHelper helper;
    private static final String TAG = "Billing ";
    AdView adView;

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);

        MobileAds.initialize(getActivity().getApplicationContext(), "ca-app-pub-3940256099942544/63009781111713");

        adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("b9c49844a26fd47b")
                .addTestDevice("1fac1e94f67cda06")
                .build();
//        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (prefs.getBoolean("purchased", false)) {
            adView.setVisibility(View.VISIBLE);
            removeAdButton.setVisibility(View.VISIBLE);
        }

        foodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FoodActivity.class);
                startActivity(intent);
            }
        });

        drinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DrinkActivity.class);
                startActivity(intent);
            }
        });

        removeAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.launchPurchaseFlow(getActivity(), ITEM_SKU, 10001, purchaseFinishedListener, "purchaseToken");
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        helper = new IabHelper(getActivity(), KEY);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!helper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                errorToast();
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
                adView.setVisibility(View.VISIBLE);
                removeAdButton.setVisibility(View.VISIBLE);
            }
        }
    };

    private void consumeItem() {
        helper.queryInventoryAsync(receivedInventoryListener);
    }

    private void errorToast() {
        Toast toast = Toast.makeText(getActivity(), R.string.error_toast, Toast.LENGTH_LONG);
        toast.show();
    }

    IabHelper.QueryInventoryFinishedListener receivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inv) {
            if (result.isSuccess()) {
                helper.consumeAsync(inv.getPurchase(ITEM_SKU), consumeFinishedListener);
            } else {
                errorToast();
            }
        }
    };

    IabHelper.OnConsumeFinishedListener consumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        @Override
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                adView.setVisibility(View.GONE);
                removeAdButton.setVisibility(View.GONE);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("purchased", true);
                editor.apply();
            } else {
                errorToast();
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
