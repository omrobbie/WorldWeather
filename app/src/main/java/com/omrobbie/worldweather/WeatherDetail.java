package com.omrobbie.worldweather;

import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestBuilder;

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

        /* setup dummy data --------------------------------------------------------------------- */

        /* kosongkan isi data array */
        data = new ArrayList<>();

        /* deklarasi variable penyimpanan array sementara */
        HashMap<String, String> tmp = new HashMap<>();

        /* simpan data weather ke array */
        tmp.put("icon", "http://openweathermap.org/img/w/10d.png");
        tmp.put("dt_txt", "2017-05-27 06:00:00");
        tmp.put("temp", "300.15");
        tmp.put("humidity", "85");
        tmp.put("speed", "1.5");
        tmp.put("main", "Clouds");
        tmp.put("description", "haze");

        /* masukkan ke data array */
        data.add(tmp);
        tmp = new HashMap<>();

        /* simpan data weather ke array */
        tmp.put("icon", "http://openweathermap.org/img/w/01d.png");
        tmp.put("dt_txt", "2017-05-27 06:00:00");
        tmp.put("temp", "300.15");
        tmp.put("humidity", "85");
        tmp.put("speed", "1.5");
        tmp.put("main", "Clouds");
        tmp.put("description", "haze");

        /* masukkan ke data array */
        data.add(tmp);

        /* setup dummy data --------------------------------------------------------------------- */

        /* setup adapter */
        adapter = new WeatherDetailAdapter(this, data);

        /* masukkan data ke adapter */
        RecyclerView weatherList = (RecyclerView) findViewById(R.id.WeatherList);
        weatherList.setLayoutManager(new LinearLayoutManager(this));
        weatherList.setAdapter(adapter);
    }
}