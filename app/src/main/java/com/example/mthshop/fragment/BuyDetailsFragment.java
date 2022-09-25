package com.example.mthshop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mthshop.activities.BuyDetailsActivity;
import com.example.mthshop.activities.LoginActivity;
import com.example.mthshop.adapter.CartAdapter;
import com.example.mthshop.adapter.OderAdapter;
import com.example.mthshop.databinding.FragmentBuyDetailsBinding;
import com.example.mthshop.fortmat.FortMartData;
import com.example.mthshop.model.Product;

import java.util.ArrayList;
import java.util.List;

public class BuyDetailsFragment extends Fragment {
    private FragmentBuyDetailsBinding thisFragment;
    private int status; // check mua ngay hay mua trogn gio hang
    private Product productCurrent;
    private OderAdapter oderAdapter;
    private List<Product> listProduct;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisFragment = FragmentBuyDetailsBinding.inflate(getLayoutInflater());
        // lây value
        status = ((BuyDetailsActivity)getActivity()).getStatus();
        productCurrent = ((BuyDetailsActivity)getActivity()).getProductCurrent();
        listProduct = ((BuyDetailsActivity)getActivity()).getListProduct();
        //
        pushData();
        setRecyclerViewProduct();
        return thisFragment.getRoot();
    }


    private void pushData() {
        double priceSale = ((BuyDetailsActivity)getActivity()).getPriceSale();
        double priceTotal = ((BuyDetailsActivity)getActivity()).getPriceTotal();
        double priceTotalBill = ((BuyDetailsActivity)getActivity()).getPriceTotalBill();
        thisFragment.fBuyDetailsTvAddress.setText(LoginActivity.userCurrent.getAddress());
        thisFragment.fBuyDetailsTvTotal.setText("\u20AB" + FortMartData.fortMartTypeDoubleToMoney(priceTotal));
        thisFragment.fBuyDetailsTvTotalBill.setText("\u20AB" + FortMartData.fortMartTypeDoubleToMoney(priceTotalBill));
        thisFragment.fBuyDetailsTvSale.setText("\u20AB" + FortMartData.fortMartTypeDoubleToMoney(priceSale));

    }

    private void setRecyclerViewProduct() {

        oderAdapter = new OderAdapter(listProduct, getActivity());
        if (listProduct != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            thisFragment.fBuyDetailsRecyclerView.setHasFixedSize(true);
            thisFragment.fBuyDetailsRecyclerView.setLayoutManager(linearLayoutManager);
            thisFragment.fBuyDetailsRecyclerView.setAdapter(oderAdapter);
            thisFragment.fBuyDetailsRecyclerView.setNestedScrollingEnabled(false);

        }

    }

}
