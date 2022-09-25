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

public class MyBillViewPager2Adapter extends FragmentStateAdapter {
    public MyBillViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return  new WaitConfirmFragment();
            case 1:
                return new WaitGetProductFragment();
            case 2:
                return new DeliveringFragment();
            case 3:
                return new DeliveredFragment();
            case 4:
                return new CancelledFragment();

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
