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
    private static final String STR = "String ";

    //    static final String ITEM_SKU = "com.hfad.ads";

    public MenuFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);

        createRequest();

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
                ((MenuActivity) getActivity()).launchPurchase();
            }
        });
        return view;
    }

    private void createRequest() {
        Log.v(STR, getString(R.string.my_api_key));

        String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.v("AndroidId", "Android ID : " + android_id);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("b9c49844a26fd47b") //my id
//                .addTestDevice("F88D920791452B0C2A6BA68A4A060E9F")
//                .build();
//        adView.loadAd(adRequest);
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
