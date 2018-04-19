package com.example.hamada.project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
;import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    String url;
    EditText e1;
    EditText e2;
    Button b1;
    Button b2;
    TextView t1;
    String a,b;
    TextInputLayout textInputUsername;
    TextInputLayout textInputPassword;
    TextInputLayout textInputEmail;
    ProgressDialog prog;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //relation entre java et activity XML
        textInputUsername= findViewById(R.id.username);
        textInputPassword= findViewById(R.id.password);
        b1=(Button) findViewById(R.id.post);
        b2=(Button) findViewById(R.id.forgetpassword);
        requestQueue = Volley.newRequestQueue(this);
        //Action Clic bouton Connecter
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validationUsername() | !validationPassword()){
                    return;
                }else {
                    url = "http://192.168.1.108:8080/PROJECT/AuthentificationAdminApp?username=" + textInputUsername.getEditText().getText() + "&password=" + textInputPassword.getEditText().getText();
                    //creation d'un progress Dialog
                    prog = new ProgressDialog(view.getContext());
                    prog.setMessage("Please Wait");
                    prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    prog.setCancelable(false);
                    prog.show();
                    //importer le contenu des EditTexts
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if ((boolean) response.get("success") == true) {
                                    UtilsClipCodes.saveSharedSetting(MainActivity.this, "ClipCodes", "false");
                                    UtilsClipCodes.SharedPrefesSAVE(getApplicationContext(), textInputUsername.getEditText().getText().toString());
                                    Intent ImLoggedIn = new Intent(getApplicationContext(), Loged.class);
                                    startActivity(ImLoggedIn);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "" + response.get("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            prog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Cant Connect Server", Toast.LENGTH_LONG).show();
                            prog.dismiss();
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }}
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder mBuilder= new AlertDialog.Builder(MainActivity.this);
                View mView =getLayoutInflater().inflate(R.layout.dialogue_forget_password,null);
                textInputEmail=mView.findViewById(R.id.email);
                Button forget=(Button)mView.findViewById(R.id.send);
                Button cancel=(Button)mView.findViewById(R.id.cancel);
                mBuilder.setView(mView);
                final AlertDialog dialog =mBuilder.create();
                dialog.show();
                forget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        url = "http://192.168.1.108:8080/PROJECT/Recoverpassword?email=" + textInputEmail.getEditText().getText();
                        //creation d'un progress Dialog
                        prog = new ProgressDialog(view.getContext());
                        prog.setMessage("Please Wait");
                        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        prog.setCancelable(false);
                        prog.show();


                        //importer le contenu des EditTexts
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if ((boolean) response.get("success") == true) {
                                        Toast.makeText(getApplicationContext(), "" + response.get("message"), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "ERREUR : " + response.get("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                prog.dismiss();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Cant Connect Server", Toast.LENGTH_LONG).show();
                                prog.dismiss();
                            }
                        });
                        requestQueue.add(jsonObjectRequest);

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    dialog.cancel();
                    }
                });

            }
        });
    }


    public boolean validationUsername(){
        String usernameInput= textInputUsername.getEditText().getText().toString().trim();
        if(usernameInput.isEmpty()){
            textInputUsername.setError("Field can't be empty");
            return false;
        }else {
            textInputUsername.setError(null);
            return true;
        }
    }

    public boolean validationPassword(){
        String passwordInput= textInputPassword.getEditText().getText().toString().trim();
        if(passwordInput.isEmpty()){
            textInputPassword.setError("Field can't be empty");
            return false;
        }else {
            textInputPassword.setError(null);
            return true;
        }
    }


}

