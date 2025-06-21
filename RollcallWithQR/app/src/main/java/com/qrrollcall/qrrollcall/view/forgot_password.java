package com.qrrollcall.qrrollcall.view;

import static com.qrrollcall.qrrollcall.adapter.ApiClient.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qrrollcall.qrrollcall.R;
import com.qrrollcall.qrrollcall.model.ogrenci;
import com.qrrollcall.qrrollcall.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class forgot_password extends AppCompatActivity {

    ArrayList<ogrenci> passwordArray;
    EditText eposta, adText, soyadText, ogr_noText, ogr_tcText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        eposta = findViewById(R.id.ogr_eposta);
        soyadText = findViewById(R.id.ogr_soyad);
        adText = findViewById(R.id.ograd);
        ogr_noText = findViewById(R.id.ogrno);
        ogr_tcText = findViewById(R.id.ogrtc);

        findViewById(R.id.resetpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                signInClicked2();
                }
                catch (Exception e){
                    Toast.makeText(forgot_password.this, "Beklenmedik bir hata oluştu", Toast.LENGTH_SHORT).show();
                    Log.e("Exception", "Beklenmedik bir hata oluştu: " + e.getMessage());

                }
            }
        });
    }

    protected void signInClicked2() {
        String ad = adText.getText().toString();
        String soyad = soyadText.getText().toString();
        int ogrTc = Integer.parseInt(ogr_tcText.getText().toString());
        String ogrePosta = eposta.getText().toString();
        int ogrNo = Integer.parseInt(ogr_noText.getText().toString());

        getUserChechk(ogrNo, ogrTc, ogrePosta, ad, soyad);
    }

    private void getUserChechk(int ogrNo, int ogrTc, String ogrEposta, String ad, String soyad) {
        ApiService apiService2 = retrofit.create(ApiService.class);
        Call<List<ogrenci>> call2 = apiService2.getOgrenci2();

        call2.enqueue(new Callback<List<ogrenci>>() {
            @Override
            public void onResponse(Call<List<ogrenci>> call2, Response<List<ogrenci>> response2) {
                if (response2.isSuccessful() && response2.body() != null) {
                    List<ogrenci> responseList = response2.body();
                    passwordArray = new ArrayList<>(responseList);
                    boolean userFound = false;

                    for (ogrenci ogrenciler : passwordArray) {
                        if (ogrenciler.ogr_no == ogrNo&& ogrenciler.ogr_tc == ogrTc &&
                                ogrenciler.ogr_ad.equalsIgnoreCase(ad) &&
                                ogrenciler.ogr_eposta.equalsIgnoreCase(ogrEposta) &&
                                ogrenciler.ogr_soyad.equalsIgnoreCase(soyad)) {



                            userFound = true;
                            break;
                        }
                    }

                    if (userFound) {

                        Intent intent = new Intent(forgot_password.this, sifre_sifirla.class);
                        intent.putExtra("ogr_no",ogrNo);
                        intent.putExtra("ogrEposta",ogrEposta);
                        intent.putExtra("ogrTc",ogrTc);
                        intent.putExtra("ad",ad);
                        intent.putExtra("soyad",soyad);
                        startActivity(intent);
                        finish();



                    } else {
                        //Toast.makeText(forgot_password.this,""+eposta+"",Toast.LENGTH_SHORT).show();
                        Toast.makeText(forgot_password.this, "Kullanıcı Bulunamadı", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(forgot_password.this, "API'den Veri Alınamadı", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ogrenci>> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(forgot_password.this, "API Hatası", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

