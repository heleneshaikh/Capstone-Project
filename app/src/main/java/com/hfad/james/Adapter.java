package com.hfad.james;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.hfad.james.model.Items;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heleneshaikh on 30/08/16.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList<Items> itemList = new ArrayList<>();
    @BindView(R.id.menu_item)
    public TextView item_title;
    @BindView(R.id.amount)
    public TextView amount;
    @BindView(R.id.price_items)
    public TextView price;

    public Adapter(Firebase ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
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
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) { //todo butterknife
        Items item = itemList.get(position);
        CardView cardView = holder.cardView;
        ButterKnife.bind(this, cardView);

        item_title.setText(item.getTitle());
        amount.setText(""+ (int)item.getAmount());
        price.setText(String.valueOf(item.getPrice()) + "€");
    }

    @Override
    public int getItemCount() {
        return (itemList.isEmpty() ? 0 : itemList.size());
    }
}
