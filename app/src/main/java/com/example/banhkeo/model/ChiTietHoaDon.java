package com.example.banhkeo.model;

import java.io.Serializable;

public class ChiTietHoaDon implements Serializable {
    public int ID;
    public int IDdonhang;
    public int IDsanpham;
    public String Tenspham;
    public int Giasanpham;
    public int Soluongsanpham;

    public ChiTietHoaDon(int ID, int IDdonhang, int IDsanpham, String tenspham, int giasanpham, int soluongsanpham) {
        this.ID = ID;
        this.IDdonhang = IDdonhang;
        this.IDsanpham = IDsanpham;
        Tenspham = tenspham;
        Giasanpham = giasanpham;
        Soluongsanpham = soluongsanpham;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getIDdonhang() {
        return IDdonhang;
    }

    public void setIDdonhang(int IDdonhang) {
        this.IDdonhang = IDdonhang;
    }

    public int getIDsanpham() {
        return IDsanpham;
    }

    public void setIDsanpham(int IDsanpham) {
        this.IDsanpham = IDsanpham;
    }

    public String getTenspham() {
        return Tenspham;
    }

    public void setTenspham(String tenspham) {
        Tenspham = tenspham;
    }

    public int getGiasanpham() {
        return Giasanpham;
    }

    public void setGiasanpham(int giasanpham) {
        Giasanpham = giasanpham;
    }

    public int getSoluongsanpham() {
        return Soluongsanpham;
    }

    public void setSoluongsanpham(int soluongsanpham) {
        Soluongsanpham = soluongsanpham;
    }
}
