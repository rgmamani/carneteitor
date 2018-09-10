package com.example.gabrielmamani.carneteitorapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    EditText txtDni;
    Button btnLogin;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtDni = findViewById(R.id.textDNI);
        btnLogin = findViewById(R.id.buttonLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dni = txtDni.getText().toString();

                try{
                    if (validateDNI(dni))
                    {
                        String url = Configuration.get_user_url + dni;
                        displayProgressDialog(getString(R.string.geting_user));
                        new RetrieveFeedTask().execute(url);
                    }
                    else{
                        goToError(getString(R.string.incorrect), getString(R.string.invalid_dni_extended), false);
                    }
                }
                catch(Exception ex){
                    goToError(getString(R.string.sorry),getString(R.string.general_error), true);
                }
            }
        });
    }

    private boolean validateDNI(String dni) {
        if (TextUtils.isEmpty(dni)){
            return false;
        }

        if(!TextUtils.isDigitsOnly(dni)){
            return false;
        }

        if(dni.length() > 8 || dni.length() < 7){
            return false;
        }

        return true;
    }

    private void displayProgressDialog(String message)
    {
        progress = new ProgressDialog(this);
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.show();
    }

    private void hideProgressDialog()
    {
        progress.dismiss();
    }

    private void displayUserInfo(JSONObject userInfo) throws Exception
    {
        Intent userInfoIntent = new Intent(this, UserInfoActivity.class);
        userInfoIntent.putExtra(Constants.keyAfiliado, userInfo.getString(Constants.keyAfiliado));
        userInfoIntent.putExtra(Constants.keyDoc, userInfo.getString(Constants.keyDoc));
        userInfoIntent.putExtra(Constants.keyNombre, userInfo.getString(Constants.keyNombre));
        userInfoIntent.putExtra(Constants.keyApellido, userInfo.getString(Constants.keyApellido));
        userInfoIntent.putExtra(Constants.keyLocalidad, userInfo.getString(Constants.keyLocalidad));
        userInfoIntent.putExtra(Constants.keyProv, userInfo.getString(Constants.keyProv));
        userInfoIntent.putExtra(Constants.keyImage, userInfo.getString(Constants.keyImage));

        startActivity(userInfoIntent);
    }

    private void goToError(String title, String message, boolean isWarning) {
        Intent intent = new Intent(LoginActivity.this, ErrorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.bundle_error_title, title);
        bundle.putString(Constants.bundle_error_message, message);
        bundle.putBoolean(Constants.bundle_error_is_warning, isWarning);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, JSONObject> {
        private Exception exception;

        protected JSONObject doInBackground(String... urls) {

            JSONObject jObject = null;

            try {
                Request request = new Request.Builder().url(urls[0]).build();
                Response responses = client.newCall(request).execute();

                try {

                    if(responses.code() == Constants.RESPONSE_OK)
                    {
                        String jsonData = responses.body().string();
                        jObject = new JSONObject(jsonData);
                    }
                    else if(responses.code() == Constants.RESPONSE_PATIENT_NOT_FOUND)
                    {
                        jObject = new JSONObject();
                        jObject.put(Constants.errorCode, getString(R.string.patient_doesnt_exist));
                    }
                    else
                    {
                        jObject = new JSONObject();
                        jObject.put(Constants.errorCode, getString(R.string.general_error));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jObject;
        }

        protected void onPostExecute(JSONObject patientInfo)
        {
            hideProgressDialog();

            try
            {
                displayUserInfo(patientInfo);
            }
            catch (Exception e)
            {
                try
                {
                    goToError(getString(R.string.sorry), patientInfo.getString(Constants.errorCode),true);
                }
                catch (Exception e1)
                {
                    goToError(getString(R.string.sorry), getString(R.string.general_error), false);
                }
            }
        }
    }
}