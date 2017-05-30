package com.omrobbie.worldweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class Weather extends AppCompatActivity implements WeatherAdapter.ItemClickListener {

    private WeatherAdapter adapter;
    private ArrayList<HashMap<String, String>> data;

    /* deklarasikan progress bar */
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_list);

        /* deklarasikan komponen untuk user */
        LinearLayout headerView = (LinearLayout) findViewById(R.id.headerView);
        ImageView imgUserAvatar = (ImageView) findViewById(R.id.imgUserAvatar);
        TextView txtUserName = (TextView) findViewById(R.id.txtUserName);
        TextView txtEmail = (TextView) findViewById(R.id.txtEmail);
        TextView txtAddress = (TextView) findViewById(R.id.txtAddress);

        /* deklarasi progress bar */
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        /* panggil data user dari shared preferences */
        final SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("userprefs", MODE_PRIVATE);
        txtUserName.setText(sharedPreferences.getString("username", ""));
        txtEmail.setText(sharedPreferences.getString("email", ""));
        txtAddress.setText(sharedPreferences.getString("address", ""));
        Glide.with(Weather.this).load(sharedPreferences.getString("image", "")).into(imgUserAvatar);

        /* panggil fungsi untuk mengambil data API */
        getJSONData();

        /* jika header user di klik, hapus shared preferences dan kembali ke layout login */
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* kosongkan isi shared preferences */
                sharedPreferences.edit().clear().commit();

                /* kembali ke layout login */
                Intent intent = new Intent(Weather.this, AuthActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /* implementasikan onItemClick dari WeatherAdapter */
    @Override
    public void onItemClick(View view, int position) {
        HashMap<String, String> item = adapter.getData(position);

        /* pindahkan tampilan ke layout item detail */
        Intent intent = new Intent(Weather.this, WeatherDetail.class);

        /* masukkan data ke intent */
        intent.putExtra("alpha2Code", item.get("alpha2Code"));
        intent.putExtra("flag", item.get("flag"));
        intent.putExtra("name", item.get("name"));
        intent.putExtra("capital", item.get("capital"));
        intent.putExtra("nativeName", item.get("nativeName"));

        /* jalankan intent */
        startActivity(intent);
    }

    /* buat fungsi untuk mendapatkan data JSON dari link API */
    private void getJSONData() {

        /* tampilkan progress bar */
        progressBar.setVisibility(View.VISIBLE);

        /* kosongkan isi data array */
        data = new ArrayList<>();

        /* deklarasi penggunaan volley untuk REST Countries API */
        RequestQueue requestQueueCountries = Volley.newRequestQueue(Weather.this);
        String urlAPI = "https://restcountries.eu/rest/v2/all/";

        /* meminta respon berupa string dari urlAPI */
        StringRequest stringRequestCountries = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        /* deklarasi response dari request REST Countries API */
                        try {
                            final JSONArray jsonArray = new JSONArray(response);

                            /* ambil keseluruhan data dari response */
                            for (int i = 0; i < jsonArray.length(); i++) {
                                final JSONObject itemCountries = (JSONObject) jsonArray.get(i);

                                /* ambil data dari objek languages */
                                JSONArray dataLanguages = (JSONArray) itemCountries.get("languages");
                                JSONObject dataLanguagesItem = (JSONObject) dataLanguages.get(0);

                                /* deklarasi variable penyimpanan data item sementara */
                                /* variable data countries */
                                String hash_alpha2Code = itemCountries.getString("alpha2Code");
                                String hash_flag = itemCountries.getString("flag");
                                String hash_name = itemCountries.getString("name");
                                String hash_population = NumberFormat.getInstance().format(itemCountries.get("population"));
                                String hash_capital = itemCountries.getString("capital");
                                String hash_nativeName = dataLanguagesItem.getString("nativeName");

                                /* deklarasi variable penyimpanan array sementara */
                                HashMap<String, String> tmp = new HashMap<>();

                                /* simpan data countries ke array */
                                tmp.put("alpha2Code", hash_alpha2Code);
                                tmp.put("flag", hash_flag);
                                tmp.put("name", hash_name);
                                tmp.put("population", hash_population);
                                tmp.put("capital", hash_capital);
                                tmp.put("nativeName", hash_nativeName);

                                /* masukkan ke data array */
                                data.add(tmp);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            /* setup adapter */
                            adapter = new WeatherAdapter(Weather.this, data);

                            /* setup item click listener */
                            adapter.setItemClickListener(Weather.this);

                            /* masukkan data ke adapter */
                            RecyclerView weatherList = (RecyclerView) findViewById(R.id.WeatherList);
                            weatherList.setLayoutManager(new LinearLayoutManager(Weather.this));
                            weatherList.setAdapter(adapter);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Weather.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        /* masukkan data request kedalam request queue */
        requestQueueCountries.add(stringRequestCountries);

        /* sembunyikan progress bar */
        progressBar.setVisibility(View.GONE);
    }
}