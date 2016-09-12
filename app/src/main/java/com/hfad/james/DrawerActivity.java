package com.hfad.james;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerActivity extends AppCompatActivity {
    @BindView(R.id.drawer_listview)
    ListView drawerListView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.include)
    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    String[] drawerItems;
    int currentPosition;

    private class DrawerClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            selectItem(position);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt("position");
            setActionBarTitle(currentPosition);
        } else {
            selectItem(0);
        }

        drawerItems = getResources().getStringArray(R.array.drawer_items);
        drawerListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerItems));
        drawerListView.setOnItemClickListener(new DrawerClickListener());

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerListView);
        menu.findItem(R.id.shopping_cart).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopping_cart:
                Intent intent = new Intent(this, ShoppingCartActivity.class);
                startActivity(intent);
        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        Fragment fragment;
        currentPosition = position;
        switch (position) {
            case 1:
                fragment = new DrinkFragment();
                break;
            case 2:
                fragment = new FoodFragment();
                break;
            case 3:
                fragment = new ShoppingCartFragment();
                break;
            case 4:
                fragment = new PaymentFragment();
                break;
            default:
                fragment = new MenuFragment();
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        setActionBarTitle(position);
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();

        drawerLayout.closeDrawer(drawerListView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentPosition);
    }

    public void setActionBarTitle(int position) {
        String title;
        drawerItems = getResources().getStringArray(R.array.drawer_items);
        if (position == 0) {
            title = getResources().getString(R.string.app_name);
        } else {
            title = drawerItems[position];
        }
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
}
