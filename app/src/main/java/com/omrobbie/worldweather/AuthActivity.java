package com.omrobbie.worldweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AuthActivity extends AppCompatActivity {

    ImageView imgUserAvatar;
    TextView txtUserName;
    TextView txtUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        /* deklarasikan component yang ada di layout activity_auth */
        imgUserAvatar = (ImageView) findViewById(R.id.imgUserAvatar);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserPassword = (TextView) findViewById(R.id.txtUserPassword);
        Button btnUserLogin = (Button) findViewById(R.id.btnUserLogin);
        Button btnUserSwitch = (Button) findViewById(R.id.btnUserSwitch);

        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthActivity.this, Weather.class);
                startActivity(intent);
            }
        });

        btnUserSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomUser();
            }
        });

        /* jalankan fungsi random user untuk pertama kali */
        getRandomUser();
    }

    private void getRandomUser() {

        /* deklarasi penggunaan volley */
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlAPI = "https://randomuser.me/api/";

        /* meminta respon berupa string dari urlAPI */

        /* masukkan data random user ke komponen */
        txtUserName.setText("omrobbie");
        txtUserPassword.setText("my password");
    }
}