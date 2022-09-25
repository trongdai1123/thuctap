package com.example.mthshop.fragment.verify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mthshop.activities.LoginActivity;
import com.example.mthshop.adapter.VerifyBillsAdapter;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.FragmentDeliveredBinding;
import com.example.mthshop.databinding.FragmentDeliveringBinding;
import com.example.mthshop.databinding.FragmentVerifyDelieveringBinding;
import com.example.mthshop.databinding.FragmentVerifyGetProductBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.BillDetails;
import com.example.mthshop.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyDeliveringFragment extends Fragment {
    private FragmentVerifyDelieveringBinding thisFragment;
    private List<Product> listProduct;
    private VerifyBillsAdapter myBillAdapter;
    private List<BillDetails> listBillDetails;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisFragment = FragmentVerifyDelieveringBinding.inflate(getLayoutInflater());
        listProduct = new ArrayList<>();
        NotificationDiaLog.showProgressBar(getActivity());
        return thisFragment.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        listProduct.clear();
        callBillDetail();
    }

    private void callBillDetail() {
        APIService.appService.callBillDetailsByOwner(LoginActivity.userCurrent.getUser()).enqueue(new Callback<List<BillDetails>>() {
            @Override
            public void onResponse(Call<List<BillDetails>> call, Response<List<BillDetails>> response) {
                NotificationDiaLog.dismissProgressBar();
                listBillDetails = null;
                listBillDetails = response.body();
                if (listBillDetails != null) {
                    for (BillDetails list: listBillDetails) {
                        if (list.getStatus() == 4) {
                            Product product = list.getProduct();
                            product.inBill = list.getIdBill();
                            listProduct.add(product);
                        }
                    }
                    setRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<List<BillDetails>> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
            }
        });
    }

    private void setRecyclerView() {
        if (!listProduct.isEmpty())
            thisFragment.fVerifyDeliveringTvEmpty.setVisibility(View.GONE);

        myBillAdapter = new VerifyBillsAdapter(listProduct, getActivity(), 4);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        thisFragment.fVerifyDeliveringRecyclerView.setHasFixedSize(true);
        thisFragment.fVerifyDeliveringRecyclerView.setLayoutManager(linearLayoutManager);
        thisFragment.fVerifyDeliveringRecyclerView.setAdapter(myBillAdapter);
    }
}
