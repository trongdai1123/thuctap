package com.example.mthshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mthshop.R;
import com.example.mthshop.activities.DetailsMeActivity;
import com.example.mthshop.activities.LoginActivity;
import com.example.mthshop.activities.MyBillsActivity;
import com.example.mthshop.activities.MyProductActivity;
import com.example.mthshop.activities.RateActivity;
import com.example.mthshop.activities.VerifyBillsActivity;
import com.example.mthshop.databinding.FragmentMeBinding;
import com.example.mthshop.dialog.NotificationDiaLog;

public class MeFragment extends Fragment {
    FragmentMeBinding thisFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisFragment = FragmentMeBinding.inflate(getLayoutInflater(), container, false);
        //status bar
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.main));
        View decorView = getActivity().getWindow().getDecorView(); //set status background black
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //set status text  light
        // push
        pushDataToLayout();
        //
        listenerToMyBill();
        //logout
        thisFragment.fMeEdLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDiaLog.showDiaLogLogOut(getContext());
            }
        });

        thisFragment.fMeEdEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DetailsMeActivity.class));
            }
        });

        thisFragment.fMeEdMyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MyProductActivity.class));
            }
        });

        thisFragment.fMeEdVerifyBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), VerifyBillsActivity.class));
            }
        });



        return thisFragment.getRoot();
    }

    private void pushDataToLayout() {
        //set avatar
        Glide.with(getContext()).load(LoginActivity.userCurrent.getAvatar()).placeholder(R.drawable.ic_login_user).into(thisFragment.fMeImgAvatar);
        //name
        thisFragment.fMeTvUser.setText(LoginActivity.userCurrent.getUser());

    }

    private void listenerToMyBill() {
        thisFragment.fMeLnWaitConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyBillsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        thisFragment.fMeLnGetProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        thisFragment.fMeLnShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        thisFragment.fMeLnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RateActivity.class);
                startActivity(intent);
            }
        });
    }


}
