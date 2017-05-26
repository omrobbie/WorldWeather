package com.omrobbie.worldweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI,
            new Response.Listener<String>() {
                /**
                 * Called when a response is received.
                 *
                 * @param response
                 */
                @Override
                public void onResponse(String response) {
                    
                }
            },

            new Response.ErrorListener() {
                /**
                 * Callback method that an error has been occurred with the
                 * provided error code and optional user-readable message.
                 *
                 * @param error
                 */
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AuthActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        );

        /* masukkan data random user ke komponen */
        txtUserName.setText("omrobbie");
        txtUserPassword.setText("my password");
    }
}