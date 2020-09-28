package com.example.kabut.phoneassistant;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    DataBase myDb;
    EditText Numele;
    EditText Numarul;
    EditText Emailul;
    Button Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myDb = new DataBase(this);
        Numele = (EditText) findViewById(R.id.Name);
        Numarul = (EditText) findViewById(R.id.Number);
        Emailul = (EditText) findViewById(R.id.Email);
        AddData();
    }

    public void AddData() {
        Add = (Button) findViewById(R.id.Done);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Nume(Numele.getText().toString()) == true) {

                    Toast.makeText(Register.this , "Exista o persoana cu acest nume in baza de date",Toast.LENGTH_LONG).show();
                    return;
                }

                boolean isInserted = myDb.InsertData(Numele.getText().toString(),Numarul.getText().toString(),Emailul.getText().toString());
                if(isInserted == true) {
                    Toast.makeText(Register.this , "Datele au fost inserate",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Register.this ,"Eroare",Toast.LENGTH_LONG).show();
                }
            }
        });
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



}
