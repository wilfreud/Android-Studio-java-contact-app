package com.example.x_contacts;

public class ModelContact {

    private String id, name, image, phone, email, note, addedTime, updatedTime;

    // Constructor


    public ModelContact(String id, String name, String image, String phone, String email, String note, String addedTime, String updatedTime) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.email = email;
        this.note = note;
        this.addedTime = addedTime;
        this.updatedTime = updatedTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getNote() {
        return note;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }
}
