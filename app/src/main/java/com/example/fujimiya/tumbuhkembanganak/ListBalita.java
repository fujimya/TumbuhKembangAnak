package com.example.fujimiya.tumbuhkembanganak;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListBalita extends AppCompatActivity {

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
        setContentView(R.layout.activity_list_balita);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Balita APP");
        actionBar.setSubtitle("Catatan Balita");

        NamaB = new ArrayList<String>();
        TempatB = new ArrayList<String>();
        TanggalB = new ArrayList<String>();
        JK = new ArrayList<String>();
        ID = new ArrayList<String>();


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        rvView = findViewById(R.id.rv_main_barang);
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(ListBalita.this, 1));

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
    public void getJSON(){

        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListBalita.this,"Mengambil Data","Mohon Tunggu...",false,false);
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


                adapter = new AdapterListBalita(ListBalita.this,ID,NamaB,TempatB,TanggalB,JK);
                rvView.setAdapter(adapter);

            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
