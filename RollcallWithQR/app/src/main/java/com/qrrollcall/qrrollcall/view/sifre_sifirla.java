package com.qrrollcall.qrrollcall.view;



import static com.qrrollcall.qrrollcall.adapter.ApiClient.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.annotation.SuppressLint;


import com.qrrollcall.qrrollcall.R;

import com.qrrollcall.qrrollcall.adapter.ApiClient;
import com.qrrollcall.qrrollcall.model.ogrenci_sifre;
import com.qrrollcall.qrrollcall.service.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class sifre_sifirla extends AppCompatActivity {
    Retrofit retrofit;
    ApiService apiService3;
    public int ogr_no,ogr_tc;
    public String ad,soyad,eposta;
    Button onayla;
    EditText yeni_sifreText,getYeni_sifre_tekrarText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_sifirla);

        Intent intent = getIntent();


        if (intent != null) {
            ogr_no = intent.getIntExtra("ogr_no", 0);
            ogr_tc = intent.getIntExtra("ogr_tc",0);
            ad = intent.getStringExtra("ad");
            soyad = intent.getStringExtra("soyad");
            eposta = intent.getStringExtra("ogrEposta");



        }
        onayla = (Button) findViewById(R.id.sifre_onayla);
        yeni_sifreText = (EditText) findViewById(R.id.sifre_girText);
        getYeni_sifre_tekrarText = (EditText) findViewById(R.id.sifre_onaylaText);

        onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = yeni_sifreText.getText().toString();
                String newPassword2 = getYeni_sifre_tekrarText.getText().toString();

                if(newPassword.equals(newPassword2)){
                    changePasswordForUser(ogr_no,newPassword);
                }
                else
                {
                    Toast.makeText(sifre_sifirla.this, "Şifreler Uyuşmuyor", Toast.LENGTH_LONG).show();
                }

            }
        });



    }

    private void changePasswordForUser(int ogr_no, String newPassword) {
        apiService3 = ApiClient.getRetrofitInstance().create(ApiService.class);


        ogrenci_sifre ogrenci_sifre_nesne = new ogrenci_sifre(ogr_no, newPassword);


        Call<Void> call = apiService3.sendNewPassword(ogr_no, ogrenci_sifre_nesne);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(sifre_sifirla.this, "Şifre Değişikliği Başarılı ", Toast.LENGTH_SHORT).show();
                    Intent ana_sayfa_donus = new Intent(sifre_sifirla.this,activity_ogrenci_giris.class);
                    startActivity(ana_sayfa_donus);
                    finish();

                }
                else {
                    int errorCode = response.code();
                    // Hatanın detaylarını almak için response.errorBody() kullanabilirsiniz

                    // Hata kodunu loglamak için
                    Log.e("HTTP Hata Kodu", String.valueOf(errorCode));

                    // Hata mesajını göstermek için
                    Toast.makeText(sifre_sifirla.this, "Veri gönderme başarısız oldu: " + errorCode, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API Error", "Hata: " + t.getMessage());
                Toast.makeText(sifre_sifirla.this, "API Hatası: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





}