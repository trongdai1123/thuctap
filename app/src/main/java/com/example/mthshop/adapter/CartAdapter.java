package com.example.mthshop.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mthshop.R;
import com.example.mthshop.activities.CartActivity;
import com.example.mthshop.activities.DetailsProductActivity;
import com.example.mthshop.api.APIService;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.fortmat.FortMartData;
import com.example.mthshop.model.BillDetails;
import com.example.mthshop.model.Product;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Product> listProduct;
    private Activity activity;
    private List<Integer> positionList = new ArrayList<>();
    private boolean checkAll;

    public CartAdapter(List<Product> listProduct, Activity activity, boolean checkAll) {
        this.listProduct = listProduct;
        this.activity = activity;
        this.checkAll = checkAll;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(activity.getLayoutInflater().inflate(R.layout.item_cart_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product productCurrent = listProduct.get(position);
        holder.tvNameProduct.setText(productCurrent.getNameProduct());
        if (productCurrent.getSale() == 0) {
            holder.tvPrice.setText( "\u20AB" + FortMartData.fortMartTypeDoubleToMoney(productCurrent.getPrice()));
            holder.tvPriceSale.setVisibility(View.GONE);
            holder.imgGifSale.setVisibility(View.GONE);

        }else {
            double sale = (double) productCurrent.getSale()/100;
            double priceSale = (double) productCurrent.getPrice() * (1 - sale);
            holder.tvPrice.setText(Html.fromHtml("<del>"+ "\u20AB" + FortMartData.fortMartTypeDoubleToMoney(productCurrent.getPrice())+"</del>"));
            holder.tvPrice.setTextColor(activity.getResources().getColor(R.color.status_false));
            holder.tvPrice.setTextSize(10);
            holder.tvPriceSale.setVisibility(View.VISIBLE);
            holder.imgGifSale.setVisibility(View.VISIBLE);
            holder.tvPriceSale.setText("\u20AB" + FortMartData.fortMartTypeDoubleToMoney(priceSale));
        }
        Glide.with(activity).load(productCurrent.getImages()).placeholder(R.drawable.ic_bell_second).into(holder.imgProduct);
        ((CartActivity)activity).checkStatusDelete(positionList.size());
        if (checkAll) {
            holder.checkBox.setChecked(true);
            positionList.clear();
            for (int i = 0; i < listProduct.size(); i++) {

                positionList.add(i);
            }
        }
        checkStateAll();

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    positionList.add(holder.getAdapterPosition());
                }else {
                    positionList.remove(new Integer(holder.getAdapterPosition()));
                }
                ((CartActivity)activity).checkStatusDelete(positionList.size());
                checkStateAll();
            }
        });
    }

    private void checkStateAll() {
        if (positionList.size() == listProduct.size()) {
            ((CartActivity)activity).referenceCheckAll(true);
        }else {
            ((CartActivity)activity).referenceCheckAll(false);
        }
        ((CartActivity)activity).checkStatusDelete(positionList.size());
    }

    public void deleteProductInCart() {
        //tao list tmp de luu du lieu cua list origin dung de xoa list origin de sau khi xoa khong bi out list
        List<Product> tmp = new ArrayList<>(listProduct);
        if (positionList.size() == 0) return;

        for (Integer i :
                positionList) {
            callBillDetailsInBill(i, tmp);
            Log.e("Integer i", i + "");
        }
    }

    private void callBillDetailsInBill(int i, List<Product> tmp) {

        APIService.appService.findBillDetails(listProduct.get(i).inBill, listProduct.get(i).getIdProduct()).enqueue(new Callback<BillDetails>() {
            @Override
            public void onResponse(Call<BillDetails> call, Response<BillDetails> response) {
                BillDetails billDetails = response.body();
                if (billDetails != null)
                    deleteBillDetails(billDetails, i, tmp);

            }

            @Override
            public void onFailure(Call<BillDetails> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
            }
        });
    }

    private void deleteBillDetails(BillDetails billDetails, int i, List<Product> tmp) {
        Log.e("deleteBillDetails",  "hihi");
        APIService.appService.deleteBillDetails(billDetails.getIdBillDetails()).enqueue(new Callback<BillDetails>() {
            @Override
            public void onResponse(Call<BillDetails> call, Response<BillDetails> response) {
                NotificationDiaLog.dismissProgressBar();
                Log.e("deleteBillDetails",  "true" + billDetails.getIdBill());
                listProduct.remove(i);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BillDetails> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                Log.e("deleteBillDetails",  "false" + billDetails.getIdBill());
                listProduct.remove(tmp.get(i));
                ((CartActivity)activity).totalBill();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvPrice, tvPriceSale, tvNameProduct;
        ImageView imgProduct;
        GifImageView imgGifSale;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.itemCart_tvNameProduct);
            checkBox = itemView.findViewById(R.id.itemCart_checkBox);
            tvPrice = itemView.findViewById(R.id.itemCart_tvPrice);
            tvPriceSale = itemView.findViewById(R.id.itemCart_tvPriceSale);
            imgProduct = itemView.findViewById(R.id.itemCart_imgProduct);
            imgGifSale = itemView.findViewById(R.id.itemCart_imgGifSale);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity, DetailsProductActivity.class);
                    intent.putExtra("product", listProduct.get(getAdapterPosition()));
                    activity.startActivity(intent);
                }
            });
        }
    }
}
