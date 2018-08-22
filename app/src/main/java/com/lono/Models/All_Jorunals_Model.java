package com.lono.Models;

public class All_Jorunals_Model {
    int id;
    String sigle;
    String name;
    String abrangency;
    String state;
    boolean selected;

    public All_Jorunals_Model(int id, String sigle, String name, String abrangency, String state, boolean selected) {
        this.id = id;
        this.sigle = sigle;
        this.name = name;
        this.abrangency = abrangency;
        this.state = state;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSigle() {
        return sigle;
    }

    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbrangency() {
        return abrangency;
    }

    public void setAbrangency(String abrangency) {
        this.abrangency = abrangency;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
