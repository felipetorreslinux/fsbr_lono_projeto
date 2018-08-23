package com.lono.Models;

public class Alerts_Model {

    int id;
    int id_user;
    String message;
    String assunt;
    String created_at;
    String recuring_time;

    public Alerts_Model(int id, int id_user, String message, String assunt, String created_at, String recuring_time) {
        this.id = id;
        this.id_user = id_user;
        this.message = message;
        this.assunt = assunt;
        this.created_at = created_at;
        this.recuring_time = recuring_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAssunt() {
        return assunt;
    }

    public void setAssunt(String assunt) {
        this.assunt = assunt;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getRecuring_time() {
        return recuring_time;
    }

    public void setRecuring_time(String recuring_time) {
        this.recuring_time = recuring_time;
    }
}
