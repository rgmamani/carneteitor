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

    private View backView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Bundle bundle = getIntent().getExtras();
        Title = bundle.getString(Constants.bundle_error_title);
        Message = bundle.getString(Constants.bundle_error_message);

        //TODO: cambiar esta comparacion
        if (Message.equalsIgnoreCase("El documento ingresado no pertenece a un afiliado activo."))
        {
            Title = "NO AFILIADO";
        }

        TextView txtTitulo = findViewById(R.id.txtTitulo);
        TextView txtMensaje = findViewById(R.id.txtMensaje);

        txtTitulo.setText(Title);
        txtMensaje.setText(Message);

        if(bundle.getBoolean(Constants.bundle_error_is_warning))
        {
            backView = findViewById(R.id.background);
            backView.setBackgroundResource(R.drawable.back_gray);
        }
    }
}