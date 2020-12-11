package com.infinium.pizza_app_rasheen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.infinium.pizza_app_rasheen.dao.Addon;
import com.infinium.pizza_app_rasheen.dao.Pizza;
import com.infinium.pizza_app_rasheen.dao.Toppings;
import com.infinium.pizza_app_rasheen.dao.User;
import com.infinium.pizza_app_rasheen.data.LoginRepository;
import com.infinium.pizza_app_rasheen.ui.cart.CartActivity;
import com.infinium.pizza_app_rasheen.ui.login.LoginActivity;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    FirebaseUser logUser = null;
    TextView name;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {




//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");

        mDatabase = FirebaseDatabase.getInstance().getReference();
//        User user = new User("sheen", "sheen.ruwisha@gmail.con","12");

//        List<Addon> list = new ArrayList<>();
//        Addon a = new Addon("123","123");
//        list.add(a);
//
//        List<Toppings> lista = new ArrayList<>();
//        Toppings b = new Toppings("123","123");
//        lista.add(b);
//
//        Pizza pizza = new Pizza("A","test","4.3",list,lista,"https://www.simplyrecipes.com/wp-content/uploads/2019/09/easy-pepperoni-pizza-lead-4.jpg",10.00);
//        mDatabase.child("pizza").child("1234").setValue(pizza);
//        mDatabase.child("pizza").child("1235").setValue(pizza);
//        mDatabase.child("pizza").child("1236").setValue(pizza);
//        mDatabase.child("pizza").child("1237").setValue(pizza);
//        mDatabase.child("pizza").child("1238").setValue(pizza);
//        mDatabase.child("pizza").child("1239").setValue(pizza);
//        mDatabase.child("pizza").child("1249").setValue(pizza);
//        mDatabase.child("pizza").child("1249").setValue(pizza);
//        mDatabase.child("pizza").child("1249").setValue(pizza);

        mAuth = FirebaseAuth.getInstance();

        logUser = mAuth.getCurrentUser();




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView user = findViewById(R.id.nav_header_title);

//        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
//        String highScore = sharedPref.getString("username","jane");
//        user.setText("alll");
//

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CharSequence title = toolbar.getTitle();
        for(int i = 0; i < toolbar.getChildCount(); i++){
            View tmpView = toolbar.getChildAt(i);
            if("androidx.appcompat.widget.AppCompatTextView".equals(tmpView.getClass().getName())){
                if(((AppCompatTextView) tmpView).getText().equals(title)){
                    ((AppCompatTextView) tmpView).setTextSize(10);
                    tmpView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(MainActivity.this, CartActivity.class));
                            //do whatever you want here
                        }
                    });
                }
            }
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
//
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        name = headerView.findViewById(R.id.nav_header_title);
        name.setText(logUser.getDisplayName());

        email = headerView.findViewById(R.id.email);
        email.setText(logUser.getEmail());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            logout();
            return true;
        });
    }


    public void logout(){
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}