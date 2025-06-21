package com.qrrollcall.qrrollcall.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qrrollcall.qrrollcall.R;
import com.qrrollcall.qrrollcall.adapter.ApiClient;
import com.qrrollcall.qrrollcall.databinding.ActivityOgrenciYoklamadanSilBinding;
import com.qrrollcall.qrrollcall.service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ogrenciYoklamadanSil extends AppCompatActivity {
    public String ders_ad,ogr_ad,ogr_soyad,ogr_mail;
    public int ogr_no,ders_no,hafta;
    private Retrofit retrofit;
    private ApiService apiService;
    ActivityOgrenciYoklamadanSilBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrenci_yoklamadan_sil);
        binding = ActivityOgrenciYoklamadanSilBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        initializeRetrofit();
        if (intent != null) {
            ders_ad = intent.getStringExtra("ders_ad");
            ogr_no = intent.getIntExtra("ogr_no",0);
            ogr_ad = intent.getStringExtra("ogr_ad");
            ogr_soyad = intent.getStringExtra("ogr_soyad");
            ogr_mail = intent.getStringExtra("ogr_mail");
            ders_no = intent.getIntExtra("ders_no",0);
            hafta = intent.getIntExtra("hafta",0);
            binding.adText1.setText("Ders No: "+ders_no);
            binding.SoyadText.setText("Ogrenci Ad: "+ogr_ad);
            binding.noText.setText("Ogrenci Soyad: " + ogr_soyad);
            binding.MailText.setText("Ogrenci No: " + ogr_no);



        }



        binding.yoklamayiSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                yoklamayi_sil();
            }
        });
    }
    private void initializeRetrofit() {
        retrofit = ApiClient.getRetrofitInstance();
        apiService = retrofit.create(ApiService.class);
    }
    private void yoklamayi_sil() {
        Call<Void> call = apiService.deleteYoklama(ders_no,ogr_no,hafta);


        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(ogrenciYoklamadanSil.this,"Kişi başarıyla silindi",Toast.LENGTH_LONG).show();



                } else {

                    Toast.makeText(ogrenciYoklamadanSil.this,"Kişi silinemedi",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

}