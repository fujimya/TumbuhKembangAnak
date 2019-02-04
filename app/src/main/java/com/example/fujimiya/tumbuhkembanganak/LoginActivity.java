package com.example.fujimiya.tumbuhkembanganak;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private String JSON_STRING;
    private String username;
    private String password;
    private String id;
    private String nama;

    EditText txt_username,txt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.masuk2akun);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        String value = sp.getString("id", "");
        if(!value.equals("")){
            startActivity(new Intent(getApplicationContext(), UtamaActivity.class));
            finish();
        }
    }

    public void daftarkuy(View view) {
        startActivity(new Intent(getApplicationContext(), DaftarActivity.class));
        finish();
    }

    public void btn_masuk(View view) {
        getJSON();
    }

    private void getJSON(){

        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                //Toast.makeText(LoginActivity.this,""+s,Toast.LENGTH_SHORT).show();
                showEmployee();

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest("http://balita.alfalaqproject.xyz/Controller_User/listUser");
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void showEmployee(){

        JSONObject jsonObject = null;
//        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray("customer");
            //Toast.makeText(Login.this,"Get Json : "+JSON_STRING,Toast.LENGTH_LONG).show();
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                id = jo.getString("id");
                nama = jo.getString("nama_user");
                username = jo.getString("email");
                password = jo.getString("password");
                //String jabatan = jo.getString("jabatan");
                //Toast.makeText(Login.this,"Get Json : "+nama,Toast.LENGTH_LONG).show();

                if(username.equals(txt_username.getText().toString()) && password.equals(txt_password.getText().toString())){
                    //Toast.makeText(LoginActivity.this,"Username : "+id+" Password : "+nama+" Anda berhasil Login",Toast.LENGTH_SHORT).show();

                    SharedPreferences sp=getSharedPreferences("login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ed=sp.edit();
                    ed.putString("id", id);
                    ed.putString("nama_user", nama);
                    ed.putString("username_user", username);
                    ed.commit();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Anda berhasil masuk akun");
                    alertDialogBuilder.setPositiveButton("Lanjutkan",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //Toast.makeText(LoginActivity.this,"klik lanjutkan",Toast.LENGTH_LONG).show();

                                    finish();
                                    startActivity(new Intent(getApplicationContext(), UtamaActivity.class));
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

            }
            if(!username.equals(txt_username.getText().toString()) && !password.equals(txt_password.getText().toString())) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Anda Gagal masuk, silahkan periksa kembali username dan password anda.");
                alertDialogBuilder.setPositiveButton("Ya",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                txt_username.setText(null);
                                txt_username.setHint("User Name");
                                txt_username.setHintTextColor(Color.WHITE);
                                txt_password.setText(null);
                                txt_password.setHint("Password");
                                txt_password.setHintTextColor(Color.WHITE);
                            }
                        });
                alertDialogBuilder.setNegativeButton("Keluar",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
