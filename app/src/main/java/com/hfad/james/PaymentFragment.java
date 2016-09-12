package com.hfad.james;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.hfad.james.adapters.OrderAdapter;
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

    public PaymentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout relativeLayout = (LinearLayout) inflater.inflate(R.layout.fragment_payment, container, false);

        ButterKnife.bind(this, relativeLayout);
        Firebase ref = new Firebase("https://james-5d3ae.firebaseio.com/");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        OrderAdapter adapter = new OrderAdapter(ref);
        recyclerView.setAdapter(adapter);

        paidButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getActivity(), "See you soon!", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        return relativeLayout;
    }

}
