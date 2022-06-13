package com.example.x_contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    // view
    private FloatingActionButton add_contact;
    private RecyclerView contactRv;

    // BD
    private DBHelper dbHelper;

    //Adapter
    private AdapterContact adapterContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        // Initialization BD
        dbHelper = new DBHelper(this);

        // Initialization
        add_contact = findViewById(R.id.add_contact);
        contactRv = findViewById(R.id.contactRv);

        contactRv.setHasFixedSize(true);


        // Ajouter un listener sur le bouton ajout de contact
        add_contact.setOnClickListener(view -> {
            // Redirection vers une nouvelle Activity -> Creation contact
            Intent intent = new Intent(this, AjoutModifContact.class);
            intent.putExtra("isEditMode", false);
            startActivity(intent);
        });

        loadData();


    }

    private void loadData() {
        adapterContact = new AdapterContact(this, dbHelper.getAllData());
        contactRv.setAdapter(adapterContact);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh lorsqu'on revient
        loadData();
    }
}