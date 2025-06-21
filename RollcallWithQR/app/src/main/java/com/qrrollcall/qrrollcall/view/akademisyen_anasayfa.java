package com.qrrollcall.qrrollcall.view;

import static com.qrrollcall.qrrollcall.adapter.ApiClient.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.qrrollcall.qrrollcall.R;
import com.qrrollcall.qrrollcall.databinding.ActivityAkademisyenAnasayfaBinding;
import com.qrrollcall.qrrollcall.databinding.ActivityYoklamaGoruntuleAkademisyenBinding;
import com.qrrollcall.qrrollcall.model.ders;
import com.qrrollcall.qrrollcall.model.atilAkademisyen;
import com.qrrollcall.qrrollcall.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class akademisyen_anasayfa extends AppCompatActivity {
    private ActivityAkademisyenAnasayfaBinding binding;
    ArrayList<ders> dersArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akademisyen_anasayfa);
        binding = ActivityAkademisyenAnasayfaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();

        if(intent != null)
        {
            String ak_ad = intent.getStringExtra("ak_ad");
            String ak_soyad = intent.getStringExtra("ak_soyad");
            int ak_no = intent.getIntExtra("ak_no",0);
            getDersler(ak_no);
            binding.textView.setText("Hoşgeldiniz "+ak_ad+" "+ak_soyad);


        }

        binding.kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideElements();
            }
        });

        binding.Derslerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ListView'i göster
                binding.listViewdene.setVisibility(View.VISIBLE);
                binding.Derslerim.setVisibility(View.GONE);
                binding.imageView4.setVisibility(View.GONE);
                binding.textView.setVisibility(View.GONE);
                binding.cikis.setVisibility(View.GONE);
                binding.kapat.setVisibility(View.VISIBLE);




            }


        });
        binding.cikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cikis_akademisyen = new Intent(akademisyen_anasayfa.this,MainActivity.class);
                finish();
                startActivity(cikis_akademisyen);

            }
        });

    }

    protected void hideElements(){
        binding.listViewdene.setVisibility(View.GONE);
        binding.kapat.setVisibility(View.GONE);
        binding.Derslerim.setVisibility(View.VISIBLE);
        binding.imageView4.setVisibility(View.VISIBLE);
        binding.cikis.setVisibility(View.VISIBLE);
        binding.textView.setVisibility(View.VISIBLE);
    }
    private void getDersler(int ak_no) {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<ders>> call = apiService.getDers();

        call.enqueue(new Callback<List<ders>>() {
            @Override
            public void onResponse(Call<List<ders>> call, Response<List<ders>> response) {
                if (response.isSuccessful()) {
                    List<ders> responseList = response.body();
                    dersArray = new ArrayList<>(responseList);
                    List<String> dersAdList = new ArrayList<>();

                    for (ders dersler : dersArray) {
                        if (ak_no == dersler.ak_no) {
                            dersAdList.add(dersler.ders_ad);
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(akademisyen_anasayfa.this, android.R.layout.simple_list_item_1, dersAdList);
                    binding.listViewdene.setAdapter(adapter);

                    binding.listViewdene.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedLesson = dersAdList.get(position);

                            // Seçilen ders adına göre ders numarasını bul
                            int selectedLessonNo = -1; // Default olarak -1 veya başka bir değer atayabilirsiniz
                            for (ders dersler : dersArray) {
                                if (selectedLesson.equals(dersler.ders_ad)) {
                                    selectedLessonNo = dersler.ders_no; // Ders numarasını aldık
                                    // Diğer değişkenler de burada alınabilir
                                    Intent intent = new Intent(akademisyen_anasayfa.this, LessonDetailActivity.class);
                                    intent.putExtra("lesson", selectedLesson);
                                    intent.putExtra("ders_no", selectedLessonNo);
                                    intent.putExtra("bolum_no", dersler.bolum_no);
                                    intent.putExtra("ak_no", dersler.ak_no);
                                    intent.putExtra("ders_ad", dersler.ders_ad);
                                    startActivity(intent);
                                    break; // Ders bulundu, döngüden çık
                                }
                            }

                            if (selectedLessonNo == -1) {
                                // Ders bulunamadıysa bir işlem yapabilir veya hata mesajı verebilirsiniz
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ders>> call, Throwable t) {
                // Hata durumları ile ilgili işlemler burada yapılabilir
            }
        });
    }



}
