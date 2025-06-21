package com.qrrollcall.qrrollcall.model;

import com.google.gson.annotations.SerializedName;

public class qr {
    @SerializedName("qr_no")
    public int qr_no;

    @SerializedName("ak_konum_x")
    public int ak_konum_x;

    @SerializedName("ak_konum_y")
    public int ak_konum_y;

    @SerializedName("hafta")
    public int hafta;

    @SerializedName("ders_no")
    public int ders_no;

    @SerializedName("ak_no")
    public int ak_no;

    @SerializedName("bolum_no")
    public int bolum_no;

    @SerializedName("tarih")
    public int tarih;

    @SerializedName("saat")
    public int saat;

}
