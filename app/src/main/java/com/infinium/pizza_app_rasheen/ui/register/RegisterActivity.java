package com.infinium.pizza_app_rasheen.ui.register;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.infinium.pizza_app_rasheen.MainActivity;
import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.dao.OrderPizza;
import com.infinium.pizza_app_rasheen.ui.cart.CartActivity;
import com.infinium.pizza_app_rasheen.ui.login.LoginActivity;
import com.infinium.pizza_app_rasheen.ui.splash_screen.CartListner;

public class RegisterActivity extends AppCompatActivity {

    private RegisterViewModel registerViewModel;
    public static final String PREFS_NAME_ORDER = "OrderPref";
    public static final String PREFS_NAME = "CartPref";

    private FirebaseAuth mAuth;
    FirebaseUser logUser = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerViewModel = ViewModelProviders.of(this, new RegisterViewModelFactory())
                .get(RegisterViewModel.class);

        final EditText usernameEditText = findViewById(R.id.reg_username);
        final EditText passwordEditText = findViewById(R.id.reg_password);
        final EditText emailEditText = findViewById(R.id.reg_email);
        final Button registerButton = findViewById(R.id.reg_login);
        final ProgressBar loadingProgressBar = findViewById(R.id.reg_loading);
        final Button loginBtn = findViewById(R.id.loginBtnReg);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        registerViewModel.getLoginFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(@Nullable RegisterFormState registerFormState) {
                if (registerFormState == null) {
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getEmailError() != null) {
                    emailEditText.setError(getString(registerFormState.getEmailError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
            }
        });

        registerViewModel.getLoginResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(@Nullable RegisterResult registerResult) {
                if (registerResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (registerResult.getError() != null) {
                    showLoginFailed(registerResult.getError());
                }
                if (registerResult.getSuccess() != null) {
                    updateUiWithUser(registerResult.getSuccess());
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
                registerViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),emailEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.login(emailEditText.getText().toString(),
                            passwordEditText.getText().toString(),usernameEditText.getText().toString());
                }
                return false;
            }
        });

        emailEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    registerViewModel.login(emailEditText.getText().toString(),
                            passwordEditText.getText().toString(),usernameEditText.getText().toString());
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerViewModel.login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),usernameEditText.getText().toString());
            }
        });
    }



    private void updateUiWithUser(RegisterUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        logUser = mAuth.getCurrentUser();

        registerViewModel.getCart(new CartListner() {
            @Override
            public void onSuccess(Cart cart) {

                SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME,MODE_PRIVATE).edit();
                Gson gson = new Gson();

                String cartString = gson.toJson(cart);
                editor.putString("cart", cartString);
                editor.apply();

                registerViewModel.getOrder(new CartListner() {
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


                        if(!(cn.getClassName().equals(CartActivity.class.getName()))){
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
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

    private void updateUI(String a){

    }
}