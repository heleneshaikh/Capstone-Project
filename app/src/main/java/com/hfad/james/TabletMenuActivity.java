package com.hfad.james;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;

public class TabletMenuActivity extends AppCompatActivity {
//    @BindView(R.id.include)
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablet_menu);

//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setTitle(R.string.app_name);
//
//        }
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
