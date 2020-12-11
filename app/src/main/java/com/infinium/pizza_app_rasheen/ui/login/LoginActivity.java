package com.infinium.pizza_app_rasheen.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.infinium.pizza_app_rasheen.MainActivity;
import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;
import com.infinium.pizza_app_rasheen.ui.cart.CartActivity;
import com.infinium.pizza_app_rasheen.ui.register.RegisterActivity;
import com.infinium.pizza_app_rasheen.ui.splash_screen.CartListner;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    public static final String PREFS_NAME_ORDER = "OrderPref";
    public static final String PREFS_NAME = "CartPref";

    private FirebaseAuth mAuth;
    FirebaseUser logUser = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);

        final Button registerButton = findViewById(R.id.register);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }

                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }



    // Update the ui
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();

        logUser = mAuth.getCurrentUser();

        loginViewModel.getCart(new CartListner() {
            @Override
            public void onSuccess(Cart cart) {

                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit();
                Gson gson = new Gson();

                String cartString = gson.toJson(cart);
                editor.putString("cart", cartString);
                editor.apply();

                loginViewModel.getOrder(new CartListner() {
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
                        // Get the callback from order and add the orders to the shared preference
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME_ORDER,MODE_PRIVATE).edit();
                        Gson gson = new Gson();

                        String orderString = gson.toJson(orders);
                        editor.putString("orders", orderString);
                        editor.apply();


                        ActivityManager am = (ActivityManager)getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;


                        if(!(cn.getClassName().equals(CartActivity.class.getName()))){
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

}