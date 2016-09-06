package com.hfad.james;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TabletMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablet_menu);

        createFragment(new DrinkFragment(), R.id.drink_frame);
        createFragment(new FoodFragment(), R.id.food_frame);

    }

    public void createFragment (Fragment fragment, int id) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.replace(id, fragment);
        transaction.commit();
    }
}
