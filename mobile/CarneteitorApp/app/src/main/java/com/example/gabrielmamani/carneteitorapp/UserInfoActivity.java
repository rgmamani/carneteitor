package com.example.gabrielmamani.carneteitorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class UserInfoActivity extends AppCompatActivity {
    TextView txtDni;
    TextView txtName;
    TextView txtLastname;
    TextView txtState;
    TextView textViewProvince;
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
        textViewProvince = findViewById(R.id.textViewProvince);
        txtProvince = findViewById(R.id.txtProvince);
        photoView = findViewById(R.id.photo);

        Intent userData = getIntent();

        if(userData != null)
        {
            txtDni.setText(userData.getStringExtra(Constants.keyDoc));
            txtName.setText(userData.getStringExtra(Constants.keyNombre));
            txtLastname.setText(userData.getStringExtra(Constants.keyApellido));

            String localidad = userData.getStringExtra(Constants.keyLocalidad);

            if (localidad.equals("null") || TextUtils.isEmpty(localidad))
            {
                txtState.setText(" - ");
            }
            else
            {
                txtState.setText(localidad);
            }

            String provincia = userData.getStringExtra(Constants.keyProv);

            if (provincia.equals("null") || provincia.isEmpty())
            {
                txtProvince.setText(" - ");
            }
            else
            {
                txtProvince.setText(provincia);
            }

            String photo = userData.getStringExtra(Constants.keyImage);

            if(photo != null && !photo.isEmpty())
            {
                Picasso.with(this).load(photo).into(photoView);
            }
        }
    }
}
