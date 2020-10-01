package com.example.suraksha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    CheckedTextView checkedTextView;
    View vieww=null;
    boolean[] bool=new boolean[] {false,false};


    public void nextActivity(View view){
        if(bool[0]&&bool[1]){
            startActivity(new Intent(MainActivity2.this,LoginInfo.class));
        }else{
            Toast.makeText(MainActivity2.this,"Please grant us the access to the requested " +
                    "permissions for your safety",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Log.i("Successful","HERE!");
            checkedTextView.setChecked(true);
            if(requestCode==1) bool[0]=true;
            else bool[1]=true;
            vieww.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        final ListView lis=findViewById(R.id.listView1);

        ArrayList<String> arr=new ArrayList<>();

        arr.add("GPS permissions");
        arr.add("SMS permissions");


        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_checked,arr);


        lis.setAdapter(adapter);



        lis.post(new Runnable() {
            @Override
            public void run() {
                View view;
                if (ActivityCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    view=lis.getChildAt(0);
                    checkedTextView = (CheckedTextView) view;
                    checkedTextView.setChecked(true);
                    bool[0]=true;
                    view.setEnabled(false);
                } else if (ActivityCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    view=lis.getChildAt(1);
                    checkedTextView = (CheckedTextView) view;
                    checkedTextView.setChecked(true);
                    bool[1]=true;
                    view.setEnabled(false);
                }
            }
        });


        lis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkedTextView = (CheckedTextView) view;
                vieww=view;
                if (!checkedTextView.isChecked() && i == 0 && !bool[0]) {
                    ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else if (!checkedTextView.isChecked() && i == 1 && !bool[1]) {
                    ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.SEND_SMS}, 2);
                }
            }
        });


    }
}