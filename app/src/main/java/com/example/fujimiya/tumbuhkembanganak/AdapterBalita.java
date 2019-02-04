package com.example.fujimiya.tumbuhkembanganak;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by fujimiya on 10/2/18.
 */

public class AdapterBalita extends RecyclerView.Adapter<AdapterBalita.ViewHolder> {
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

    public AdapterBalita(Context contxt, ArrayList<String> IDBalita, ArrayList<String> NamaBalita, ArrayList<String> TempatLahir, ArrayList<String> TanggalLahir, ArrayList<String> JenisKelamin){
        NamaB = NamaBalita;
        TempatB = TanggalLahir;
        TanggalB = TempatLahir;
        JK = JenisKelamin;
        ID = IDBalita;
        context = contxt;
    }


    @Override
    public AdapterBalita.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_masuk, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        AdapterBalita.ViewHolder vh = new AdapterBalita.ViewHolder(v);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        return vh;
    }

    @Override
    public void onBindViewHolder(final AdapterBalita.ViewHolder holder, final int position) {
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
            posisi = position;
            Toast.makeText(context,""+ID.get(position),Toast.LENGTH_SHORT).show();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptView = layoutInflater.inflate(R.layout.pilihan_input, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setView(promptView);

            Button btnperkembangan = promptView.findViewById(R.id.btnPerkembangan);
            Button btncatatan = promptView.findViewById(R.id.btnCatatan);
            Button hapus = promptView.findViewById(R.id.btnhapus);

            btnperkembangan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutperkembangan = LayoutInflater.from(context);
                    View perkembang = layoutperkembangan.inflate(R.layout.balita_perkembangan, null);
                    AlertDialog.Builder dialogperkembangan = new AlertDialog.Builder(context);
                    dialogperkembangan.setView(perkembang);

                    txtTanggal  = perkembang.findViewById(R.id.txtTanggal);
                    txtBerat = perkembang.findViewById(R.id.txtBerat_badan);
                    txtTinggi = perkembang.findViewById(R.id.txtTinggi_badan);
                    txtlingkar = perkembang.findViewById(R.id.txtLingkar);

                    Button simpan = perkembang.findViewById(R.id.btnSimpanPerkembangan);

                    txtTanggal.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getTanggal();
                        }
                    });

                    simpan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            addEmployee(txtTinggi.getText().toString(),txtBerat.getText().toString(),txtlingkar.getText().toString(),txtTanggal.getText().toString());
                            alertperkembangan.cancel();

                        }
                    });




                    // create an alert dialog
                    alertperkembangan = dialogperkembangan.create();
                    alertperkembangan.show();

                    alert.cancel();
                }
            });


            btncatatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutcatatan = LayoutInflater.from(context);
                    View catatan = layoutcatatan.inflate(R.layout.catatan_balita, null);
                    AlertDialog.Builder dialogcatatan = new AlertDialog.Builder(context);
                    dialogcatatan.setView(catatan);

                    txttanggal_catat = catatan.findViewById(R.id.txtTanggal);
                    txtjudul_catat = catatan.findViewById(R.id.txt_judul);
                    txtisi_catat = catatan.findViewById(R.id.txt_isi);

                    // create an alert dialog
                    alertcatatan = dialogcatatan.create();
                    alertcatatan.show();

                    txttanggal_catat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getTanggalCatat();
                        }
                    });

                    Button simpancatatan = catatan.findViewById(R.id.btnSimpanCatatan);

                    simpancatatan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addEmployeeCatat(txtjudul_catat.getText().toString(),txtisi_catat.getText().toString(),txttanggal_catat.getText().toString());
                            alertcatatan.cancel();
                        }
                    });


                    alert.cancel();
                }
            });

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    hapusbalita();
//                    ((BalitaTambahActivity)context).finish();
//                    context.startActivity(new Intent(context, BalitaTambahActivity.class));
                    AlertDialog.Builder alrtDialogBuilder = new AlertDialog.Builder(context);
                    alrtDialogBuilder.setMessage("Apakah anda yakin ingin menghapus?");
                    alrtDialogBuilder.setPositiveButton("YA",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    //Toast.makeText(context,"klik lanjutkan",Toast.LENGTH_LONG).show();
                                    hapusbalita();
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


    private void getTanggal(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

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

    private void getTanggalCatat(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

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
                txttanggal_catat.setText(dateFormatter.format(newDate.getTime()));

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();


    }
    private void addEmployee(final String tinggi,final String berat,final String lingkar_kepala,final String tanggal){



        class AddEmployee extends AsyncTask<Void,Void,String> {

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

                alertperkembangan.cancel();



            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id_balita",ID.get(posisi));
                params.put("tinggi",tinggi);
                params.put("berat",berat);
                params.put("lingkar_kepala",lingkar_kepala);
                params.put("tanggal",tanggal);
                SharedPreferences sp= context.getSharedPreferences("login", Context.MODE_PRIVATE);
                String baca = sp.getString("id", "");
                params.put("id_ortu",baca);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://balita.alfalaqproject.xyz/Controller_Balita/tambah_perkembangan/", params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();

    }

    private void addEmployeeCatat(final String judul,final String isi,final String tanggal){



        class addEmployeeCatat extends AsyncTask<Void,Void,String> {

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

                alertcatatan.cancel();



            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id_balita",ID.get(posisi));
                params.put("judul",judul);
                params.put("isi",isi);
                SharedPreferences sp= context.getSharedPreferences("login", Context.MODE_PRIVATE);
                String baca = sp.getString("id", "");
                params.put("id_ortu",baca);
                params.put("tanggal",tanggal);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://balita.alfalaqproject.xyz/Controller_Balita/tambah_catat/", params);
                return res;
            }
        }

        addEmployeeCatat ae = new addEmployeeCatat();
        ae.execute();

    }
    private void hapusbalita(){



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
                params.put("id_balita",ID.get(posisi));
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://balita.alfalaqproject.xyz/Controller_Balita/hapus/", params);
                return res;
            }
        }

        hapusbalita ae = new hapusbalita();
        ae.execute();

    }

}
