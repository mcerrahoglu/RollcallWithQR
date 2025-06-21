package com.qrrollcall.qrrollcall.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.qrrollcall.qrrollcall.adapter.ApiClient;
import com.qrrollcall.qrrollcall.databinding.ActivityOgrenciAnasayfaBinding;
import com.qrrollcall.qrrollcall.model.ders;
import com.qrrollcall.qrrollcall.model.ders_yoklama;
import com.qrrollcall.qrrollcall.model.ogrenci_ders;
import com.qrrollcall.qrrollcall.model.ogrenci_konum_izin;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.qrrollcall.qrrollcall.R;
import com.qrrollcall.qrrollcall.service.ApiService;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import android.Manifest;



public class activity_ogrenci_anasayfa extends AppCompatActivity implements ogrenci_konum_izin.KonumBilgisiCallback {
    private boolean konumHazir = false;
    private ogrenci_konum_izin izinNesnesi2;
    private ActivityOgrenciAnasayfaBinding binding;
    public String Jak_konum_x, Jak_konum_y, Jhafta, Jders_no, Jak_no, Jbolum_no, Jay, Jgun, Jsaat, Jdakika;

    //TextView textView;
    public Calendar calendar;
    public int month, day, hour, minute;
    String ogr_ad,ogr_soyad;

    Retrofit retrofit;
    public int ogr_no;
    ArrayList<ogrenci_ders> dersArray;
    private List<ders> ders_ad;
    public ArrayList<String> filteredDers;
    public ArrayList<ogrenci_ders> filteredList;
    private List<ogrenci_ders> ogrenciders;




    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;

