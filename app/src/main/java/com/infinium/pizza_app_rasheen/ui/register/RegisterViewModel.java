package com.infinium.pizza_app_rasheen.ui.register;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;
import com.infinium.pizza_app_rasheen.data.RegisterRepository;
import com.infinium.pizza_app_rasheen.ui.splash_screen.CartListner;

public class RegisterViewModel extends ViewModel {

    private MutableLiveData<RegisterFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> loginResult = new MutableLiveData<>();
    private RegisterRepository loginRepository;
    private DatabaseReference mDatabase;
    private MutableLiveData<Cart> mCart;
    RegisterViewModel(RegisterRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<RegisterFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<RegisterResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password,String username) {
        // can be launched in a separate asynchronous job
        loginRepository.login(email, password,username, new OnRegisterListener() {
            @Override
            public void onLoginResult(FirebaseUser result) {
                loginResult.postValue(new RegisterResult(new RegisterUserView(result.getEmail())));
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {
                loginResult.postValue(new RegisterResult(R.string.login_failed));
            }
        });
    }

    public void loginDataChanged(String username, String password, String email) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new RegisterFormState(R.string.invalid_username, null, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new RegisterFormState(null, R.string.invalid_password,null));
        } else if (!isEmailValid(email)) {
            loginFormState.setValue(new RegisterFormState(null, null,R.string.invalid_email));
        } else {
            loginFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    private boolean isEmailValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

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