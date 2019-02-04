package com.example.fujimiya.tumbuhkembanganak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class DaftarActivity extends AppCompatActivity {
    EditText txt_nama,txt_email,txt_nope,txt_alamat,txt_password;
    String nama,email,nope,alamat,password = "";
    String validasi = "no";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar2);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_nama = findViewById(R.id.txtnama);
        txt_email = findViewById(R.id.txtemail);
        txt_nope = findViewById(R.id.txttelp);
        txt_alamat = findViewById(R.id.txtalamat);
        txt_password = findViewById(R.id.txtpassword);
    }

    public void klik_login(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }



    public void daftar(View view) {
        if(!txt_nama.getText().toString().equals("") && !txt_alamat.getText().toString().equals("") && !txt_nope.getText().toString().equals("") && !txt_email.getText().toString().equals("") && !txt_password.getText().toString().equals("")){
            nama = txt_nama.getText().toString();
            alamat = txt_alamat.getText().toString();
            nope = txt_nope.getText().toString();
            email = txt_email.getText().toString();
            password = txt_password.getText().toString();
            validasi = "ok";
            ///Toast.makeText(this,"Data terisi : "+nama,Toast.LENGTH_SHORT).show();
            addEmployee(nama,alamat,nope,email,password);
        }else {
            //Toast.makeText(this,"Data Kosong",Toast.LENGTH_SHORT).show();
            if(txt_nama.getText().toString().equals("")){
                txt_nama.setHint("Nama Harap Diisi");
                txt_nama.setHintTextColor(Color.RED);
            }
           if(txt_alamat.getText().toString().equals("")){
                txt_alamat.setHint("Alamat Harap Diisi");
                txt_alamat.setHintTextColor(Color.RED);
            }
            if(txt_nope.getText().toString().equals("")){
                txt_nope.setHint("Telpon Harap Diisi");
                txt_nope.setHintTextColor(Color.RED);
            }
            if(txt_email.getText().toString().equals("")){
                txt_nama.setHint("Email Harap Diisi");
                txt_email.setHintTextColor(Color.RED);
            }
            if(txt_password.getText().toString().equals("")){
                txt_password.setHint("Password Harap Diisi");
                txt_password.setHintTextColor(Color.RED);
            }

        }


    }

    private void addEmployee(final String namasend,final String alamatsend,final String telpsend, final String emailsend, final String passwordsend){



        class AddEmployee extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DaftarActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(DaftarActivity.this,s, Toast.LENGTH_LONG).show();
                Intent kelogin = new Intent(DaftarActivity.this, LoginActivity.class);
                startActivity(kelogin);
                finish();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("nama_user",namasend);
                params.put("email",emailsend);
                params.put("nope",telpsend);
                params.put("alamat",alamatsend);
                params.put("password",passwordsend);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://balita.alfalaqproject.xyz/Controller_User/tambah/", params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();

    }
}
