package com.example.x_contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    // Constructor (initialization db)
    public DBHelper(@Nullable Context context) {
        super(context, Contacts.DB_NAME, null, Contacts.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table
        db.execSQL(Contacts.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Mise a jour table si changement sur structure

        // suppression si table existe
        db.execSQL("DROP TABLE IF EXISTS " + Contacts.DB_TABLE_NAME);

        // re-creation de la table
        onCreate(db);
    }

    // Insertion de donnees
    public long insertContacts(String image, String nom, String telephone, String email, String note, String addedTime, String updatedTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contacts.C_IMAGE, image);
        contentValues.put(Contacts.C_NOM, nom);
        contentValues.put(Contacts.C_TELEPHONE, telephone);
        contentValues.put(Contacts.C_EMAIL, email);
        contentValues.put(Contacts.C_NOTE, note);
        contentValues.put(Contacts.C_ADDED_TIME, addedTime);
        contentValues.put(Contacts.C_UPDATED_TIME, updatedTime);

        // Ajout d'un enregistrement (return -> id de l'enregistrement)
        long id;
        id = db.insert(Contacts.DB_TABLE_NAME, null, contentValues);

        db.close();
        return id;
    }

    // Update un contact
    public void updateContacts(String id, String image, String nom, String telephone, String email, String note, String addedTime, String updatedTime) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Contacts.C_IMAGE, image);
        contentValues.put(Contacts.C_NOM, nom);
        contentValues.put(Contacts.C_TELEPHONE, telephone);
        contentValues.put(Contacts.C_EMAIL, email);
        contentValues.put(Contacts.C_NOTE, note);
        contentValues.put(Contacts.C_ADDED_TIME, addedTime);
        contentValues.put(Contacts.C_UPDATED_TIME, updatedTime);

        // Mise a jour d'un enregistrement (return -> id de l'enregistrement)

        db.update(Contacts.DB_TABLE_NAME, contentValues, Contacts.C_ID+" =? ", new String[] {id});

        db.close();
    }

    // Delete un un contact
    public void deleteContact(String id){
        // writeable db
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Contacts.DB_TABLE_NAME, Contacts.C_ID + " =? ", new String[]{id});

        db.close();
    }

    // REcuperation des donnees
    public ArrayList<ModelContact> getAllData(){
        // Creation arraylist
        ArrayList<ModelContact> arrayList = new ArrayList<>();

        // Requete sql
        String selectQuery = "SELECT * FROM " +Contacts.DB_TABLE_NAME;

        // Recuperation readable Db
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Fetching de la query
        if(cursor.moveToFirst()){
            do{
                ModelContact modelContact = new ModelContact(
                        "" + cursor.getInt(cursor.getColumnIndexOrThrow(Contacts.C_ID)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_NOM)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_IMAGE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_TELEPHONE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_EMAIL)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_NOTE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_ADDED_TIME)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Contacts.C_UPDATED_TIME))
                );
                arrayList.add(modelContact);
            }while (cursor.moveToNext());
        }
        db.close();
        return arrayList;
    }
}