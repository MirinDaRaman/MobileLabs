package com.example.android.mobilecourse;

public class UserCustomFields {


    public String name;
    public String email;
    public String phone;

    public UserCustomFields(){

    }
    public UserCustomFields(String name,String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
