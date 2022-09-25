package com.example.mthshop.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mthshop.R;
import com.example.mthshop.adapter.ProductAdapter;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.ActivityPhoneBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneActivity extends AppCompatActivity {
    private ActivityPhoneBinding activityPhoneBinding;
    private ProductAdapter phoneAdapter;
    private List<Product> listProduct;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPhoneBinding = ActivityPhoneBinding.inflate(getLayoutInflater());
        setContentView(activityPhoneBinding.getRoot());
        //status bar
        //status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.back_ground));
        NotificationDiaLog.showProgressBar(this);
        //listener
        activityPhoneBinding.fPhoneImgBntBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //
        getListPhone();


    }

    private void getListPhone() {
        APIService.appService.callProductsByIDCategory(1000).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                NotificationDiaLog.dismissProgressBar();
                listProduct = response.body();
                if (listProduct == null)
                    NotificationDiaLog.showDiaLogValidDate("Tải dữ liệu thất bại.", PhoneActivity.this);


                //adapter
                setPhoneAdapter();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                NotificationDiaLog.showDiaLogValidDate("Không thể gọi dữ liệu.", PhoneActivity.this);
            }
        });
    }


    private void setPhoneAdapter() {
        GridLayoutManager tmp = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        phoneAdapter = new ProductAdapter(this, listProduct);
        activityPhoneBinding.fPhoneRecyclerView.setLayoutManager(tmp);
        activityPhoneBinding.fPhoneRecyclerView.setHasFixedSize(true);
        activityPhoneBinding.fPhoneRecyclerView.setAdapter(phoneAdapter);
    }

}
