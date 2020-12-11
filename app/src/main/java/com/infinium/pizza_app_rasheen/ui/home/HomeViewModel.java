package com.infinium.pizza_app_rasheen.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infinium.pizza_app_rasheen.dao.Pizza;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private DatabaseReference mDatabase;
    private MutableLiveData<List<Pizza>> pizzs;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


    // Get the pizza list from firebase
    public void getPizzs(final PizzaListner listner) {

        // set thhe reference
        mDatabase = FirebaseDatabase.getInstance().getReference().child("pizza");

        listner.onStart();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Pizza> post = new ArrayList<>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    post.add(postSnapshot.getValue(Pizza.class));
                }
                pizzs = new MutableLiveData<>();
                pizzs.setValue(post);
                // Send a callback with the pizza lsit
                listner.onPizzaResult(post);
                System.out.println(post.get(0).getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(postListener);
    }

}