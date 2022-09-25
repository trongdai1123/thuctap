package com.example.mthshop.api;

import com.example.mthshop.model.Bill;
import com.example.mthshop.model.BillDetails;
import com.example.mthshop.model.Category;
import com.example.mthshop.model.Product;
import com.example.mthshop.model.Rate;
import com.example.mthshop.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    public String baseUrl = "http://nth.congtydacap.club/";

    APIService appService = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(APIService.class);

    //users
    @GET("api/users")
    Call<List<User>> callAllUsers();

    @GET("api/user-info")
    Call<List<User>> callAllUserInfo();

    @GET("api/users/{user}")
    Call<User> callUser(@Path("user") String user);

    @POST("api/users/put")
    Call<User> editInfoUser(@Body User user);



    //bill
    @GET("api/bills")
    Call<List<Bill>> callAllBills();

    @GET("api/bills/status/{status}/{user}")
    Call<List<Bill>> callBillInCart(@Path("status") int status, @Path("user") String user);

    @GET("api/bill/{bill}")
    Call<Bill> callBill(@Path("bill") int idBill);

    @POST("api/bill/post")
    Call<Bill> postBillCart(@Body Bill bill);

    @POST("api/bill/put")
    Call<Bill> putBill(@Body Bill bill);

    //bill  details
    @GET("api/bill-details")
    Call<List<BillDetails>> callAllBillDetails();

    @GET("api/bill-details/product/{idProduct}")
    Call<List<BillDetails>> callBillDetailsByIdProduct(@Path("idProduct") int idProduct);

    @POST("api/bill-details/post")
    Call<BillDetails> postBillDetails(@Body BillDetails billDetails);

    @GET("api/bill-details/find-bill/{idBill}/{idProduct}")
    Call<BillDetails> findBillDetails(@Path("idBill") int idBill, @Path("idProduct") int idProduct);

    @GET("api/bill-details/confirm-bill/{user}")
    Call<List<BillDetails>> callBillDetailsByOwner(@Path("user") String user);

    @POST("api/bill-details/put")
    Call<BillDetails> putBillDetails(@Body BillDetails billDetails);

    @DELETE("api/bill-details/delete/{id}")
    Call<BillDetails> deleteBillDetails(@Path("id") int id);

    //category
    @GET("api/category")
    Call<List<Category>> callAllCategory();

    //product
    @GET("api/products")
    Call<List<Product>> callAllProducts();

    @GET("api/product/{product}")
    Call<Product> callProductById(@Path("product") int product);

    @POST("api/product/post")
    Call<Product> postProduct(@Body Product product);

    @POST("api/product/put")
    Call<Product> putProduct(@Body Product product);

    @GET("api/products/users/{owner}")
    Call<List<Product>> callMyProduct(@Path("owner") String owner);

    @GET("api/products/bill-cart/{idBill}")
    Call<List<Product>> callProductInCart(@Path("idBill") int idBill);

    @GET("api/products/category/{idCategory}")
    Call<List<Product>> callProductsByIDCategory(@Path("idCategory") int idCategory);

    @GET("api/products/bill-cart/{idBillCart}/{status}")
    Call<List<Product>> callProductByIdBillAndStatus(@Path("idBillCart") int idBill, @Path("status") int status);
    //rate
    @GET("api/rates")
    Call<List<Rate>> callAllRates();

    @GET("api/rates/products/{idProduct}")
    Call<List<Rate>> callRatesByIdProduct(@Path("idProduct") int idProduct);

    @POST("api/rate/post")
    Call<Rate> addRate(@Body Rate rate);

    @POST("api/rate/put")
    Call<Rate> editRate(@Body Rate rate);





}
