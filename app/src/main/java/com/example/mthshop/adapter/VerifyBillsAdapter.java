package com.example.mthshop.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mthshop.R;
import com.example.mthshop.activities.DetailsProductActivity;
import com.example.mthshop.api.APIService;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.fortmat.FortMartData;
import com.example.mthshop.model.Bill;
import com.example.mthshop.model.BillDetails;
import com.example.mthshop.model.Product;
import com.example.mthshop.model.Rate;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyBillsAdapter extends RecyclerView.Adapter<VerifyBillsAdapter.ViewHolder> {
    private List<Product> listProduct;
    private Activity activity;
    private int status;// 3 cho lay hang || 4 dang giao || 2 cho xac nhan || 5 da huy || 1 da giao | 5 huy
    private BillDetails billDetails;

    public VerifyBillsAdapter(List<Product> listProduct, Activity activity, int status) {
        this.listProduct = listProduct;
        this.activity = activity;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(activity.getLayoutInflater().inflate(R.layout.item_verify_bills, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product productCurrent = listProduct.get(position);
        holder.tvOwner.setText(productCurrent.getOwner());
        holder.tvNameProduct.setText(productCurrent.getNameProduct());
        holder.tvColor.setText(productCurrent.getNameColor());
        double sale = (double) productCurrent.getSale()/100;
        double priceSale = (double) productCurrent.getPrice() * (1 - sale);
        holder.tvPrice.setText("\u20AB" + FortMartData.fortMartTypeDoubleToMoney(priceSale));
        Glide.with(activity).load(productCurrent.getImages()).placeholder(R.drawable.product).into(holder.imgProduct);
        if (status == 1) { // da giao
            stateDelivered(holder);
        }else if (status == 2) { // xac nhan
            stateWaitConfirmation(holder, position, 3);
        }else if (status == 3) { // lay hang
            stateWaitGetProduct(holder, position, 4);
        }else if (status == 4) { // dang giao
            stateDelivering(holder, position, 1);
        }else if (status == 5) { // huy
            stateCancelled(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    //nhung don hang da bi huy
    private void stateCancelled(ViewHolder holder, int position) {
        holder.tvStatus.setText("Đơn hàng đã hủy");
        holder.btnResult.setVisibility(View.GONE);
    }

    // da giao
    private void stateDelivered(ViewHolder holder) {
        holder.tvStatus.setText("Đã giao");
        holder.btnResult.setVisibility(View.GONE);
    }

    //nhung don hang dang giao
    private void stateDelivering(ViewHolder holder, int position, int status) {
        holder.tvStatus.setText("Đang giao");
        holder.btnResult.setText("Đã giao");
        holder.btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDiaLog.showProgressBar(activity);
                findBillDetails(position, status);
                callBill(listProduct.get(position));
                listProduct.remove(position);
                notifyDataSetChanged();

            }
        });
    }

    private void callBill(Product product) {
        APIService.appService.callBill(product.inBill).enqueue(new Callback<Bill>() {
            @Override
            public void onResponse(Call<Bill> call, Response<Bill> response) {
                Bill bill = response.body();
                if (bill != null) {
                    Rate rate = new Rate(0, "", "", 0, 0, bill.getOwner(), product.getIdProduct());
                    Log.e("calBill in rate", bill.toString());
                    addRate(rate);
                }
            }

            @Override
            public void onFailure(Call<Bill> call, Throwable t) {
                Log.e("calBill in rate", t.toString());
            }
        });
    }

    private void addRate(Rate rate){
        APIService.appService.addRate(rate).enqueue(new Callback<Rate>() {
            @Override
            public void onResponse(Call<Rate> call, Response<Rate> response) {
                Log.e("addRate in rate", rate.toString());
            }

            @Override
            public void onFailure(Call<Rate> call, Throwable t) {
                Log.e("addRate in rate", t.toString());
            }
        });
    }

    //cho shop dua hang cho shipper
    private void stateWaitGetProduct(ViewHolder holder, int position, int status) {
        holder.tvStatus.setText("Lấy hàng");
        holder.btnResult.setText("Đã lấy hàng");
        holder.btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDiaLog.showProgressBar(activity);
                findBillDetails(position, status);
                listProduct.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    //tao rate khi don hang da giao


    //cho xac nhan
    private void stateWaitConfirmation(ViewHolder holder, int position, int status) {
        holder.tvStatus.setText("Xác nhận");
        holder.btnResult.setText("Xác nhận");
        holder.btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDiaLog.showProgressBar(activity);
                findBillDetails(position, status);
                listProduct.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.btnDelete.setVisibility(View.VISIBLE);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDiaLog.showProgressBar(activity);
                findBillDetails(position, 5);
                listProduct.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    //find bill detais
    private void findBillDetails(int position, int status) {
        APIService.appService.findBillDetails(listProduct.get(position).inBill, listProduct.get(position).getIdProduct()).enqueue(new Callback<BillDetails>() {
            @Override
            public void onResponse(Call<BillDetails> call, Response<BillDetails> response) {
                billDetails = response.body();
                if (billDetails != null) {
                    updateChangeStateBill(status);
                }
            }

            @Override
            public void onFailure(Call<BillDetails> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
            }
        });
    }
    //
    private void updateChangeStateBill(int status) {
        billDetails.setStatus(status);
        APIService.appService.putBillDetails(billDetails).enqueue(new Callback<BillDetails>() {
            @Override
            public void onResponse(Call<BillDetails> call, Response<BillDetails> response) {
                Log.e("bill-details", response.body().toString());
                NotificationDiaLog.dismissProgressBar();
            }

            @Override
            public void onFailure(Call<BillDetails> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvOwner, tvNameProduct, tvPrice, tvStatus, tvColor;
        AppCompatButton btnResult, btnDelete;
        ImageView imgProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOwner = itemView.findViewById(R.id.itemVerifyMyProduct_tvOwner);
            tvNameProduct = itemView.findViewById(R.id.itemVerifyMyProduct_tvNameProduct);
            tvPrice = itemView.findViewById(R.id.itemVerifyMyProduct_tvPrice);
            tvStatus = itemView.findViewById(R.id.itemVerifyMyProduct_tvStatus);
            tvColor = itemView.findViewById(R.id.itemVerifyMyProduct_tvColor);
            btnResult = itemView.findViewById(R.id.itemVerifyMyProduct_btnEdit);
            imgProduct = itemView.findViewById(R.id.itemVerifyMyProduct_imgProduct);
            btnDelete = itemView.findViewById(R.id.itemVerifyMyProduct_btnDelete);
        }
    }
}
