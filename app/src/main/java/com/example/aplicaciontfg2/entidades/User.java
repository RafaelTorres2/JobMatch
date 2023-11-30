package com.example.aplicaciontfg2.entidades;

import java.io.Serializable;

public class User implements Serializable {

    private String name, email, token, id;

    public User(){


    }

    public User(String name,String email,String token,String id){

        this.name = name;
        this.email = email;
        this.token = token;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
