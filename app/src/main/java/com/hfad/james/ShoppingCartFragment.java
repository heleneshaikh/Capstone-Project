package com.hfad.james;


import android.content.ClipData;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.hfad.james.adapters.OrderAdapter;
import com.hfad.james.model.Items;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCartFragment extends Fragment {
    @BindView(R.id.order_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.total_amount_tv)
    TextView totalPrice;

    public ShoppingCartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fragment_shopping_cart, container, false);
        ButterKnife.bind(this, view);
        Firebase ref = new Firebase("https://james-5d3ae.firebaseio.com/");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        OrderAdapter adapter = new OrderAdapter(ref);
        recyclerView.setAdapter(adapter);

        Items item = new Items();
        totalPrice.setText("" + item.getTotalSum());

        return view;
    }

}
