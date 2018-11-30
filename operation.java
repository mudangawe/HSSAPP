package com.example.ndivhuwo.hssapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

/**    
 * Created by ndivhuwo on 16/10/2018.
 */

public class operation extends AppCompatActivity {

      String digit ;
      Button btnRefresh, btnAlarm,btnGate,btnDoor;
      TextView viewRegister, viewLogOut;
      private static String operation = "http://192.168.0.131/command.php";
      ImageView imageView;
      int d = 0,g =0,a = 0;
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operation);
        imageView = (ImageView)findViewById(R.id.imageview);
        btnAlarm = (Button)findViewById(R.id.alarm);
        btnDoor =(Button)findViewById(R.id.door);
        btnRefresh  =(Button)findViewById(R.id.refress);
        btnGate = (Button)findViewById(R.id.gate);
        viewRegister = (TextView)findViewById(R.id.register);
        viewLogOut = (TextView)findViewById(R.id.logout);

        viewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
          viewLogOut.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  logout();
              }
          });


        imageView();
        /*String v = readtxt();
         //if(v == "a")
          {
              nofication("They is something wrong going!","Alarm ON");
          }
          else
           if (v=="g")
          {
              nofication("They is someone in the gate","A person in gate");
          }
            */
          btnAlarm.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //Retrieve the data entered in the edit texts
                  digit = "a";
                  if(a==0) {

                  a =1;
                  btnAlarm.setText("Turn Off Alarm");
                  }
                  else
                  {
                      a =0;
                      btnAlarm.setText("Turn On Alarm");
                  }

                  submitForm();

              }
          });

          btnRefresh.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //Retrieve the data entered in the edit texts
                 // Picasso.with(getApplicationContext()).load("").resize(250, 180).into(imageView);

                  imageView();
              }
          });
          btnDoor.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  //Retrieve the data entered in the edit texts
                  digit = "d";
                  if(d==0) {

                      d =1;
                      btnDoor.setText("Close Door");
                  }
                  else
                  {
                      d =0;
                      btnDoor.setText("Open Door");
                  }

                  submitForm();


              }
          });
          btnGate.setOnClickListener(new View.OnClickListener() {
              @Override

              public void onClick(View v) {
                  //Retrieve the data entered in the edit texts
                  digit = "g";
                  if(g==0) {

                      g =1;
                      btnGate.setText("Close Gate");
                  }
                  else
                  {
                      g =0;
                      btnGate.setText("Open Gate");
                  }

                  submitForm();
                  //nofication();


              }
          });


    }

    private void submitForm() {
        HashMap<String, String> loginData = new HashMap<>();
        loginData.put("command", digit );
        //  loginData.put("password", password);
       try {
           PostResponseAsyncTask loginTask = new PostResponseAsyncTask(this,
                   loginData, new AsyncResponse() {
               @Override
               public void processFinish(String s) {

                   if (s.contains("LoginSuccess")) {

                       Toast.makeText(getApplicationContext(),
                               s, Toast.LENGTH_LONG).show();
                   } else {
                       Toast.makeText(getApplicationContext(),
                               s, Toast.LENGTH_LONG).show();
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
           loginTask.execute(operation);
       }
       catch (Exception e)
       {
           Toast.makeText(getApplicationContext(),
                   e.getMessage(), Toast.LENGTH_SHORT).show();

       }
    }

    private void imageView()
    {

        //Picasso picasso = Picasso.with(context);

      // Picasso.get().load("http://192.168.0.131/image/image.jpg").into(imageView);

        //String url ="http://192.168.0.131/image/image.jpg";


        try
        {
            Picasso.with(this)
                    .load("http://192.168.0.131/image/image.jpg")
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageView);
        }

        catch(Exception ex)
        {
            Toast.makeText(getApplicationContext(),
                    ex.getMessage(), Toast.LENGTH_LONG).show();
        }



    }
    private void register()
    {
        Intent i = new Intent(this, Register.class);
        startActivity(i);
        finish();
    }
    private void logout()
    {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
    public void nofication(String message, String headline)
    {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(headline)   //this is the title of notification
                        .setColor(101)
                        .setContentText(message);   //this is the message showed in notification

        Intent intent = new Intent(this, operation.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
    public String readtxt()
    {

      /*  try {
            URL url = new URL("http://192.168.0.131/operation.txt");
            String text = IOUtils.toString(url.openStream());

        } catch (Exception e) {
            Log.d("MyTag",e.toString());
        }
        */

        try {
            URL url = new URL("http://192.168.0.131/gate.txt");
            Scanner s = new Scanner(url.openStream());
            return  s.toString();
            // read from your scanner
        }
        catch(IOException ex) {
            // there was some connection problem, or the file did not exist on the server,
            // or your URL was not in the right format.
            // think about what to do now, and put it here.
             // for now, simply output it.
            Toast.makeText(getApplicationContext(),
                    ex.getMessage(), Toast.LENGTH_LONG).show();
        }
     return " ";

    }
    }

