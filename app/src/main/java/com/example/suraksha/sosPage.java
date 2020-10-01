package com.example.suraksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class sosPage extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    Boolean sent=false;

    public void sendSMS(String url){
        Log.i("Inside sendSMS",url);

        try {

            SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            String name = myPrefs.getString("name", null);
            Log.i("Name-",name);
            Set<String> emergencyContacts = myPrefs.getStringSet("emergencyContacts", null);
            Log.i("Emergency Contacts are", emergencyContacts.toString());

            EditText editText=findViewById(R.id.emergencyText);
            String Message=editText.getText().toString();

            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> contacts=new ArrayList<>(emergencyContacts);

//            String[] contacts = (String[]) emergencyContacts.toArray();
            for (int i = 0; i < emergencyContacts.size(); i++) {
                if(Message.length()>0)
                smsManager.sendTextMessage(contacts.get(i), null, "HELP\n" + "Location Link- " + url+"\nmessage-"+Message, null, null);
                else{
                    smsManager.sendTextMessage(contacts.get(i), null, "HELP\n" + "Location Link- " + url, null, null);
                }
                Toast.makeText(sosPage.this,"Message Sent",Toast.LENGTH_SHORT).show();
                Log.i("MESSAGE SENT TO", contacts.get(i));
            }
        }catch(Exception e){
            Log.i("ERROR",e.toString());
            e.printStackTrace();
        }
    }

    public void openSettings(View view){
        Intent intent=new Intent(sosPage.this,settings.class);
        startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_page);
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        final double[] latitude = {0};
        final double[] longitude = { 0 };

        locationListener=new LocationListener() {

            public void onLocationChanged(@NonNull Location location) {
                if(!sent) {
                    Log.i("Location", location.toString());
                    latitude[0] = location.getLatitude();
                    longitude[0] = location.getLongitude();
                    String url = "https://www.google.com/maps/search/?api=1&query=" + latitude[0] + "," + longitude[0];
                    Log.i("Location URL", url);
                    sendSMS(url);
                    sent = true;
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }
        };
        final Button sosButton=(Button)findViewById(R.id.sosButton);

        sosButton.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
            }

            @Override
            public void onDoubleClick(View v) {
                Log.i("DoubleTap","detected");
                Toast.makeText(sosPage.this,"Sending Message",Toast.LENGTH_SHORT).show();

                if(ContextCompat.checkSelfPermission(sosPage.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                }
            }
        });
    }
}