package com.qrrollcall.qrrollcall.model;

import com.google.gson.annotations.SerializedName;

public class ogrenci_sifre {
    @SerializedName("ogr_no")
    public int ogr_no;
    @SerializedName("ogr_tc")
    public int ogr_tc;
    @SerializedName("ogr_ad")
    public String ogr_ad;
    @SerializedName("ogr_soyad")
    public String ogr_soyad;
    @SerializedName("bolum_no")
    public int bolum_no;
    @SerializedName("ogr_eposta")
    public String ogr_eposta;
    @SerializedName("ogr_sifre")
    public String ogr_sifre;

    public ogrenci_sifre(int ogr_no, String ogr_sifre) {
        this.ogr_no = ogr_no;
        this.ogr_sifre = ogr_sifre;
    }
}
