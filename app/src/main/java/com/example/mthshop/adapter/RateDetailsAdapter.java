package com.example.mthshop.adapter;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mthshop.R;
import com.example.mthshop.activities.InputRateActivity;
import com.example.mthshop.activities.LoginActivity;
import com.example.mthshop.api.APIService;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.Product;
import com.example.mthshop.model.Rate;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateDetailsAdapter extends RecyclerView.Adapter<RateDetailsAdapter.ViewHolder> {
    private List<Rate> listRate;
    private Activity activity;


    public RateDetailsAdapter(List<Rate> listRate, Activity activity) {
        this.listRate = listRate;
        this.activity = activity;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(activity.getLayoutInflater().inflate(R.layout.item_rate_details, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rate tmp = listRate.get(position);
        holder.tvContent.setText(tmp.getContent());
        holder.tvDate.setText(tmp.getDate());
        holder.ratingBar.setRating(tmp.getRateStar());
        if (tmp.product != null) {
            holder.tvNameColor.setText(tmp.product.getNameColor());
            holder.tvNameProduct.setText(tmp.product.getNameProduct());
            holder.tvUser.setText(LoginActivity.userCurrent.getUser());
            Glide.with(activity).load(tmp.product.getImages()).placeholder(R.drawable.product).into(holder.imgProduct);
        }
        Glide.with(activity).load(LoginActivity.userCurrent.getAvatar()).placeholder(R.drawable.product).into(holder.imgUser);
        if (tmp.getStatus() == 0) holder.btnRate.setVisibility(View.VISIBLE);

        holder.btnRate.setOnClickListener((view) -> {
            Intent i = new Intent(activity, InputRateActivity.class);
            i.putExtra("rate", tmp);
            activity.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return listRate.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imgUser;
        TextView tvUser, tvContent, tvDate, tvNameProduct, tvNameColor;
        RatingBar ratingBar;
        ImageView imgProduct;
        AppCompatButton btnRate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.itemRateDetails_imgAvatar);
            tvUser = itemView.findViewById(R.id.itemRateDetails_tvUser);
            tvContent = itemView.findViewById(R.id.itemRateDetails_tvContent);
            tvDate = itemView.findViewById(R.id.itemRateDetails_tvDate);
            tvNameProduct = itemView.findViewById(R.id.itemRateDetails_tvNameProduct);
            tvNameColor = itemView.findViewById(R.id.itemRateDetails_tvNameColor);
            ratingBar = itemView.findViewById(R.id.itemRateDetails_rating);
            imgProduct = itemView.findViewById(R.id.itemRateDetails_imgProduct);
            btnRate = itemView.findViewById(R.id.itemRateDetails_btnRate);
        }
    }
}
