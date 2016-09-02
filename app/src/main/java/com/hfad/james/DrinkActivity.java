package com.hfad.james;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrinkActivity extends AppCompatActivity {
    @BindView(R.id.include)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        ButterKnife.bind(this);

        toolbar.setTitle(R.string.toolbar_drinks);

        createFragment();
    }

    private void createFragment() {
        DrinkFragment drinkFragment = new DrinkFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.replace(R.id.drink_container, drinkFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
