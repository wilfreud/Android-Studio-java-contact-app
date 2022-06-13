package com.example.x_contacts;

public class Contacts {

    // Informations sur la DB
    public static final String DB_NAME = "X_CONTACTS_DB";
    public static final int DB_VERSION = 1;
    public static final String DB_TABLE_NAME = "contacts";

    public static final String C_ID = "ID";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_NOM = "NOM";
    public static final String C_TELEPHONE = "TELEPHONE";
    public static final String C_EMAIL = "EMAIL";
    public static final String C_NOTE = "NOTE";
    public static final String C_ADDED_TIME = "DATE_AJOUT";
    public static final String C_UPDATED_TIME = "DATE_MAJ";

    // Requete SQL
    public final static String CREATE_TABLE = "CREATE TABLE " + DB_TABLE_NAME + " ( "
            + C_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + C_IMAGE + " TEXT, "
            + C_NOM + " VARCHAR, "
            + C_TELEPHONE + " VARCHAR, "
            + C_EMAIL + " VARCHAR, "
            + C_NOTE + " TEXT, "
            + C_ADDED_TIME + " VARCHAR, "
            + C_UPDATED_TIME + " VARCHAR "
            + ");";


}
