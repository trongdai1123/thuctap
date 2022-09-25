package com.example.mthshop.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mthshop.R;
import com.example.mthshop.adapter.CartAdapter;
import com.example.mthshop.adapter.OderAdapter;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.ActivityBuyDetailsBinding;
import com.example.mthshop.datalocal.SQLite;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.fortmat.FortMartData;
import com.example.mthshop.fragment.BuyDetailsFragment;
import com.example.mthshop.model.Bill;
import com.example.mthshop.model.BillDetails;
import com.example.mthshop.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyDetailsActivity extends AppCompatActivity {
    private ActivityBuyDetailsBinding thisActivity;
    private Product productCurrent;
    private int status; // mua ngay hay mua trogn gio hang
    private List<Product> listProduct;
    private double priceSale;
    private double priceTotal;
    private double priceTotalBill;
    private Bill billCurrent; // bill mau ngay
    private Bill billInCart; // bill trong cart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = ActivityBuyDetailsBinding.inflate(getLayoutInflater());
        setContentView(thisActivity.getRoot());
        //status bar
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //     //

        status = getIntent().getIntExtra("buyNow", 3);
        callBillInCart();
        //tinh toan bill
        setBill();
        //call fragment
        getSupportFragmentManager().beginTransaction().add(R.id.aBuyDetails_frameLayout, new BuyDetailsFragment()).commit();


        //

        //listener
        thisActivity.aBuyDetailsBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //listener dat hang
        thisActivity.aBuyDetailsBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDiaLog.dismissProgressBar();
                if (status == 0) {// oder trong gio hang
                    NotificationDiaLog.showProgressBar(BuyDetailsActivity.this);
                    buyInCart();
                }else {// mua ngay
                    NotificationDiaLog.showProgressBar(BuyDetailsActivity.this);
                    createBillBuyNow();

                }
            }
        });
    }

    private void buyInCart() { //bill status 0 -> 3
        billInCart.setStatus(3);
        billInCart.setDate(FortMartData.getDateCurrent());
        billInCart.setTotal(priceTotalBill);
        APIService.appService.putBill(billInCart).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                callBillDetails();
                Log.e("billInCart", "true");
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                Log.e("billInCart", "false");
            }
        });
    }

    private void callBillDetails() { //status 0 -> 2 trang thai cho xac nhan
        for (Product p: listProduct) {
            APIService.appService.findBillDetails(billInCart.getIdBill(), p.getIdProduct()).enqueue(new Callback<BillDetails>() {
                @Override
                public void onResponse(Call<BillDetails> call, Response<BillDetails> response) {
                    BillDetails billDetails = response.body();
                    if (billDetails != null) {
                        updateBillDetails(billDetails, listProduct.get(listProduct.size() - 1).getIdProduct()); // lấy product cuối cùng trong líst thực hiện đúng 1 lần intent
                        SQLite.addNotification(BuyDetailsActivity.this,"Bạn đặt thành công đơn hàng " + p.inBill  + " .Sản phẩm: " + p.getNameProduct() + " .Vào ngày " + billInCart.getDateNotification());
                    }


                }

                @Override
                public void onFailure(Call<BillDetails> call, Throwable t) {

                }
            });
        }
    }

    private void updateBillDetails(BillDetails billDetails, int idProduct) {
        billDetails.setStatus(2);
        APIService.appService.putBillDetails(billDetails).enqueue(new Callback<BillDetails>() {
            @Override
            public void onResponse(Call<BillDetails> call, Response<BillDetails> response) {
                NotificationDiaLog.dismissProgressBar();
                Log.e("billDetails", "true");
                if (billDetails.getIdProduct() == idProduct) {
                    NotificationDiaLog.showDiaLogValidDate("Mua hàng thành công!" , BuyDetailsActivity.this);
                    startActivity(new Intent(BuyDetailsActivity.this, MyBillsActivity.class));
                    finish();
                }

            }

            @Override
            public void onFailure(Call<BillDetails> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                NotificationDiaLog.showDiaLogValidDate("Mua thất bại!", BuyDetailsActivity.this);
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

    private void createBillBuyNow() {
        Bill tmp = new Bill(0, priceTotalBill, FortMartData.getDateCurrent(), 2, LoginActivity.userCurrent.getUser());
        APIService.appService.postBillCart(tmp).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {

            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                callBillBuyNow();
            }
        });
    }

    private void callBillBuyNow() {
        APIService.appService.callBillInCart(2, LoginActivity.userCurrent.getUser()).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                try {
                    billCurrent = response.body().get(0);
                    if (billCurrent != null) {
                        createBillDetails();
                    }
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {

            }
        });
    }

    private void createBillDetails() { // thêm các sản phẩm vào bill detais
        for(Product p : listProduct) {
            BillDetails billDetails = new BillDetails(0, billCurrent.getIdBill(), p.getIdProduct(), 2);
            APIService.appService.postBillDetails(billDetails).enqueue(new Callback<BillDetails>() {
                @Override
                public void onResponse(Call<BillDetails> call, Response<BillDetails> response) {


                }

                @Override
                public void onFailure(Call<BillDetails> call, Throwable t) {
                    changeStatusBill(); // chỉnh trạng thái mua ngay thành chờ đợi 2 -> 3
                    SQLite.addNotification(BuyDetailsActivity.this,"Bạn đặt thành công đơn hàng " + billCurrent.getIdBill()  + " .Sản phẩm: " + p.getNameProduct() + " .Vào ngày " + billCurrent.getDate());
                }
            });
        }

    }

    private void changeStatusBill() {
        billCurrent.setStatus(3);
        Log.e("bullCurrent", billCurrent.toString());
        APIService.appService.putBill(billCurrent).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                Log.e("put", "thanh cong");
                NotificationDiaLog.dismissProgressBar();
                NotificationDiaLog.showDiaLogValidDate("Mua thành công!", BuyDetailsActivity.this);

                Intent intent = new Intent(BuyDetailsActivity.this, MyBillsActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Log.e("put", "that bai");
            }
        });
    }


    private void setBill() {

        if (status == 0) {
            listProduct = (List<Product>) getIntent().getSerializableExtra("listProduct");
            double sale = 0;
            double total = 0;
            double totalBill = 0;
            for (Product p : listProduct) {
                double percent = (double) p.getSale() / 100;
                sale += (double) p.getPrice() * percent;
                total += p.getPrice();
                totalBill += (double) p.getPrice() * (1 - percent);
            }
            priceSale = sale;
            priceTotal = total;
            priceTotalBill = totalBill;



        } else {
            productCurrent = (Product) getIntent().getSerializableExtra("product");
            listProduct = new ArrayList<>();
            listProduct.add(productCurrent);
            double percent = (double) productCurrent.getSale() / 100;
            priceSale = (double) productCurrent.getPrice() * percent;
            priceTotal = productCurrent.getPrice();
            priceTotalBill = (double) productCurrent.getPrice() * (1 - percent);
        }
        thisActivity.aBuyDetailsTvTotal.setText(FortMartData.fortMartTypeDoubleToMoney(priceTotalBill));
    }

    public Product getProductCurrent() {
        return productCurrent;
    }

    public int getStatus() {
        return status;
    }

    public List<Product> getListProduct() {
        return listProduct;
    }

    public double getPriceSale() {
        return priceSale;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public double getPriceTotalBill() {
        return priceTotalBill;
    }
}