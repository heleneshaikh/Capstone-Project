package com.hfad.james;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
    @BindView(R.id.ads_container)
    RelativeLayout container;
    @BindView(R.id.adView)
    AdView adView;
    //    static final String ITEM_SKU = "com.hfad.ads";
    static final String ITEM_SKU = "android.test.purchased";
    IabHelper helper;
    private static final String TAG = "Billing ";
    private static final String STR = "String ";
    private static final String CONSUMPTION = "consumption";

    public MenuFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        helper = new IabHelper(getActivity(), getString(R.string.my_api_key));
//        helper.enableDebugLogging(true);
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
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, relativeLayout);
        Log.v(STR, getString(R.string.my_api_key));

        String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.v("AndroidId", "Android ID : " + android_id);

        MobileAds.initialize(getActivity().getApplicationContext(), getString(R.string.my_admob_key));

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("b9c49844a26fd47b") //my id
//                .addTestDevice("F88D920791452B0C2A6BA68A4A060E9F")
//                .build();
//        adView.loadAd(adRequest);

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
                helper.launchPurchaseFlow(getActivity(), ITEM_SKU, 1001, purchaseFinishedListener, "purchaseToken");
            }
        });
        return relativeLayout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("onActivityFragment", String.valueOf(resultCode));
        if (!helper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    IabHelper.OnIabPurchaseFinishedListener purchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.v("onIabPurchase", result.toString());
            if (result.isFailure()) {
                errorToast();
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
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
            Log.v("QueryInventory", result.toString());
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
                Log.v(CONSUMPTION, "consumption ok" + " purchase" + purchase + "result " + result);
                container.removeAllViews();
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


    /*    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean b = prefs.getBoolean("purchased", false);
        if (b) {
            container.removeAllViews();
       }
         SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
               SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("purchased", true);
                editor.apply();
 */

}
