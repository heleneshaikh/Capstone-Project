package com.hfad.james.adapters;

import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.hfad.james.R;
import com.hfad.james.model.Items;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heleneshaikh on 05/09/16.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    ArrayList<Items> itemList = new ArrayList<>();
    String key;
    @BindView(R.id.menu_item)
    public TextView item_title;
    @BindView(R.id.amount)
    public TextView amount;
    @BindView(R.id.price_items)
    public TextView price;
    @BindView(R.id.plus_button)
    Button plusButton;
    @BindView(R.id.minus_button)
    Button minusButton;

    public OrderAdapter(Firebase ref) {

        ref.child("Food").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) { //contains Drinks and Food
                itemList.clear();
                for (DataSnapshot datasnapshot : snapshot.getChildren()) { //contains Drinks and Food
                    Items items = datasnapshot.getValue(Items.class);
                    itemList.add(items);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.v("error", firebaseError.getMessage());
            }

        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardview)
        CardView cardView;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderAdapter.ViewHolder holder, int position) {
        final Items item = itemList.get(position);
        CardView cardView = holder.cardView;
        ButterKnife.bind(this, cardView);

        item_title.setText(item.getTitle());
        amount.setText("" + (int) item.getAmount());
        price.setText(String.valueOf(item.getPrice()) + "€");

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri.Builder builder = getBuilder(holder);
                Firebase rootRef = new Firebase(builder.toString());
                double amount = item.getAmount() - 1;
                rootRef.setValue(amount);
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri.Builder builder = getBuilder(holder);
                Firebase rootRef = new Firebase(builder.toString());
                double amount = item.getAmount() + 1;
                rootRef.setValue(amount);
            }
        });
    }

    @NonNull
    private Uri.Builder getBuilder(ViewHolder holder) {
        int position = holder.getAdapterPosition();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("james-5d3ae.firebaseio.com")
                .appendPath(key)
                .appendPath(String.valueOf(position))
                .appendPath("amount")
                .build();
        return builder;
    }

    @Override
    public int getItemCount() {
        return (itemList.isEmpty() ? 0 : itemList.size());
    }


}
