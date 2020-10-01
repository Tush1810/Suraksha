package com.example.suraksha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final boolean bool1= ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)&&(ActivityCompat.checkSelfPermission
                (this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED));
        SharedPreferences myPref=this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        final boolean bool2=myPref.getBoolean("infoSaved",false);

        Handler handler=new Handler();

        Runnable run=new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(!bool1) {
                    intent = new Intent(MainActivity.this, MainActivity2.class);
                }else if(!bool2){
                    intent = new Intent(MainActivity.this,LoginInfo.class);
                }else{
                    intent = new Intent(MainActivity.this,sosPage.class);
                }
                startActivity(intent);
            }
        };

        handler.postDelayed(run,2000);
    }
}