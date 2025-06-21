package com.qrrollcall.qrrollcall.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qrrollcall.qrrollcall.R;
import com.qrrollcall.qrrollcall.adapter.ApiClient;
import com.qrrollcall.qrrollcall.databinding.ActivityAkademisyenGirisBinding;
import com.qrrollcall.qrrollcall.model.Akademisyen;
import com.qrrollcall.qrrollcall.model.atilAkademisyen;
import com.qrrollcall.qrrollcall.service.ApiService;


import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class akademisyen_giris extends AppCompatActivity {
    private ActivityAkademisyenGirisBinding binding;
    ArrayList<atilAkademisyen> akademisyenArray;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAkademisyenGirisBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        retrofit = ApiClient.getRetrofitInstance();

        binding.loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInClicked();
            }
        });
    }

    private void getUser(String userName, String password) {
        ApiService apiservice = retrofit.create(ApiService.class);
        Call<List<atilAkademisyen>> call = apiservice.getAkademisyenler();

        call.enqueue(new Callback<List<atilAkademisyen>>() {
            @Override
            public void onResponse(Call<List<atilAkademisyen>> call, Response<List<atilAkademisyen>> response) {
                if (response.isSuccessful()) {
                    List<atilAkademisyen> responseList = response.body();
                    akademisyenArray = new ArrayList<>(responseList);
                    boolean userFound = false;

                    for (atilAkademisyen akademisyen : akademisyenArray) {

                        try {
                            if (akademisyen.ak_no == Integer.parseInt(userName) && akademisyen.ak_sifre.equals(password)) {
                                userFound = true;
                                // Kullanıcı bulunduğunda intent'e bilgileri ekleyerek yeni aktiviteye geç
                                Intent intent = new Intent(akademisyen_giris.this, akademisyen_anasayfa.class);
                                intent.putExtra("ak_ad", akademisyen.ak_ad);
                                intent.putExtra("ak_soyad", akademisyen.ak_soyad);
                                intent.putExtra("ak_tc", akademisyen.ak_tc);
                                intent.putExtra("ak_no", akademisyen.ak_no);

                                startActivity(intent);
                                //buraya bir finsh koydum hata olursa sil

                                break;
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();

                            Toast.makeText(akademisyen_giris.this, "Kullanıcı adını düzgün giriniz.", Toast.LENGTH_LONG).show();
                        }

                    }

                    if (userFound) {
                        //akademisyen_anasayfa();
                    } else {
                        Toast.makeText(akademisyen_giris.this, "Kullanıcı Bulunmuyor", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(akademisyen_giris.this, "API'den veri alınamadı", Toast.LENGTH_LONG).show();
                    Log.e("API_ERROR", "API'den veri alınamadı - Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<atilAkademisyen>> call, Throwable t) {
                Toast.makeText(akademisyen_giris.this, "Database bağlantısı başarısız", Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", "Database bağlantısı başarısız: " + t.getMessage());

            }
        });
    }

    protected void signInClicked() {
        String userName = binding.akid.getText().toString();
        String password = binding.akpassword.getText().toString();
        getUser(userName, password);
    }

}
