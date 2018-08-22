package com.lono.Models;

public class Journals_Model {
    int id;
    String sigle;
    String name;
    String name_organ;
    String sigle_organ;

    public Journals_Model(int id, String sigle, String name, String name_organ, String sigle_organ) {
        this.id = id;
        this.sigle = sigle;
        this.name = name;
        this.name_organ = name_organ;
        this.sigle_organ = sigle_organ;
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

    public String getName_organ() {
        return name_organ;
    }

    public void setName_organ(String name_organ) {
        this.name_organ = name_organ;
    }

    public String getSigle_organ() {
        return sigle_organ;
    }

    public void setSigle_organ(String sigle_organ) {
        this.sigle_organ = sigle_organ;
    }
}
