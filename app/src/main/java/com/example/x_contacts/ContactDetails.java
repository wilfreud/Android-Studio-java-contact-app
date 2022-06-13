package com.example.x_contacts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

public class ContactDetails extends AppCompatActivity {

    // view
    private TextView nameTv, phoneTv, emailTv, addedTimeTv, updatedTimeTv, noteTv;
    private ImageView profilTv;
    private String id;

    private DBHelper dbHelper;

    public ContactDetails() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        // init Db
        dbHelper = new DBHelper(this);


        // recuperation des data depuis l'Intent
        Intent intent = getIntent();
        id = intent.getStringExtra("contactId");


        // Initialization view
        nameTv = findViewById(R.id.nameTv);
        phoneTv = findViewById(R.id.phoneTv);
        emailTv = findViewById(R.id.emailTv);
        addedTimeTv = findViewById(R.id.addedTimeTv);
        updatedTimeTv = findViewById(R.id.updatedTimeTv);
        noteTv = findViewById(R.id.noteTv);
        profilTv = findViewById(R.id.profilTv);

        loadDataById();
    }

    private void loadDataById() {
        // Requete + filtrage Id
        String selectQuery = "SELECT * FROM " + Contacts.DB_TABLE_NAME + " WHERE " + Contacts.C_ID + "= \"" + id + "\"";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                // recuper  donnees
                String name = "" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_NOM));
                String image ="" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_IMAGE));
                String phone ="" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_TELEPHONE));
                String email ="" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_EMAIL));
                String note ="" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_NOTE));
                String addedTime ="" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_ADDED_TIME));
                String updatedTime ="" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_UPDATED_TIME));

                // Conversion du temps en date
                Calendar calendar = Calendar.getInstance(Locale.getDefault());

                calendar.setTimeInMillis(Long.parseLong(addedTime));
                String timeAdd = "" + DateFormat.format("dd/MM/yy hh:mm:aa", calendar);

                calendar.setTimeInMillis(Long.parseLong(updatedTime));
                String timeUpdate = "" + DateFormat.format("dd/MM/yy hh:mm:aa", calendar);


                // Set data
                nameTv.setText(name);
                phoneTv.setText(phone);
                emailTv.setText(email);
                noteTv.setText(note);
                addedTimeTv.setText(timeAdd);
                updatedTimeTv.setText(timeUpdate);

                if(image.equals("null")){
                    profilTv.setImageResource(R.drawable.ic_baseline_person_24);
                }
                else{
                    profilTv.setImageURI(Uri.parse(image));
                }


            }while(cursor.moveToNext());
        }

        db.close();
    }

}