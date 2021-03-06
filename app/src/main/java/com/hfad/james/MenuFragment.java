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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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
    boolean isConsumption;
    boolean purchased;

    public MenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            purchased = savedInstanceState.getBoolean("purchase");
            if (purchased) {
                container.removeAllViews();
            }
        }

        EventBus.getDefault().register(this);
        if (!purchased) {
            createRequest();
        } else {
            removeAdButton.setVisibility(View.INVISIBLE);
            container.removeAllViews();
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
                ((DrawerActivity) getActivity()).launchPurchase();
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

 /*  testing:  AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("b9c49844a26fd47b") //my id
                .addTestDevice("F88D920791452B0C2A6BA68A4A060E9F")
                .build();
                 adView.loadAd(adRequest);
*/
    }

    @Subscribe
    public void onIABEvent(IABEvent event) {
        isConsumption = event.isConsumptionOK();
        if (isConsumption) {
            container.removeAllViews();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("consumption", true);
            editor.apply();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        boolean isConsumptionOK = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("consumption", false);
        outState.putBoolean("purchase", isConsumptionOK);
        Log.v("test1", String.valueOf(isConsumptionOK));
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
