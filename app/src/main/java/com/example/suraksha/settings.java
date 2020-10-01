package com.example.suraksha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class settings extends AppCompatActivity {


    ArrayList<String> phoneNumbers=new ArrayList<>();

    public void openDirectory(View view){
        if(ActivityCompat.checkSelfPermission(settings.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(settings.this,new String[]{Manifest.permission.READ_CONTACTS},3);
        }
        if(ActivityCompat.checkSelfPermission(settings.this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED){
            contactsActivity();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        checkAlreadyExisting();

        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        Log.i("Child Count",String.valueOf(linearLayout.getChildCount()));
        LinearLayout linearLayout1=(LinearLayout)linearLayout.getChildAt(linearLayout.getChildCount()-1);
        EditText editText=
                (EditText) linearLayout1.getChildAt(0);
        if(editText.getText().toString().equals("")){
            Toast.makeText(this,"Field is empty",Toast.LENGTH_SHORT).show();
            return;
        }
        boolean found=false;
        for(int i=0;i<phoneNumbers.size();i++){
            Log.i("Phone Number at index",i+" is "+phoneNumbers.get(i));
            Log.i("Text inside ","editText is "+editText.getText().toString());
            if(phoneNumbers.get(i).equals(editText.getText().toString())){
                found=true;
            }
        }
        if(!found){
            phoneNumbers.add(editText.getText().toString());
        }else{
            if(phoneNumbers.get(phoneNumbers.size()-1).equals(editText.getText().toString())){
                Log.i("HERE","I AM");
//                addChild();
            }else {
                Toast.makeText(this, "Contact already exists", Toast.LENGTH_SHORT).show();
            }
        }

        Log.i("Phone Numbers",phoneNumbers.toString());



        SharedPreferences myPrefs=getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        try {
            Set<String> set = new HashSet<String>(phoneNumbers);
            Log.i("set", set.toString());
            myPrefs.edit().putStringSet("emergencyContacts", set).apply();

            myPrefs.edit().putBoolean("infoSaved", true).apply();
        }catch(Exception e){
            Log.i("Error-",e.toString());
            e.printStackTrace();
        }
        try {
            Log.i("After storing", "-----------------------------------------------------------------");
            Log.i("Saving name-", myPrefs.getString("name", null));
            Log.i("set", String.valueOf(myPrefs.getStringSet("emergencyContacts", null)));
        }catch (Exception e){
            Log.i("SharedPreferences is","returning NULL value");
        }
        startActivity(new Intent(settings.this,sosPage.class));

    }

    public void contactsActivity(){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    Cursor c = getContentResolver().query(contactData, null, null, null);

                    if (c.moveToFirst()) {
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String num = "";
                        if (Integer.valueOf(hasNumber) == 1) {
                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                            ArrayList<String> arrayList=new ArrayList<>();
                            while (numbers.moveToNext()) {
                                num=numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                if(num.length()==10){
                                    if(!arrayList.contains(num)){
                                        Log.i("Adding",String.valueOf(num));
                                        arrayList.add(num);
                                    }
                                }else{
                                    String newNum1="";
                                    for(int i=0,j=num.length()-1;i<num.length();i++,j--){
                                        newNum1=newNum1+num.charAt(j);
                                    }
                                    num="";

                                    for(int i=0;i<newNum1.length();i++){
                                        if (newNum1.charAt(i)!=' ') {
                                            if(num.length()<10)
                                                num = num + newNum1.charAt(i);
                                        }

                                    }
                                    newNum1="";
                                    for(int i=0,j=num.length()-1;i<num.length();i++,j--){
                                        newNum1=newNum1+num.charAt(j);
                                    }
                                    if(!arrayList.contains(newNum1)){
                                        Log.i("Adding",String.valueOf(newNum1));
                                        arrayList.add(newNum1);
                                    }
                                }


//                                Toast.makeText(LoginInfo.this, "Number="+num, Toast.LENGTH_LONG).show();
                            }
                            Log.i("list",arrayList.toString());
                            if(arrayList.size()==0){
                                return;
                            }
                            if(arrayList.size()==1){
                                updateChilds(arrayList);
                            }

//                            AlertDialog.Builder alertdialogueBuilder=new AlertDialog.Builder(LoginInfo.this);
//                            ArrayList<String> newArrayList=new ArrayList<>();
//                            newArrayList.add("All "+arrayList.size()+" numbers");
//
//                            for(int i=0;i<arrayList.size();i++){
//                                newArrayList.add(arrayList.get(i));
//                            }
//
//                            Log.i("size",String.valueOf(newArrayList.size()));
//
//                            final String[] alertdialogueitems=new String[newArrayList.size()];
//
//                            for(int i=0;i<newArrayList.size();i++){
//                                alertdialogueitems[i]=newArrayList.get(i);
//                                Log.i("alertdialogueitems["+Integer.toString(i)+"]",alertdialogueitems[i]);
//                            }
//                            final boolean[] selectedTrueFalse=new boolean[newArrayList.size()];
//
//                            for(int i=0;i<newArrayList.size();i++){
//                                selectedTrueFalse[i]=false;
//                            }
//
//                            Log.i("alertdialogueitems", Arrays.toString(alertdialogueitems));
//                            Log.i("alertdialogueitems",newArrayList.toString());
//
//
//                            alertdialogueBuilder.setMultiChoiceItems(alertdialogueitems, selectedTrueFalse, new DialogInterface.OnMultiChoiceClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
//                                    Log.i("i","(INSIDE setMultiChoiceItems)= "+String.valueOf(i));
//                                }
//                            });
//
//                            alertdialogueBuilder.setCancelable(false);
//
//                            alertdialogueBuilder.setMessage("Do you want to add -:");
//                            alertdialogueBuilder.setTitle("Adding Contact");
//
//                            alertdialogueBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    Log.i("i(INSIDE OK)=",String.valueOf(i));
//                                }
//                            });
//
//                            alertdialogueBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    Log.i("i(INSIDE CANCEL)=",String.valueOf(i));
//                                    return;
//                                }
//                            });
//                            alertdialogueBuilder.show();


                            updateChilds(arrayList);
                        }
                    }


                }
            }
        }
    }

    public void checkAlreadyExisting(){
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        LinearLayout linearLayout1;
        for(int i=0;i<phoneNumbers.size();i++){

            linearLayout1=(LinearLayout) linearLayout.getChildAt(i);


            EditText editText= (EditText) linearLayout1.getChildAt(0);
            if(editText.getText().toString()!=phoneNumbers.get(i)){
                phoneNumbers.set(i,editText.getText().toString());
            }
        }
    }

    public void checkIfLastAdded(){
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        LinearLayout linearLayout1=(LinearLayout)linearLayout.getChildAt(linearLayout.getChildCount()-1);
        EditText editText=(EditText)linearLayout1.getChildAt(0);
        for(int i=0;i<phoneNumbers.size();i++){
            if(editText.getText().toString().equals(phoneNumbers.get(i))){
                Log.i("Last EditText value","exists. Removing it");
                phoneNumbers.remove(i);
            }
        }
    }

    public void updateChilds(ArrayList<String> arrayList){
        checkAlreadyExisting();

        if(arrayList.size()==0){
            return;
        }

        checkIfLastAdded();


        int sizeAdded=0;
        for(int i=0;i<arrayList.size();i++){
            if(!phoneNumbers.contains(arrayList.get(i))){
                phoneNumbers.add(arrayList.get(i));
                sizeAdded++;
            }
        }
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        LinearLayout linearLayout1;

        int childCount=linearLayout.getChildCount();
        Log.i("ChildCount"," of LinearLayout is "+String.valueOf(childCount));

        int numberCount=phoneNumbers.size();
        Log.i("Number of phone Numbers",String.valueOf(numberCount));

        for(int i=0;i<sizeAdded-1;i++){
            addChild();
        }

        for(int i=linearLayout.getChildCount()-sizeAdded;i<linearLayout.getChildCount();i++){
            Log.i("i=",Integer.toString(i));
            linearLayout1=(LinearLayout) linearLayout.getChildAt(i);
            EditText editText=(EditText) linearLayout1.getChildAt(0);
            editText.setText(phoneNumbers.get(i));
        }

        phoneNumbers.remove(phoneNumbers.size()-1);



        Log.i("Phone Numbers-",phoneNumbers.toString());
    }

    public void addEditText(View view){
        checkAlreadyExisting();

        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        LinearLayout linearLayout1=(LinearLayout)linearLayout.getChildAt(linearLayout.getChildCount()-1);
        Log.i("Child Count",String.valueOf(linearLayout.getChildCount()));
        EditText editText=
                (EditText) linearLayout1.getChildAt(0);
        if(editText.getText().toString().equals("")){
            Toast.makeText(this,"Field is empty",Toast.LENGTH_SHORT).show();
            return;
        }
        boolean found=false;
        for(int i=0;i<phoneNumbers.size();i++){
            Log.i("Phone Number at index",i+" is "+phoneNumbers.get(i));
            Log.i("Text inside ","editText is "+editText.getText().toString());
            if(phoneNumbers.get(i).equals(editText.getText().toString())){
                found=true;
            }
        }
        if(!found){

            phoneNumbers.add(editText.getText().toString());
            addChild();
        }else{
            if(phoneNumbers.get(phoneNumbers.size()-1).equals(editText.getText().toString())){
                Log.i("HERE","I AM");
                addChild();
            }else {
                Toast.makeText(this, "Contact already exists", Toast.LENGTH_SHORT).show();
            }
        }

        Log.i("Phone Numbers",phoneNumbers.toString());

    }

    public void addChild(){
        final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearLayout);

        EditText editText=new EditText(settings.this);

        Resources r=settings.this.getResources();

        LinearLayout linearLayout1=new LinearLayout(this);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout1.setLayoutParams(params);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);


        int layoutWidth=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,230,r.getDisplayMetrics());
        params=new LinearLayout.LayoutParams(layoutWidth, ViewGroup.LayoutParams.WRAP_CONTENT);


        int padding=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,r.getDisplayMetrics());
        int topMargin=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,15,r.getDisplayMetrics());
        int leftMargin=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,r.getDisplayMetrics());
