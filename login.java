package com.example.ndivhuwo.hssapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;


/**
 * Created by ndivhuwo on 16/10/2018.
 */

public class login extends AppCompatActivity{

    EditText txtLogin, txtPassword;
    Button btnLogin;
    RequestQueue mRequest;
    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMPTY = "";
    private String username;
    private String password;

    private ProgressDialog pDialog;
    private String login_url = "http://192.168.0.131//login.php";



    SessionHandler session ;

    AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

   //     SessionHandler.getInstance(getApplicationContext());


        session = session.getInstance(getApplicationContext());
        Cache cache = new DiskBasedCache(getCacheDir(),1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());

        mRequest = new RequestQueue(cache,network);


       // if(session.isLoggedIn()){
         //  loadDashboard();
        //}

        setContentView(R.layout.login);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

         txtLogin = (EditText)findViewById(R.id.username);
         txtPassword = (EditText)findViewById(R.id.password);
         btnLogin = (Button) findViewById(R.id.btnlogin);

        awesomeValidation.addValidation(this, R.id.username, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(this, R.id.password, regexPassword, R.string.err_password);


        //btnLogin.setOnClickListener(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                username = txtLogin.getText().toString().toLowerCase().trim();
                password = txtPassword.getText().toString().trim();
                submitForm();

            }
        });

    }
    private void submitForm() {
        //first validate the form then move ahead
        if (awesomeValidation.validate()) {
            //Toast.makeText(this, "Validation Successfull", Toast.LENGTH_LONG).show();
          //  Intent i = new Intent(getApplicationContext(),NewActivity.class);
            //startActivity(i);
            mRequest.start();
            requestLogin();
        }
    }





    private void requestLogin() {

            //final String email = etEmail.getText().toString();
           // password =  MD5.encrypt(etPassword.getText().toString());

            HashMap<String, String> loginData = new HashMap<>();
            loginData.put(KEY_USERNAME, username);
            loginData.put(KEY_PASSWORD, password);
        try {
            PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,
                    loginData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {

                    if (s.contains("LoginSuccess")) {
                        Toast.makeText(getApplicationContext(), "User login Successfuly", Toast.LENGTH_LONG).show();
                        // session.loginUser(username);
                         loadDashboard();
                    } else {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }
                }
            });
            loginTask.setExceptionHandler(new ExceptionHandler() {
                @Override
                public void handleException(Exception e) {
                    if (e != null && e.getMessage() != null) {
                        Toast.makeText(getApplicationContext(),
                                e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            loginTask.execute(login_url);
        }
        catch (Exception e )
        {
            Toast.makeText(getApplicationContext(),
                    e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }


    private void loadDashboard() {
        Intent i = new Intent(this, operation.class);
        startActivity(i);
        finish();

    }


    private void displayLoader() {
        pDialog = new ProgressDialog(login.this);
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }



}
