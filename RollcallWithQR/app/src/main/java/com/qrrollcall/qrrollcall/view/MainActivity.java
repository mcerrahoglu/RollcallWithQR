package com.qrrollcall.qrrollcall.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.qrrollcall.qrrollcall.adapter.ApiClient;
import com.qrrollcall.qrrollcall.service.ApiService;
import com.qrrollcall.qrrollcall.databinding.ActivityMainBinding;
import com.qrrollcall.qrrollcall.model.atilAkademisyen;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        }
    public void akademisyen_girisi(View view)
    {
        akademisyen_giris();
    }
    public void ogrenci_girisi(View view)
    {
        ogrenci_giris();
    }

    protected void akademisyen_giris() {
        Intent forgotintent = new Intent(this, akademisyen_giris.class);
        startActivity(forgotintent);
    }
    protected void ogrenci_giris()
    {
        Intent ogrenciGiris = new Intent(this, activity_ogrenci_giris.class);
        startActivity(ogrenciGiris);
    }
}