//        int rightMargin=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,r.getDisplayMetrics());
        int bottomMargin=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,15,r.getDisplayMetrics());



        params.setMargins(leftMargin,topMargin,0,bottomMargin);
        editText.setLayoutParams(params);
        editText.setBackgroundResource(R.drawable.edittext_background);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_phone_24,0,0,0);
        editText.setTextColor(Color.parseColor("#367CF6"));
        editText.setHintTextColor(Color.parseColor("#303C50"));
        editText.setPadding(padding,padding,padding,padding);
        editText.setHint("Enter Emergency Contact Number");
        editText.setCompoundDrawablePadding(padding);
        editText.setInputType(InputType.TYPE_CLASS_PHONE);

        linearLayout1.addView(editText);

        layoutWidth=(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40,r.getDisplayMetrics());

        params=new LinearLayout.LayoutParams(layoutWidth, layoutWidth);
        params.setMargins(0,topMargin,0,bottomMargin);

        ImageView imageView=new ImageView(this);
        imageView.setImageResource(R.drawable.ic_baseline_delete_24);
        imageView.setLayoutParams(params);


        final int index=linearLayout.getChildCount();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout1=(LinearLayout)findViewById(R.id.linearLayout);
                if(layout1.getChildCount()==1){
                    Log.i("Only","1 child found");
                    Toast.makeText(settings.this,
                            "You have to enter at least 1 emergency Contact Number",Toast.LENGTH_SHORT).show();
                    return;
                }

                LinearLayout layout=(LinearLayout)view.getParent();

                EditText editText1=(EditText)layout.getChildAt(0);

                for(int i=0;i<phoneNumbers.size();i++){
                    if(phoneNumbers.get(i).equals(editText1.getText().toString())){
                        Log.i("MATCH FOUND",editText1.getText().toString()+"="+phoneNumbers.get(i));
                        phoneNumbers.remove(i);
                        Log.i("Number Found","Deleting "+editText1.getText().toString());
                        break;
                    }
                }



                Log.i("inside","onClickListener");
                layout1.removeView((View)view.getParent());

                Log.i("Phone Numbers-",phoneNumbers.toString());

            }
        });

        linearLayout1.addView(imageView);

        linearLayout.addView(linearLayout1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("permission","requested");

        if(grantResults[0]== PackageManager.PERMISSION_GRANTED&&grantResults.length>0){
            Log.i("permission","granted");
            contactsActivity();
        }
    }



    public void removeChild(int index){
        LinearLayout linearLayout=(LinearLayout)findViewById(R.id.linearLayout);

        if(linearLayout.getChildCount()==1){
            Toast.makeText(settings.this,
                    "You have to enter at least 1 emergency Contact Number",Toast.LENGTH_SHORT).show();
            Log.i("Only","1 child found");
            return;
        }
        LinearLayout linearLayout1=(LinearLayout)linearLayout.getChildAt(index);

        EditText editText=(EditText)linearLayout1.getChildAt(0);

        for(int i=0;i<phoneNumbers.size();i++){
            if(phoneNumbers.get(i).equals(editText.getText().toString())){
                Log.i("MATCH FOUND",editText.getText().toString()+"="+phoneNumbers.get(i));
                phoneNumbers.remove(i);
                Log.i("Number Found","Deleting "+editText.getText().toString());
                break;
            }
        }

        linearLayout.removeViewAt(index);

        Log.i("Phone Numbers-",phoneNumbers.toString());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        try {

            SharedPreferences sharedPreferences = this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

            Set<String> emergencyContacts = sharedPreferences.getStringSet("emergencyContacts", null);

            phoneNumbers = new ArrayList<>(emergencyContacts);

            LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);

            if (phoneNumbers.size() > 0) {
                LinearLayout layout1 = (LinearLayout) layout.getChildAt(0);
                EditText editText = (EditText) layout1.getChildAt(0);
                editText.setText(phoneNumbers.get(0));
            }



            for(int i=1;i<phoneNumbers.size();i++) {
                addChild();
                LinearLayout layout1 = (LinearLayout) layout.getChildAt(i);
                EditText editText = (EditText) layout1.getChildAt(0);
                editText.setText(phoneNumbers.get(i));
            }
        }catch (Exception e){
            Log.i("ERROR-",e.toString());
            e.printStackTrace();
        }
    }
}