package com.example.myapplication;

import com.google.firebase.database.snapshot.StringNode;

public class ModelDocData {
    String Name , PIN_Code , Image_Url , Email , Degree , Specialization ;

    public ModelDocData() {
    }

    public ModelDocData(String name, String PIN_Code , String Image_Url, String degree, String specialization) {
        Name = name;
        PIN_Code = PIN_Code;
        Image_Url = Image_Url;
        Email = Email;
        Degree = degree;
        Specialization = specialization;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPIN_Code() {
        return PIN_Code;
    }

    public void setPinCode(String PIN_Code) {
        PIN_Code = PIN_Code;
    }

    public String getImage_Url() {
        return Image_Url;
    }

    public void setImage_Url(String Image_Url) {
        Image_Url = Image_Url;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        Specialization = specialization;
    }
}
