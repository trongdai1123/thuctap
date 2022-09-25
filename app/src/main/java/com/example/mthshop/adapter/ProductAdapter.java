package com.example.mthshop.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mthshop.R;
import com.example.mthshop.activities.DetailsProductActivity;
import com.example.mthshop.databinding.ItemMyProductBinding;
import com.example.mthshop.databinding.ItemViewProductBinding;
import com.example.mthshop.fortmat.FortMartData;
import com.example.mthshop.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Activity activity;
    private List<Product> listProduct;

    public ProductAdapter(Activity activity, List<Product> listProduct) {
        this.activity = activity;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(activity.getLayoutInflater().inflate(R.layout.item_view_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product productCurrent = listProduct.get(position);
        if (productCurrent.getSale() == 0) {
            holder.tvPrice.setText("\u20AB" + FortMartData.fortMartTypeDoubleToMoney(productCurrent.getPrice()));
            holder.tvPriceSale.setVisibility(View.GONE);
        }else {
            double sale = (double) productCurrent.getSale()/100;
            double priceSale = (double) productCurrent.getPrice() * (1 - sale);
            holder.tvPrice.setText(Html.fromHtml("<del>" + "\u20AB" + FortMartData.fortMartTypeDoubleToMoney(productCurrent.getPrice())  + "</del>"));
            holder.tvPrice.setTextColor(activity.getResources().getColor(R.color.status_false));
            holder.tvPrice.setTextSize(10);
            holder.tvPriceSale.setVisibility(View.VISIBLE);
            holder.tvPriceSale.setText("\u20AB" + FortMartData.fortMartTypeDoubleToMoney(priceSale));
        }
        holder.tvNameProduct.setText(productCurrent.getNameProduct());
        Glide.with(activity).load(productCurrent.getImages()).placeholder(R.drawable.ic_bell_second).into(holder.imgProduct);



    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNameProduct, tvPrice, tvPriceSale;
        ImageView imgProduct;
        public ViewHolder(@NonNull View view) {
            super(view);
            tvNameProduct = view.findViewById(R.id.itemProduct_tvNameProduct);
            tvPrice = view.findViewById(R.id.itemProduct_tvPrice);
            tvPriceSale = view.findViewById(R.id.itemProduct_tvPriceSale);
            imgProduct = view.findViewById(R.id.itemProduct_imgProduct);

            //qua chi tiết sản phẩm
            view.setOnClickListener(new View.OnClickListener() {
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
