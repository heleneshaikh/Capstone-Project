package com.hfad.james;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodFragment extends ItemFragment {

    public FoodFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createReference("https://james-5d3ae.firebaseio.com/Food");
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

