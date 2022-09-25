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
import com.example.mthshop.databinding.ActivityHeadPhone2Binding;
import com.example.mthshop.databinding.ActivityLaptopBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HeadPhoneActivity extends AppCompatActivity {
    private ActivityHeadPhone2Binding thisFragment;
    private ProductAdapter phoneAdapter;
    private List<Product> listProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisFragment = ActivityHeadPhone2Binding.inflate(getLayoutInflater());
        //status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.back_ground));
        setContentView(thisFragment.getRoot());

        NotificationDiaLog.showProgressBar(this);

        thisFragment.fHeadPhoneImgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getListHeadPhone();

    }

    private void getListHeadPhone() {
        APIService.appService.callProductsByIDCategory(1003).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                NotificationDiaLog.dismissProgressBar();
                listProduct = response.body();
                if (listProduct == null)
                    NotificationDiaLog.showDiaLogValidDate("Tải dữ liệu thất bại.", HeadPhoneActivity.this);


                //adapter
                setHeadPhoneAdapter();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                NotificationDiaLog.showDiaLogValidDate("Không thể gọi dữ liệu.", HeadPhoneActivity.this);
            }
        });
    }

    private void setHeadPhoneAdapter() {
        GridLayoutManager tmp = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        phoneAdapter = new ProductAdapter(this, listProduct);
        thisFragment.fHeadPhoneRecyclerView.setLayoutManager(tmp);
        thisFragment.fHeadPhoneRecyclerView.setHasFixedSize(true);
        thisFragment.fHeadPhoneRecyclerView.setAdapter(phoneAdapter);
    }
}