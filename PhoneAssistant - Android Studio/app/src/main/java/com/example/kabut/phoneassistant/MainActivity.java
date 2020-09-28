package com.example.kabut.phoneassistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txvResult;
    DataBase myDb;
    public Button register;
    public Button seeData;
    private static final int REQUEST_CALL = 1;
    String Anumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txvResult = (TextView) findViewById(R.id.txvResult);
        myDb = new DataBase(this);
        register = (Button) findViewById(R.id.register);
        seeData = (Button) findViewById(R.id.seeData);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent IntentReg = new Intent(MainActivity.this, com.example.kabut.phoneassistant.Register.class);
                startActivity(IntentReg);
            }
        });

        seeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IntentReg = new Intent(MainActivity.this, com.example.kabut.phoneassistant.ViewList.class);
                startActivity(IntentReg);
            }
        });
    }

    public void getSpeechInput(View view) {

        Intent  intent = new Intent (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,  Locale.getDefault());

        if(intent.resolveActivity(getPackageManager()) !=  null) {
         startActivityForResult(intent,10);
        }
        else {
            Toast.makeText(this,"Your Device Don`t Support Speech Input",Toast.LENGTH_SHORT).show();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        switch(requestCode) {
            case 10:
                if(resultCode == RESULT_OK && data!= null) {
                  ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                  String a  = result.get(0);
                  txvResult.setText(a);
                  String parts[] = a.split("to");

                      if(parts[0].equals("email "))  {
                         String nume = parts[1].trim();
                         if(Nume(nume)) {
                             String mail = Email(nume);
                             mailIntent(nume,mail);
                         }
                      }

                      if(parts[0].equals("call "))  {
                        String nume = parts[1].trim();
                        if(Nume(nume)) {
                             Anumber = Number(nume);
                             callIntent();
                        }
                    }



                }
          break;
        }
     }

    public boolean Nume(String name) {
        Cursor res = myDb.getName(name);
        int a = res.getCount();
        if(a == 0) {
            return false;
        }
        else {
            return true;
        }
    }


    public String Email(String name) {
        Cursor res = myDb.getEmail(name);
        int a = res.getCount();
        if(a == 0) {
            Toast.makeText(this,"Eroare",Toast.LENGTH_SHORT).show();
            return "EROARE";
        }
        else {
            res.moveToNext();
            return res.getString(0);

        }

    }

    public String Number(String name) {
        Cursor res = myDb.getNumber(name);
        int a = res.getCount();
        if(a == 0) {
            Toast.makeText(this,"Eroare",Toast.LENGTH_SHORT).show();
            return "EROARE";
        }
        else {
            res.moveToNext();
            return res.getString(0);

        }
    }

    public void mailIntent(String name , String ArgumentEmail) {
        Intent Intentmail = new Intent(Intent.ACTION_SEND);
        String[] EmailArray = {ArgumentEmail};
        Intentmail.putExtra(Intentmail.EXTRA_EMAIL,EmailArray);
        Intentmail.putExtra(Intentmail.EXTRA_TEXT,"Dear " + name + ",");
        Intentmail.putExtra(Intentmail.EXTRA_SUBJECT,"Subject...");

        Intentmail.setType("message/rfc822");

        startActivity(Intent.createChooser(Intentmail,"alege email-client"));
    }

    public void callIntent() {
        Intent callInt = new Intent(Intent.ACTION_CALL);
        callInt.setData(Uri.parse("tel:" + Anumber));
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);

        }
        else {
            startActivity(callInt);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL)  {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callIntent();
            }
            else {
                Toast.makeText(this,"Eroare",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
