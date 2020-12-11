package com.infinium.pizza_app_rasheen.ui.cart;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;
import com.infinium.pizza_app_rasheen.ui.splash_screen.CartListner;

public class CartViewModel extends ViewModel {

    private MutableLiveData<Cart> mCart;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    FirebaseUser logUser = null;


    public void addCart(Cart cart, final CartListner listner, int position) {

        mAuth = FirebaseAuth.getInstance();
        logUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(logUser.getUid());

        mDatabase.setValue(cart);


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCart = new MutableLiveData<>();
                mCart.setValue(dataSnapshot.getValue(Cart.class));
                listner.successAdded(mCart.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(postListener);
    }

    public void removeCart(Cart cart, final CartListner listner, int position) {

        mAuth = FirebaseAuth.getInstance();
        logUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(logUser.getUid());

        mDatabase.setValue(cart);


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCart = new MutableLiveData<>();
                mCart.setValue(dataSnapshot.getValue(Cart.class));
                listner.successRemoved(mCart.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(postListener);
    }




    public void deleteCart(final CartListner listner, Cart cart) {

        mAuth = FirebaseAuth.getInstance();
        logUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(logUser.getUid());

        mDatabase.setValue(cart);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCart = new MutableLiveData<>();
                mCart.setValue(dataSnapshot.getValue(Cart.class));
                listner.successDeleted(mCart.getValue(), 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(postListener);
    }

    public void placeOrder(final CartListner listner, OrderPizza orderPizza) {

        mAuth = FirebaseAuth.getInstance();
        logUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(logUser.getUid());

        mDatabase.setValue(new Cart());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("orders").child(logUser.getUid());

        mDatabase.setValue(orderPizza);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCart = new MutableLiveData<>();
                listner.placeOrder();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };

        mDatabase.addValueEventListener(postListener);
    }


}
