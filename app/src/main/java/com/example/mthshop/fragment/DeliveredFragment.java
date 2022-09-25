package com.example.mthshop.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mthshop.activities.LoginActivity;
import com.example.mthshop.adapter.MyBillAdapter;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.FragmentDeliveredBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.Bill;
import com.example.mthshop.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveredFragment extends Fragment {
    private FragmentDeliveredBinding thisFragment;
    private List<Product> listProduct;
    private MyBillAdapter myBillAdapter;
    private List<Bill> listMyBill;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisFragment = FragmentDeliveredBinding.inflate(getLayoutInflater());
        listProduct = new ArrayList<>();
        return thisFragment.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        listProduct.clear();
        setRecyclerView();
        callBills();
    }

    private void callBills() {
        APIService.appService.callBillInCart(3, LoginActivity.userCurrent.getUser()).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                listMyBill = response.body();
                callListProductInBill();
                listProduct = new ArrayList<>();
                Log.e("listMyBill", listMyBill.get(0).toString());
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
            }
        });
    }

    private void callListProductInBill() {
        for (Bill bill : listMyBill) {
            APIService.appService.callProductByIdBillAndStatus(bill.getIdBill(), 1).enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    List<Product> tmp = response.body();
                    NotificationDiaLog.dismissProgressBar();
                    if (tmp != null && (!tmp.isEmpty())) {
                        for (Product p : tmp) {
                            p.inBill = bill.getIdBill();
                            listProduct.add(p);
                            setRecyclerView();
                        }
                    }
                }
                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    NotificationDiaLog.dismissProgressBar();
                }
            });
        }
    }

    private void setRecyclerView() {
        if (!listProduct.isEmpty())
            thisFragment.fDeliveredTvEmpty.setVisibility(View.GONE);

        myBillAdapter = new MyBillAdapter(listProduct, getActivity(), 1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        thisFragment.fDeliveredRecyclerView.setHasFixedSize(true);
        thisFragment.fDeliveredRecyclerView.setLayoutManager(linearLayoutManager);
        thisFragment.fDeliveredRecyclerView.setAdapter(myBillAdapter);
    }
}
