package com.example.fujimiya.tumbuhkembanganak;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.AlarmClock;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Alrm extends AppCompatActivity {
    private TimePickerDialog timePickerDialog;
    String jam_pagi,jam_siang,jam_sore;
    String menit_pagi,menit_siang, menit_sore;
    TextView txtpagi,txtsiang,txtsore;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alrm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Balita APP");
        actionBar.setSubtitle("Menu Lain");


        txtpagi = findViewById(R.id.txtsarapan);
        txtsiang = findViewById(R.id.txtsiang);
        txtsore = findViewById(R.id.txtsore);

        SharedPreferences sp=getSharedPreferences("sarapan", Context.MODE_PRIVATE);
        txtpagi.setText( sp.getString("jam", "")+":"+ sp.getString("menit", ""));

        SharedPreferences ss=getSharedPreferences("siang", Context.MODE_PRIVATE);
        txtsiang.setText( ss.getString("jam", "")+":"+ ss.getString("menit", ""));

        SharedPreferences se=getSharedPreferences("sore", Context.MODE_PRIVATE);
        txtsore.setText( se.getString("jam", "")+":"+ se.getString("menit", ""));
    }

    public void setsarapan(View view) {
        showTimesarapan();
    }

    public void siang(View view) {
        showTimessiang();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void sore(View view) {
        showTimessore();
    }

//    public void alrm(View view) {
//        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
//        i.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
//        i.putExtra(AlarmClock.EXTRA_HOUR, 9);
//        i.putExtra(AlarmClock.EXTRA_MINUTES, 35);
//        startActivity(i);
//    }

    private void showTimesarapan() {

        /**
         * Calendar untuk mendapatkan waktu saat ini
         */
        Calendar calendar = Calendar.getInstance();

        /**
         * Initialize TimePicker Dialog
         */
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
//                tvTimeResult.setText("Waktu dipilih = "+hourOfDay+":"+minute);
                txtpagi.setText(hourOfDay+":"+minute);
                SharedPreferences sp=getSharedPreferences("sarapan", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed=sp.edit();
                ed.putString("jam", String.valueOf(hourOfDay));
                ed.putString("menit", String.valueOf(minute));
                ed.commit();

                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                i.putExtra(AlarmClock.EXTRA_MESSAGE, "Sarapan");
                i.putExtra(AlarmClock.EXTRA_HOUR, hourOfDay);
                i.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                startActivity(i);

            }
        },
                /**
                 * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
                 */
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                /**
                 * Cek apakah format waktu menggunakan 24-hour format
                 */
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }
    private void showTimessiang() {

        /**
         * Calendar untuk mendapatkan waktu saat ini
         */
        Calendar calendar = Calendar.getInstance();

        /**
         * Initialize TimePicker Dialog
         */
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
//                tvTimeResult.setText("Waktu dipilih = "+hourOfDay+":"+minute);
                txtsiang.setText(hourOfDay+":"+minute);
                SharedPreferences sp=getSharedPreferences("siang", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed=sp.edit();
                ed.putString("jam", String.valueOf(hourOfDay));
                ed.putString("menit", String.valueOf(minute));
                ed.commit();

                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                i.putExtra(AlarmClock.EXTRA_MESSAGE, "Siang");
                i.putExtra(AlarmClock.EXTRA_HOUR, hourOfDay);
                i.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                startActivity(i);

            }
        },
                /**
                 * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
                 */
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                /**
                 * Cek apakah format waktu menggunakan 24-hour format
                 */
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    private void showTimessore() {

        /**
         * Calendar untuk mendapatkan waktu saat ini
         */
        Calendar calendar = Calendar.getInstance();

        /**
         * Initialize TimePicker Dialog
         */
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
//                tvTimeResult.setText("Waktu dipilih = "+hourOfDay+":"+minute);
                txtsore.setText(hourOfDay+":"+minute);
                SharedPreferences sp=getSharedPreferences("sore", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed=sp.edit();
                ed.putString("jam", String.valueOf(hourOfDay));
                ed.putString("menit", String.valueOf(minute));
                ed.commit();

                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                i.putExtra(AlarmClock.EXTRA_MESSAGE, "Sore");
                i.putExtra(AlarmClock.EXTRA_HOUR, hourOfDay);
                i.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                startActivity(i);

            }
        },
                /**
                 * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
                 */
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                /**
                 * Cek apakah format waktu menggunakan 24-hour format
                 */
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }
}
