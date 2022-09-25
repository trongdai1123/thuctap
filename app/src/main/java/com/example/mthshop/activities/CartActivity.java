package com.example.mthshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.mthshop.R;
import com.example.mthshop.adapter.CartAdapter;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.ActivityCartBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.fortmat.FortMartData;
import com.example.mthshop.model.Bill;
import com.example.mthshop.model.BillDetails;
import com.example.mthshop.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding thisActivity;
    private CartAdapter cartAdapter;
    private Bill billInCart;
    private List<Product> listProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        //
        thisActivity = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(thisActivity.getRoot());
        //listener back
        thisActivity.aCartImgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listProduct = new ArrayList<>();
        NotificationDiaLog.showProgressBar(this);
        callBillInCart();

        //mua heest gio hang
        thisActivity.aCartBtnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("size", listProduct.size() + "");
                if (listProduct.size() == 0) {
                    NotificationDiaLog.showDiaLogValidDate("Giỏ hàng đang rỗng mỡi thêm sản phẩm!", CartActivity.this);
                }else {
                    Intent intent = new Intent(CartActivity.this, BuyDetailsActivity.class);
                    intent.putExtra("buyNow", 0);
                    intent.putExtra("listProduct", (ArrayList)listProduct);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

//    private void buyNow() { //bill status 1 -> 3
//        billInCart.setStatus(3);
//
//        APIService.appService.putBill(billInCart).enqueue(new Callback<Bill>() {
//            @Override
//            public void onResponse(Call<Bill> call, Response<Bill> response) {
//                callBillDetails();
//                Log.e("billInCart", "true");
//            }
//
//            @Override
//            public void onFailure(Call<Bill> call, Throwable t) {
//                NotificationDiaLog.dismissProgressBar();
//                Log.e("billInCart", "false");
//            }
//        });
//    }

//    private void callBillDetails() { //status 0 -> 2 trang thai cho xac nhan
//        for (Product p: listProduct) {
//            APIService.appService.findBillDetails(billInCart.getIdBill(), p.getIdProduct()).enqueue(new Callback<BillDetails>() {
//                @Override
//                public void onResponse(Call<BillDetails> call, Response<BillDetails> response) {
//                    BillDetails billDetails = response.body();
//                    if (billDetails != null)
//                        updateBillDetails(billDetails);
//                }
//
//                @Override
//                public void onFailure(Call<BillDetails> call, Throwable t) {
//
//                }
//            });
//        }
//    }

//    private void updateBillDetails(BillDetails billDetails) {
//        billDetails.setStatus(2);
//        APIService.appService.putBillDetails(billDetails).enqueue(new Callback<BillDetails>() {
//            @Override
//            public void onResponse(Call<BillDetails> call, Response<BillDetails> response) {
//                listProduct.clear();
//                setAdapterRecycleView(false);
//                NotificationDiaLog.dismissProgressBar();
//                NotificationDiaLog.showDiaLogValidDate("Mua thành công!", CartActivity.this);
//                Log.e("billDetails", "true");
//                startActivity(new Intent(CartActivity.this, MyBillsActivity.class));
//                finish();
//            }
//
//            @Override
//            public void onFailure(Call<BillDetails> call, Throwable t) {
//                NotificationDiaLog.dismissProgressBar();
//                NotificationDiaLog.showDiaLogValidDate("Mua thất bại!", CartActivity.this);
//            }
//        });
//    }


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
                        Log.e(thisActivity.toString(), t.toString());

                    }
                });
    }

    private void callProductInCart() {
        APIService.appService.callProductInCart(billInCart.getIdBill()).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {

                listProduct = response.body();
                if (listProduct != null && listProduct.size() != 0)
                    for (int i = 0; i < listProduct.size(); i++) {
                        listProduct.get(i).inBill = billInCart.getIdBill();
                    }

                Log.e("hoho", listProduct.get(0).toString());
                NotificationDiaLog.dismissProgressBar();
                setAdapterRecycleView(false);
                setCheckAll();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                Log.e(thisActivity.toString(), t.toString());
            }
        });
    }

    private void setAdapterRecycleView(Boolean b) {
        totalBill();

        thisActivity.aCartTvEmpty.setVisibility(View.GONE);
        cartAdapter = new CartAdapter(listProduct, this, b);
        thisActivity.aCartTvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDiaLog.showProgressBar(CartActivity.this);

                cartAdapter.deleteProductInCart();

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        thisActivity.aCartRecyclerView.setHasFixedSize(true);
        thisActivity.aCartRecyclerView.setLayoutManager(linearLayoutManager);
        thisActivity.aCartRecyclerView.setAdapter(cartAdapter);
    }

    private void setCheckAll() {
        thisActivity.aCartChkSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!compoundButton.isPressed()) {
                    return;
                }
                setAdapterRecycleView(b);
            }
        });
    }

    public void referenceCheckAll(boolean b) {
        thisActivity.aCartChkSelectAll.setChecked(b);
    }

    public void totalBill() {
        double total = 0;
        for (Product p : listProduct) {
            double sale = (double) p.getSale() / 100;
            double priceSale = (double) p.getPrice() * (1 - sale);
            total += priceSale;
        }
        if (total == 0 || listProduct.size() == 0) {
            thisActivity.aCartTvEmpty.setVisibility(View.VISIBLE);
            thisActivity.aCartChkSelectAll.setChecked(false);
            checkStatusDelete(0);
        }
        billInCart.setTotal(total);
        thisActivity.aCartTvTotalPay.setText("Tổng thanh toán \u20AB" + FortMartData.fortMartTypeDoubleToMoney(total));
    }



    public void checkStatusDelete(int i) {
        if (i == 0) {
            thisActivity.aCartTvDelete.setTextColor(getResources().getColor(R.color.second_text));
            thisActivity.aCartTvDelete.setEnabled(false);
        } else {
            thisActivity.aCartTvDelete.setTextColor(getResources().getColor(R.color.main));
            thisActivity.aCartTvDelete.setEnabled(true);
        }
    }

}