package com.example.mthshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.mthshop.R;
import com.example.mthshop.databinding.ActivityChangeInfoAcountBinding;
import com.example.mthshop.dialog.NotificationDiaLog;

public class ChangeInfoAccount extends AppCompatActivity {
    private ActivityChangeInfoAcountBinding thisActivity;
    private String originValue;
    private String title;
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = ActivityChangeInfoAcountBinding.inflate(getLayoutInflater());
        setContentView(thisActivity.getRoot());
        //status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.back_ground));
        //
        title = getIntent().getStringExtra("title");
        originValue = getIntent().getStringExtra("value");
        type  = getIntent().getIntExtra("type", 0);
        //
        thisActivity.aChaneInfoTvTitle.setText(title);
        thisActivity.aChangeInfoEdInput.setText(originValue);

        //set listener
        thisActivity.aChangeInfoBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //
        thisActivity.aChangeInfoTvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = thisActivity.aChangeInfoEdInput.getText().toString().trim();
                if (value.isEmpty()) {
                    NotificationDiaLog.showDiaLogValidDate("Bạn không được bỏ rổng!" , ChangeInfoAccount.this);
                }else {
                    if (DetailsMeActivity.USER_NAME_CODE == type) {
                        Intent intent = new Intent();
                        intent.putExtra("value", value);
                        setResult(type, intent);
                        finish();
                    }
                }



            }
        });
    }
}