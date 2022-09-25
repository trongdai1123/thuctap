package com.example.mthshop.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mthshop.R;
import com.example.mthshop.dialog.NotificationDiaLog;

public class SignUpActivity extends AppCompatActivity {
    private EditText edName, edEmail, edPass, edConfirmPass;
    private ImageButton imgEyeP, imgEyeCP;
    private AppCompatButton btnSignUp;
    private TextView tvSignIn;
    private boolean checkStatusPassword = true;
    private boolean checkStatusCP = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initWidgets();

        //hien hoac an pass word
        setShowHiddenPass();

        //su kien khi focus edittext
        setFocusEdittext();

        //su kien an dang ky
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edName.getText().toString();
                String email = edEmail.getText().toString();
                String pass = edPass.getText().toString();
                String passConfirm = edConfirmPass.getText().toString();

                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || passConfirm.isEmpty()) {
                    if (name.isEmpty()) {
                        edName.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.form_style_login_false));
                        NotificationDiaLog.showDiaLogValidDate("Ui!! Bạn chưa nhập họ và tên.", SignUpActivity.this);
                    }else if(email.isEmpty()){
                        edEmail.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.form_style_login_false));
                        NotificationDiaLog.showDiaLogValidDate("Ui!! Bạn chưa nhập email.", SignUpActivity.this);
                    }else if(pass.isEmpty()) {
                        edPass.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.form_style_login_false));
                        NotificationDiaLog.showDiaLogValidDate("Ui!! Bạn chưa nhập mật khẩu.", SignUpActivity.this);
                    }else {
                        edConfirmPass.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.form_style_login_false));
                        NotificationDiaLog.showDiaLogValidDate("Ui!! Bạn chưa nhập xác nhận mật khẩu.", SignUpActivity.this);
                    }
                }else {
                    if (pass.equals(passConfirm)) {
                        //to do
                    }else {
                        edConfirmPass.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.form_style_login_false));
                        NotificationDiaLog.showDiaLogValidDate("Ui!! Mật khẩu và xác nhận mật khẩu không giống nhau.", SignUpActivity.this);
                    }
                }
            }
        });

        //set su kien quay lai dang nhap
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initWidgets() {
        edName = findViewById(R.id.aSignUp_edName);
        edEmail = findViewById(R.id.aSignUp_edEmail);
        edPass = findViewById(R.id.aSignUP_edPass);
        edConfirmPass = findViewById(R.id.aSignUP_edConfirmPass);
        btnSignUp = findViewById(R.id.aSignUP_btnSignUp);
        tvSignIn = findViewById(R.id.aSignUP_tvSignIn);
        imgEyeP = findViewById(R.id.aSignUP_imgShowP);
        imgEyeCP = findViewById(R.id.aSignUP_imgShowCP);
    }


    private void setShowHiddenPass() {
        imgEyeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStatusPassword) {
                    imgEyeP.setImageResource(R.drawable.ic_eye_show);
                    edPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edPass.setSelection(edPass.getText().length());
                    checkStatusPassword = false;
                }else {
                    imgEyeP.setImageResource(R.drawable.ic_eye_hidden);
                    edConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edConfirmPass.setSelection(edConfirmPass.getText().length());
                    checkStatusPassword = true;
                }

            }
        });
        imgEyeCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkStatusPassword) {
                    imgEyeCP.setImageResource(R.drawable.ic_eye_show);
                    edConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edConfirmPass.setSelection(edConfirmPass.getText().length());
                    checkStatusCP = false;
                }else {
                    imgEyeCP.setImageResource(R.drawable.ic_eye_hidden);
                    edConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edConfirmPass.setSelection(edConfirmPass.getText().length());
                    checkStatusCP = true;
                }

            }
        });
    }

    private void setFocusEdittext() {
        edName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setDrawableEdittext(b, edName);
            }
        });
        edEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setDrawableEdittext(b, edEmail);
            }
        });

        edPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setDrawableEdittext(b, edPass);
            }
        });

        edConfirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                setDrawableEdittext(b, edConfirmPass);
            }
        });

    }

    private void setDrawableEdittext(Boolean check, View v) {
        if (check) {
            v.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.form_style_login_focus));
        } else {
            v.setBackground(ContextCompat.getDrawable(SignUpActivity.this, R.drawable.form_style_login));
        }
    }
}