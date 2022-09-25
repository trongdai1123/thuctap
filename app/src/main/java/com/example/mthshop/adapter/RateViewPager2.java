package com.example.mthshop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mthshop.fragment.CancelledFragment;
import com.example.mthshop.fragment.DeliveredFragment;
import com.example.mthshop.fragment.DeliveringFragment;
import com.example.mthshop.fragment.WaitConfirmFragment;
import com.example.mthshop.fragment.WaitGetProductFragment;
import com.example.mthshop.fragment.rates.DoNotRateFragment;
import com.example.mthshop.fragment.rates.RatedFragment;

public class RateViewPager2 extends FragmentStateAdapter {

    public RateViewPager2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return  new RatedFragment();
            case 1:
                return new DoNotRateFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
