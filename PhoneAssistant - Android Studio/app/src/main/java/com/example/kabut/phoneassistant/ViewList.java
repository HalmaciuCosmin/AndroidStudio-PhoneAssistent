package com.example.kabut.phoneassistant;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;


import java.util.ArrayList;

public class ViewList extends AppCompatActivity {

    DataBase myDb;
    public Button Delete;
    EditText EditNume;
     ListView  Lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);
        Lista = (ListView) findViewById(R.id.VList);



        myDb = new DataBase(this);

        ArrayList<String> theList = new ArrayList<>();

        Cursor data = myDb.getAll();
        String base = "Nume - Numar telefon - Email";
        theList.add(base);


        if(data.getCount() == 0) {
            Toast.makeText(ViewList.this,"Baza de date e goala",Toast.LENGTH_LONG).show();
        }
        else {
            while(data.moveToNext()) {
               String aux = data.getString(0);
               String aux1 = data.getString(1);
                String aux2 = data.getString(2);
                String str = aux + "-" + aux1 + "-" + aux2;
                theList.add(str);


              ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,theList);
              Lista.setAdapter(listAdapter);
          }
      }
            DeleteFriend();
    }


    public void DeleteFriend() {

            Delete = (Button) findViewById(R.id.Delete);
            EditNume = (EditText) findViewById(R.id.Nume);

            Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String aux = EditNume.getText().toString();
                    if(getNume(aux) == false) {
                        Toast.makeText(ViewList.this , "Persoana nu se afla in baza de date",Toast.LENGTH_LONG).show();
                        return;
                    }

                    myDb.Delete(aux);
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                }
            });

    }


    public boolean getNume(String name) {
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
