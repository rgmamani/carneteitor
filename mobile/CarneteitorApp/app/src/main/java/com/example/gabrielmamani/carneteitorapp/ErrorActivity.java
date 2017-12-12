package com.example.gabrielmamani.carneteitorapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity {
    private String Title = "";
    private String Message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Bundle bundle = getIntent().getExtras();
        Title = bundle.getString("Title");
        Message = bundle.getString("Message");

        TextView txtTitulo = (TextView)findViewById(R.id.txtTitulo);
        TextView txtMensaje = (TextView)findViewById(R.id.txtMensaje);

        txtTitulo.setText(Title);
        txtMensaje.setText(Message);
    }
}