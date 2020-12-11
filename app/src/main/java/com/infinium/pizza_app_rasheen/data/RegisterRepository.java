package com.infinium.pizza_app_rasheen.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.infinium.pizza_app_rasheen.dao.Cart;
import com.infinium.pizza_app_rasheen.ui.login.OnLoginListener;
import com.infinium.pizza_app_rasheen.ui.register.OnRegisterListener;

public class RegisterRepository {

    private static volatile RegisterRepository instance;
    private FirebaseAuth mAuth;

    DatabaseReference mDatabase;

    public static RegisterRepository getInstance() {
        if (instance == null) {
            instance = new RegisterRepository();
        }
        return instance;
    }

    public void login(String email, String password,String name,final OnRegisterListener listner) {
        listner.onStart();

        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                                mDatabase.child("users").child(user.getUid()).setValue(new Cart());
                                                Log.d("TAG", "User profile updated.");
                                                listner.onLoginResult(user);
                                            }
                                        }
                                    });


                        } else {
                            listner.onFailure();

                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }


}
