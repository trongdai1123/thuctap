package com.example.mthshop.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    private int idType;
    @SerializedName("NameType")
    private String nameType;


    public Category(int idType, String nameType) {
        this.idType = idType;
        this.nameType = nameType;
    }

    public Category() {
    }

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    @Override
    public String toString() {
        return "Category{" +
                "idType=" + idType +
                ", nameType='" + nameType + '\'' +
                '}';
    }
}
