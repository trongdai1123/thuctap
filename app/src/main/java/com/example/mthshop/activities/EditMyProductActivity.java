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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.mthshop.R;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.ActivityEditMyProductBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.Product;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMyProductActivity extends AppCompatActivity {
    private Product productCurrent;
    private ActivityEditMyProductBinding thisActivity;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        //
        thisActivity = ActivityEditMyProductBinding.inflate(getLayoutInflater());
        setContentView(thisActivity.getRoot());

        productCurrent = (Product) getIntent().getSerializableExtra("product");

        //back
        thisActivity.aEditProductBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //
        //init cloudinary
        try {
            initConfigCloudinary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSpinner();
        pushDataOnForm();
        // chose img
        thisActivity.aEditProductImgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });

        thisActivity.aEditProductBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDiaLog.showProgressBar(EditMyProductActivity.this);
                String urlProduct = "";
                String nameProduct = thisActivity.aEditProductEdNameProduct.getText().toString().trim();
                String stage = thisActivity.aEditProductEdStage.getText().toString().trim();
                String whereProduction = thisActivity.aEditProductEdWhereProduction.getText().toString().trim();
                String warranty = thisActivity.aEditProductEdWarranty.getText().toString().trim();
                String batteryCapacity = thisActivity.aEditProductEdBatteryCapacity.getText().toString().trim();
                String nameColor = thisActivity.aEditProductEdNameColor.getText().toString().trim();
                String sale = thisActivity.aEditProductEdSale.getText().toString().trim();
                String price = thisActivity.aEditProductEdPrice.getText().toString().trim();
                int typeProduct = getIdTypeProduct(thisActivity.appCompatSpinner.getSelectedItem().toString());
                if (nameProduct.isEmpty() || stage.isEmpty() || warranty.isEmpty() || whereProduction.isEmpty() || batteryCapacity.isEmpty() ||
                        nameColor.isEmpty() || sale.isEmpty() || price.isEmpty()) {
                    NotificationDiaLog.showDiaLogValidDate("Bạn nhập thiếu thông tin", EditMyProductActivity.this);
                    Toast.makeText(EditMyProductActivity.this, "Bạn nhập thiếu thông tin !", Toast.LENGTH_SHORT).show();
                    Log.e("Bạn", "Bạn nhập thiếu thông tin");
                }else {
                    Product product = new Product(productCurrent.getIdProduct(), nameProduct, whereProduction, stage, warranty, batteryCapacity, nameProduct, nameColor, LoginActivity.userCurrent.getUser(), typeProduct, Integer.parseInt(sale), Integer.parseInt(price), urlProduct);
                    Log.e("fasdfs", product.toString());
                    if (uri != null) {
                        Log.e("product", product.toString());
                        upImagesToCloudinary(product);
                    }else {
                        product.setImages(productCurrent.getImages());
                        postProduct(product);
                    }


                }

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

    private void upImagesToCloudinary(Product product) {

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
                product.setImages(resultData.get("url").toString());
                Log.e("hihi", resultData.get("url").toString());
                postProduct(product);




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


    private void postProduct(Product product) {
        APIService.appService.putProduct(product).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                NotificationDiaLog.dismissProgressBar();
                finish();
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                NotificationDiaLog.dismissProgressBar();
                Log.e("addProduct", "that bai");
                finish();
            }
        });
    }

    private int getIdTypeProduct(String value) {
        switch (value) {
            case "Điện thoại":
                return 1000;
            case "Laptop":
                return 1001;
            case "Đồng hồ":
                return 1002;
            case "Tai nghe":
                return 1003;
        }
        return 0;
    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (ContextCompat.checkSelfPermission(EditMyProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        }else {
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

                uri =  data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    thisActivity.aEditProductImgProduct.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private void setSpinner() {
        String[] listSp = {"Điện thoại", "Laptop", "Đồng hồ", "Tai nghe"};
        thisActivity.appCompatSpinner.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listSp));
    }

    private void pushDataOnForm() {
        thisActivity.aEditProductEdNameProduct.setText(productCurrent.getNameProduct());
        thisActivity.aEditProductEdStage.setText(productCurrent.getState());
        thisActivity.aEditProductEdWhereProduction.setText(productCurrent.getWhereProduction());
        thisActivity.aEditProductEdWarranty.setText(productCurrent.getWarranty());
        thisActivity.aEditProductEdBatteryCapacity.setText(productCurrent.getBatteryCapacity());
        thisActivity.aEditProductEdNameColor.setText(productCurrent.getNameColor());
        thisActivity.aEditProductEdSale.setText(productCurrent.getSale() + "");
        thisActivity.aEditProductEdPrice.setText(productCurrent.getPrice() + "");
        Glide.with(this).load(productCurrent.getImages()).placeholder(R.drawable.product).into(thisActivity.aEditProductImgProduct);
        selectSpinner(productCurrent.getIdType());
    }

    private void selectSpinner(int i) {
        switch (i) {
            case 1000 :
                thisActivity.appCompatSpinner.setSelection(0);
                break;
            case 1001:
                thisActivity.appCompatSpinner.setSelection(1);
                break;
            case 1002:
                thisActivity.appCompatSpinner.setSelection(2);
                break;
            case 1003:
                thisActivity.appCompatSpinner.setSelection(3);
                break;
        }
    }
}