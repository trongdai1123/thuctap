package com.example.mthshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.mthshop.R;
import com.example.mthshop.adapter.MyBillViewPager2Adapter;
import com.example.mthshop.databinding.ActivityMyBillsBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyBillsActivity extends AppCompatActivity {
    private ActivityMyBillsBinding thisActivity;
    private MyBillViewPager2Adapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = ActivityMyBillsBinding.inflate(getLayoutInflater());
        setContentView(thisActivity.getRoot());
        viewPagerAdapter = new MyBillViewPager2Adapter(this);
        ////status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        //sett listener
        thisActivity.aMyBillsBtnBack.setOnClickListener( view -> { finish(); });


        //set mau tablayout
        thisActivity.aMyBillsTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#1C1B1B"));
        thisActivity.aMyBillsTabLayout.setTabTextColors(Color.parseColor("#B8B1B1"), Color.parseColor("#1C1B1B"));
        thisActivity.aMyBillsViewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(thisActivity.aMyBillsTabLayout, thisActivity.aMyBillsViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Chờ xác nhận");
                        break;
                    case 1:
                        tab.setText("Chờ lấy hàng");
                        break;
                    case 2:
                        tab.setText("Đang giao");
                        break;
                    case 3:
                        tab.setText("Đã giao");
                        break;
                    case 4:
                        tab.setText("Đã hủy");
                        break;
                }
            }
        }).attach();


    }
}