package com.qrrollcall.qrrollcall.view;

import static com.qrrollcall.qrrollcall.adapter.ApiClient.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import com.qrrollcall.qrrollcall.R;
import com.qrrollcall.qrrollcall.model.ders_yoklama;
import com.qrrollcall.qrrollcall.model.izin;
import com.qrrollcall.qrrollcall.model.ogrenci_ders;
import com.qrrollcall.qrrollcall.service.ApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonDetailActivity extends AppCompatActivity implements izin.KonumBilgisiCallback {
    private boolean konumHazir = false;

    private boolean qrOlusturuldu = false;
    private izin izinNesnesi;
    public Spinner spinner;
    public TextView denemeHoca;
    public int selectedWeekPosition;

    private int ak_no, ders_no, bolum_no;
    private ImageView imageView;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123; // Kendi belirlediğiniz bir değer olabilir

    String[] haftalar  = {"1. Hafta", "2. Hafta", "3. Hafta", "4. Hafta", "5. Hafta",
            "6. Hafta", "7. Hafta", "8. Hafta", "9. Hafta", "10. Hafta",
            "11. Hafta", "12. Hafta", "13. Hafta", "14. Hafta", "15. Hafta"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_detail);


        izinNesnesi = new izin(this);
        izinNesnesi.setKonumCallback(this);
        izinNesnesi.requestLocationUpdates();



        imageView = findViewById(R.id.imageView6);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else {
            // İzin zaten var, konum alımı işlemlerine devam et
        }




        Intent intent = getIntent();
        if (intent != null) {
            ak_no = intent.getIntExtra("ak_no", 0);
            ders_no = intent.getIntExtra("ders_no", 0);
            bolum_no = intent.getIntExtra("bolum_no", 0);
        }


        Button tus = (Button) findViewById(R.id.yoklama_al);
        tus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedWeekPosition = spinner.getSelectedItemPosition(); // Seçilen öğenin pozisyonunu alır

                if (!qrOlusturuldu && konumHazir) { // Yalnızca QR kodu henüz oluşturulmadıysa ve konum hazırsa
                    createQRCode();
                    qrOlusturuldu = true; // QR kodunun oluşturulduğunu işaretleyin
                }

            }
        });
        Button yoklama_gor = (Button) findViewById(R.id.yoklama_goruntule);
        yoklama_gor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedWeekPosition = spinner.getSelectedItemPosition(); // Seçilen öğenin pozisyonunu alır
                yoklama_goruntule_intent();

            }
        });
        spinner = findViewById(R.id.spinner); // Spinner referansını atayın

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, haftalar);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);



// selectedWeek değişkeni seçilen haftanın adını içerecektir

    }

    private void createQRCode() {
        // QR kodu oluşturma işlemi...

        Calendar calendar = Calendar.getInstance(); // Mevcut tarih ve saat bilgisini alır

        int month = calendar.get(Calendar.MONTH)+1; // Not: Ay Aralık ayı için 0'dan başlar, Ocak 0'dır, Aralık 11'dir
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        String akademisyenBilgileri = "{ \"QR\": { \"ak_konum_x\": \"" + izinNesnesi.getLatitude() + "\", \"ak_konum_y\": \"" + izinNesnesi.getLongitude() + "\", \"hafta\": \"" + (selectedWeekPosition+1) + "\", \"ders_no\": \"" + ders_no + "\",\"ak_no\":\"" + ak_no + "\",\"bolum_no\": \"" + bolum_no + "\",\"ay\": \"" + month + "\",\"gun\": \"" + day + "\",\"saat\": \"" + hour +"\",\"dakika\": \"" + minute + "\" } }";


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(akademisyenBilgileri, BarcodeFormat.QR_CODE, 500, 500);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
            saveQRToGallery2(bitmap);
            saveQRToGallery(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    /*
    @Override
    public void konumGuncellendi(final double enlem, final double boylam) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("Konum", "Enlem: " + enlem + ", Boylam: " + boylam);
                denemeHoca = findViewById(R.id.denemeHoca);
                denemeHoca.setText("Enlem: " + enlem + ", Boylam: " + boylam);
                konumHazir = true;
            }
        });
    }
     */
    @Override
    public void konumGuncellendi(double enlem, double boylam) {
        Log.d("Konum", "Enlem: " + enlem + ", Boylam: " + boylam);
        denemeHoca = (TextView)findViewById(R.id.denemeHoca);
        denemeHoca.setText("Konum Hazır");
        konumHazir = true;
    }

    private void saveQRToGallery2(Bitmap bitmap) {
        String imageFileName = "QR_Code_" + System.currentTimeMillis() + ".png";
        MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                imageFileName,
                "QR Code"
        );
    }

    private void saveQRToGallery(Bitmap bitmap) {
        String fileName = "QRCode.png";
        String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/QRCodeFolder";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(folderPath, fileName);
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void yoklama_goruntule_intent(){
        Intent yoklama_intent = new Intent(LessonDetailActivity.this,yoklama_goruntule_akademisyen.class);
        yoklama_intent.putExtra("ders_no",ders_no);
        yoklama_intent.putExtra("ak_no",ak_no);
        yoklama_intent.putExtra("hafta_no",(selectedWeekPosition+1));
        startActivity(yoklama_intent);
    }



}

