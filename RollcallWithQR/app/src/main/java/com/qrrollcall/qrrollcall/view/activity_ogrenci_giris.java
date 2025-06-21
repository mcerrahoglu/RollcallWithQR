package com.qrrollcall.qrrollcall.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.qrrollcall.qrrollcall.R;
import com.qrrollcall.qrrollcall.adapter.ApiClient;
import com.qrrollcall.qrrollcall.databinding.ActivityAkademisyenGirisBinding;
import com.qrrollcall.qrrollcall.databinding.ActivityOgrenciGirisBinding;
import com.qrrollcall.qrrollcall.model.ogrenci;
import com.qrrollcall.qrrollcall.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class activity_ogrenci_giris extends AppCompatActivity {
    private ActivityOgrenciGirisBinding binding;
    ArrayList<ogrenci> ogrenciArray;
    Retrofit retrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOgrenciGirisBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        retrofit = ApiClient.getRetrofitInstance();

        binding.ogrgirisbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInClicked();
            }
        });
        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sifremiUnuttum = new Intent(activity_ogrenci_giris.this,forgot_password.class);
                startActivity(sifremiUnuttum);
            }
        });
    }
    protected void signInClicked()
    {
        String userName = binding.ogrid.getText().toString();
        String password = binding.ogrpassword.getText().toString();
        getUserChechk(userName,password);

    }
    private void getUserChechk(String userName, String password)
    {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<ogrenci>> call= apiService.getOgrenci();
        call.enqueue(new Callback<List<ogrenci>>() {
            @Override
            public void onResponse(Call<List<ogrenci>> call, Response<List<ogrenci>> response) {
                if(response.isSuccessful()) {
                    List<ogrenci> responseList=response.body();
                    ogrenciArray = new ArrayList<>(responseList);
                    boolean userFound = false;
                    for(ogrenci ogrenciler : ogrenciArray) {
                        try {
                            if (ogrenciler.ogr_no == Integer.parseInt(userName) && ogrenciler.ogr_sifre.equals(password)) {
                                userFound = true;
                                Intent intent = new Intent(activity_ogrenci_giris.this, activity_ogrenci_anasayfa.class);
                                intent.putExtra("ogr_ad", ogrenciler.ogr_ad);
                                intent.putExtra("ogr_soyad", ogrenciler.ogr_soyad);
                                intent.putExtra("ogr_eposta", ogrenciler.ogr_eposta);
                                intent.putExtra("ogr_tc", ogrenciler.ogr_tc);
                                intent.putExtra("ogr_no", ogrenciler.ogr_no);
                                intent.putExtra("bolum_no", ogrenciler.bolum_no);
                                startActivity(intent);
                                break;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            // NumberFormatException hatası burada yakalanacak ve işlenecek
                            // Hata durumunu kullanıcıya bildirmek için bir Toast veya başka bir işlem yapılabilir
                            Toast.makeText(activity_ogrenci_giris.this, "Kullanıcı adını düzgün giriniz", Toast.LENGTH_LONG).show();
                        }

                    }
                    if(userFound == true){
                    }
                    else{
                        Toast.makeText(activity_ogrenci_giris.this, "Kullanıcı Bulunamadı", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(activity_ogrenci_giris.this, "API'den Veri Alınamadı", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<ogrenci>> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
}