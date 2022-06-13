package com.example.x_contacts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.ContactViewHolder> {

    private Context context;
    private ArrayList<ModelContact> contactList;
    private DBHelper dbHelper;

    // Constructor
    public AdapterContact(Context context, ArrayList<ModelContact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_contact_item, parent, false);
        ContactViewHolder vh = new ContactViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        ModelContact modelcontact = contactList.get(position);


        // Get data
        String id = modelcontact.getId();
        String image = modelcontact.getImage();
        String name = modelcontact.getName();
        String phone = modelcontact.getPhone();
        String email = modelcontact.getEmail();
        String note = modelcontact.getNote();
        String addedTime = modelcontact.getAddedTime();
        String updatedTime = modelcontact.getUpdatedTime();

        // Set data in view
        holder.contactName.setText(name);
        if(image.equals("")){
            holder.contactImage.setImageResource(R.drawable.ic_baseline_person_24);
        }
        else{
            holder.contactImage.setImageURI(Uri.parse(image));
        }

        // Gestion des listener pour les clicks
        holder.contactDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // gestion click pour afficher infos contact
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // creation intent -> Activity d'affichage
                Intent intent = new Intent(context, ContactDetails.class);
                intent.putExtra("contactId", id);
                context.startActivity(intent);
            }
        });


        // gestion click pour editer
        holder.contactEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent de move vers l'edition
                Intent intent = new Intent(context, AjoutModifContact.class);

                // passage par "argument" des valeurs du contact
                intent.putExtra("ID", id);
                intent.putExtra("NAME", name);
                intent.putExtra("PHONE", phone);
                intent.putExtra("EMAIL", email);
                intent.putExtra("NOTE", note);
                intent.putExtra("ADDEDTIME", addedTime);
                intent.putExtra("UPDATEDTIME", updatedTime);
                intent.putExtra("IMAGE", image);

                // bool pour montrer que'em edit mode
                intent.putExtra("isEditMode", true);
                context.startActivity(intent);
            }
        });

        // gestion click pour supprimer
        holder.contactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // init DB
                dbHelper = new DBHelper(context);
                dbHelper.deleteContact(id);

                // Refresh apres deletion
                ((MainActivity)context).onResume();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{

        //View pour row_contact_item
        ImageView contactImage, contactDial, contactEdit, contactDelete;
        TextView contactName;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initializationde la view
            contactImage = itemView.findViewById(R.id.contact_image);
            contactDial = itemView.findViewById(R.id.contact_number_dial);
            contactName = itemView.findViewById(R.id.contact_name);
            contactDelete = itemView.findViewById(R.id.contact_delete);
            contactEdit = itemView.findViewById(R.id.contact_edit);

        }
    }
}
