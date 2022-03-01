package com.lmalvarez.fileadmin.model;

public class User {
    private String user;
    private String name;
    private String token;
    private boolean logged;

    public User() {
        this.user = null;
        this.name = null;
        this.token = null;
        this.logged = false;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    @Override
    public String toString() {
        return "User{" +
                "user='" + user + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", logged=" + logged +
                '}';
    }
}
