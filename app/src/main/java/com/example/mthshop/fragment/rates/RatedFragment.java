package com.example.mthshop.fragment.rates;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mthshop.adapter.RateDetailsAdapter;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.FragmentDoNotRateBinding;
import com.example.mthshop.databinding.FragmentRatedBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.Product;
import com.example.mthshop.model.Rate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatedFragment extends Fragment {
    private FragmentRatedBinding thisFragment;
    private List<Rate> listRate;
    private RateDetailsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisFragment = FragmentRatedBinding.inflate(getLayoutInflater());

        return thisFragment.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        listRate = new ArrayList<>();
        setRecyclerView();
        callRateStatus1();
    }

    private void callRateStatus1() {
        NotificationDiaLog.showProgressBar(getContext());
        APIService.appService.callAllRates().enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                List<Rate> tmp = response.body();
                if (tmp != null) {
                    for (Rate rate :
                            tmp) {
                        if (rate.getStatus() == 1)
                            listRate.add(rate);
                    }
                    callProductInRate();
                    NotificationDiaLog.dismissProgressBar();
                }

            }

            @Override
            public void onFailure(Call<List<Rate>> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
            }
        });
    }

    private void callProductInRate() {

        for (int i = 0; i < listRate.size(); i++) {
            int finalI = i;
            APIService.appService.callProductById(listRate.get(i).getIdProduct()).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Product tmp = response.body();
                    if (tmp != null)
                        listRate.get(finalI).product = tmp;

                    if (finalI == listRate.size() - 1) {
                        setRecyclerView();
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Log.e("Rate Details", "Call Prodcut false :" + t.toString());
                }
            });
        }
    }

    private void setRecyclerView() {
        NotificationDiaLog.dismissProgressBar();
        thisFragment.fRatedRecyclerView.setHasFixedSize(true);
        LinearLayoutManager ln = new LinearLayoutManager(getContext());
        adapter = new RateDetailsAdapter(listRate, getActivity());
        thisFragment.fRatedRecyclerView.setLayoutManager(ln);
        thisFragment.fRatedRecyclerView.setAdapter(adapter);
    }
}
