package com.lono.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Terms_Model {
    int id;
    String name;
    boolean all_jornals;
    boolean literal;

    public Terms_Model(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id_nome_pesquisa");
        this.name = jsonObject.getString("nome_pesquisa");
        this.all_jornals = jsonObject.getBoolean("todos_jornais");
        this.literal = jsonObject.getBoolean("literal");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAll_jornals() {
        return all_jornals;
    }

    public void setAll_jornals(boolean all_jornals) {
        this.all_jornals = all_jornals;
    }

    public boolean isLiteral() {
        return literal;
    }

    public void setLiteral(boolean literal) {
        this.literal = literal;
    }
}
