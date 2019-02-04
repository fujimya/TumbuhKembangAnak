package com.example.fujimiya.tumbuhkembanganak;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class BalitaTambahActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    EditText txtnama,txttempatlahir,txttanggal;

    String nama,tempat,jk,tanggal;
    AlertDialog alert;

    private String JSON_STRING;

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> NamaB ;
    private ArrayList<String> TempatB;
    private ArrayList<String> TanggalB ;
    private ArrayList<String> JK;
    private ArrayList<String> ID;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balita_tambah);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Balita APP");
        actionBar.setSubtitle("Data Balita");

        NamaB = new ArrayList<String>();
        TempatB = new ArrayList<String>();
        TanggalB = new ArrayList<String>();
        JK = new ArrayList<String>();
        ID = new ArrayList<String>();

        txttanggal = findViewById(R.id.txtTgl_lahir);
        txtnama = findViewById(R.id.txtNama_balita);
        txttempatlahir = findViewById(R.id.txttempat_lahir);
        txttanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTanggal();
            }
        });


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        rvView = findViewById(R.id.rv_main_barang);
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(BalitaTambahActivity.this, 1));

        getJSON();

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJSON();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
//    public void tambah(View view) {
//        LayoutInflater layoutInflater = LayoutInflater.from(BalitaTambahActivity.this);
//        View promptView = layoutInflater.inflate(R.layout.balita_input, null);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BalitaTambahActivity.this);
//        alertDialogBuilder.setView(promptView);
//
//
//
////        final EditText txtKategori = (EditText) promptView.findViewById(R.id.txtkategori);
//        final Button btnSimpan = promptView.findViewById(R.id.btnSimpan);
//
//        txttanggal = promptView.findViewById(R.id.txtTgl_lahir);
//        txtnama = promptView.findViewById(R.id.txtNama_balita);
//        txttempatlahir = promptView.findViewById(R.id.txttempat_lahir);
//        txttanggal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getTanggal();
//            }
//        });
//
//        // create an alert dialog
//        alert = alertDialogBuilder.create();
//        alert.show();
//    }


    private void getTanggal(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                txttanggal.setText(dateFormatter.format(newDate.getTime()));

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();


    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rdlaki:
                jk = "L";
                break;
            case R.id.rdperempuan:
                if (checked)
                jk = "P";
                break;
        }
    }

    public void simpan(View view) {
        nama = txtnama.getText().toString();
        tempat = txttempatlahir.getText().toString();
        tanggal = txttanggal.getText().toString();
        addEmployee(nama,tempat,tanggal,jk);

    }

    private void addEmployee(final String namabalita,final String tempatlahir,final String tanggallahir, final String jeniskelamin){



        class AddEmployee extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(BalitaTambahActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(BalitaTambahActivity.this,s, Toast.LENGTH_LONG).show();
//                Intent kelogin = new Intent(BalitaTambahActivity.this, LoginActivity.class);
//                startActivity(kelogin);
//                finish();

                getJSON();
//                alert.cancel();

                txtnama.setText(null);
                txttanggal.setText(null);
                txttempatlahir.setText(null);



            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("nama_balita",namabalita);
                params.put("tempat_lahir",tempatlahir);
                params.put("tanggal_lahir",tanggallahir);
                params.put("jenis_kelamin",jeniskelamin);
                SharedPreferences sp=getSharedPreferences("login", Context.MODE_PRIVATE);
                String baca = sp.getString("id", "");
                params.put("id_ortu",baca);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://balita.alfalaqproject.xyz/Controller_Balita/tambah/", params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();

    }

    public void getJSON(){

        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(BalitaTambahActivity.this,"Mengambil Data","Mohon Tunggu...",false,false);
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
                SharedPreferences sp=getSharedPreferences("login", Context.MODE_PRIVATE);
                String value = sp.getString("id", "");
                String s = rh.sendGetRequest("http://balita.alfalaqproject.xyz/Controller_Balita/listbalita/"+value);
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
            JSONArray result = jsonObject.getJSONArray("balita");
//            Toast.makeText(BalitaTambahActivity.this,"Get Json : "+JSON_STRING,Toast.LENGTH_LONG).show();

            if(!NamaB.isEmpty()){
                NamaB.clear();
            }


            if(!TanggalB.isEmpty()){
                TanggalB.clear();
            }


            if(!TempatB.isEmpty()){
                TempatB.clear();
            }

            if(!JK.isEmpty()){
                JK.clear();
            }


            if(!ID.isEmpty()){
                ID.clear();
            }

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
//                nama = jo.getString("nama_user");
                //String jabatan = jo.getString("jabatan");
                //Toast.makeText(Login.this,"Get Json : "+nama,Toast.LENGTH_LONG).show();
                ID.add(""+jo.getString("id_balita"));
                NamaB.add(""+jo.getString("nama_balita"));
                TempatB.add(""+jo.getString("tempat_lahir"));
                TanggalB.add(""+jo.getString("tanggal_lahir"));
                JK.add(""+jo.getString("jenis_kelamin"));


                adapter = new AdapterBalita(BalitaTambahActivity.this,ID,NamaB,TempatB,TanggalB,JK);
                rvView.setAdapter(adapter);

            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
