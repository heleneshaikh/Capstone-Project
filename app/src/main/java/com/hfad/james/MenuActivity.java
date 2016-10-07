package com.hfad.james;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
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


public class MenuActivity extends AppCompatActivity {
    @BindView(R.id.include)
    Toolbar toolbar;
    IabHelper helper;
    RelativeLayout container;
    static final String ITEM_SKU = "android.test.purchased";
    private static final String TAG = "Billing ";
    private static final String CONSUMPTION = "consumption";

    @Override
    public void onStart() {
        super.onStart();
        helper = new IabHelper(this, getString(R.string.my_api_key));
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.app_name);
        }
        replaceFragment();
        MobileAds.initialize(this.getApplicationContext(), getString(R.string.my_admob_key));
    }

    public void launchPurchase() {
        helper.launchPurchaseFlow(this, ITEM_SKU, 10001, purchaseFinishedListener, "purchaseToken");
    }

    private void replaceFragment() {
        Fragment menuFragment = new MenuFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.replace(R.id.menu_container, menuFragment);
        transaction.commit();
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
        Toast toast = Toast.makeText(this, R.string.error_toast, Toast.LENGTH_LONG);
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
//                container.removeAllViews();
                Toast.makeText(MenuActivity.this, "OnConsumeFinishedListener ok",Toast.LENGTH_LONG ).show();

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
