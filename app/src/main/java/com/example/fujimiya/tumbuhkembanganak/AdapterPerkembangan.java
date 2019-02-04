package com.example.fujimiya.tumbuhkembanganak;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by fujimiya on 1/19/19.
 */

public class AdapterPerkembangan extends RecyclerView.Adapter<AdapterPerkembangan.ViewHolder> {
    private ArrayList<String> Tanggal;
    private ArrayList<String> Data;
    private ArrayList<String> ID;

    AlertDialog alert;

    Context context;
    int posisi = 0;

    public AdapterPerkembangan(Context contxt,ArrayList<String> IDget, ArrayList<String> Tanggalget, ArrayList<String> Dataget){
      Data = Dataget;
      Tanggal = Tanggalget;
      context = contxt;
      ID = IDget;
    }

    @Override
    public AdapterPerkembangan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_perkembangan, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterPerkembangan.ViewHolder vh = new AdapterPerkembangan.ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterPerkembangan.ViewHolder holder, final int position) {
        holder.txtTanggal.setText(Tanggal.get(position));
        holder.txtData.setText(Data.get(position));
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posisi = position;
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.dialog_kembang, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptView);

                Button hapus = promptView.findViewById(R.id.btnhapus);

                hapus.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 AlertDialog.Builder alrtDialogBuilder = new AlertDialog.Builder(context);
                                                 alrtDialogBuilder.setMessage("Apakah anda yakin ingin menghapus?");
                                                 alrtDialogBuilder.setPositiveButton("YA",
                                                         new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface arg0, int arg1) {
                                                                 //Toast.makeText(context,"klik lanjutkan",Toast.LENGTH_LONG).show();
//                                                              );
                                                                 hapuskembangan();
                                                                 alert.cancel();

                                                             }
                                                         });

                                                 alrtDialogBuilder.setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialog, int which) {
                                                         dialog.cancel();

                                                     }
                                                 });

                                                 AlertDialog alrtDialog = alrtDialogBuilder.create();
                                                 alrtDialog.show();

                                             }
                                         });




                // create an alert dialog
                alert = alertDialogBuilder.create();
                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTanggal;
        public TextView txtData;
        public CardView cvMain;

        public ViewHolder(View v) {
            super(v);
            cvMain = (CardView) v.findViewById(R.id.cv_main);
            txtTanggal = (TextView) v.findViewById(R.id.tanggal);
            txtData = (TextView) v.findViewById(R.id.data);
        }
    }

    private void hapuskembangan(){



        class hapusbalita extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(context,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(context,s, Toast.LENGTH_LONG).show();
//                Intent kelogin = new Intent(BalitaTambahActivity.this, LoginActivity.class);
//                startActivity(kelogin);
//                finish();

                alert.cancel();



            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id_perkembangan",ID.get(posisi));
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://balita.alfalaqproject.xyz/Controller_Balita/hapus_perkembangan/", params);
                return res;
            }
        }

        hapusbalita ae = new hapusbalita();
        ae.execute();

    }
}
