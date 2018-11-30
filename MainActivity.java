package com.example.ndivhuwo.hssapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.collect.Range;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;

//import com.kosalgeek.android.md5simply.MD5;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_STATUS = "status";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_DEVICE = "device";
    private static final String KEY_EMPTY = "";

    private static HashMap<String, String> codes = new HashMap<>();
    private static  String POST_PARAMS ;

    private ProgressDialog pDialog;
    private static String device_url = "http://192.168.0.131/security.php";
    EditText txtDeviceNumber;
    Button buttonSubmit;
    String devicenumber;
    private AwesomeValidation awesomeValidation;
    HttpConnection hhtp = new HttpConnection();

    SessionHandler session ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        session = session.getInstance(getApplicationContext());

        if(session.isLoggedIn()){
         loadDashboard();
        }
        txtDeviceNumber = (EditText) findViewById(R.id.deviceNumber);
        buttonSubmit = (Button) findViewById(R.id.bSubmit);

        awesomeValidation.addValidation(this, R.id.deviceNumber, Range.closed(1, 250), R.string.ageerror);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                devicenumber = txtDeviceNumber.getText().toString().toLowerCase().trim();

               // codes.put(KEY_DEVICE, devicenumber);
                submitForm();

            }
        });
    }

    private void submitForm() {
        //first validate the form then move ahead
        if (awesomeValidation.validate()) {
            //Toast.makeText(this, "Validation Successfull", Toast.LENGTH_LONG).show();
            displayLoader();
            sendPOST();

        }
    }



    private void loadDashboard() {
        Intent i = new Intent(this, login.class);
        startActivity(i);
        finish();

    }

    private void loadDashboar() {
        Intent i = new Intent(this, operation.class);
        startActivity(i);
        finish();

    }
    private void displayLoader() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }

    public void sendPOST() {

     //   final String email = etEmail.getText().toString();
      //  password =  MD5.encrypt(etPassword.getText().toString());

        HashMap<String, String> loginData = new HashMap<>();
        loginData.put(KEY_DEVICE, devicenumber);
      //  loginData.put("password", password);

        PostResponseAsyncTask devicelogin = new PostResponseAsyncTask(this,
                loginData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {

                if(s.contains("LoginSuccess")){
                    loadDashboard();
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            s, Toast.LENGTH_LONG).show();
                }
            }
        });
        devicelogin.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if(e != null && e.getMessage() != null){
                    Toast.makeText(getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
        devicelogin.execute(device_url);
    }

}




