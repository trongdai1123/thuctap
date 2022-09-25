package com.example.mthshop.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.mthshop.R;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.ActivityDetailsMeBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsMeActivity extends AppCompatActivity {
    private ActivityDetailsMeBinding thisActivity;
    private String name;
    private String user;
    private String sex;
    private String YOB;
    private String email;
    private String address;
    private String dateUpDatabase; // kiểu 2000-05-18 format để úp database
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener setListener;
    public static final int USER_NAME_CODE = 18;
    public static final int EMAIL_CODE = 98;
    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.back_ground));
        // binding
        thisActivity = ActivityDetailsMeBinding.inflate(getLayoutInflater());
        setContentView(thisActivity.getRoot());


        pushData();
        setUpDatePicker();
        try {
            initConfigCloudinary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTypeSendValueToChangeInfo();
        //set effect for save
        thisActivity.aDetailsMeImgBtnSave.setColorFilter(ContextCompat.getColor(this, R.color.icon_second), android.graphics.PorterDuff.Mode.SRC_IN);
        thisActivity.aDetailsMeImgBtnSave.setEnabled(false);

        //back
        thisActivity.aDetailsMeImgBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //save
        thisActivity.aDetailsMeImgBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDiaLog.showProgressBar(DetailsMeActivity.this);
                String name = thisActivity.aDetailsMeTvName.getText().toString().trim();
                String sex = thisActivity.aDetailsMeTvSex.getText().toString().trim();
                String email = thisActivity.aDetailsMeTvEmail.getText().toString().trim();
                String address = thisActivity.aDetailsMeTvBio.getText().toString().trim();
                LoginActivity.userCurrent.setAddress(address);
                LoginActivity.userCurrent.setEmail(email);
                LoginActivity.userCurrent.setSex(sex);
                LoginActivity.userCurrent.setDate(YOB);
                LoginActivity.userCurrent.setNameUser(name);
                if (!dateUpDatabase.isEmpty()) {
                    LoginActivity.userCurrent.setDate(dateUpDatabase);
                }
                if (uri == null) {
                    Log.e("LoginActivity", LoginActivity.userCurrent.toString());
                    CallAPIUpdateInfo();
                    checkAlternativeValues("hao",  "hao");
                }else {
                        MediaManager.get().upload(uri).callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {
                                Log.e("onStart", "ok");
                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {
                                Log.e("onProgress", "ok");
                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                NotificationDiaLog.dismissProgressBar();
                                LoginActivity.userCurrent.setAvatar(resultData.get("url").toString());
                                Log.e("hihi", resultData.get("url").toString());
                                CallAPIUpdateInfo();
                                checkAlternativeValues("hao",  "hao");

                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {
                                NotificationDiaLog.dismissProgressBar();
                                Log.e("onProgress", error.getDescription());
                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {
                                NotificationDiaLog.dismissProgressBar();
                                Log.e("onProgress", error.getDescription());
                            }
                        }).dispatch();

                }


            }
        });
        //chon hinh
        thisActivity.linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });

    }

    private void initConfigCloudinary() throws Exception {
        Map config = new HashMap();
        config.put("cloud_name", "dr7hoxpif");
        config.put("api_key", "467642843646499");
        config.put("api_secret", "_Y5A0dT57bsF0vjHYqWmEERcC6M");
        config.put("secure", true);
        MediaManager.init(this, config);

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (ContextCompat.checkSelfPermission(DetailsMeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(intent);

    }

    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data == null) return;

                uri = data.getData();
                if (uri != null) {
                    checkAlternativeValues("2", "1");
                }
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    thisActivity.aDetailsMeImgAvatar.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });


    private void CallAPIUpdateInfo() {
        APIService.appService.editInfoUser(LoginActivity.userCurrent).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.e("editInfoUser", "Ok");
                updateUserCurrent();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                Log.e("editInfoUser", "false");
            }
        });
    }

    private void updateUserCurrent() {
        APIService.appService.callUser(LoginActivity.userCurrent.getUser()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                NotificationDiaLog.dismissProgressBar();
                LoginActivity.userCurrent = response.body();
                pushData();
                NotificationDiaLog.showDiaLogValidDate("Cập nhật thành công!", DetailsMeActivity.this);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                Log.e("callUser", "false");
            }
        });
    }


    public void pushData() {
        //set default
        dateUpDatabase = "";

        name = LoginActivity.userCurrent.getNameUser();
        user = LoginActivity.userCurrent.getUser();
        sex = LoginActivity.userCurrent.getSex();
        YOB = LoginActivity.userCurrent.getDate();
        email = LoginActivity.userCurrent.getEmail();
        address = LoginActivity.userCurrent.getAddress();
        thisActivity.aDetailsMeTvName.setText(name);
        thisActivity.aDetailsMeTvUser.setText(user);
        thisActivity.aDetailsMeTvBio.setText(address);
        thisActivity.aDetailsMeTvSex.setText(sex);
        thisActivity.aDetailsMeTvYOB.setText(YOB);
        thisActivity.aDetailsMeTvEmail.setText(email);
        Glide.with(this).load(LoginActivity.userCurrent.getAvatar()).placeholder(R.drawable.user_avatar).into(thisActivity.aDetailsMeImgAvatar);

    }

    public void openSomeActivityForResult(String title, String value, int type) {
        Intent intent = new Intent(this, ChangeInfoAccount.class);
        intent.putExtra("value", value);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == USER_NAME_CODE) {
                        Intent data = result.getData();
                        String originValue = thisActivity.aDetailsMeTvName.getText().toString().trim();
                        String value = data.getStringExtra("value");
                        thisActivity.aDetailsMeTvName.setText(value);
                        checkAlternativeValues(originValue, value);

                    } else if (result.getResultCode() == EMAIL_CODE) {

                    }
                }


            });

    private void checkAlternativeValues(String originValue, String value) {
        if (originValue.equals(value)) {
            thisActivity.aDetailsMeImgBtnSave.setColorFilter(ContextCompat.getColor(this, R.color.icon_second), android.graphics.PorterDuff.Mode.SRC_IN);
            thisActivity.aDetailsMeImgBtnSave.setEnabled(false);
        } else {
            thisActivity.aDetailsMeImgBtnSave.setColorFilter(ContextCompat.getColor(this, R.color.main), android.graphics.PorterDuff.Mode.SRC_IN);
            thisActivity.aDetailsMeImgBtnSave.setEnabled(true);
        }
    }

    private void setUpDatePicker() {
        calendar = Calendar.getInstance();
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                String fmMonth = month + "";
                String fmDay = dayOfMonth + "";
                if (month < 10) {
                    fmMonth = "0" + month;
                }
                if (dayOfMonth < 10) {
                    fmDay = "0" + dayOfMonth;
                }
                String date = fmDay + "-" + fmMonth + "-" + year;
                thisActivity.aDetailsMeTvYOB.setText(date);
                dateUpDatabase = year + "-" + fmMonth + "-" + fmDay;
                checkAlternativeValues("56", "4535");
            }
        };
    }

    private void setTypeSendValueToChangeInfo() {
        //user name
        thisActivity.aDetailsMeLnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSomeActivityForResult("Sửa tên", name, USER_NAME_CODE);
            }
        });
        //YOB
        thisActivity.aDetailsMeTvYOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(DetailsMeActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        //sex
        thisActivity.aDetailsMeLnSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiaLogSex();
            }
        });
    }

    private void showDiaLogSex() {
        Dialog dialogLogOut = new Dialog(this);
        dialogLogOut.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogLogOut.setContentView(R.layout.dialog_sex);

        Window window = dialogLogOut.getWindow();
        if (window == null)
            return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        if (Gravity.BOTTOM == Gravity.CENTER) {
            dialogLogOut.setCancelable(true);
        } else {
            dialogLogOut.setCancelable(false);
        }
        TextView tvMale = dialogLogOut.findViewById(R.id.diaLogSex_tvMale);
        TextView tvFemale = dialogLogOut.findViewById(R.id.diaLogSex_tvFemale);
        TextView tvLGBT = dialogLogOut.findViewById(R.id.diaLogSex_tvLGBT);

        tvMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisActivity.aDetailsMeTvSex.setText("Nam");
                dialogLogOut.dismiss();
                checkAlternativeValues("56", "4535");
            }
        });
        tvFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisActivity.aDetailsMeTvSex.setText("Nữ");
                dialogLogOut.dismiss();
                checkAlternativeValues("56", "4535");
            }
        });

        tvLGBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thisActivity.aDetailsMeTvSex.setText("Khác");
                dialogLogOut.dismiss();
                checkAlternativeValues("56", "4535");
            }
        });

        dialogLogOut.show();
    }


}