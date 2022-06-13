package com.example.x_contacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import javax.xml.transform.sax.SAXResult;

public class AjoutModifContact extends AppCompatActivity {

    ImageView profilePic;
    EditText nom, telephone, email, note;
    FloatingActionButton add_contact;
    private String name, phone, mail, writeNotes, id, addedTime, updatedTime, image;
    private ActionBar actionbar;
    private Boolean isEditMode;

    // variables pour permission
    final static int CAMERA_PERM_CODE = 100;
    final static int STORAGE_PERM_CODE = 200;
    final static int GALLERY_IMG_PERM_CODE = 300;
    final static int CAMERA_IMG_PERM_CODE = 400;

    // Listes des permissions
    String[] camera_perm;
    String[] storage_perm;

    // Uri des images
    Uri imageUri;

    // Db Helper
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_modif_contact);

        // Initialization dbHerlper
        dbHelper = new DBHelper(this);

        // Initialization permissions
        camera_perm = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storage_perm = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Configuration + intialization action bar
        actionbar = getSupportActionBar();


        // Bouton de retour
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        // Initialization formulaire
        profilePic = findViewById(R.id.profilePic);
        nom = findViewById(R.id.nom);
        telephone = findViewById(R.id.telephone);
        email = findViewById(R.id.email);
        note = findViewById(R.id.notes);
        add_contact = findViewById(R.id.add_contact);

        // Recuperation intent pour l'edition d'infos
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("isEditMode", false);

        if (isEditMode) {
            // Setting de la toolbar
            actionbar.setTitle("Mise A jour Contact");

            // Recuperation des params passes a l'intent
            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            phone = intent.getStringExtra("PHONE");
            mail = intent.getStringExtra("EMAIL");
            writeNotes = intent.getStringExtra("NOTE");
            addedTime = intent.getStringExtra("ADDEDTIME");
            updatedTime = intent.getStringExtra("UPDATEDTIME");
            image = intent.getStringExtra("IMAGE");

            // Setting des valeurs
            nom.setText(name);
            telephone.setText(phone);
            email.setText(mail);
            note.setText(writeNotes);
            imageUri = Uri.parse(image);

            if(image.equals("")){
                profilePic.setImageResource(R.drawable.ic_baseline_person_24);
            }
            else{
                profilePic.setImageURI(imageUri);
            }
        }
        else{
            // Ajouter mode on
            actionbar.setTitle("Ajouter Contact");
        }

        // Gestion des events
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });
    }

    private void showImagePickerDialog() {
        // Choix de type d'image
        String[] options = {"Camera", "Stockage"};

        // Boite de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Definir la source");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    // Choix de la cam
                    if(!checkCamPerm()){
                        requestCamPerm();
                    }
                    else{
                        pickFromCam();
                    }
                }
                else if (which == 1){
                    // Choix de la galerie
                    if (!checkStoragePerm()){
                        requestStoragePerm();
                    }
                    else{
                        pickFromGallery();
                    }
                }
            }
        }).create().show();
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_IMG_PERM_CODE);
    }

    private void pickFromCam() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "IMAGE_DETAILS");

        // sauvegarde Uti image
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Ouverture camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(cameraIntent, CAMERA_PERM_CODE);
    }

    private void saveData() {
        // Stockage des donnees
        name = nom.getText().toString();
        phone = telephone.getText().toString();
        mail = email.getText().toString();
        writeNotes = note.getText().toString();

        // Recuperation timestamp & cie
        String timestamp = "" + System.currentTimeMillis();

        // Controle de saisie
        if(!name.isEmpty() && !phone.isEmpty()){
            // Sauvegarde des infos dans une BD SQLite
            // Verification Edit/Ajout mode
            if (isEditMode){
                // Mode EDIT
                dbHelper.updateContacts(
                        "" + id,
                        "" + image,
                        "" + name,
                        "" + phone,
                        "" + email,
                        "" + note,
                        "" + addedTime,
                        "" + timestamp
                );

                Toast.makeText(getApplicationContext(), "Mise a jour effectuee", Toast.LENGTH_SHORT).show();
            }
            else{
                // Mode AJOUT
                long id = dbHelper.insertContacts(
                        "" + imageUri,
                        "" + name,
                        "" + phone,
                        "" + mail,
                        "" + writeNotes,
                        "" + timestamp,
                        "" + timestamp
                );

                // Insertion notification
                Toast.makeText(getApplicationContext(), id+" insere !", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Erreur sur un champ", Toast.LENGTH_SHORT).show();
        }
    }

    // Verification permission camera
    private boolean checkCamPerm(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return result && result1;
    }

    // Demander permission camera
    private void requestCamPerm(){
        ActivityCompat.requestPermissions(this, camera_perm, CAMERA_PERM_CODE);
    }


    // Verification permission stockage
    private boolean checkStoragePerm(){
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return result1;
    }

    // Demander permission stockage
    private void requestStoragePerm(){
        ActivityCompat.requestPermissions(this, storage_perm, STORAGE_PERM_CODE);
    }

    // Gestion de request de permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_PERM_CODE:
                if (grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    // Choix cmaera si deux permissions validees
                    if (cameraAccepted && storageAccepted){
                        pickFromCam();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Permission camera/stockage requise(s)", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_PERM_CODE:
                if (grantResults.length > 0){
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    // Choix gallerie
                    if (storageAccepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Permission stockage requise", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    // Bouton de retour
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    // Choix d'image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == GALLERY_IMG_PERM_CODE){
                // L'image a ete choisie depuis la galerie
                // Rognage de l'image
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            }
            else if(requestCode == CAMERA_PERM_CODE){
                // Prise de photo + rognage
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                // Succes du rognage
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                profilePic.setImageURI(imageUri);
            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                // Gestion d'erreur
                Toast.makeText(getApplicationContext(), "Erreur lors de la procedure", Toast.LENGTH_SHORT).show();
            }
        }
    }
}