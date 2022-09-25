package com.example.mthshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mthshop.R;
import com.example.mthshop.api.APIService;
import com.example.mthshop.fragment.HomeFragment;
import com.example.mthshop.fragment.MeFragment;
import com.example.mthshop.fragment.NotificationFragment;
import com.example.mthshop.model.Bill;
import com.example.mthshop.model.BillDetails;
import com.example.mthshop.model.Rate;
import com.example.mthshop.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Currency;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.back_ground));

        initWidgets();

        //set fragment category
        getSupportFragmentManager().beginTransaction().add(R.id.aMain_frameLayout ,new HomeFragment()).commit();

        //bottom navigation
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Menu menu = bottomNavigationView.getMenu();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.mBN_home:
                        menu.getItem(0).setIcon(R.drawable.ic_home_main);
                        menu.getItem(1).setIcon(R.drawable.ic_chat_second);
                        menu.getItem(2).setIcon(R.drawable.ic_bell_second);
                        menu.getItem(3).setIcon(R.drawable.ic_me_second);
                        fragment = new HomeFragment();
                        break;
                    case R.id.mBN_chat:
                        menu.getItem(0).setIcon(R.drawable.ic_home_second);
                        menu.getItem(1).setIcon(R.drawable.ic_chat_main);
                        menu.getItem(2).setIcon(R.drawable.ic_bell_second);
                        menu.getItem(3).setIcon(R.drawable.ic_me_second);
                        break;
                    case R.id.mBN_notification:
                        menu.getItem(0).setIcon(R.drawable.ic_home_second);
                        menu.getItem(1).setIcon(R.drawable.ic_chat_second);
                        menu.getItem(2).setIcon(R.drawable.ic_bell_main);
                        menu.getItem(3).setIcon(R.drawable.ic_me_second);
                        fragment = new NotificationFragment();
                        break;
                    case R.id.mBN_me:
                        menu.getItem(0).setIcon(R.drawable.ic_home_second);
                        menu.getItem(1).setIcon(R.drawable.ic_chat_second);
                        menu.getItem(2).setIcon(R.drawable.ic_bell_second);
                        menu.getItem(3).setIcon(R.drawable.ic_me_main);
                        fragment = new MeFragment();
                }

                fragmentTransaction.replace(R.id.aMain_frameLayout, fragment).commit();
                return true;
            }
        });

    }

    private void initWidgets() {
        bottomNavigationView = findViewById(R.id.aMain_bottomNavigation);
    }


}