package com.qrrollcall.qrrollcall.service;
import com.qrrollcall.qrrollcall.model.atilAkademisyen;
import com.qrrollcall.qrrollcall.model.ogrenci;
import com.qrrollcall.qrrollcall.model.ders;
import com.qrrollcall.qrrollcall.model.ders_yoklama;
import com.qrrollcall.qrrollcall.model.ogrenci_sifre;
import com.qrrollcall.qrrollcall.model.ogrenci_ders;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface ApiService {
    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("api/Akademisyen")
    Call<List<atilAkademisyen>> getAkademisyenler();

    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("api/ogrenci")
    Call<List<ogrenci>> getOgrenci();

    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("api/ogrenci")
    Call<List<ogrenci>> getOgrenci2();

    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("api/ders")
    Call<List<ders>> getDers();
    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("api/ders_ogrenci")
    Call<List<ogrenci_ders>> getOgrenciDers();
    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("api/ders_yoklama")
    Call<List<ders_yoklama>> getDers_yoklama();


    @Headers("Content-Type: application/json;charset=UTF-8")
    @POST("api/ders_yoklama")
    Call<Void> sendOgrenciVeri(@Body ders_yoklama postDers_Yoklama);


    @Headers("Content-Type: application/json;charset=UTF-8")
    @PUT("api/ogrenci_sifre/{ogr_no}/sifre")
    Call<Void> sendNewPassword(
            @Path("ogr_no") int ogr_no,
            @Body ogrenci_sifre ogrenciSifre
    );

    @Headers("Content-Type: application/json;charset=UTF-8")
    @DELETE("api/ders_yoklama/{ders_no}/{ogr_no}/{hafta}") // Burada ders_no, ogr_no ve hafta parametreleri bekleniyor
    Call<Void> deleteYoklama(@Path("ders_no") int ders_no, @Path("ogr_no") int ogr_no, @Path("hafta") int hafta);



}
