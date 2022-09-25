package com.example.mthshop.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mthshop.R;
import com.example.mthshop.activities.LoginActivity;
import com.example.mthshop.fortmat.FortMartData;
import com.example.mthshop.model.Product;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class OderAdapter extends RecyclerView.Adapter<OderAdapter.ViewHolder> {
    private List<Product> listProduct;
    private Activity activity;

    public OderAdapter(List<Product> listProduct, Activity activity) {
        this.listProduct = listProduct;
        this.activity = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(activity.getLayoutInflater().inflate(R.layout.item_product_in_bill, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product productCurrent = listProduct.get(position);
        holder.tvUser.setText(LoginActivity.userCurrent.getUser());
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
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvPrice, tvPriceSale, tvNameProduct, tvUser;
        ImageView imgProduct;
        GifImageView imgGifSale;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.itemOder_tvNameProduct);
            tvUser = itemView.findViewById(R.id.itemOder_tvUser);
            tvPrice = itemView.findViewById(R.id.itemOder_tvPrice);
            tvPriceSale = itemView.findViewById(R.id.itemOder_tvPriceSale);
            imgProduct = itemView.findViewById(R.id.itemOder_imgProduct);
            imgGifSale = itemView.findViewById(R.id.itemOder_imgGifSale);
        }
    }
}
