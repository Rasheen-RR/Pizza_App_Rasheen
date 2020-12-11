package com.infinium.pizza_app_rasheen.ui.orders;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;

import static android.content.Context.MODE_PRIVATE;

public class OrderFragment extends Fragment {


    public static final String PREFS_NAME_ORDER = "OrderPref";
    private OrderViewModel orderViewModel;
    OrderPizza orderPizza;
    ConstraintLayout orderEmpty;
    ConstraintLayout orderListCon;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        orderViewModel =
                ViewModelProviders.of(this).get(OrderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_order, container, false);


        orderEmpty = root.findViewById(R.id.order_no_items);
        orderListCon = root.findViewById(R.id.order_list_container);

        // Get the shared preference from the orders
        Gson gson = new Gson();
        SharedPreferences orderPrefs = getContext().getSharedPreferences(PREFS_NAME_ORDER, MODE_PRIVATE);
        String orderString = orderPrefs.getString("orders", "{}");
        orderPizza = gson.fromJson(orderString, OrderPizza.class);


        // Check if the order list is null
        if(orderPizza == null){
            // Set the visibility of no items message to true and hide the list
            orderEmpty.setVisibility(View.VISIBLE);
            orderListCon.setVisibility(View.GONE);
        }else{

            // Update the list with the orders
            orderEmpty.setVisibility(View.GONE);
            orderListCon.setVisibility(View.VISIBLE);
            RecyclerView rvContacts = (RecyclerView) root.findViewById(R.id.list_order_items);

            OrderListAdapter adapter = new OrderListAdapter(orderPizza,getContext());
            rvContacts.setAdapter(adapter);
            rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        }



        return root;

    }
}
