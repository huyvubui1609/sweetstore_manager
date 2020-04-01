package com.example.banhkeo.model;

public class Loaisp {
    public int id;
    public String Tenloaisp;
    public String Hinhloaisp;

    public Loaisp(int id, String tenloaisp, String hinhloaisp) {
        this.id = id;
        Tenloaisp = tenloaisp;
        Hinhloaisp = hinhloaisp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenloaisp() {
        return Tenloaisp;
    }

    public void setTenloaisp(String tenloaisp) {
        Tenloaisp = tenloaisp;
    }

    public String getHinhloaisp() {
        return Hinhloaisp;
    }

    public void setHinhloaisp(String hinhloaisp) {
        Hinhloaisp = hinhloaisp;
    }
}
