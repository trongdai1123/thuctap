package com.example.mthshop.model;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    @SerializedName("User1")
    private String user;
    @SerializedName("password")
    private String password;
    @SerializedName("NameUser")
    private String nameUser;
    @SerializedName("Email")
    private String email;
    @SerializedName("Address")
    private String address;
    @SerializedName("Role")
    private String role;
    @SerializedName("Avatar")
    private String avatar;
    @SerializedName("Sex")
    private String sex;
    @SerializedName("Date")
    private String date;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDate() {
        formatDate(date);
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User(String user, String password, String nameUser, String email, String address, String role, String avatar, String sex, String date) {
        this.user = user;
        this.password = password;
        this.nameUser = nameUser;
        this.email = email;
        this.address = address;
        this.role = role;
        this.avatar = avatar;
        this.sex = sex;
        this.date = date;
    }

    public User() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    @Override
    public String toString() {
        return "User{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", nameUser='" + nameUser + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", role='" + role + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex='" + sex + '\'' +
                ", date='" + date + '\'' +
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
