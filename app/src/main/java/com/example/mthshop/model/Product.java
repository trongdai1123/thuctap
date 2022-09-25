package com.example.mthshop.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Product implements Serializable {
    private int idProduct;
    @SerializedName("NameProduct")
    private String nameProduct;
    @SerializedName("WhereProduction")
    private String whereProduction;
    @SerializedName("State")
    private String state;
    @SerializedName("Warranty")
    private String warranty;
    @SerializedName("BatteryCapacity")
    private String batteryCapacity;
    @SerializedName("Description")
    private String description;
    @SerializedName("NameColor")
    private String nameColor;
    @SerializedName("Owner")
    private String owner;

    private int idType;
    @SerializedName("Sale")
    private int sale;
    @SerializedName("Price")
    private int price;
    @SerializedName("Images")
    private String images;

    public int inBill;

    public Product(int idProduct, String nameProduct, String whereProduction, String state, String warranty, String batteryCapacity, String description, String nameColor, String owner, int idType, int sale, int price, String images) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.whereProduction = whereProduction;
        this.state = state;
        this.warranty = warranty;
        this.batteryCapacity = batteryCapacity;
        this.description = description;
        this.nameColor = nameColor;
        this.owner = owner;
        this.idType = idType;
        this.sale = sale;
        this.price = price;
        this.images = images;
    }

    public Product() {
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getWhereProduction() {
        return whereProduction;
    }

    public void setWhereProduction(String whereProduction) {
        this.whereProduction = whereProduction;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(String batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameColor() {
        return nameColor;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idProduct=" + idProduct +
                ", nameProduct='" + nameProduct + '\'' +
                ", whereProduction='" + whereProduction + '\'' +
                ", state='" + state + '\'' +
                ", warranty='" + warranty + '\'' +
                ", batteryCapacity='" + batteryCapacity + '\'' +
                ", description='" + description + '\'' +
                ", nameColor='" + nameColor + '\'' +
                ", owner='" + owner + '\'' +
                ", idType=" + idType +
                ", sale=" + sale +
                ", price=" + price +
                ", images='" + images + '\'' +
                '}';
    }
}
