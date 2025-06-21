package com.qrrollcall.qrrollcall.view;

import static com.qrrollcall.qrrollcall.adapter.ApiClient.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.qrrollcall.qrrollcall.R;
import com.qrrollcall.qrrollcall.databinding.ActivityYoklamaGoruntuleAkademisyenBinding;
import com.qrrollcall.qrrollcall.model.ders;
import com.qrrollcall.qrrollcall.model.ders_yoklama;
import com.qrrollcall.qrrollcall.model.ogrenci;
import com.qrrollcall.qrrollcall.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class yoklama_goruntule_akademisyen extends AppCompatActivity {
    public int ak_no, ders_no, hafta_no;

    private List<ders_yoklama> yoklamaList;
    private List<ogrenci> ogrenci_ad;
    public ArrayList<String> filteredOgrenci;
    public ArrayList<ders_yoklama> filteredList;
    private ActivityYoklamaGoruntuleAkademisyenBinding binding;
    public String ders_ad;
    public Intent intent_yoklama;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoklama_goruntule_akademisyen);

        binding = ActivityYoklamaGoruntuleAkademisyenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        intent_yoklama= getIntent();
        if (intent_yoklama != null) {
            ak_no = intent_yoklama.getIntExtra("ak_no", 0);
            ders_no = intent_yoklama.getIntExtra("ders_no", 0);
            hafta_no = intent_yoklama.getIntExtra("hafta_no", 0);
            ders_ad = intent_yoklama.getStringExtra("ders_ad");
            Log.d("VeriGonderme2", "ders_no: " + ders_no);

            Log.d("VeriGonderme2", "ders_ad: " + ders_ad);

            Log.d("VeriGonderme2", "hafta: " + hafta_no);
            getogr_id(ders_no);
        }


    }

    private void getogr_id(int dersler_no) {
        ApiService apiService_yoklama = retrofit.create(ApiService.class);
        Call<List<ders_yoklama>> call_yoklama = apiService_yoklama.getDers_yoklama();
        call_yoklama.enqueue(new Callback<List<ders_yoklama>>() {
            @Override
            public void onResponse(Call<List<ders_yoklama>> call, Response<List<ders_yoklama>> response) {
                if (response.isSuccessful()) {
                    yoklamaList = response.body();
                    filteredList = new ArrayList<>();
                    for (ders_yoklama yoklama : yoklamaList) {
                        if (dersler_no == yoklama.ders_no && hafta_no == yoklama.hafta) {
                            filteredList.add(yoklama);

                        }
                    }
                    getogr_ad();

                }
            }


            @Override
            public void onFailure(Call<List<ders_yoklama>> call, Throwable t) {
                // Hata durumunda yapılacaklar
            }
        });
    }
    private void getogr_ad() {
        ApiService apiService2 = retrofit.create(ApiService.class);
        Call<List<ogrenci>> isim = apiService2.getOgrenci();
        isim.enqueue(new Callback<List<ogrenci>>() {
            @Override
            public void onResponse(Call<List<ogrenci>> call, Response<List<ogrenci>> response) {
                if (response.isSuccessful()) {
                    ogrenci_ad = response.body();
                    //filteredOgrenci = new ArrayList<>();
                    filteredOgrenci = new ArrayList<>();

                    // Iterate through filteredList and compare with student IDs to get names
                    for (ders_yoklama yoklama : filteredList) {
                        for (ogrenci ogrenci : ogrenci_ad) {
                            if (yoklama.ogr_no == ogrenci.ogr_no) {
                                // Matched student ID, display the name
                                filteredOgrenci.add(""+ogrenci.ogr_no+" "+ogrenci.ogr_ad+" "+ogrenci.ogr_soyad+"");
                                break; // Move to the next filtered item
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(yoklama_goruntule_akademisyen.this, android.R.layout.simple_list_item_1, filteredOgrenci);
                    binding.adList.setAdapter(adapter);


                    binding.adList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedStudent = filteredOgrenci.get(position);


                            // Seçilen ders adına göre ders numarasını bul
                            int selectedStudentNo = -1; // Default olarak -1 veya başka bir değer atayabilirsiniz
                            for (ogrenci ogrenci : ogrenci_ad) {
                                if (selectedStudent.equals(""+ogrenci.ogr_no+" "+ogrenci.ogr_ad+" "+ogrenci.ogr_soyad+"")) {

                                    // Diğer değişkenler de burada alınabilir
                                    Intent intent = new Intent(yoklama_goruntule_akademisyen.this, ogrenciYoklamadanSil.class);
                                    intent.putExtra("ders_no",ders_no);
                                    intent.putExtra("ogr_ad",ogrenci.ogr_ad);
                                    intent.putExtra("ders_ad", ders_ad);
                                    intent.putExtra("ogr_tc",ogrenci.ogr_tc);
                                    intent.putExtra("ogr_no",ogrenci.ogr_no);
                                    intent.putExtra("ogr_soyad",ogrenci.ogr_soyad);
                                    intent.putExtra("ogr_eposta",ogrenci.ogr_eposta);
                                    intent.putExtra("bolum_no",ogrenci.bolum_no);
                                    intent.putExtra("hafta",hafta_no);

                                    // Pass necessary data using intent extras
                                    intent.putExtra("selectedStudentNo", selectedStudentNo);

                                    startActivity(intent);
                                    break; // Ders bulundu, döngüden çık
                                }
                            }

                            if (selectedStudentNo == -1) {
                                // Ders bulunamadıysa bir işlem yapabilir veya hata mesajı verebilirsiniz
                            }
                        }
                    });


                }

                    // Toast mesajı için boyut bilgisi
                    Toast.makeText(yoklama_goruntule_akademisyen.this, "Filtered List Size: " + filteredOgrenci.size(), Toast.LENGTH_SHORT).show();
                }


            @Override
            public void onFailure(Call<List<ogrenci>> call, Throwable t) {
                // Handle failure
            }
        });
    }




}
