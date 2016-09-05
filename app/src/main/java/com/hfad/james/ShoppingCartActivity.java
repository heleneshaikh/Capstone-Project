package com.hfad.james;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;

public class ShoppingCartActivity extends AppCompatActivity {
    @BindView(R.id.include)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        toolbar.setTitle(R.string.order_toolbar);
    }
}
