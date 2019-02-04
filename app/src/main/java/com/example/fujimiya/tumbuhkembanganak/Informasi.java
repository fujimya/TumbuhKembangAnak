package com.example.fujimiya.tumbuhkembanganak;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.SearchView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Informasi extends AppCompatActivity {
    private ArrayList<String> Tanggal;
    private ArrayList<String> Data;
    private ArrayList<String> Judul;
    private ArrayList<String> ID;


    private ArrayList<String> TanggalCari;
    private ArrayList<String> DataCari;
    private ArrayList<String> JudulCari;
    private ArrayList<String> IDCari;

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String JSON_STRING;


    FloatingActionButton btntambah;
    AlertDialog alert;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    EditText txtTanggal,txtJudul,txtIsi;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Balita APP");
        actionBar.setSubtitle("List Perkembangan");

        Tanggal = new ArrayList<String>();
        Data = new ArrayList<String>();
        Judul =  new ArrayList<String>();
        ID =  new ArrayList<String>();

        TanggalCari = new ArrayList<String>();
        DataCari = new ArrayList<String>();
        JudulCari =  new ArrayList<String>();
        IDCari =  new ArrayList<String>();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        btntambah = findViewById(R.id.tambah_info);

        SharedPreferences sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        String value = sp.getString("username_user", "");
        if(value.equals("admin@mail.com")){
           btntambah.setVisibility(View.VISIBLE);
        }
        if(!value.equals("admin@mail.com")){
            btntambah.setVisibility(View.INVISIBLE);
        }

        rvView = findViewById(R.id.rv_main_barang);
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(Informasi.this, 1));

        getJSON();

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJSON();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        final SearchView searchView = findViewById(R.id.svCari);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()) {
                    if(!TanggalCari.isEmpty()){
                        TanggalCari.clear();
                    }

                    if(!JudulCari.isEmpty()){
                        JudulCari.clear();
                    }

                    if(!DataCari.isEmpty()){
                        DataCari.clear();
                    }

                    if(!IDCari.isEmpty()){
                        IDCari.clear();
                    }

                    for(int a =0; a < ID.size(); a++){
                        if(Judul.get(a).toLowerCase().contains(newText.toLowerCase())) {
                            IDCari.add(ID.get(a));
                            DataCari.add(Data.get(a));
                            JudulCari.add(Judul.get(a));
                            TanggalCari.add(Tanggal.get(a));


                        }
                    }
                    adapter = new AdapterInformasi(Informasi.this,IDCari,TanggalCari,JudulCari,DataCari);
                    rvView.setAdapter(adapter);
                }
                else{
                    adapter = new AdapterInformasi(Informasi.this,ID,Tanggal,Judul,Data);
                    rvView.setAdapter(adapter);
                }
                return false;
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

    public void getJSON(){

        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Informasi.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
//                Toast.makeText(Informasi.this,""+s, Toast.LENGTH_SHORT).show();
                showEmployee();

            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest("http://balita.alfalaqproject.xyz/Controller_Balita/listinformasi");
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
            JSONArray result = jsonObject.getJSONArray("informasi");
//            Toast.makeText(BalitaTambahActivity.this,"Get Json : "+JSON_STRING,Toast.LENGTH_LONG).show();

            if(!Tanggal.isEmpty()){
                Tanggal.clear();
            }

            if(!Judul.isEmpty()){
                Judul.clear();
            }

            if(!Data.isEmpty()){
                Data.clear();
            }

            if(!ID.isEmpty()){
                ID.clear();
            }


            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
//                nama = jo.getString("nama_user");
                //String jabatan = jo.getString("jabatan");
                //Toast.makeText(Login.this,"Get Json : "+nama,Toast.LENGTH_LONG).show();
                Tanggal.add(""+jo.getString("tanggal"));
                Data.add(""+jo.getString("isi"));
                Judul.add(""+jo.getString("judul"));
                ID.add(""+jo.getString("id_informasi"));


                adapter = new AdapterInformasi(Informasi.this,ID,Tanggal,Judul,Data);
                rvView.setAdapter(adapter);

            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void tambah(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(Informasi.this);
        View promptView = layoutInflater.inflate(R.layout.informasi_add, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Informasi.this);
        alertDialogBuilder.setView(promptView);


//
////        final EditText txtKategori = (EditText) promptView.findViewById(R.id.txtkategori);

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

        // create an alert dialog

        txtTanggal = promptView.findViewById(R.id.txtTanggal);
        txtJudul = promptView.findViewById(R.id.txt_judul);
        txtIsi = promptView.findViewById(R.id.txt_isi);
        final Button btnSimpan = promptView.findViewById(R.id.btnSimpanInfo);

        txtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTanggal();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmployeeInfo(txtJudul.getText().toString(),txtIsi.getText().toString(),txtTanggal.getText().toString());
            }
        });


        alert = alertDialogBuilder.create();
        alert.show();
    }

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
                txtTanggal.setText(dateFormatter.format(newDate.getTime()));

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();


    }

    private void addEmployeeInfo(final String judul,final String isi,final String tanggal){



        class addEmployeeInfo extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Informasi.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Informasi.this,s, Toast.LENGTH_LONG).show();
//                Intent kelogin = new Intent(BalitaTambahActivity.this, LoginActivity.class);
//                startActivity(kelogin);
//                finish();

                getJSON();

                alert.cancel();



            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("judul",judul);
                params.put("isi",isi);
                params.put("tanggal",tanggal);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://balita.alfalaqproject.xyz/Controller_Balita/tambah_informasi/", params);
                return res;
            }
        }

        addEmployeeInfo ae = new addEmployeeInfo();
        ae.execute();

    }

    public void catatanimun(View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(Informasi.this);
        View promptView = layoutInflater.inflate(R.layout.popup_imun, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Informasi.this);
        alertDialogBuilder.setView(promptView);
        PhotoView photoView = promptView.findViewById(R.id.imageView);
        photoView.setImageResource(R.drawable.catatan_imunisasi_anak);

        alert = alertDialogBuilder.create();
        alert.show();

    }
}
