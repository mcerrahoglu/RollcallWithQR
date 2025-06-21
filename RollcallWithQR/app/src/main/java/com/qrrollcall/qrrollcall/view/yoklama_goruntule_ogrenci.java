package com.qrrollcall.qrrollcall.view;

import static com.qrrollcall.qrrollcall.adapter.ApiClient.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.qrrollcall.qrrollcall.R;
import com.qrrollcall.qrrollcall.databinding.ActivityYoklamaGoruntuleOgrenciBinding;
import com.qrrollcall.qrrollcall.model.ders_yoklama;
import com.qrrollcall.qrrollcall.model.ogrenci;
import com.qrrollcall.qrrollcall.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class yoklama_goruntule_ogrenci extends AppCompatActivity {
    public int ogr_no, ders_no;

    private List<ders_yoklama> dersList;
    private List<ogrenci> ogrenci_id;
    public ArrayList<String> filteredOgrenci;
    public ArrayList<ders_yoklama> filteredList;
    private ActivityYoklamaGoruntuleOgrenciBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoklama_goruntule_ogrenci);
        /*
        binding = ActivityAkademisyenAnasayfaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
         */
        binding = ActivityYoklamaGoruntuleOgrenciBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent_yoklama = getIntent();
        if (intent_yoklama != null) {
            ders_no = intent_yoklama.getIntExtra("ders_no", 0);
            ogr_no = intent_yoklama.getIntExtra("ogr_no", 0);

        }
        get_ders_id();
    }
    //derslerini getireceğiz
    //derse tıkladığında

    private void get_ders_id() {
        ApiService apiService_yoklama = retrofit.create(ApiService.class);
        Call<List<ders_yoklama>> call_yoklama = apiService_yoklama.getDers_yoklama();
        call_yoklama.enqueue(new Callback<List<ders_yoklama>>() {
            @Override
            public void onResponse(Call<List<ders_yoklama>> call, Response<List<ders_yoklama>> response) {
                if (response.isSuccessful()) {
                    dersList = response.body();
                    filteredList = new ArrayList<>();
                    for (ders_yoklama yoklama : dersList) {
                        if (ders_no == yoklama.ders_no && ogr_no == yoklama.ogr_no) {
                            filteredList.add(yoklama);

                        }
                    }
                    gethafta();
                }
            }


            @Override
            public void onFailure(Call<List<ders_yoklama>> call, Throwable t) {
                // Hata durumunda yapılacaklar
            }
        });
    }
    private void gethafta() {
        ApiService apiService2 = retrofit.create(ApiService.class);
        Call<List<ogrenci>> isim = apiService2.getOgrenci();
        isim.enqueue(new Callback<List<ogrenci>>() {
            @Override
            public void onResponse(Call<List<ogrenci>> call, Response<List<ogrenci>> response) {
                if (response.isSuccessful()) {
                    ogrenci_id = response.body();
                    filteredOgrenci = new ArrayList<>();

                    for (ders_yoklama yoklama : filteredList) {

                            if (yoklama.ogr_no == ogr_no) {

                                filteredOgrenci.add("hafta: "+yoklama.hafta+" ✓");

                            }
                        }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(yoklama_goruntule_ogrenci.this, android.R.layout.simple_list_item_1, filteredOgrenci);
                    binding.devamsizlikView.setAdapter(adapter);

                    // Toast mesajı için boyut bilgisi
                    Toast.makeText(yoklama_goruntule_ogrenci.this, "Filtered List Size: " + filteredOgrenci.size(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ogrenci>> call, Throwable t) {
                // Hata durumunda yapılacaklar
            }
        });
    }

}