package com.omrobbie.worldweather;

import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class WeatherDetail extends AppCompatActivity {

    WeatherDetailAdapter adapter;
    private ArrayList<HashMap<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_detail);

        /* setup Glide agar bisa membaca format SVG */
        RequestBuilder<PictureDrawable> requestBuilder = GlideApp.with(this)
                .as(PictureDrawable.class)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .transition(withCrossFade())
                .listener(new SvgSoftwareLayerSetter());

        /* masukkan data intent ke variable */
        String int_alpha2Code = getIntent().getStringExtra("alpha2Code");
        String int_flag = getIntent().getStringExtra("flag");
        String int_name = getIntent().getStringExtra("name");
        String int_capital = getIntent().getStringExtra("capital");
        String int_nativeName = getIntent().getStringExtra("nativeName");

        /* deklarasikan semua komponen yang dipakai di dalam layout */
        ImageView imgFlag = (ImageView) findViewById(R.id.imgFlag);
        TextView txtCountry = (TextView) findViewById(R.id.txtCountry);
        TextView txtCapital = (TextView) findViewById(R.id.txtCapital);
        TextView txtLanguage = (TextView) findViewById(R.id.txtLanguage);

        /* masukkan data variable ke komponen */
        requestBuilder.load(Uri.parse(int_flag)).into(imgFlag);
        txtCountry.setText(int_name + " (" + int_alpha2Code + ")");
        txtCapital.setText(int_capital);
        txtLanguage.setText(int_nativeName);

        /* panggil fungsi untuk mengambil data API */
        getJSONData();

        /* setup adapter */
        adapter = new WeatherDetailAdapter(this, data);

        /* masukkan data ke adapter */
        RecyclerView weatherList = (RecyclerView) findViewById(R.id.WeatherList);
        weatherList.setLayoutManager(new LinearLayoutManager(this));
        weatherList.setAdapter(adapter);
    }

    /* buat fungsi untuk mendapatkan data JSON dari link API */
    private void getJSONData() {

        /* kosongkan isi data array */
        data = new ArrayList<>();

        /* deklarasi penggunaan volley untuk REST Weather API */
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlAPI = "http://api.openweathermap.org/data/2.5/forecast?q=" + getIntent().getStringExtra("capital") + ","+ getIntent().getStringExtra("alpha2Code") +"&appid=c2818357c736d789a6086696fc8d9b30";

        /* meminta respon berupa string dari urlAPI */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        /* deklarasi response dari request REST Weather API */
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            /* ambil keseluruhan data dari response */
                            JSONArray jsonArray = (JSONArray) jsonObject.get("list");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject itemWeather = (JSONObject) jsonArray.get(i);

                                /* ambil data dari objek main */
                                JSONObject dataMain = (JSONObject) itemWeather.get("main");
                                String hash_temp = dataMain.getString("temp");
                                String hash_humidity = dataMain.getString("humidity");

                                /* ambil data dari objek weather */
                                JSONArray dataWeather = (JSONArray) itemWeather.get("weather");
                                JSONObject dataWeatherItem = (JSONObject) dataWeather.get(0);
                                String hash_main = dataWeatherItem.getString("main");
                                String hash_description = dataWeatherItem.getString("description");
                                String hash_icon = dataWeatherItem.getString("icon");

                                /* ambil data dari objek wind */
                                JSONObject dataWind = (JSONObject) itemWeather.get("wind");
                                String hash_speed = dataWind.getString("speed");

                                /* ambil data dari root objek */
                                String hash_dt_txt = itemWeather.getString("dt_txt");

                                /* deklarasi variable penyimpanan array sementara */
                                HashMap<String, String> tmp = new HashMap<>();

                                /* simpan data weather ke array */
                                tmp.put("icon", "http://openweathermap.org/img/w/" + hash_icon + ".png");
                                tmp.put("dt_txt", hash_dt_txt);
                                tmp.put("temp", hash_temp);
                                tmp.put("humidity", hash_humidity);
                                tmp.put("speed", hash_speed);
                                tmp.put("main", hash_main);
                                tmp.put("description", hash_description);

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
                        Toast.makeText(WeatherDetail.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        /* masukkan data request kedalam request queue */
        requestQueue.add(stringRequest);
    }
}