package com.example.banhkeo.model;

import java.io.Serializable;

public class DonHang implements Serializable {
    public int ID;
    public String Tenkhachhang;
    public int Sodienthoai;
    public String Email;
    public String Diachi;

    public DonHang(int ID, String tenkhachhang, int sodienthoai, String email, String diachi) {
        this.ID = ID;
        Tenkhachhang = tenkhachhang;
        Sodienthoai = sodienthoai;
        Email = email;
        Diachi = diachi;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTenkhachhang() {
        return Tenkhachhang;
    }

    public void setTenkhachhang(String tenkhachhang) {
        Tenkhachhang = tenkhachhang;
    }

    public int getSodienthoai() {
        return Sodienthoai;
    }

    public void setSodienthoai(int sodienthoai) {
        Sodienthoai = sodienthoai;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDiachi() {
        return Diachi;
    }

    public void setDiachi(String diachi) {
        Diachi = diachi;
    }
}
