package com.omrobbie.worldweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
    }
}