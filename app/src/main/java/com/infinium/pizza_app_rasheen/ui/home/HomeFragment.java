package com.infinium.pizza_app_rasheen.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Pizza;
import com.infinium.pizza_app_rasheen.ui.pizza_details.PizzaDetailViewModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    PizzaDetailViewModel pizzaDetailViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        pizzaDetailViewModel = ViewModelProviders.of(this).get(PizzaDetailViewModel.class);

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getPizzs(new PizzaListner() {
            @Override
            public void onPizzaResult(List<Pizza> result) {
                RecyclerView rvContacts = (RecyclerView) root.findViewById(R.id.list_view_piz);
                PizzaListAdapter adapter = new PizzaListAdapter(result,getContext(),pizzaDetailViewModel);
                rvContacts.setAdapter(adapter);
                rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
            }



            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });


        return root;
    }
}