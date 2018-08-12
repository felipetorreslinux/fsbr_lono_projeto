package com.lono.Models;

public class Slide_Payment_Model {


    int type;
    String document;
    String name;
    String title;
    String subtitle;
    String qtd_termos;
    double value_termos;
    String description_plam;
    String label_button;
    int min_terms;
    int max_terms;

    public Slide_Payment_Model(int type, String document, String name, String title, String subtitle, String qtd_termos, double value_termos, String description_plam, String label_button, int min_terms, int max_terms) {
        this.type = type;
        this.document = document;
        this.name = name;
        this.title = title;
        this.subtitle = subtitle;
        this.qtd_termos = qtd_termos;
        this.value_termos = value_termos;
        this.description_plam = description_plam;
        this.label_button = label_button;
        this.min_terms = min_terms;
        this.max_terms = max_terms;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getQtd_termos() {
        return qtd_termos;
    }

    public void setQtd_termos(String qtd_termos) {
        this.qtd_termos = qtd_termos;
    }

    public double getValue_termos() {
        return value_termos;
    }

    public void setValue_termos(double value_termos) {
        this.value_termos = value_termos;
    }

    public String getDescription_plam() {
        return description_plam;
    }

    public void setDescription_plam(String description_plam) {
        this.description_plam = description_plam;
    }

    public String getLabel_button() {
        return label_button;
    }

    public void setLabel_button(String label_button) {
        this.label_button = label_button;
    }

    public int getMin_terms() {
        return min_terms;
    }

    public void setMin_terms(int min_terms) {
        this.min_terms = min_terms;
    }

    public int getMax_terms() {
        return max_terms;
    }

    public void setMax_terms(int max_terms) {
        this.max_terms = max_terms;
    }
}
