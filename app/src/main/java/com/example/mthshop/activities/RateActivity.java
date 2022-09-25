package com.example.mthshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.mthshop.R;
import com.example.mthshop.adapter.RateViewPager2;
import com.example.mthshop.databinding.ActivityRateBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class RateActivity extends AppCompatActivity {
    private ActivityRateBinding thisActivity;
    private RateViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = ActivityRateBinding.inflate(getLayoutInflater());
        ////status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        //sett listener
        thisActivity.aRateBack.setOnClickListener( view -> { finish(); });
        setContentView(thisActivity.getRoot());

        //
        viewPager2 = new RateViewPager2(this);

        //set mau tablayout
        thisActivity.aRateTabLayout.setSelectedTabIndicatorColor(Color.parseColor("#1C1B1B"));
        thisActivity.aRateTabLayout.setTabTextColors(Color.parseColor("#B8B1B1"), Color.parseColor("#1C1B1B"));
        thisActivity.aRateViewPager2.setAdapter(viewPager2);
        new TabLayoutMediator(thisActivity.aRateTabLayout, thisActivity.aRateViewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Đã đánh giá");
                        break;
                    case 1:
                        tab.setText("Chưa đánh giá");
                        break;
                }
            }
        }).attach();
    }
}