package com.pracktic.yogaspirit.data.user;


import java.io.Serializable;

public class Session implements Serializable {
    private String email, password, login;

    public Session(){

    }

    public Session(String email, String password) {
        this.email = email;
        this.password = password;
        login = email.split("@")[0];
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
