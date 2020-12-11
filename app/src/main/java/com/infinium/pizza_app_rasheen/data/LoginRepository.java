package com.infinium.pizza_app_rasheen.data;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.infinium.pizza_app_rasheen.R;
import com.infinium.pizza_app_rasheen.data.model.LoggedInUser;
import com.infinium.pizza_app_rasheen.ui.login.LoggedInUserView;
import com.infinium.pizza_app_rasheen.ui.login.LoginActivity;
import com.infinium.pizza_app_rasheen.ui.login.OnLoginListener;

import java.util.concurrent.Executor;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private FirebaseAuth mAuth;

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public void login(String username, String password,final OnLoginListener listner) {
        listner.onStart();

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            listner.onLoginResult(user);
                        } else {
                            listner.onFailure();
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}