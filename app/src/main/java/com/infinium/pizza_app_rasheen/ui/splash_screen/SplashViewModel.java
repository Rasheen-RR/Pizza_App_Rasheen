package com.infinium.pizza_app_rasheen.ui.splash_screen;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;

public class SplashViewModel extends ViewModel {

    private MutableLiveData<Cart> mCart;
    private DatabaseReference mDatabase;


    public void getCart(final CartListner listner, String uid) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mCart = new MutableLiveData<>();
                mCart.setValue(dataSnapshot.getValue(Cart.class));
                listner.onSuccess(mCart.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(postListener);
    }

    public void getOrder(final CartListner listner, String uid) {

        mDatabase = FirebaseDatabase.getInstance().getReference().child("orders").child(uid);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mCart = new MutableLiveData<>();
                listner.getOrders(dataSnapshot.getValue(OrderPizza.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(postListener);
    }



}
