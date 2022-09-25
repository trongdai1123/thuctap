package com.example.mthshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.mthshop.R;
import com.example.mthshop.adapter.ProductAdapter;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.ActivityLaptopBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaptopActivity extends AppCompatActivity {
    private ActivityLaptopBinding thisFragment;
    private ProductAdapter phoneAdapter;
    private List<Product> listProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFragment = ActivityLaptopBinding.inflate(getLayoutInflater());
        //status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.back_ground));
        setContentView(thisFragment.getRoot());

        //
        NotificationDiaLog.showProgressBar(this);
        thisFragment.fLaptopImgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getListLaptop();
    }

    private void getListLaptop() {
        APIService.appService.callProductsByIDCategory(1001).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                NotificationDiaLog.dismissProgressBar();
                listProduct = response.body();
                if (listProduct == null)
                    NotificationDiaLog.showDiaLogValidDate("Tải dữ liệu thất bại.", LaptopActivity.this);


                //adapter
                setLaptopAdapter();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                NotificationDiaLog.showDiaLogValidDate("Không thể gọi dữ liệu.", LaptopActivity.this);
            }
        });
    }

    private void setLaptopAdapter() {
        GridLayoutManager tmp = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        phoneAdapter = new ProductAdapter(this, listProduct);
        thisFragment.fLaptopRecyclerView.setLayoutManager(tmp);
        thisFragment.fLaptopRecyclerView.setHasFixedSize(true);
        thisFragment.fLaptopRecyclerView.setAdapter(phoneAdapter);
    }
}