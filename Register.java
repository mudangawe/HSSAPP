package com.example.ndivhuwo.hssapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.util.HashMap;


 /* Created by ndivhuwo on 16/10/2018.
 */

public class Register extends AppCompatActivity{


    EditText txtUsername, txtName,txtSurname,txtRePassword,txtNumber,txtPassword, txtEmail;
    Button btnRegister;
    TextView viewback;

    private static final String KEY_SURNAME= "surname";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "password";
    private static final String KEY_EMPTY = "";
    private ProgressDialog pDialog;
    private String rgr_url = "http://192.168.0.131//register.php";

    private SessionHandler session;
    private String username;
    private String password;
    private String confirmPassword;
    private String name;
    private String surname;
    private String contact;
    private String email;

    private AwesomeValidation awesomeValidation;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerlayout);
        viewback = (TextView)findViewById(R.id.back);
        viewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDashboard();
            }
        });
        txtSurname = (EditText)findViewById(R.id.surname);
        txtName = (EditText)findViewById(R.id.rname);
        txtUsername = (EditText)findViewById(R.id.rUserName);
        txtNumber = (EditText)findViewById(R.id.mobile);
        txtPassword = (EditText)findViewById(R.id.r_password);
        txtEmail = (EditText)findViewById(R.id.email);
        txtRePassword = (EditText)findViewById(R.id.r_re_password);
        btnRegister = (Button)findViewById(R.id.register);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //adding validation to edittexts
       awesomeValidation.addValidation(this, R.id.rname, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.rUserName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.surmerror);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.nameerror);
       // awesomeValidation.addValidation(this, R.id.editTextDob, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.mobile, Patterns.PHONE, R.string.mobileerror);
        // to validate the confirmation of another field
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(this, R.id.r_password, regexPassword, R.string.err_password);




        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Retrieve the data entered in the edit texts
                username = txtUsername.getText().toString().toLowerCase().trim();
                password = txtPassword.getText().toString().trim();
                confirmPassword = txtRePassword.getText().toString().trim();
                name = txtName.getText().toString().trim();
                surname = txtSurname.getText().toString().trim();
                contact = txtNumber.getText().toString().trim();

                submitForm();

            }
        });

    }

    private void submitForm() {
        //first validate the form then move ahead
        if (awesomeValidation.validate()) {

            if(confirmPassword.equals(password))
            {

                registerUser();
            }
            else
            {
                Toast.makeText(this,password + " " + confirmPassword  , Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Display Progress bar while registering
     */
    private void displayLoader() {
        pDialog = new ProgressDialog(Register.this);
        pDialog.setMessage("Signing Up.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }
    /**
     * Launch Dashboard Activity on Successful Sign Up
     */
    private void loadDashboard() {
        Intent i = new Intent(this, operation.class);
        startActivity(i);
        finish();

    }






    private void registerUser() {

       try {
           HashMap<String, String> dataPost = new HashMap<>();
           dataPost.put(KEY_SURNAME, surname);
           dataPost.put(KEY_PASSWORD, password);
           dataPost.put(KEY_EMAIL, email);
           dataPost.put(KEY_NAME, name);
           dataPost.put(KEY_CONTACT, contact);
           dataPost.put(KEY_USERNAME,username);

           PostResponseAsyncTask taskRegister = new PostResponseAsyncTask(this,
                   dataPost, new AsyncResponse() {
               @Override
               public void processFinish(String z) {

                   if (z.contains("LoginSuccess")) {
                       Toast.makeText(getApplicationContext(), z, Toast.LENGTH_LONG).show();
                     //  loadDashboard();
                   } else {
                       Toast.makeText(getApplicationContext(), z, Toast.LENGTH_LONG).show();
                   }
               }
           });
           taskRegister.setExceptionHandler(new ExceptionHandler() {
               @Override
               public void handleException(Exception e) {
                   if (e != null && e.getMessage() != null) {
                       Toast.makeText(getApplicationContext(),
                               e.getMessage(), Toast.LENGTH_SHORT).show();
                   }
               }
           });
           taskRegister.execute(rgr_url);
       }
       catch (Exception e)
       {
           Toast.makeText(getApplicationContext(),
                   e.getMessage(), Toast.LENGTH_SHORT).show();
       }

    }
}


