package com.example.mthshop.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Since;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rate implements Serializable {
    private int idRate;
    @SerializedName("Date")
    private String date;
    @SerializedName("Content")
    private String content;
    @SerializedName("RateStar")
    private int rateStar;
    @SerializedName("Status")
    private int status;
    @SerializedName("Owner")
    private String owner;
    private int idProduct;
    public Product product;

    public Rate(int idRate, String date, String content, int rateStar, int status, String owner, int idProduct) {
        this.idRate = idRate;
        this.date = date;
        this.content = content;
        this.rateStar = rateStar;
        this.status = status;
        this.owner = owner;
        this.idProduct = idProduct;
        formatDate(this.date);
    }

    public Rate() {
    }

    public int getIdRate() {
        return idRate;
    }

    public void setIdRate(int idRate) {
        this.idRate = idRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRateStar() {
        return rateStar;
    }

    public void setRateStar(int rateStar) {
        this.rateStar = rateStar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "idRate=" + idRate +
                ", date=" + date +
                ", content='" + content + '\'' +
                ", rateStar=" + rateStar +
                ", status='" + status + '\'' +
                ", owner='" + owner + '\'' +
                ", idProduct=" + idProduct +
                '}';
    }

    private void formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date tmp = inputFormat.parse(date);
            String formattedDate = outputFormat.format(tmp);
            this.date = formattedDate;
        }catch (Exception ex) {
            Log.e("date", "Sai dinh dang");
        }

    }
}
