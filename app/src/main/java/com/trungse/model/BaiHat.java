package com.trungse.model;

public class BaiHat {
    private String ma;
    private String ten;
    private String tacgia;
    private int thich;

    public BaiHat(String ma, String ten, String tacgia, int thich) {
        this.ma = ma;
        this.ten = ten;
        this.tacgia = tacgia;
        this.thich = thich;
    }

    public BaiHat() {
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getTacgia() {
        return tacgia;
    }

    public void setTacgia(String tacgia) {
        this.tacgia = tacgia;
    }

    public int getThich() {
        return thich;
    }

    public void setThich(int thich) {
        this.thich = thich;
    }
}