    private static final int REQUEST_IMAGE_PICK = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrenci_anasayfa);
        binding = ActivityOgrenciAnasayfaBinding.inflate(getLayoutInflater());
        View view= binding.getRoot();
        setContentView(view);


        izinNesnesi2 = new ogrenci_konum_izin(this);
        izinNesnesi2.setKonumCallback(this); // Bu satır hata alıyorsa, buradaki this ifadesini kontrol et
        izinNesnesi2.requestLocationUpdates();
        retrofit = ApiClient.getRetrofitInstance();
        Intent intent = getIntent();
        if (intent != null) {
            ogr_no = intent.getIntExtra("ogr_no",0);
            ogr_ad = intent.getStringExtra("ogr_ad");
            ogr_soyad = intent.getStringExtra("ogr_soyad");


        }
        binding.textViewOgrenci.setText("Hoşgeldiniz "+ogr_ad+" "+ogr_soyad);

        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH); // Not: Ay Aralık ayı için 0'dan başlar, Ocak 0'dır, Aralık 11'dir
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);


        binding.devamsizlikGoruntu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ListView'i göster
                binding.dersleriGizle.setVisibility(View.VISIBLE);
                binding.galeriQr.setVisibility(View.GONE);
                binding.imageView7.setVisibility(View.GONE);
                binding.qrOku.setVisibility(View.GONE);
                binding.devamsizlikGoruntu.setVisibility(View.GONE);
                binding.cikisOgrenci.setVisibility(View.GONE);
                binding.textViewOgrenci.setVisibility(View.GONE);
                binding.deneme123.setVisibility(View.GONE);
                binding.ogrDersList.setVisibility(View.VISIBLE);
                getDersler();
            }
        });
        binding.dersleriGizle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.dersleriGizle.setVisibility(View.GONE);
                binding.galeriQr.setVisibility(View.VISIBLE);
                binding.imageView7.setVisibility(View.VISIBLE);
                binding.qrOku.setVisibility(View.VISIBLE);
                binding.devamsizlikGoruntu.setVisibility(View.VISIBLE);
                binding.cikisOgrenci.setVisibility(View.VISIBLE);
                binding.textViewOgrenci.setVisibility(View.VISIBLE);
                binding.deneme123.setVisibility(View.VISIBLE);
                binding.ogrDersList.setVisibility(View.GONE);

            }
        });
        binding.cikisOgrenci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cikis_yap = new Intent(activity_ogrenci_anasayfa.this,MainActivity.class);
                startActivity(cikis_yap);
            }
        });
        binding.qrOku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(activity_ogrenci_anasayfa.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Bir QR Kod Okutun");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();

            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else {
            // İzin zaten var, konum alımı işlemlerine devam et
        }



    }


    @Override
    public void konumGuncellendi(double enlem, double boylam) {
        Log.d("OGRENCİ Konum", "Enlem: " + enlem + ", Boylam: " + boylam);

        Log.d("deneme", "deneme_Enlem" + izinNesnesi2.getLatitude() + ",Boylam deneme" + izinNesnesi2.getLongitude());
        binding.deneme123.setText("Konum Hazır");
        konumHazir = true;
    }


    public void galeri_qr_basildi(View view) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, REQUEST_IMAGE_PICK);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data == null || data.getData() == null) {
                Log.e("TAG", "The uri is null, probably the user cancelled the image selection process using the back button.");
                return;
            }

            try {
                Uri imageUri = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(imageUri);

                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    if (bitmap != null) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        int[] pixels = new int[width * height];
                        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                        RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
                        BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
                        MultiFormatReader reader = new MultiFormatReader();

                        try {
                            Result result = reader.decode(bBitmap);
                            String content = result.getText(); // QR kodunun içeriği
                            Log.d("JSON", "JSON İçeriği: " + content);

                            // QR kod içeriğini JSON olarak analiz ediyoruz
                            JSONObject jsonObject = new JSONObject(content);
                            JSONObject qrObject = jsonObject.getJSONObject("QR");

                            // JSON'daki verilere erişerek değişkenlere atama yapıyoruz
                            Jak_konum_x = qrObject.getString("ak_konum_x");
                            Jak_konum_y = qrObject.getString("ak_konum_y");
                            Jhafta = qrObject.getString("hafta");
                            Jders_no = qrObject.getString("ders_no");
                            Jak_no = qrObject.getString("ak_no");
                            Jbolum_no = qrObject.getString("bolum_no");
                            Jay = qrObject.getString("ay");
                            Jgun = qrObject.getString("gun");
                            Jsaat = qrObject.getString("saat");
                            Jdakika = qrObject.getString("dakika");

                            if (konumHazir) {
                                //buraya yazacağız yeni fonksiyonu dakika karşılaştırıda oraya
                                ders_kontrol();

                            }



                        } catch (NotFoundException e) {
                            Log.e("TAG", "QR kodu bulunamadı", e);
                        } catch (JSONException e) {
                            Log.e("TAG", "JSON hatası", e);
                        }

                    } else {
                        Log.e("TAG", "Resim bitmap olarak okunamadı");
                    }

                    inputStream.close();
                }
            } catch (FileNotFoundException e) {
                Log.e("TAG", "Dosya bulunamadı", e);
            } catch (Exception e) {
                Log.e("TAG", "Bir hata oluştu", e);
            }
        }
        else {
                IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (intentResult != null) {
                    String content = intentResult.getContents();
                    if (content != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(content);
                            JSONObject qrObject = jsonObject.getJSONObject("QR");

                            Jak_konum_x = qrObject.getString("ak_konum_x");
                            Jak_konum_y = qrObject.getString("ak_konum_y");
                            Jhafta = qrObject.getString("hafta");
                            Jders_no = qrObject.getString("ders_no");
                            Jak_no = qrObject.getString("ak_no");
                            Jbolum_no = qrObject.getString("bolum_no");
                            Jay = qrObject.getString("ay");
                            Jgun = qrObject.getString("gun");
                            Jsaat = qrObject.getString("saat");
                            Jdakika = qrObject.getString("dakika");


                            if (konumHazir) {
                                ders_kontrol();

                            }

                        } catch (JSONException e) {
                            Log.e("TAG", "JSON hatası", e);
                        }
                    } else {
                        Log.e("TAG", "QR kod içeriği boş");
                    }
                }
            }

        }

