package com.lono.Models;

public class Alerts_Model {

    int id;
    String message;
    String assunt;
    String recurring_time;

    public Alerts_Model(int id, String message, String assunt, String recurring_time) {
        this.id = id;
        this.message = message;
        this.assunt = assunt;
        this.recurring_time = recurring_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getRecurring_time() {
        return recurring_time;
    }

    public void setRecurring_time(String recurring_time) {
        this.recurring_time = recurring_time;
    }
}
