package com.example.android.mobilecourse;

public class UserCustomFields {

    public String email;
    public String phone;

    public UserCustomFields(){

    }
    public UserCustomFields(String phone, String email) {
        this.email = email;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
