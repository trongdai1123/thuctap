package com.example.mthshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;

import com.cloudinary.Api;
import com.example.mthshop.R;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.ActivityInputRateBinding;
import com.example.mthshop.databinding.ActivityRateBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.fortmat.FortMartData;
import com.example.mthshop.model.Rate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputRateActivity extends AppCompatActivity {
    private ActivityInputRateBinding thisActivity;
    private int ratingValues;
    private Rate rateCurrent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = ActivityInputRateBinding.inflate(getLayoutInflater());
        ////status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        setContentView(thisActivity.getRoot());
        rateCurrent = (Rate) getIntent().getSerializableExtra("rate");
        //sett listener
        thisActivity.aInputRateBack.setOnClickListener( view -> { finish(); });
        thisActivity.aInputRateRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingValues = (int) v;
            }
        });

        thisActivity.aInputBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = thisActivity.aInputRateEdContent.getText().toString().trim();
                if (ratingValues <= 0) {
                    NotificationDiaLog.showDiaLogValidDate("Bạn chưa đánh giá sao !" ,InputRateActivity.this);
                }else {
                    rateCurrent.setStatus(1);
                    rateCurrent.setRateStar(ratingValues);
                    rateCurrent.setContent(content);
                    rateCurrent.setDate(FortMartData.getDateCurrent());
                    editRate();
                }
            }
        });

    }

    private void editRate() {
        NotificationDiaLog.showProgressBar(this);
        APIService.appService.editRate(rateCurrent).enqueue(new Callback<Rate>() {
            @Override
            public void onResponse(Call<Rate> call, Response<Rate> response) {
                NotificationDiaLog.dismissProgressBar();
                finish();
            }

            @Override
            public void onFailure(Call<Rate> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                finish();
            }
        });
    }


}