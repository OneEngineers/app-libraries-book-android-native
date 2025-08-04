package com.assistant.libraries;

public class User {
    private String name, email, password;
    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    };
    public User(String email, String password){
        this.email = email;
        this.password = password;

    };
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword(){
        return  password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
