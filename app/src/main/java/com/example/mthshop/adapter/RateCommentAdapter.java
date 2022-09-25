package com.example.mthshop.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mthshop.R;
import com.example.mthshop.model.Rate;
import com.example.mthshop.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RateCommentAdapter extends RecyclerView.Adapter<RateCommentAdapter.ViewHolder> {
    private List<Rate> listRates;
    private List<User> listUsers;
    private Activity activity;

    public RateCommentAdapter(List<Rate> listRates, List<User> listUsers, Activity activity) {
        this.listRates = listRates;
        this.listUsers = listUsers;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rate_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Rate ratCurrent = listRates.get(position);
        User tmp = null;
        holder.tvContent.setText(ratCurrent.getContent());
        holder.tvDate.setText(ratCurrent.getDate());
        holder.ratingBar.setRating(ratCurrent.getRateStar());
        for (User user:listUsers) {
            if (user.getUser().equals(ratCurrent.getOwner())) {
                tmp = user;
                break;
            }
        }
        if (tmp != null) {
            Glide.with(activity).load(tmp.getAvatar()).placeholder(R.drawable.ic_bell_second).into(holder.imgAvatar);
            holder.tvUser.setText(tmp.getUser());
        }

    }

    @Override
    public int getItemCount() {
        return listRates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContent, tvUser, tvDate;
        CircleImageView imgAvatar;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.itemRate_tvContent);
            tvUser = itemView.findViewById(R.id.itemRate_tvUser);
            tvDate = itemView.findViewById(R.id.itemRate_tvDate);
            imgAvatar = itemView.findViewById(R.id.itemRate_imgAvatar);
            ratingBar = itemView.findViewById(R.id.itemRate_rating);
        }
    }


}
