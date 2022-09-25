package com.example.mthshop.fragment;

import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mthshop.R;
import com.example.mthshop.activities.DetailsProductActivity;
import com.example.mthshop.adapter.RateCommentAdapter;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.ActivityDetailsProductBinding;
import com.example.mthshop.databinding.FragmentDetailsProductBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.BillDetails;
import com.example.mthshop.model.Product;
import com.example.mthshop.model.Rate;
import com.example.mthshop.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsProductFragment extends Fragment {
    private FragmentDetailsProductBinding thisCurrent;
    private Product productCurrent;
    private List<Rate> listRate;
    private User ownerProduct;
    private double averageStar;
    private List<BillDetails> listBillDetails;
    private List<User> listUserInfo;
    private int sold;
    private int AmountReview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisCurrent = FragmentDetailsProductBinding.inflate(getLayoutInflater());

        productCurrent = ((DetailsProductActivity)getActivity()).getProductCurrent();

        if (productCurrent == null)
            NotificationDiaLog.showDiaLogValidDate("Tải dữ liệu thất bại", getContext());
        else{
            NotificationDiaLog.showProgressBar(getContext());
            getOwnerProduct();
        }

        return thisCurrent.getRoot();
    }
    private void pushDataProduct() {
        thisCurrent.aDetailsProductTvNameUser.setText(ownerProduct.getNameUser());
        thisCurrent.aDetailsProductTvNameProduct.setText(productCurrent.getNameProduct());
        thisCurrent.aDetailsProductTvWarranty.setText(productCurrent.getWarranty());
        thisCurrent.aDetailsProductTvWhereProduction.setText(productCurrent.getWhereProduction());
        thisCurrent.aDetailsProductTvState.setText(productCurrent.getState());
        thisCurrent.aDetailsProductTvBatteryCapacity.setText(productCurrent.getBatteryCapacity());
        thisCurrent.aDetailsProductTvDescription.setText(productCurrent.getDescription());
        Glide.with(this).load(ownerProduct.getAvatar()).placeholder(R.drawable.ic_bell_second).into(thisCurrent.aDetailsProductImgAvatar);
        Glide.with(this).load(productCurrent.getImages()).placeholder(R.drawable.ic_bell_second).into(thisCurrent.aDetailsProductImgImages);

    }


    private void getOwnerProduct() {
        APIService.appService.callUser(productCurrent.getOwner()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                ownerProduct = response.body();
                Log.e("hihi", "bucqua");
                if (ownerProduct == null) {
                    NotificationDiaLog.showDiaLogValidDate("Lấy dữ liệu thất bại", getContext());
                    getActivity().finish();
                }else {
                    pushDataProduct();
                    getRatesByIdProduct();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                NotificationDiaLog.showDiaLogValidDate("Lấy dữ liệu thất bại", getContext());
                Log.e("user on details", t.toString());
                getActivity().finish();
            }
        });
    }

    private void getRatesByIdProduct() {
        APIService.appService.callRatesByIdProduct(productCurrent.getIdProduct()).enqueue(new Callback<List<Rate>>() {
            @Override
            public void onResponse(Call<List<Rate>> call, Response<List<Rate>> response) {
                List<Rate> tmp = response.body();
                if (tmp != null) {
                    listRate = new ArrayList<>();
                    for (Rate rate :
                            tmp) {
                        if (rate.getStatus() == 1) {
                            listRate.add(rate);
                        }
                    }
                }
                getBillDetailsByIdProduct();
                Log.e("fsadf", "fasdfasdfsa");
            }

            @Override
            public void onFailure(Call<List<Rate>> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                NotificationDiaLog.showDiaLogValidDate("Lấy dữ liệu thất bại", getContext());
                Log.e("rates on details", t.toString());
                getActivity().finish();
            }
        });
    }

    private void getBillDetailsByIdProduct() {
        APIService.appService.callBillDetailsByIdProduct(productCurrent.getIdProduct()).enqueue(new Callback<List<BillDetails>>() {
            @Override
            public void onResponse(Call<List<BillDetails>> call, Response<List<BillDetails>> response) {
                listBillDetails = response.body();
                getAllUserInfo();
            }

            @Override
            public void onFailure(Call<List<BillDetails>> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                NotificationDiaLog.showDiaLogValidDate("Lấy dữ liệu thất bại", getContext());
                getActivity().finish();
            }
        });
    }

    private void getAllUserInfo() {
        APIService.appService.callAllUserInfo().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                listUserInfo = response.body();
                NotificationDiaLog.dismissProgressBar();
                pushDataRateAndBillDetails();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                NotificationDiaLog.showDiaLogValidDate("Lấy dữ liệu thất bại", getContext());
                getActivity().finish();
            }
        });
    }

    private void pushDataRateAndBillDetails() {
        Log.e("pustRate", "pushasdf");
        if (listRate == null || listBillDetails == null)
        {
            if (listRate == null) Log.e("listRate", "null");
            if (listBillDetails == null) Log.e("listBillDetails", "null");
            sold = 0;
            averageStar = 0;
            Log.e("null", "null");
            AmountReview = 0;
        }else {
            getAverageStar();
            setRecycleViewRates();
            sold = listBillDetails.size();
            AmountReview = listRate.size();
            Log.e("ok", "ok");
        }


        thisCurrent.aDetailsProductTvReview.setText(AmountReview + " đánh giá");
        thisCurrent.aDetailsProductTvSold.setText( "Đã bán "+ sold);
        thisCurrent.aDetailsProductRBRateStarTop.setRating((float)averageStar);
        thisCurrent.aDetailsProductRBRateStarBottom.setRating((float)averageStar);
    }

    private void setRecycleViewRates() {
        RateCommentAdapter adapter = new RateCommentAdapter(listRate, listUserInfo, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        thisCurrent.aDetailsProductRvRate.setHasFixedSize(true);
        thisCurrent.aDetailsProductRvRate.setLayoutManager(linearLayoutManager);
        thisCurrent.aDetailsProductRvRate.setAdapter(adapter);
    }

    private void getAverageStar() {
        double tmp = 0;
        for (Rate rate : listRate) {
            tmp += rate.getRateStar();
        }
        averageStar = tmp/listRate.size();
    }
}
