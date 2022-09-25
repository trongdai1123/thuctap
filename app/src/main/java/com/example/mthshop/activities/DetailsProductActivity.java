

package com.example.mthshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mthshop.R;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.ActivityDetailsProductBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.fortmat.FortMartData;
import com.example.mthshop.fragment.DetailsProductFragment;
import com.example.mthshop.model.Bill;
import com.example.mthshop.model.BillDetails;
import com.example.mthshop.model.Product;
import com.example.mthshop.model.Rate;
import com.example.mthshop.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsProductActivity extends AppCompatActivity {
    private ActivityDetailsProductBinding thisActivity;
    private Product productCurrent;
    private Bill billInCart;
    private List<Product> listProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = ActivityDetailsProductBinding.inflate(getLayoutInflater());
        //status bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(thisActivity.getRoot());

        productCurrent = (Product) getIntent().getSerializableExtra("product");
        //add fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fDetailsProduct_frameLayout, new DetailsProductFragment()).commit();
        //listener
        thisActivity.fDetailsProductBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        thisActivity.fDetailsProductBtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailsProductActivity.this, CartActivity.class));
            }
        });
        //block sản phảm của chính mình
        if (LoginActivity.userCurrent.getUser().equals(productCurrent.getOwner())){
            thisActivity.fDetailsProductBtnBuyNow.setBackgroundColor(getResources().getColor(R.color.second_text));
        }else {
            //mua ngay
            thisActivity.fDetailsProductBtnBuyNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailsProductActivity.this, BuyDetailsActivity.class);
                    intent.putExtra("product", productCurrent);
                    intent.putExtra("buyNow", 1);
                    startActivity(intent);
                }
            });
        }
        //add Cart
        thisActivity.fDetailsProductLnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginActivity.userCurrent.getUser().equals(productCurrent.getOwner())){
                    NotificationDiaLog.showDiaLogValidDate("Không thể thên sản phẩm của chính mình vào giỏ !", DetailsProductActivity.this);
                }else
                    addProductInCart();
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        //call api
        callBillInCart();
    }

    private void addProductInCart() {
        if (billInCart != null) {
            if (checkProduct()) {
                BillDetails billDetails = new BillDetails(0, billInCart.getIdBill(), productCurrent.getIdProduct(), 0);
                APIService.appService.postBillDetails(billDetails).enqueue(new Callback<BillDetails>() {
                    @Override
                    public void onResponse(Call<BillDetails> call, Response<BillDetails> response) {
                        Log.e("BillDetais", "ok");
                        callProductInCart();
                    }

                    @Override
                    public void onFailure(Call<BillDetails> call, Throwable t) {
                        Log.e("BillDetais", "hihi");
                        callProductInCart();
                    }
                });
            }
            else {
                NotificationDiaLog.showDiaLogValidDate("Sản phẩm đã có trong giỏ hàng!", this);
            }
            Log.e("billCart", billInCart.toString());
            }

    }

    private void createCartInBill() {
        String date = FortMartData.getDateCurrent();
        Bill bill = new Bill(0, 0, date, 0, LoginActivity.userCurrent.getUser());
        APIService.appService.postBillCart(bill).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                Log.e(thisActivity.toString() + "cart" , "ok");
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Log.e(thisActivity.toString() + "cart" , "false");
                callBillInCart();

            }
        });
    }

    private void callBillInCart() {
        APIService.appService.callBillInCart(0, LoginActivity.userCurrent.getUser())
                .enqueue(new Callback<List<Bill>>() {
                    @Override
                    public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                        try {
                            billInCart = response.body().get(0);
                            Log.e("hoho", billInCart.getIdBill() + "");
                            callProductInCart();

                        } catch (Exception e) {
                            billInCart = null;
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Bill>> call, Throwable t) {
                        NotificationDiaLog.dismissProgressBar();
                        setInfoCart(0);
                        createCartInBill();

                        Log.e(thisActivity.toString(), t.toString());

                    }
                });
    }

    private void callProductInCart() {
        APIService.appService.callProductInCart(billInCart.getIdBill()).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                listProduct = response.body();
                if (listProduct != null)
                    setInfoCart(listProduct.size());


                NotificationDiaLog.dismissProgressBar();
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                setInfoCart(0);
                Log.e(thisActivity.toString(), t.toString());
            }
        });
    }

    private boolean checkProduct() {
        if (listProduct != null) {
            for (Product product : listProduct) {
                if (product.getIdProduct() == productCurrent.getIdProduct()) {
                    return false;
                }
            }
        }
        return true;
    }


    public Product getProductCurrent() {
        return productCurrent;
    }
    private void setInfoCart(int i) {
        if (i <= 0) {
            thisActivity.fDetailsProductTvInfoCart.setVisibility(View.GONE);
        }else {
            thisActivity.fDetailsProductTvInfoCart.setVisibility(View.VISIBLE);
            thisActivity.fDetailsProductTvInfoCart.setText(i + "");
        }
    }
}