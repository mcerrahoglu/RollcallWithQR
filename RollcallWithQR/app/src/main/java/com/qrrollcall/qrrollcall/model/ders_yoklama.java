package com.qrrollcall.qrrollcall.model;
import com.google.gson.annotations.SerializedName;

public class ders_yoklama {
    @SerializedName("yok_id")
    public int yok_id;

    @SerializedName("ders_no")
    public int ders_no;

    @SerializedName("ogr_no")
    public int ogr_no;

    @SerializedName("hafta")
    public int hafta;

    public ders_yoklama(int ders_no, int ogr_no, int hafta) {
        this.ders_no = ders_no;
        this.ogr_no = ogr_no;
        this.hafta = hafta;
    }

}
