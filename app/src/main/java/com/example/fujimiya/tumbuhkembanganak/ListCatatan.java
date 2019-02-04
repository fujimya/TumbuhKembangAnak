package com.example.fujimiya.tumbuhkembanganak;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListCatatan extends AppCompatActivity {


    private ArrayList<String> Tanggal;
    private ArrayList<String> Data;
    private ArrayList<String> ID;

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String JSON_STRING;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_catatan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Balita APP");
        actionBar.setSubtitle("List Catatan");

        Tanggal = new ArrayList<String>();
        Data = new ArrayList<String>();
        ID = new ArrayList<String>();

        rvView = findViewById(R.id.rv_main_barang);
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(ListCatatan.this, 1));

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
                loading = ProgressDialog.show(ListCatatan.this,"Mengambil Data","Mohon Tunggu...",false,false);
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
                SharedPreferences sp=getSharedPreferences("balita", Context.MODE_PRIVATE);
                String value = sp.getString("id_balita", "");
                String s = rh.sendGetRequest("http://balita.alfalaqproject.xyz/Controller_Balita/listcatatan/"+value);
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

            if(!Tanggal.isEmpty()){
                Tanggal.clear();
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
                Data.add(""+jo.getString("data"));
                ID.add(""+jo.getString("id_catatan"));


                adapter = new AdapterCatatan(ListCatatan.this,ID,Tanggal,Data);
                rvView.setAdapter(adapter);

            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
