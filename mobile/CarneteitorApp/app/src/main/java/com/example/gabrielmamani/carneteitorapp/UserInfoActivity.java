package com.example.gabrielmamani.carneteitorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class UserInfoActivity extends AppCompatActivity {

    TextView txtName;
    TextView txtDni;

    ImageView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        txtName = findViewById(R.id.txtName);
        txtDni = findViewById(R.id.txtDNI);
        photoView = findViewById(R.id.photo);

        Intent userData = getIntent();
        if(userData!=null)
        {
            txtDni.setText(userData.getStringExtra(Constants.keyDoc));
            txtName.setText(userData.getStringExtra(Constants.keyNombre));

            Picasso.with(this).load(userData.getStringExtra(Constants.keyImage)).into(photoView);
        }

    }
}
