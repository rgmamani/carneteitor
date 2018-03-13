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

    String GENERIC_ERROR = "Ha ocurrido un error, intentelo nuevamente";
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
                        String url = "http://amet.glubatec.com/api/Afiliado/get-by-id?documento=" + dni;
                        new RetrieveFeedTask().execute(url);
                    }
                    else{
                        goToError("DNI inválido","El DNI ingresado no es un numero válido.");
                    }
                }
                catch(Exception ex){
                    goToError("Lo sentimos",GENERIC_ERROR);
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

    private void displayUserInfo(JSONObject userInfo) throws Exception
    {
        Intent userInfoIntent = new Intent(this, UserInfoActivity.class);

        userInfoIntent.putExtra(Constants.keyDoc, userInfo.getString(Constants.keyDoc));
        userInfoIntent.putExtra(Constants.keyNombre, userInfo.getString(Constants.keyNombre));
        userInfoIntent.putExtra(Constants.keyApellido, userInfo.getString(Constants.keyApellido));
        userInfoIntent.putExtra(Constants.keyLocalidad, userInfo.getString(Constants.keyLocalidad));
        userInfoIntent.putExtra(Constants.keyProv, userInfo.getString(Constants.keyProv));
        userInfoIntent.putExtra(Constants.keyImage, userInfo.getString(Constants.keyImage));

        startActivity(userInfoIntent);
    }

    private void goToError(String title, String message) {
        Intent intent = new Intent(LoginActivity.this, ErrorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        bundle.putString("Message", message);
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

                    if(responses.code() == 200)
                    {
                        String jsonData = responses.body().string();
                        jObject = new JSONObject(jsonData);
                    }
                    else if(responses.code() == 404)
                    {
                        jObject = new JSONObject();
                        jObject.put("error", "El documento ingresado no pertenece a un afiliado activo");
                    }
                    else
                    {
                        jObject = new JSONObject();
                        jObject.put("error", GENERIC_ERROR);
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
            try
            {
                displayUserInfo(patientInfo);
            }
            catch (Exception e)
            {
                try
                {
                    goToError("Lo sentimos", patientInfo.getString("error") );
                }
                catch (JSONException e1)
                {
                    goToError("Lo sentimos", GENERIC_ERROR);
                }
            }
        }
    }
}