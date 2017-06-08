package com.omrobbie.worldweather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static com.omrobbie.worldweather.R.id.ChatList;

public class Chat extends AppCompatActivity {

    private String sUserName;
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        /* deklarasi komponen untuk header chat */
        ImageView imgUserAvatar = (ImageView) findViewById(R.id.imgUserAvatar);
        final TextView txtUserName = (TextView) findViewById(R.id.txtUserName);
        TextView txtCountry = (TextView) findViewById(R.id.txtCountry);
        TextView txtCity = (TextView) findViewById(R.id.txtCity);

        /* deklarasi floating action button */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        /* tampilkan progress bar */
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        /* masukkan data intent ke variable */
        final String int_alpha2Code = getIntent().getStringExtra("alpha2Code");
        final String int_country = getIntent().getStringExtra("name");
        final String int_cityName = getIntent().getStringExtra("cityName");

        /* panggil data user dari shared preferences */
        final SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences("userprefs", MODE_PRIVATE);
        sUserName = sharedPreferences.getString("username", "");

        /* masukkan data ke komponen */
        Glide.with(Chat.this).load(sharedPreferences.getString("image", "")).into(imgUserAvatar);
        txtUserName.setText(sUserName);
        txtCountry.setText(int_country);
        txtCity.setText(int_cityName);

        /* sign ke firebase */
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(Chat.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Toast.makeText(Chat.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* kirim chat saat tombol send di klik */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtChat = (EditText) findViewById(R.id.txtChat);

                /* tulis data chat ke firebase */
                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .child(int_alpha2Code + "," + int_cityName)
                        .push()
                        .setValue(new ChatMessage(sUserName, txtChat.getText().toString()));

                txtChat.setText("");
            }
        });

        /* baca data dari firebase dan tampilkan ke listview */
        final ListView ChatList = (ListView) findViewById(R.id.ChatList);

        adapter = new FirebaseListAdapter<ChatMessage>(Chat.this, ChatMessage.class, R.layout.chat_items,
                FirebaseDatabase.getInstance().getReference(int_alpha2Code + "," + int_cityName)) {
            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();

                /* sembunyikan progress bar */
                progressBar.setVisibility(View.GONE);

                /* posisikan listview ke text */
                ChatList.setSelection(adapter.getCount() -1);
            }

            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                /* deklarasikan komponen di layout chat items */
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                /* masukkan data ke komponen */
                messageUser.setText(model.getMessageUser());
                messageText.setText(model.getMessageText());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

                /* rubah warna jika user chat = user login */
                if (sUserName.contains(model.getMessageUser())) {
                    messageUser.setTextColor(Color.LTGRAY);
                    messageTime.setTextColor(Color.LTGRAY);
                }
            }
        };

        ChatList.setAdapter(adapter);
    }
}