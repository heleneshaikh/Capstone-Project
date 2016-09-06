package com.hfad.james;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.client.Firebase;
import com.hfad.james.adapters.MenuAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {
    @BindView(R.id.food_recycler)
    RecyclerView recyclerView;
    protected Firebase ref;

    public ItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView view = (RecyclerView) inflater.inflate(R.layout.fragment_food, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        MenuAdapter adapter = new MenuAdapter(ref);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void createReference(String url) {
        ref = new Firebase(url);
    }

}
