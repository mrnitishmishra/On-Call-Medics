package com.example.myapplication.model;

public class Users {

    String name , mail , password , user_type , age , gender , image;

    public Users(String name, String mail, String password, String user_type, String age, String gender) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.user_type = user_type;
        this.age = age;
        this.gender = gender;
        this.image = image;
    }

    public Users(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
