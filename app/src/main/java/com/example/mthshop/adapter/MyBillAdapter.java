package com.example.mthshop.adapter;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
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
import com.example.mthshop.activities.EditMyProductActivity;
import com.example.mthshop.activities.LoginActivity;
import com.example.mthshop.api.APIService;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.fortmat.FortMartData;
import com.example.mthshop.model.BillDetails;
import com.example.mthshop.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBillAdapter extends RecyclerView.Adapter<MyBillAdapter.ViewHolder> {
    private List<Product> listProduct;
    private Activity activity;
    private int status;// 3 cho lay hang || 4 dang giao || 2 cho xac nhan || 5 da huy || 1 da giao || 6 product cua user
    private BillDetails billDetails;

    public MyBillAdapter(List<Product> listProduct, Activity activity, int status) {
        this.listProduct = listProduct;
        this.activity = activity;
        this.status = status;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(activity.getLayoutInflater().inflate(R.layout.item_my_product, parent, false));
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
        if (status == 1) {
            stateDelivered(holder, position);
        }else if (status == 2) {
            stateWaitConfirmation(holder, position);
        }else if (status == 3) {
            stateWaitGetProduct(holder);
        }else if (status == 4) {
            stateDelivering(holder);
        }else if (status == 5) {
            stateCancelled(holder, position);
        }else if (status == 6) {
            myProduct(holder, position);
        }

    }

    private void myProduct(ViewHolder holder, int position) {
        holder.tvStatus.setText(LoginActivity.userCurrent.getUser());
        holder.btnResult.setText("Chỉnh sữa");
        holder.btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, EditMyProductActivity.class); // qua chinh sua product
                intent.putExtra("product" ,listProduct.get(position));
                activity.startActivity(intent);
            }
        });
        holder.btnDelete.setVisibility(View.VISIBLE);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // xoa san pham cua minh
                NotificationDiaLog.showProgressBar(activity);
                Product tmp = listProduct.get(position);
                tmp.setState("Hết hàng");
                putProduct(tmp);


            }
        });
    }

    private void removeSoldOutProduct(List<Product> listProduct) {
        List<Product> tmp = new ArrayList<>(listProduct);
        for (int i = 0; i < tmp.size(); i++) {
            if (tmp.get(i).getState().equalsIgnoreCase("Hết hàng")) {
                listProduct.remove(tmp.get(i));
            }
        }
    }

    private void putProduct(Product product) {
        APIService.appService.putProduct(product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                NotificationDiaLog.dismissProgressBar();
                removeSoldOutProduct(listProduct);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                Log.e("xoa hang", "false");
            }
        });
    }

    // da giao
    private void stateDelivered(ViewHolder holder, int position) {
        holder.tvStatus.setText("Đã giao");
        holder.btnResult.setText("Mua lại");
        holder.btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetailsProductActivity.class);
                intent.putExtra("product" ,listProduct.get(position));
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }

    //cho xac nhan
    private void stateWaitConfirmation(ViewHolder holder, int position) {
        holder.tvStatus.setText("Chờ xác nhận");
        holder.btnResult.setText("Hủy đơn hàng");
        holder.btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDiaLog.showProgressBar(activity);
                findBillDetails(position);
                listProduct.remove(position);
                notifyDataSetChanged();
            }
        });
    }
    //find bill detais
    private void findBillDetails(int position) {
        APIService.appService.findBillDetails(listProduct.get(position).inBill, listProduct.get(position).getIdProduct()).enqueue(new Callback<BillDetails>() {
            @Override
            public void onResponse(Call<BillDetails> call, Response<BillDetails> response) {
                billDetails = response.body();
                if (billDetails != null) {
                    updateCancelledBill();
                }
            }

            @Override
            public void onFailure(Call<BillDetails> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
            }
        });
    }

    private void updateCancelledBill() {
        billDetails.setStatus(5);
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


    //cho shop dua hang cho shipp
    private void stateWaitGetProduct(ViewHolder holder) {
        holder.tvStatus.setText("Chờ lấy hàng");
        holder.btnResult.setVisibility(View.GONE);
    }

    //nhung don hang dang giao
    private void stateDelivering(ViewHolder holder) {
        holder.tvStatus.setText("Hàng đang giao");
        holder.btnResult.setVisibility(View.GONE);
    }
    //nhung don hang da bi huy
    private void stateCancelled(ViewHolder holder, int position) {
        holder.tvStatus.setText("Hàng đã hủy");
        holder.btnResult.setText("Mua lại");
        holder.btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, DetailsProductActivity.class);
                intent.putExtra("product" ,listProduct.get(position));
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }
    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvOwner, tvNameProduct, tvPrice, tvStatus, tvColor;
        AppCompatButton btnResult, btnDelete;
        ImageView imgProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOwner = itemView.findViewById(R.id.itemMyProduct_tvOwner);
            tvNameProduct = itemView.findViewById(R.id.itemMyProduct_tvNameProduct);
            tvPrice = itemView.findViewById(R.id.itemMyProduct_tvPrice);
            tvStatus = itemView.findViewById(R.id.itemMyProduct_tvStatus);
            tvColor = itemView.findViewById(R.id.itemMyProduct_tvColor);
            btnResult = itemView.findViewById(R.id.itemMyProduct_btnEdit);
            imgProduct = itemView.findViewById(R.id.itemMyProduct_imgProduct);
            btnDelete = itemView.findViewById(R.id.itemMyProduct_btnDelete);
        }
    }
}
