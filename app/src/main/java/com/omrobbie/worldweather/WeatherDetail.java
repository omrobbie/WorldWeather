package com.omrobbie.worldweather;

import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class WeatherDetail extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ArrayList<HashMap<String, String>> data;
    private String sAlpha2Code;
    private String sCapital;
    private String sCityName;

    /* deklarasi progress bar */
    ProgressBar progressBar;

    /* deklarasi search view */
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_detail);

        /* deklarasi progress bar */
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        /* deklarasi search view */
        searchView = (SearchView) findViewById(R.id.searchView);

        /* deklarasi tombol chat */
        Button btnChat = (Button) findViewById(R.id.btnChat);

        /* setup Glide agar bisa membaca format SVG */
        RequestBuilder<PictureDrawable> requestBuilder = GlideApp.with(WeatherDetail.this)
                .as(PictureDrawable.class)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .transition(withCrossFade())
                .listener(new SvgSoftwareLayerSetter());

        /* masukkan data intent ke variable */
        final String int_flag = getIntent().getStringExtra("flag");
        final String int_name = getIntent().getStringExtra("name");
        String int_nativeName = getIntent().getStringExtra("nativeName");
        sAlpha2Code = getIntent().getStringExtra("alpha2Code");
        sCapital = getIntent().getStringExtra("capital");

        /* deklarasikan semua komponen yang dipakai di dalam layout */
        ImageView imgFlag = (ImageView) findViewById(R.id.imgFlag);
        TextView txtCountry = (TextView) findViewById(R.id.txtCountry);
        TextView txtCapital = (TextView) findViewById(R.id.txtCapital);
        TextView txtLanguage = (TextView) findViewById(R.id.txtLanguage);

        /* masukkan data variable ke komponen */
        requestBuilder.load(Uri.parse(int_flag)).into(imgFlag);
        txtCountry.setText(int_name + " (" + sAlpha2Code + ")");
        txtCapital.setText(sCapital);
        txtLanguage.setText(int_nativeName);

        /* panggil fungsi untuk mengambil data API */
        getJSONData();

        /* setup search view */
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(WeatherDetail.this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search by city name...");

        /* intent ke layout chat saat tombol btnChat di klik */
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sCityName == null) {
                    Toast.makeText(WeatherDetail.this, "You have to insert city name!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(WeatherDetail.this, Chat.class);
                    intent.putExtra("name", int_name);
                    intent.putExtra("alpha2Code", sAlpha2Code);
                    intent.putExtra("cityName", sCityName);
                    startActivity(intent);
                }
            }
        });
    }

    /* buat fungsi overloading untuk optional parameter */
    private void getJSONData() {
        getJSONData("");
    }

    /* buat fungsi untuk mendapatkan data JSON dari link API */
    private void getJSONData(String cityName) {

        /* check if city name value */
        if(cityName.equals("")) cityName = sCapital;

        /* tampilkan progress bar */
        progressBar.setVisibility(View.VISIBLE);

        /* kosongkan isi data array */
        data = new ArrayList<>();

        /* deklarasi penggunaan volley untuk REST Weather API */
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherDetail.this);
        String urlAPI = "http://api.openweathermap.org/data/2.5/forecast?q=" + cityName + ","+ sAlpha2Code +"&appid=c2818357c736d789a6086696fc8d9b30";

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

                            /* ambil data nama kota */
                            JSONObject dataCity = (JSONObject) jsonObject.get("city");

                            /* tulis nama kota di search view */
                            sCityName = dataCity.getString("name");
                            searchView.setQuery(sCityName, false);

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
                        } finally {
                            /* setup adapter */
                            WeatherDetailAdapter adapter = new WeatherDetailAdapter(WeatherDetail.this, data);

                            /* masukkan data ke adapter */
                            RecyclerView weatherList = (RecyclerView) findViewById(R.id.WeatherList);
                            weatherList.setLayoutManager(new LinearLayoutManager(WeatherDetail.this));
                            weatherList.setAdapter(adapter);

                             /* sembunyikan progress bar */
                            progressBar.setVisibility(View.GONE);
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

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        getJSONData(query);
        return true;
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}