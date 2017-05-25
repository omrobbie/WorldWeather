package com.omrobbie.worldweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        /* deklarasikan component yang ada di layout activity_auth */
        ImageView imgUserAvatar = (ImageView) findViewById(R.id.imgUserAvatar);
        TextView txtUserName = (TextView) findViewById(R.id.txtUserName);
        TextView txtUserPassword = (TextView) findViewById(R.id.txtUserPassword);
        Button btnUserLogin = (Button) findViewById(R.id.btnUserLogin);
        Button btnUserSwitch = (Button) findViewById(R.id.btnUserSwitch);

        btnUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthActivity.this, Weather.class);
                startActivity(intent);
            }
        });
    }
}