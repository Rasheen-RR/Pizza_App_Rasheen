package com.infinium.pizza_app_rasheen.ui.splash_screen;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;
import com.infinium.pizza_app_rasheen.ui.cart.CartActivity;
import com.infinium.pizza_app_rasheen.ui.login.LoginActivity;
import com.infinium.pizza_app_rasheen.MainActivity;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser logUser = null;
    DatabaseReference mDatabase;
    private SplashViewModel splashViewModel;
    public static final String PREFS_NAME = "CartPref";
    public static final String PREFS_NAME_ORDER = "OrderPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);


        mAuth = FirebaseAuth.getInstance();

        logUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Check if the user is null if not send the user to menu if its null send the user to register pae
        if(logUser != null){
            splashViewModel.getCart(new CartListner() {
                @Override
                public void onSuccess(Cart cart) {
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit();
                    Gson gson = new Gson();

                    String cartString = gson.toJson(cart);
                    editor.putString("cart", cartString);
                    editor.apply();

                    splashViewModel.getOrder(new CartListner() {
                        @Override
                        public void onSuccess(Cart cart) {

                        }

                        @Override
                        public void successAdded(Cart cart) {

                        }

                        @Override
                        public void successRemoved(Cart cart) {

                        }

                        @Override
                        public void successDeleted(Cart cart, int pos) {

                        }

                        @Override
                        public void placeOrder() {

                        }

                        @Override
                        public void getOrders(OrderPizza orders) {
                            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME_ORDER,MODE_PRIVATE).edit();
                            Gson gson = new Gson();

                            String orderString = gson.toJson(orders);
                            editor.putString("orders", orderString);
                            editor.apply();


                            ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;


                            if(!(cn.getClassName().equals(CartActivity.class.getName())) && !(cn.getClassName().equals(MainActivity.class.getName()))){
                                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                                finish();
                            }
                        }
                    },logUser.getUid());
                }

                @Override
                public void successAdded(Cart cart) {

                }

                @Override
                public void successRemoved(Cart cart) {

                }

                @Override
                public void successDeleted(Cart cart, int pos) {

                }

                @Override
                public void placeOrder() {

                }

                @Override
                public void getOrders(OrderPizza orders) {

                }
            },logUser.getUid());
        }else{
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
        }
    }
}