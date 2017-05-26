package com.omrobbie.worldweather;

import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import org.w3c.dom.Text;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class WeatherIntent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_item_detail);

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
        String int_icon = getIntent().getStringExtra("icon");
        String int_description = getIntent().getStringExtra("description");
        String int_temp = getIntent().getStringExtra("temp");
        String int_speed = getIntent().getStringExtra("speed");

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
    }
}