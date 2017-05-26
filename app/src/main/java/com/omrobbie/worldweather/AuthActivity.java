package com.omrobbie.worldweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthActivity extends AppCompatActivity {

    ImageView imgUserAvatar;
    TextView txtUserName, txtUserPassword;
    String txtEmail, txtAddress, txtImage;

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
                /* deklarasi SharedPreferences */
                final SharedPreferences.Editor sharedPreferences = getBaseContext().getSharedPreferences("userprefs", MODE_PRIVATE).edit();

                /* simpan data preferensi */
                sharedPreferences.putString("username", txtUserName.getText().toString());
                sharedPreferences.putString("email", txtEmail);
                sharedPreferences.putString("address", txtAddress);
                sharedPreferences.putString("image", txtImage);
                sharedPreferences.commit();

                /* lanjutkan ke layout utama */
                gotoMainLayout();
            }
        });

        btnUserSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomUser();
            }
        });

        /* saat aplikasi di load, cek apakah sudah pernah login */
        /* jika sudah pernah login, maka lanjutkan ke layout utama */
        SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("userprefs", MODE_PRIVATE);
        String login_status = sharedPreferences.getString("username", null);
        if(login_status != null) gotoMainLayout(); else getRandomUser();
    }

    /* lanjutkan ke layout utama */
    private void gotoMainLayout() {
        Intent intent = new Intent(AuthActivity.this, Weather.class);
        startActivity(intent);
        finish();
    }

    /* buat fungsi untuk mendapatkan data user baru */
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
                    try {
                        /* deklarasi response dari request API */
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = (JSONArray) jsonObject.get("results");

                        /* ambil keseluruhan data pertama dari response */
                        JSONObject item = (JSONObject) jsonArray.get(0);

                        /* ambil data nama user dan password, serta gambar avatar user */
                        JSONObject name = (JSONObject) item.get("name");
                        JSONObject login = (JSONObject) item.get("login");
                        JSONObject picture = (JSONObject) item.get("picture");
                        JSONObject location = (JSONObject) item.get("location");

                        /* tampilkan data gender dengan toast */
                        // Toast.makeText(AuthActivity.this, name.get("gender").toString(), Toast.LENGTH_SHORT).show();

                        /* masukkan data random user ke komponen */
                        txtUserName.setText(name.getString("first") + " " + name.getString("last"));
                        txtUserPassword.setText(login.getString("password"));
                        Glide.with(AuthActivity.this).load(picture.getString("medium")).into(imgUserAvatar);

                        /* bikin gambar pada ImageView menjadi bulat */
                        Bitmap bitmap = BitmapFactory.decodeResource(AuthActivity.this.getResources(), R.drawable.weather_icon);
                        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
                        imgUserAvatar.setImageResource(circularBitmap.getGenerationId());

                        txtEmail = item.getString("email");
                        txtAddress = location.getString("street") + ", " + location.getString("city");
                        txtImage = picture.getString("medium");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

        /* masukkan data request kedalam request queue */
        requestQueue.add(stringRequest);
    }
}