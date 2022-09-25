package com.example.mthshop.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mthshop.activities.LoginActivity;
import com.example.mthshop.adapter.MyBillAdapter;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.FragmentWaitConfirmBinding;
import com.example.mthshop.databinding.FragmentWaitGetProductBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.Bill;
import com.example.mthshop.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitConfirmFragment extends Fragment {
    private FragmentWaitConfirmBinding thisFragment;
    private List<Product> listProduct;
    private MyBillAdapter myBillAdapter;
    private List<Bill> listMyBill;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisFragment = FragmentWaitConfirmBinding.inflate(getLayoutInflater());
        NotificationDiaLog.showProgressBar(getActivity());
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
            APIService.appService.callProductByIdBillAndStatus(bill.getIdBill(), 2).enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    List<Product> tmp = response.body();
                    NotificationDiaLog.dismissProgressBar();
                    if (tmp != null && (!tmp.isEmpty())) {
                        for (Product p :
                                tmp) {
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
            thisFragment.fWaitConfirmTvEmpty.setVisibility(View.GONE);

        myBillAdapter = new MyBillAdapter(listProduct, getActivity(), 2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        thisFragment.fWaitConfirmRecyclerView.setHasFixedSize(true);
        thisFragment.fWaitConfirmRecyclerView.setLayoutManager(linearLayoutManager);
        thisFragment.fWaitConfirmRecyclerView.setAdapter(myBillAdapter);
    }
}
