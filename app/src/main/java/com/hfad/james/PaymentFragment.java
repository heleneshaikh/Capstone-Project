package com.hfad.james;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.hfad.james.adapters.OrderAdapter;
import com.hfad.james.model.TotalPriceEvent;
import com.hfad.james.util.IabHelper;
import com.hfad.james.util.IabResult;
import com.hfad.james.util.Inventory;
import com.hfad.james.util.Purchase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {
    @BindView(R.id.payment_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.total_amount_tv)
    TextView totalPrice;
    @BindView(R.id.mark_paid_btn)
    Button paidButton;
    @BindView(R.id.app_payment_btn)
    Button appPayment;
    static final String ITEM_SKU = "com.hfad.pay";

    public PaymentFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout relativeLayout = (LinearLayout) inflater.inflate(R.layout.fragment_payment, container, false);

        ButterKnife.bind(this, relativeLayout);
        Firebase ref = new Firebase("https://james-5d3ae.firebaseio.com/");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        OrderAdapter adapter = new OrderAdapter(ref, getActivity());
        recyclerView.setAdapter(adapter);

        paidButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getActivity(), R.string.see_you_toast, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        return relativeLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onPriceEvent(TotalPriceEvent event) {
        double price = event.totalPrice;
        totalPrice.setText(getString(R.string.set_price, (double) price));
    }
}
