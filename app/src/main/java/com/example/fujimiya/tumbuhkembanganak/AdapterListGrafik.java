package com.example.fujimiya.tumbuhkembanganak;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by fujimiya on 1/22/19.
 */

public class AdapterListGrafik extends RecyclerView.Adapter<AdapterListGrafik.ViewHolder> {
    private ArrayList<String> NamaB;
    private ArrayList<String> TempatB;
    private ArrayList<String> TanggalB;
    private ArrayList<String> JK;
    private ArrayList<String> ID;

    Context context;
    AlertDialog alert,alertperkembangan,alertcatatan;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private String JSON_STRING;

    EditText txtTanggal,txtBerat,txtTinggi,txtlingkar;

    EditText txttanggal_catat,txtjudul_catat,txtisi_catat;

    int posisi = 0;

    public AdapterListGrafik(Context contxt, ArrayList<String> IDBalita, ArrayList<String> NamaBalita, ArrayList<String> TempatLahir, ArrayList<String> TanggalLahir, ArrayList<String> JenisKelamin){
        NamaB = NamaBalita;
        TempatB = TanggalLahir;
        TanggalB = TempatLahir;
        JK = JenisKelamin;
        ID = IDBalita;
        context = contxt;
    }
    @Override
    public AdapterListGrafik.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_masuk, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterListGrafik.ViewHolder vh = new AdapterListGrafik.ViewHolder(v);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterListGrafik.ViewHolder holder, final int position) {
        if(JK.get(position).equals("L")) {
            holder.nama_jk.setText(NamaB.get(position) + " (" + "Laki-Laki" + ") ");
        }
        if(JK.get(position).equals("P")) {
            holder.nama_jk.setText(NamaB.get(position) + " (" + "Perempuan" + ") ");
        }
        holder.tempat_tanggal.setText(TempatB.get(position)+", "+TanggalB.get(position));
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View promptView = layoutInflater.inflate(R.layout.pilih_grafik, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setView(promptView);

                Button btnBerat = promptView.findViewById(R.id.btnBerat);
                Button btnTinggi = promptView.findViewById(R.id.btnTinggi);
                Button btnLkepala = promptView.findViewById(R.id.btnLkepala);

                btnBerat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sp=context.getSharedPreferences("balita", Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed=sp.edit();
                        ed.putString("id_balita", ID.get(position));
                        ed.commit();
                        context.startActivity(new Intent(context, GrafikBerat.class));
                        alert.cancel();
                    }
                });

                btnTinggi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sp=context.getSharedPreferences("balita", Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed=sp.edit();
                        ed.putString("id_balita", ID.get(position));
                        ed.commit();
                        context.startActivity(new Intent(context, GrafikTinggi.class));
                        alert.cancel();
                    }
                });

                btnLkepala.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences sp=context.getSharedPreferences("balita", Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed=sp.edit();
                        ed.putString("id_balita", ID.get(position));
                        ed.commit();
                        context.startActivity(new Intent(context, GrafikL_kepala.class));
                        alert.cancel();
                    }
                });

//

                // create an alert dialog
                alert = alertDialogBuilder.create();
                alert.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return NamaB.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nama_jk;
        public TextView tempat_tanggal;
        public CardView cvMain;
        public ViewHolder(View v) {
            super(v);
            nama_jk = (TextView) v.findViewById(R.id.nama_jk_balita);
            tempat_tanggal = (TextView) v.findViewById(R.id.tempat_tanggal);
            cvMain = (CardView) v.findViewById(R.id.cv_main);


        }
    }
}
