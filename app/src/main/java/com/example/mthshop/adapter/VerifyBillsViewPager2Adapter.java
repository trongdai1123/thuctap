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
import com.example.mthshop.fragment.verify.VerifyCancelledFragment;
import com.example.mthshop.fragment.verify.VerifyConfirmFragment;
import com.example.mthshop.fragment.verify.VerifyDeliveredFragment;
import com.example.mthshop.fragment.verify.VerifyDeliveringFragment;
import com.example.mthshop.fragment.verify.VerifyGetProductFragment;

public class VerifyBillsViewPager2Adapter extends FragmentStateAdapter {
    public VerifyBillsViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return  new VerifyConfirmFragment();
            case 1:
                return new VerifyGetProductFragment();
            case 2:
                return new VerifyDeliveringFragment();
            case 3:
                return new VerifyDeliveredFragment();
            case 4:
                return new VerifyCancelledFragment();

        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

