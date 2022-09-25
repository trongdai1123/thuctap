package com.example.mthshop.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.cloudinary.Api;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.mthshop.R;
import com.example.mthshop.api.APIService;
import com.example.mthshop.databinding.ActivityAddMyProductBinding;
import com.example.mthshop.dialog.NotificationDiaLog;
import com.example.mthshop.model.Product;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMyProductActivity extends AppCompatActivity {
    private ActivityAddMyProductBinding thisActivity;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        //
        thisActivity = ActivityAddMyProductBinding.inflate(getLayoutInflater());
        setContentView(thisActivity.getRoot());
        setSpinner();
        //init cloudinary
        try {
            initConfigCloudinary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //back
        thisActivity.aEditProductBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //
        thisActivity.aAddProductImgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        //them product
        thisActivity.aAddProductBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationDiaLog.showDiaLogValidDate("Bạn chưa chọn  loại sản phẩm", AddMyProductActivity.this);
                String urlProduct = "";
                String nameProduct = thisActivity.aAddProductEdNameProduct.getText().toString().trim();
                String stage = thisActivity.aAddProductEdStage.getText().toString().trim();
                String whereProduction = thisActivity.aAddProductEdWhereProduction.getText().toString().trim();
                String warranty = thisActivity.aAddProductEdWarranty.getText().toString().trim();
                String batteryCapacity = thisActivity.aAddProductEdBatteryCapacity.getText().toString().trim();
                String nameColor = thisActivity.aAddProductEdNameColor.getText().toString().trim();
                String sale = thisActivity.aAddProductEdSale.getText().toString().trim();
                String price = thisActivity.aAddProductEdPrice.getText().toString().trim();
                int typeProduct = getIdTypeProduct(thisActivity.aAddProductAutoComplete.getText().toString());
                if (nameProduct.isEmpty() || stage.isEmpty() || warranty.isEmpty() || whereProduction.isEmpty() || batteryCapacity.isEmpty() ||
                nameColor.isEmpty() || sale.isEmpty() || price.isEmpty()) {
                    NotificationDiaLog.showDiaLogValidDate("Bạn nhập thiếu thông tin", AddMyProductActivity.this);
                    Toast.makeText(AddMyProductActivity.this, "Bạn nhập thiếu thông tin !", Toast.LENGTH_SHORT).show();
                    Log.e("Bạn", "Bạn nhập thiếu thông tin");
                }else {
                    if (uri != null && typeProduct != 0) {
                        NotificationDiaLog.showProgressBar(AddMyProductActivity.this);
                        Product product = new Product(0, nameProduct, whereProduction, stage, warranty, batteryCapacity, nameProduct, nameColor, LoginActivity.userCurrent.getUser(), typeProduct, Integer.parseInt(sale), Integer.parseInt(price), urlProduct);
                        Log.e("product", product.toString());
                        upImagesToCloudinary(product);
                    }else {
                        if (uri == null) {
                            NotificationDiaLog.showDiaLogValidDate("bạn chưa chọn ảnh", AddMyProductActivity.this);
                            Toast.makeText(AddMyProductActivity.this, "bạn chưa chọn ảnh!", Toast.LENGTH_SHORT).show();
                        }
                        if (typeProduct == 0) {
                            NotificationDiaLog.showDiaLogValidDate("bạn chưa loại sản phẩm !", AddMyProductActivity.this);
                            Toast.makeText(AddMyProductActivity.this, "bạn chưa loại sản phẩm !", Toast.LENGTH_SHORT).show();
                            Log.e(typeProduct + "" , typeProduct +" ");
                        }
                    }


                }



            }
        });
        //
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
        APIService.appService.postProduct(product).enqueue(new Callback<Product>() {
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

    private void setSpinner() {
        String[] listSp = {"Điện thoại", "Laptop", "Đồng hồ", "Tai nghe"};
        thisActivity.aAddProductAutoComplete.setAdapter(new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listSp));
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
        if (ContextCompat.checkSelfPermission(AddMyProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                    thisActivity.aAddProductImgProduct.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

}