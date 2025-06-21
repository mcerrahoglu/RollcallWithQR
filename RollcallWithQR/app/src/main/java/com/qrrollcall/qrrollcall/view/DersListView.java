package com.qrrollcall.qrrollcall.view;

import static com.qrrollcall.qrrollcall.adapter.ApiClient.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.qrrollcall.qrrollcall.R;
import com.qrrollcall.qrrollcall.databinding.ActivityDersListViewBinding;
import com.qrrollcall.qrrollcall.model.ders;
import com.qrrollcall.qrrollcall.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DersListView extends AppCompatActivity {
    private ActivityDersListViewBinding binding;
    ArrayList<ders> dersArray;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ders_list_view);

        setContentView(R.layout.activity_ders_list_view);
        binding = ActivityDersListViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();



        Intent intent = getIntent();

        if(intent != null)
        {

            int ak_no = intent.getIntExtra("ak_no",0);
            Toast.makeText(DersListView.this,String.valueOf(ak_no),Toast.LENGTH_LONG).show();
            getDersler(ak_no);








        }





    }

    private void getDersler(int ak_no) {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<ders>> call = apiService.getDers();

        call.enqueue(new Callback<List<ders>>() {
            @Override
            public void onResponse(Call<List<ders>> call, Response<List<ders>> response) {
                if(response.isSuccessful()){
                    List<ders> responseList = response.body();
                    dersArray = new ArrayList<>(responseList);
                    boolean dersFound = false;
                    List<String> dersAdList = new ArrayList<>();
                    for(ders dersler: dersArray){
                        if(ak_no==dersler.ak_no){
                            dersAdList.add(dersler.ders_ad);
                            dersFound = true;
                            binding.derslerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String selectedLesson = dersArray.get(position).ders_ad;
                                    Intent intent = new Intent(DersListView.this, LessonDetailActivity.class);
                                    intent.putExtra("lesson", selectedLesson);
                                    intent.putExtra("ders_no",dersler.ders_no);
                                    intent.putExtra("bolum_no",dersler.bolum_no);
                                    intent.putExtra("ak_no",dersler.ak_no);
                                    intent.putExtra("ders_ad",dersler.ders_ad);
                                    startActivity(intent);
                                }
                            });

                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(DersListView.this, android.R.layout.simple_list_item_1, dersAdList);
                    binding.derslerListView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<List<ders>> call, Throwable t) {

            }
        });

    }
}