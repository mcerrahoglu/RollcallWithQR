package com.qrrollcall.qrrollcall.model;

import com.google.gson.annotations.SerializedName;

public class Akademisyen {

    @SerializedName("ak_no")
    private int ak_no;
    private int ak_tc;
    private String ak_ad,ak_soyad,ak_sifre;


    public Akademisyen(int akNo, int akTc, String akAd, String akSoyad, String akSifre) {
        ak_no = akNo;
        ak_tc = akTc;
        ak_ad = akAd;
        ak_soyad = akSoyad;
        ak_sifre = akSifre;

    }

    public int getAk_no() {
        return ak_no;
    }

    public void setAk_no(int ak_no) {
        this.ak_no = ak_no;
    }

    public int getAk_tc() {
        return ak_tc;
    }

    public void setAk_tc(int ak_tc) {
        this.ak_tc = ak_tc;
    }

    public String getAk_ad() {
        return ak_ad;
    }

    public void setAk_ad(String ak_ad) {
        this.ak_ad = ak_ad;
    }

    public String getAk_soyad() {
        return ak_soyad;
    }

    public void setAk_soyad(String ak_soyad) {
        this.ak_soyad = ak_soyad;
    }

    public String getAk_sifre() {
        return ak_sifre;
    }

    public void setAk_sifre(String ak_sifre) {
        this.ak_sifre = ak_sifre;
    }




}


