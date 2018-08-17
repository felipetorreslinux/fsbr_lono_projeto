package com.lono.Models;

public class Info_Plano_Profile_Model {

    String name_plan;
    String type_pay;
    int qtd_terms;
    int terms_cad;
    double price_plan;

    public Info_Plano_Profile_Model(String name_plan, String type_pay, int qtd_terms, int terms_cad, double price_plan) {
        this.name_plan = name_plan;
        this.type_pay = type_pay;
        this.qtd_terms = qtd_terms;
        this.terms_cad = terms_cad;
        this.price_plan = price_plan;
    }

    public String getName_plan() {
        return name_plan;
    }

    public void setName_plan(String name_plan) {
        this.name_plan = name_plan;
    }

    public String getType_pay() {
        return type_pay;
    }

    public void setType_pay(String type_pay) {
        this.type_pay = type_pay;
    }

    public int getQtd_terms() {
        return qtd_terms;
    }

    public void setQtd_terms(int qtd_terms) {
        this.qtd_terms = qtd_terms;
    }

    public int getTerms_cad() {
        return terms_cad;
    }

    public void setTerms_cad(int terms_cad) {
        this.terms_cad = terms_cad;
    }

    public double getPrice_plan() {
        return price_plan;
    }

    public void setPrice_plan(double price_plan) {
        this.price_plan = price_plan;
    }
}
