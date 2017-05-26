package com.omrobbie.worldweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.y;

public class Weather extends AppCompatActivity implements WeatherAdapter.ItemClickListener {

    private WeatherAdapter adapter;
    private ArrayList<HashMap<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_list);

        /* deklarasi data array */
        data = new ArrayList<>();

        /* deklarasi data hashmap */
        HashMap<String, String> tmp = new HashMap<>();

        /* setup dummy data */
        /* API Source: https://restcountries.eu/rest/v2/all*/
        tmp.put("alpha2Code", "ID");
        tmp.put("flag", "https://restcountries.eu/data/idn.svg");
        tmp.put("name", "Indonesia");
        tmp.put("capital", "Jakarta");
        tmp.put("nativeName", "Bahasa Indonesia");

        /* API Source: http://openweathermap.org/api */
        tmp.put("icon", "http://openweathermap.org/img/w/" + "09d" + ".png");
        tmp.put("description", "light intensity drizzle");
        tmp.put("temp", "280.32");
        tmp.put("speed", "4.1");

        /* masukkan ke data array */
        data.add(tmp);

        /* API Source: https://restcountries.eu/rest/v2/all*/
        tmp = new HashMap<>();
        tmp.put("alpha2Code", "US");
        tmp.put("flag", "https://restcountries.eu/data/usa.svg");
        tmp.put("name", "United States of America");
        tmp.put("capital", "Washington, D.C.");
        tmp.put("nativeName", "English");

        /* API Source: http://openweathermap.org/api */
        tmp.put("icon", "http://openweathermap.org/img/w/" + "04n" + ".png");
        tmp.put("description", "cloudy");
        tmp.put("temp", "230.71");
        tmp.put("speed", "3.8");

        /* masukkan ke data array */
        data.add(tmp);

        /* setup adapter */
        adapter = new WeatherAdapter(this, data);

        /* setup item click listener */
        adapter.setItemClickListener(this);

        /* masukkan data ke adapter */
        RecyclerView weatherList = (RecyclerView) findViewById(R.id.WeatherList);
        weatherList.setLayoutManager(new LinearLayoutManager(this));
        weatherList.setAdapter(adapter);
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
}