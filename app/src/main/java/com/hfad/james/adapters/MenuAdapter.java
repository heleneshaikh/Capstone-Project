package com.hfad.james.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
 * Created by heleneshaikh on 30/08/16.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    ArrayList<Items> itemList = new ArrayList<>();
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
    public String key;
    @BindView(R.id.total)
    TextView total;
    Context context;

    public MenuAdapter(Firebase ref, Context mContext) {
        context = mContext;
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                key = snapshot.getKey();
                itemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Items items = dataSnapshot.getValue(Items.class);
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
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MenuAdapter.ViewHolder holder, final int position) {
        final Items item = itemList.get(position);
        CardView cardView = holder.cardView;
        ButterKnife.bind(this, cardView);

        item_title.setText(item.getTitle());
        amount.setText(context.getString(R.string.set_amount, (int) item.getAmount()));
        price.setText(context.getString(R.string.set_price, (double) item.getPrice()));

        calculatePricePerItem(item);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri.Builder builder = getBuilder(holder);
                Firebase rootRef = new Firebase(builder.toString());
                double amount = item.getAmount() - 1;
                if (amount >= 0) {
                    rootRef.setValue(amount);
                } else {
                    rootRef.setValue(0);
                }
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

    private void calculatePricePerItem(Items item) {
        double basePrice = item.getPrice();
        double newPrice = basePrice * item.getAmount();
        item.setTotalPricePerItem(newPrice);
        total.setText(context.getString(R.string.set_total_price_item, item.getTotalPricePerItem()));
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
