package com.example.mthshop.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Bill {
    private int idBill;
    @SerializedName("Total")
    private double total;
    private String date;
    @SerializedName("Status")
    private int status;
    @SerializedName("Owner")
    private String owner;

    public Bill(int idBill, double total, String date, int status, String owner) {
        this.idBill = idBill;
        this.total = total;
        this.status = status;
        this.owner = owner;
        this.date = date;
    }

    public Bill() {

    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDate() {
        formatDate(date);
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getDateNotification() {
        return formatDateTypeNotification(date);
    }
    public double getStatus() {
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

    @Override
    public String toString() {
        return "Bill{" +
                "idBill=" + idBill +
                ", total=" + total +
                ", date=" + date +
                ", status=" + status +
                ", owner=" + owner +
                '}';
    }

    private String formatDateTypeNotification(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date tmp = inputFormat.parse(date);
            String formattedDate = outputFormat.format(tmp);
            return formattedDate;
        }catch (Exception ex) {
            Log.e("date", "Sai dinh dang");
        }
        return "";
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
