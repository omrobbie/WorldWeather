package com.omrobbie.worldweather;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        /* deklarasi komponen untuk header chat */
        ImageView imgUserAvatar = (ImageView) findViewById(R.id.imgUserAvatar);
        TextView txtUserName = (TextView) findViewById(R.id.txtUserName);
        TextView txtCountry = (TextView) findViewById(R.id.txtCountry);
        TextView txtCity = (TextView) findViewById(R.id.txtCity);

        /* masukkan data intent ke variable */
        String int_alpha2Code = getIntent().getStringExtra("alpha2Code");
        String int_country = getIntent().getStringExtra("name");
        String int_cityName = getIntent().getStringExtra("cityName");

        /* panggil data user dari shared preferences */
        final SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("userprefs", MODE_PRIVATE);

        /* masukkan data ke komponen */
        Glide.with(Chat.this).load(sharedPreferences.getString("image", "")).into(imgUserAvatar);
        txtUserName.setText(sharedPreferences.getString("username", ""));
        txtCountry.setText(int_country);
        txtCity.setText(int_cityName);
    }
}