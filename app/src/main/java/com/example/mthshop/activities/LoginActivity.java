package com.example.mthshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mthshop.R;
import com.example.mthshop.api.APIService;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edUSerName, edPassWord;
    private LinearLayout lnGg, lnFb;
    private AppCompatButton btnLogin;
    private TextView tvForgotPass, tvSignUp;
    private ImageButton imgStatusEye;
    private boolean checkStatusPassword = true; // xu ly an hien pass word
    public static User userCurrent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWidgets();
        //status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));


        //hien hoac an pass word
        imgStatusEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStatusPassword) {
                    imgStatusEye.setImageResource(R.drawable.ic_eye_show);
                    edPassWord.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edPassWord.setSelection(edPassWord.getText().length());
                    checkStatusPassword = false;
                }else {
                    imgStatusEye.setImageResource(R.drawable.ic_eye_hidden);
                    edPassWord.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edPassWord.setSelection(edPassWord.getText().length());
                    checkStatusPassword = true;
                }

            }
        });

        //su kien khi focus edittext
        edUSerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setFocusEdittext(b, edUSerName);


            }
        });
        edPassWord.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setFocusEdittext(b, edPassWord);
            }
        });

        //su kien dang nhap bang fb
        lnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //su kien dang nhap bang gg
        lnGg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //su kien tao tai khoan
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));

            }
        });
        //su kien quen mat khau
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class));

            }
        });
        //su kien dang nhap
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = edUSerName.getText().toString().trim();
                String pass = edPassWord.getText().toString().trim();
                if (user.isEmpty() || pass.isEmpty()) {
                    if (user.isEmpty()) {
                        edUSerName.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.form_style_login_false));
                        NotificationDiaLog.showDiaLogValidDate("Ui!! Bạn chưa nhập email.", LoginActivity.this);
                    }
                    else {
                        edPassWord.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.form_style_login_false));
                        NotificationDiaLog.showDiaLogValidDate("Ui!! Bạn chưa nhập mật khẩu.", LoginActivity.this);
                    }

                }else {
                    NotificationDiaLog.showProgressBar(LoginActivity.this);
                    checkLoginOnApi(user, pass);
                }

            }
        });
    }

    private void initWidgets() {
        edUSerName = findViewById(R.id.aLogin_edUserName);
        edPassWord = findViewById(R.id.aLogin_edPassword);
        lnGg = findViewById(R.id.aLogin_lnLoginWithGoogle);
        lnFb = findViewById(R.id.aLogin_lnLoginWithFb);
        btnLogin = findViewById(R.id.aLogin_btnLogin);
        tvForgotPass = findViewById(R.id.aLogin_tvForgotPassword);
        tvSignUp = findViewById(R.id.aLogin_tvSingUp);
        imgStatusEye = findViewById(R.id.aLogin_imgStatusEye);
    }


    private void checkLoginOnApi(String user, String pass) {
        APIService.appService.callUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                NotificationDiaLog.dismissProgressBar();
                User tmp = response.body();
                if (tmp != null) {
                    if (tmp.getPassword().equals(pass)) {
                        userCurrent = tmp;
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                        edPassWord.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.form_style_login_false));
                        NotificationDiaLog.showDiaLogValidDate( "Sai mật khẩu !!", LoginActivity.this);
                    }
                }else {
                    edUSerName.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.form_style_login_false));
                    NotificationDiaLog.showDiaLogValidDate( "Tài khoản không tồn tại !!", LoginActivity.this);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                NotificationDiaLog.showDiaLogValidDate( "Lấy dữ liệu thất bại !!", LoginActivity.this);
            }
        });
    }








    private void setFocusEdittext(Boolean check, View v) {
        if (check) {
            v.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.form_style_login_focus));
        } else {
            v.setBackground(ContextCompat.getDrawable(LoginActivity.this, R.drawable.form_style_login));
        }
    }
}