/*
    public void yoklama_kabul() {
        double double_konum_x = Double.parseDouble(Jak_konum_x);
        double double_konum_y = Double.parseDouble(Jak_konum_y);
        //saat olayını düzelt ve veri tabanına kayıt gönder


        //double epsilon = 0.0001; // Karşılaştırma hassasiyeti
        double epsilon = 0.0004;

        if ((double_konum_x - epsilon <= izinNesnesi2.getLatitude() && izinNesnesi2.getLatitude() <= double_konum_x + epsilon) &&
                (double_konum_y - epsilon <= izinNesnesi2.getLongitude() && izinNesnesi2.getLongitude() <= double_konum_y + epsilon)) {
            Toast.makeText(this, "QR Başarıyla Okutuldu", Toast.LENGTH_LONG).show();
            veriGonder();
        } else {
            Toast.makeText(this, "Geçersiz Konum. Konumunuzu Güncelleyiniz", Toast.LENGTH_LONG).show();
        }

    }
*/

public void yoklama_kabul() {
    double scannedLatitude = Double.parseDouble(Jak_konum_x);
    double scannedLongitude = Double.parseDouble(Jak_konum_y);

    double savedLatitude = izinNesnesi2.getLatitude();
    double savedLongitude = izinNesnesi2.getLongitude();

    double threshold = 400.0; // Mesafe eşik değeri (metre cinsinden)

    if (isWithinDistance(savedLatitude, savedLongitude, scannedLatitude, scannedLongitude, threshold)) {
        Toast.makeText(this, "QR Başarıyla Okutuldu", Toast.LENGTH_LONG).show();
        veriGonder();
    } else {
        Toast.makeText(this, "Geçersiz Konum. Konumunuzu Güncelleyiniz", Toast.LENGTH_LONG).show();
    }
}

    public boolean isWithinDistance(double savedLatitude, double savedLongitude, double scannedLatitude, double scannedLongitude, double threshold) {
        float[] results = new float[1];
        Location.distanceBetween(savedLatitude, savedLongitude, scannedLatitude, scannedLongitude, results);
        float distanceInMeters = results[0];
        return distanceInMeters <= threshold;
    }





    public void tarih_karsilastirma(){
        if (month + 1 == Integer.parseInt(Jay) && day == Integer.parseInt(Jgun)){
            yoklama_kabul();
        }
        else {
            Toast.makeText(this, "Yok Yazıldınız", Toast.LENGTH_LONG).show();
        }
    }

    public void dakika_karsilastirma(){

        int gecerlilikSure = 15;
        int qr_Saat = Integer.parseInt(Jsaat);
        int qr_dakika = Integer.parseInt(Jdakika);
        int toplam_saat = hour-qr_Saat;
        int toplam_dakika = minute -qr_dakika;
        if(toplam_saat<0){
            toplam_saat=+24;
        }
        if(toplam_dakika<0){
            toplam_dakika=+60;
        }

        int toplam_sure = (toplam_saat)*60 + toplam_dakika;
        if(toplam_sure>gecerlilikSure){
            Toast.makeText(this, "Yok Yazıldınız", Toast.LENGTH_LONG).show();
        }
        else
        {
            tarih_karsilastirma();
        }

    }
    public void veriGonder(){

        ders_yoklama ogrenciData = new ders_yoklama(Integer.parseInt(Jders_no), ogr_no, Integer.parseInt(Jhafta));
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Void> call = apiService.sendOgrenciVeri(ogrenciData);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Veri başarıyla gönderildiğinde burada işlem yapabilirsiniz
                    Toast.makeText(activity_ogrenci_anasayfa.this, "Veri başarıyla gönderildi,Var Yazıldınız", Toast.LENGTH_LONG).show();
                }
                else if (!response.isSuccessful()) {
                    int errorCode = response.code();
                    // Hatanın detaylarını almak için response.errorBody() kullanabilirsiniz

                    // Hata kodunu loglamak için
                    Log.e("HTTP Hata Kodu", String.valueOf(errorCode));

                    // Hata mesajını göstermek için
                    Toast.makeText(activity_ogrenci_anasayfa.this, "Yoklamada zaten varsınız! " + errorCode, Toast.LENGTH_LONG).show();
                }
                else {
                    // Başarısız bir cevap alındığında burada işlem yapabilirsiniz
                    Toast.makeText(activity_ogrenci_anasayfa.this, "Veri gönderme başarısız oldu", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(activity_ogrenci_anasayfa.this, "Bir hata oluştu: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void getDersler() {
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<ogrenci_ders>> call = apiService.getOgrenciDers();

        call.enqueue(new Callback<List<ogrenci_ders>>() {
            @Override
            public void onResponse(Call<List<ogrenci_ders>> call, Response<List<ogrenci_ders>> response) {
                if (response.isSuccessful()) {
                    List<ogrenci_ders> responseList = response.body();
                    dersArray = new ArrayList<>(responseList);
                    List<String> dersAdList = new ArrayList<>();
                    filteredList = new ArrayList<>();

                    for (ogrenci_ders dersler : dersArray) {
                        // Belirli bir öğrenci numarasına göre dersleri filtreleme...
                        if (dersler.ogr_no == ogr_no) {
                            filteredList.add(dersler);
                        }
                    }

                   getders_ad();
                }
            }

            @Override
            public void onFailure(Call<List<ogrenci_ders>> call, Throwable t) {
                // Hata durumunda yapılacak işlemler...
            }
        });
    }
    private void getders_ad() {
        ApiService apiService2 = retrofit.create(ApiService.class);
        Call<List<ders>> ders = apiService2.getDers();
        ders.enqueue(new Callback<List<ders>>() {
            @Override
            public void onResponse(Call<List<ders>> call, Response<List<ders>> response) {
                if (response.isSuccessful()) {
                    List<ders> dersList = response.body();
                    filteredDers = new ArrayList<>();

                    // Iterate through filteredList and compare with student IDs to get names
                    for (ogrenci_ders ogrenciDers : filteredList) {
                        for (ders dersObj : dersList) {
                            if (ogrenciDers.ders_no == dersObj.ders_no) {
                                // Matched student ID, display the name
                                filteredDers.add(""+dersObj.ders_ad+"");
                                break; // Move to the next filtered item
                            }
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(activity_ogrenci_anasayfa.this, android.R.layout.simple_list_item_1, filteredDers);
                    binding.ogrDersList.setAdapter(adapter);

                    binding.ogrDersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedLesson = filteredDers.get(position);

                            // Seçilen ders adına göre ders numarasını bul
                            int selectedLessonNo = -1; // Default olarak -1 veya başka bir değer atayabilirsiniz
                            for (ders dersObj : dersList) {
                                if (selectedLesson.equals(""+dersObj.ders_ad+"")) {
                                    selectedLessonNo = dersObj.ders_no; // Ders numarasını aldık
                                    // Diğer değişkenler de burada alınabilir
                                    Intent intent = new Intent(activity_ogrenci_anasayfa.this, yoklama_goruntule_ogrenci.class);
                                    intent.putExtra("ders_no",dersObj.ders_no);
                                    intent.putExtra("ogr_no",ogr_no);

                                    // Pass necessary data using intent extras
                                    intent.putExtra("selectedLessonNo", selectedLessonNo);

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
                // Handle failure
            }
        });
    }
    private void ders_kontrol() {
        ApiService apiService_ders = retrofit.create(ApiService.class);
        Call<List<ogrenci_ders>> call_yoklama = apiService_ders.getOgrenciDers();
        call_yoklama.enqueue(new Callback<List<ogrenci_ders>>() {
            @Override
            public void onResponse(Call<List<ogrenci_ders>> call, Response<List<ogrenci_ders>> response) {
                if (response.isSuccessful()) {
                    ogrenciders = response.body();

                    boolean found = false; // Flag to check if the student is enrolled in the course

                    for (ogrenci_ders yoklama : ogrenciders) {
                        if (yoklama.ogr_no == ogr_no && yoklama.ders_no == Integer.parseInt(Jders_no)) {
                            found = true;
                            dakika_karsilastirma();
                            break; // No need to continue checking once found
                        }
                    }

                    if (!found) {
                        Toast.makeText(activity_ogrenci_anasayfa.this, "Bu Derse Kayıtlı Değilsiniz", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ogrenci_ders>> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
