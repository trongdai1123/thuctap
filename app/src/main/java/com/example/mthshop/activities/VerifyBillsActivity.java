package com.example.mthshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.mthshop.R;
import com.example.mthshop.adapter.MyBillViewPager2Adapter;
import com.example.mthshop.adapter.VerifyBillsViewPager2Adapter;
import com.example.mthshop.databinding.ActivityMyBillsBinding;
import com.example.mthshop.databinding.ActivityVerifyBillsBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class VerifyBillsActivity extends AppCompatActivity {
    private ActivityVerifyBillsBinding thisActivity;
    private VerifyBillsViewPager2Adapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = ActivityVerifyBillsBinding.inflate(getLayoutInflater());
        setContentView(thisActivity.getRoot());
        viewPagerAdapter = new VerifyBillsViewPager2Adapter(this);
        ////status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        //sett listener
        thisActivity.aVerifyMyBillsBtnBack.setOnClickListener( view -> { finish(); });

        //set mau tablayout
        thisActivity.aVerifyMyBillsTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#1C1B1B"));
        thisActivity.aVerifyMyBillsTabLayout.setTabTextColors(Color.parseColor("#B8B1B1"), Color.parseColor("#1C1B1B"));
        thisActivity.aVerifyMyBillsViewPager2.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(thisActivity.aVerifyMyBillsTabLayout, thisActivity.aVerifyMyBillsViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Xác nhận");
                        break;
                    case 1:
                        tab.setText("lấy hàng");
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