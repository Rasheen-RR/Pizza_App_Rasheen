package com.infinium.pizza_app_rasheen.ui.order_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Cart;

public class OrderDetailsActivity extends AppCompatActivity {

    Cart orderItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.list_view_order_items);


        Intent intent = getIntent();

        orderItems = (Cart)intent.getSerializableExtra("order");


        OrderDetailsAdapter adapter = new OrderDetailsAdapter(orderItems,this);
        rvContacts.setAdapter(adapter);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}