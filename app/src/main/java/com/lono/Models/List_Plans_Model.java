package com.lono.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class List_Plans_Model {
    int id_servico;
    String nome_servico;
    double valor_mensal;
    double valor_anual;
    double valor_termo;
    String desc1;
    String desc2;
    String desc3;
    String desc4;
    String desc5;
    String tipo;
    int num_termos;
    int num_jornais;
    int num_clinntes;
    int num_advogados;
    int num_usuarios;
    int dias_retroatividade;
    String sit_cad;
    String dat_cad;
    boolean contato_only;
    int show_order;
    double desconto_anual;
    int limite_min_termos;
    int limite_max_termos;

    public List_Plans_Model(JSONObject dados) {
        try{
            this.id_servico = dados.getInt("id_servico");
            this.nome_servico = dados.getString("nome_servico");
            this.valor_mensal = dados.getDouble("valor_mensal");
            this.valor_anual = dados.getDouble("valor_anual");
            this.valor_termo = dados.getDouble("valor_termo");
            this.desc1 = dados.getString("desc1");
            this.desc2 = dados.getString("desc2");
            this.desc3 = dados.getString("desc3");
            this.desc4 = dados.getString("desc4");
            this.desc5 = dados.getString("desc5");
            this.tipo = dados.getString("tipo");
            this.num_termos = dados.getInt("num_termos");
            this.num_jornais = dados.getInt("num_jornais");
            this.num_clinntes = dados.getInt("num_clientes");
            this.num_advogados = dados.getInt("num_advogados");
            this.num_usuarios = dados.getInt("num_usuarios");
            this.dias_retroatividade = dados.getInt("dias_retroatividade");
            this.sit_cad = dados.getString("sit_cad");
            this.dat_cad = dados.getString("dat_cad");
            this.contato_only = dados.getBoolean("contato_only");
            this.show_order = dados.getInt("show_order");
            this.desconto_anual = dados.getDouble("desconto_anual");
            this.limite_min_termos = dados.getInt("limite_min_termos");
            this.limite_max_termos = dados.getInt("limite_max_termos");
        }catch (JSONException e){}catch (NullPointerException e){}
    }

    public int getId_servico() {
        return id_servico;
    }

    public void setId_servico(int id_servico) {
        this.id_servico = id_servico;
    }

    public String getNome_servico() {
        return nome_servico;
    }

    public void setNome_servico(String nome_servico) {
        this.nome_servico = nome_servico;
    }

    public double getValor_mensal() {
        return valor_mensal;
    }

    public void setValor_mensal(double valor_mensal) {
        this.valor_mensal = valor_mensal;
    }

    public double getValor_anual() {
        return valor_anual;
    }

    public void setValor_anual(double valor_anual) {
        this.valor_anual = valor_anual;
    }

    public double getValor_termo() {
        return valor_termo;
    }

    public void setValor_termo(double valor_termo) {
        this.valor_termo = valor_termo;
    }

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }

    public String getDesc4() {
        return desc4;
    }

    public void setDesc4(String desc4) {
        this.desc4 = desc4;
    }

    public String getDesc5() {
        return desc5;
    }

    public void setDesc5(String desc5) {
        this.desc5 = desc5;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getNum_termos() {
        return num_termos;
    }

    public void setNum_termos(int num_termos) {
        this.num_termos = num_termos;
    }

    public int getNum_jornais() {
        return num_jornais;
    }

    public void setNum_jornais(int num_jornais) {
        this.num_jornais = num_jornais;
    }

    public int getNum_clinntes() {
        return num_clinntes;
    }

    public void setNum_clinntes(int num_clinntes) {
        this.num_clinntes = num_clinntes;
    }

    public int getNum_advogados() {
        return num_advogados;
    }

    public void setNum_advogados(int num_advogados) {
        this.num_advogados = num_advogados;
    }

    public int getNum_usuarios() {
        return num_usuarios;
    }

    public void setNum_usuarios(int num_usuarios) {
        this.num_usuarios = num_usuarios;
    }

    public int getDias_retroatividade() {
        return dias_retroatividade;
    }

    public void setDias_retroatividade(int dias_retroatividade) {
        this.dias_retroatividade = dias_retroatividade;
    }

    public String getSit_cad() {
        return sit_cad;
    }

    public void setSit_cad(String sit_cad) {
        this.sit_cad = sit_cad;
    }

    public String getDat_cad() {
        return dat_cad;
    }

    public void setDat_cad(String dat_cad) {
        this.dat_cad = dat_cad;
    }

    public boolean isContato_only() {
        return contato_only;
    }

    public void setContato_only(boolean contato_only) {
        this.contato_only = contato_only;
    }

    public int getShow_order() {
        return show_order;
    }

    public void setShow_order(int show_order) {
        this.show_order = show_order;
    }

    public double getDesconto_anual() {
        return desconto_anual;
    }

    public void setDesconto_anual(double desconto_anual) {
        this.desconto_anual = desconto_anual;
    }

    public int getLimite_min_termos() {
        return limite_min_termos;
    }

    public void setLimite_min_termos(int limite_min_termos) {
        this.limite_min_termos = limite_min_termos;
    }

    public int getLimite_max_termos() {
        return limite_max_termos;
    }

    public void setLimite_max_termos(int limite_max_termos) {
        this.limite_max_termos = limite_max_termos;
    }
}
