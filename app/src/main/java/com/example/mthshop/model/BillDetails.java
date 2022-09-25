package com.example.mthshop.model;

import com.google.gson.annotations.SerializedName;

public class BillDetails {
    private int idBillDetails;
    private int idBill;
    private int idProduct;
    @SerializedName("Status")
    private int status;
    private Product product;


    public BillDetails(int idBillDetails, int idBill, int idProduct, int status) {
        this.idBillDetails = idBillDetails;
        this.idBill = idBill;
        this.idProduct = idProduct;
        this.status = status;
    }

    public BillDetails() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIdBillDetails() {
        return idBillDetails;
    }

    public void setIdBillDetails(int idBillDetails) {
        this.idBillDetails = idBillDetails;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    @Override
    public String toString() {
        return "BillDetails{" +
                "idBillDetails=" + idBillDetails +
                ", idBill=" + idBill +
                ", idProduct=" + idProduct +
                ", status=" + status +
                '}';
    }
}
