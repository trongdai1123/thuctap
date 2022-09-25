package com.example.mthshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.mthshop.R;

public class ForgotPassword extends AppCompatActivity {
    private EditText edEmail;
    private AppCompatImageButton bntBack, btnSendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        //status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initWidgets();

        // tro lai trang login
        bntBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //xu ly send code


    }

    private void initWidgets() {
        edEmail = findViewById(R.id.aForgotP_edEmail);
        bntBack = findViewById(R.id.aForgotP_btnBack);
        btnSendCode = findViewById(R.id.aForgotP_btnSendCode);
    }
}