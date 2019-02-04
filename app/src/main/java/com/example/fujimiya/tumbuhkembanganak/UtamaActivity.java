package com.example.fujimiya.tumbuhkembanganak;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class UtamaActivity extends AppCompatActivity {

    AlertDialog  alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_utama);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void balita(View view) {
        startActivity(new Intent(getApplicationContext(), BalitaTambahActivity.class));
    }

    public void keluar(View view) {
        SharedPreferences sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed=sp.edit();
        ed.putString("id", "");
        ed.putString("nama_user", "");
        ed.commit();

        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    public void list(View view) {
//        LayoutInflater layoutInflater = LayoutInflater.from(UtamaActivity.this);
//        View promptView = layoutInflater.inflate(R.layout.pilihan_input, null);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UtamaActivity.this);
//        alertDialogBuilder.setView(promptView);
//
//        Button btnperkembangan = promptView.findViewById(R.id.btnPerkembangan);
//        Button btncatatan = promptView.findViewById(R.id.btnCatatan);
//
//
//
//        alert = alertDialogBuilder.create();
//        alert.show();

        startActivity(new Intent(getApplicationContext(), ListBalita.class));


    }

    public void grafik(View view) {
        startActivity(new Intent(getApplicationContext(), ListGrafik.class));
    }

    public void menulain(View view) {
        startActivity(new Intent(getApplicationContext(), Alrm.class));
    }

    public void info(View view) {
        startActivity(new Intent(getApplicationContext(), Informasi.class));
    }
}
