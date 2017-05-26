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

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.id;

public class Weather extends AppCompatActivity implements WeatherAdapter.ItemClickListener {

    private WeatherAdapter adapter;
    private ArrayList<HashMap<String, String>> data;

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

        /* panggil data user dari shared preferences */
        final SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("userprefs", MODE_PRIVATE);
        txtUserName.setText(sharedPreferences.getString("username", ""));
        txtEmail.setText(sharedPreferences.getString("email", ""));
        txtAddress.setText(sharedPreferences.getString("address", ""));
        Glide.with(this).load(sharedPreferences.getString("image", "")).into(imgUserAvatar);

        /* panggil fungsi untuk mengambil data API */
        getJSONData();

        /* setup adapter */
        adapter = new WeatherAdapter(this, data);

        /* setup item click listener */
        adapter.setItemClickListener(this);

        /* masukkan data ke adapter */
        RecyclerView weatherList = (RecyclerView) findViewById(R.id.WeatherList);
        weatherList.setLayoutManager(new LinearLayoutManager(this));
        weatherList.setAdapter(adapter);

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

        /* tampilkan nama negara yang di klik dengan toast */
        // Toast.makeText(this, item.get("name"), Toast.LENGTH_SHORT).show();

        /* pindahkan tampilan ke layout item detail */
        Intent intent = new Intent(this, WeatherIntent.class);

        /* masukkan data ke intent */
        intent.putExtra("alpha2Code", item.get("alpha2Code"));
        intent.putExtra("flag", item.get("flag"));
        intent.putExtra("name", item.get("name"));
        intent.putExtra("capital", item.get("capital"));
        intent.putExtra("nativeName", item.get("nativeName"));
        intent.putExtra("icon", item.get("icon"));
        intent.putExtra("description", item.get("description"));
        intent.putExtra("temp", item.get("temp"));
        intent.putExtra("speed", item.get("speed"));

        /* jalankan intent */
        startActivity(intent);
    }

    /* buat fungsi untuk mendapatkan data JSON dari link API */
    private void getJSONData() {

        /* kosongkan isi data array */
        data = new ArrayList<>();

        /* deklarasi penggunaan volley untuk REST Countries API */
        RequestQueue requestQueueCountries = Volley.newRequestQueue(this);
        String urlAPI = "https://restcountries.eu/rest/v2/all";

        /* meminta respon berupa string dari urlAPI */
        StringRequest stringRequestCountries = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        /* deklarasi response dari request REST Countries API */
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            /* ambil keseluruhan data dari response */
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject item = (JSONObject) jsonArray.get(i);

                                /* deklarasi variable penyimpanan data item sementara */
                                /* variable data countries */
                                String hash_alpha2Code = item.getString("alpha2Code");
                                String hash_flag = item.getString("flag");
                                String hash_name = item.getString("name");
                                String hash_capital = item.getString("capital");
                                String hash_nativeName = item.getString("nativeName");

                                /* variable data weather */
                                String hash_icon;
                                String hash_description;
                                String hash_temp;
                                String hash_speed;

                                /* deklarasi penggunaan volley untuk open weather API */
                                RequestQueue requestQueueWeather = Volley.newRequestQueue(Weather.this);
                                String urlAPI = "http://api.openweathermap.org/data/2.5/weather?q=" + hash_capital + "," + hash_alpha2Code + "id&appid=c2818357c736d789a6086696fc8d9b30";

                                /* meminta respon berupa string dari urlAPI */
                                StringRequest stringRequestWeather = new StringRequest(Request.Method.GET, urlAPI,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                            }
                                        },

                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(Weather.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );

                                /* deklarasi variable penyimpanan data item weather sementara */
                                hash_icon = "http://openweathermap.org/img/w/" + "09d" + ".png";
                                hash_description = "light intensity drizzle";
                                hash_temp = "280.32";
                                hash_speed = "4.1";

                                /* deklarasi variable penyimpanan array sementara */
                                HashMap<String, String> tmp = new HashMap<>();

                                /* simpan data countries ke array */
                                tmp.put("alpha2Code", hash_alpha2Code);
                                tmp.put("flag", hash_flag);
                                tmp.put("name", hash_name);
                                tmp.put("capital", hash_capital);
                                tmp.put("nativeName", hash_nativeName);

                                /* simpan data weather ke array */
                                tmp.put("icon", hash_icon);
                                tmp.put("description", hash_description);
                                tmp.put("temp", hash_temp);
                                tmp.put("speed", hash_speed);

                                /* masukkan ke data array */
                                data.add(tmp);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
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
    }
}