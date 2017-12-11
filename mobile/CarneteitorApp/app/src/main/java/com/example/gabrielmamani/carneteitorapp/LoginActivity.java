package com.example.gabrielmamani.carneteitorapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
                    if (validateDNI(dni)) {
                        String url = "http://192.168.1.7:8090/api/Afiliado/get-by-id?documento=" + dni;
                        new RetrieveFeedTask().execute(url);
                        //new MockRetrieveFeedTask().execute(url);
                    }
                    else{
                        // TODO: redireccionar a la vista de error
                    }
                }
                catch(Exception ex){
                    // TODO: redireccionar a la vista de error
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

        if(dni.length() < 6 || dni.length() > 8){
            return false;
        }

        return true;
    }

    private void displayUserInfo(JSONObject userInfo)
    {
        Intent userInfoIntent = new Intent(this, UserInfoActivity.class);

        try {
            userInfoIntent.putExtra(Constants.keyDoc, userInfo.getString(Constants.keyDoc));
            userInfoIntent.putExtra(Constants.keyNombre, userInfo.getString(Constants.keyNombre));
            userInfoIntent.putExtra(Constants.keyApellido, userInfo.getString(Constants.keyApellido));
            userInfoIntent.putExtra(Constants.keyLocalidad, userInfo.getString(Constants.keyLocalidad));
            userInfoIntent.putExtra(Constants.keyProv, userInfo.getString(Constants.keyProv));
            userInfoIntent.putExtra(Constants.keyImage, userInfo.getString(Constants.keyImage));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startActivity(userInfoIntent);
    }

    class MockRetrieveFeedTask extends AsyncTask<String, Void, JSONObject> {
        protected JSONObject doInBackground(String... urls) {

        try {
            JSONObject mockPatient = new JSONObject();
            mockPatient.put(Constants.keyDoc,"1234566");
            mockPatient.put(Constants.keyNombre, "pepito");
            mockPatient.put(Constants.keyApellido, "lalalas");
            mockPatient.put(Constants.keyLocalidad, "capital");
            mockPatient.put(Constants.keyProv, "cordoba");
            mockPatient.put(Constants.keyImage, "https://lh3.googleusercontent.com/-r0P3KDF-49M/AAAAAAAAAAI/AAAAAAAABwg/jT4NoQnkHvg/s120-p-k-rw-no/photo.jpg");

            return mockPatient;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

        protected void onPostExecute(JSONObject patientInfo) {
        displayUserInfo(patientInfo);
    }
}

    class RetrieveFeedTask extends AsyncTask<String, Void, JSONObject> {
        private Exception exception;

        protected JSONObject doInBackground(String... urls) {
            try {
                Request request = new Request.Builder().url(urls[0]).build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String jsonData = responses.body().string();
                JSONObject Jobject = new JSONObject(jsonData);

                return Jobject;
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject patientInfo) {
            displayUserInfo(patientInfo);
        }
    }
}