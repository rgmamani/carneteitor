package com.example.gabrielmamani.carneteitorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class UserInfoActivity extends AppCompatActivity {
    TextView txtDni;
    TextView txtName;
    TextView txtLastname;
    TextView txtState;
    TextView txtProvince;
    ImageView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        txtDni = findViewById(R.id.txtDNI);
        txtName = findViewById(R.id.txtName);
        txtLastname = findViewById(R.id.txtLastname);
        txtState = findViewById(R.id.txtState);
        txtProvince = findViewById(R.id.txtProvince);
        photoView = findViewById(R.id.photo);

        Intent userData = getIntent();

        if(userData != null)
        {
            txtDni.setText(userData.getStringExtra(Constants.keyDoc));
            txtName.setText(userData.getStringExtra(Constants.keyNombre));
            txtLastname.setText(userData.getStringExtra(Constants.keyApellido));
            txtState.setText(userData.getStringExtra(Constants.keyLocalidad));
            txtProvince.setText(userData.getStringExtra(Constants.keyProv));

            Picasso.with(this).load(userData.getStringExtra(Constants.keyImage)).into(photoView);
        }
    }
}
