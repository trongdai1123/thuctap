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
import com.example.mthshop.adapter.MyBillAdapter;
import com.example.mthshop.adapter.VerifyBillsAdapter;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.FragmentCancelledBinding;
import com.example.mthshop.databinding.FragmentVerifyConfirmBinding;
import com.example.mthshop.databinding.FragmentWaitConfirmBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.Bill;
import com.example.mthshop.model.BillDetails;
import com.example.mthshop.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyConfirmFragment extends Fragment {
    private FragmentVerifyConfirmBinding thisFragment;
    private List<Product> listProduct;
    private VerifyBillsAdapter myBillAdapter;
    private List<BillDetails> listBillDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisFragment = FragmentVerifyConfirmBinding.inflate(getLayoutInflater());
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
                        if (list.getStatus() == 2) {
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
            thisFragment.fVerifyWaitConfirmTvEmpty.setVisibility(View.GONE);

        myBillAdapter = new VerifyBillsAdapter(listProduct, getActivity(), 2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        thisFragment.fVerifyWaitConfirmRecyclerView.setHasFixedSize(true);
        thisFragment.fVerifyWaitConfirmRecyclerView.setLayoutManager(linearLayoutManager);
        thisFragment.fVerifyWaitConfirmRecyclerView.setAdapter(myBillAdapter);
    }
}
