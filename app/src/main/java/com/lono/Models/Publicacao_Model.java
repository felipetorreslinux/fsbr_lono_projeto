package com.lono.Models;

public class Publicacao_Model {

    int id_cliente;
    boolean materia_conferida;
    String sit_cad;
    boolean percentual;
    String termo_percentual;
    String pagina;
    boolean exportado_email;
    boolean sincronizado_android;
    boolean sincronizado_iphone;
    boolean exportado_pdf;

    int id_publicacao;
    int id_materia;
    String titulo_materia;
    String subtitulo;
    String pre_materia;
    String processo;
    String materia;

    String materia_hash;
    boolean corte_lono;
    String dt_divulgacao;
    String dt_publicacao;
    String edicao_publicacao;

    int id_jornal;
    String status_publicacao;
    String nome_pesquisa;
    boolean literal;
    boolean oab;

    String id_pauta;
    String pagina_pauta;
    String pauta;

    String nome_jornal;
    String sigla_jornal;
    String nome_orgao;
    String sigla_orgao;

    public Publicacao_Model(int id_cliente, boolean materia_conferida, String sit_cad, boolean percentual, String termo_percentual, String pagina, boolean exportado_email, boolean sincronizado_android, boolean sincronizado_iphone, boolean exportado_pdf, int id_publicacao, int id_materia, String titulo_materia, String subtitulo, String pre_materia, String processo, String materia, String materia_hash, boolean corte_lono, String dt_divulgacao, String dt_publicacao, String edicao_publicacao, int id_jornal, String status_publicacao, String nome_pesquisa, boolean literal, boolean oab, String id_pauta, String pagina_pauta, String pauta, String nome_jornal, String sigla_jornal, String nome_orgao, String sigla_orgao) {
        this.id_cliente = id_cliente;
        this.materia_conferida = materia_conferida;
        this.sit_cad = sit_cad;
        this.percentual = percentual;
        this.termo_percentual = termo_percentual;
        this.pagina = pagina;
        this.exportado_email = exportado_email;
        this.sincronizado_android = sincronizado_android;
        this.sincronizado_iphone = sincronizado_iphone;
        this.exportado_pdf = exportado_pdf;
        this.id_publicacao = id_publicacao;
        this.id_materia = id_materia;
        this.titulo_materia = titulo_materia;
        this.subtitulo = subtitulo;
        this.pre_materia = pre_materia;
        this.processo = processo;
        this.materia = materia;
        this.materia_hash = materia_hash;
        this.corte_lono = corte_lono;
        this.dt_divulgacao = dt_divulgacao;
        this.dt_publicacao = dt_publicacao;
        this.edicao_publicacao = edicao_publicacao;
        this.id_jornal = id_jornal;
        this.status_publicacao = status_publicacao;
        this.nome_pesquisa = nome_pesquisa;
        this.literal = literal;
        this.oab = oab;
        this.id_pauta = id_pauta;
        this.pagina_pauta = pagina_pauta;
        this.pauta = pauta;
        this.nome_jornal = nome_jornal;
        this.sigla_jornal = sigla_jornal;
        this.nome_orgao = nome_orgao;
        this.sigla_orgao = sigla_orgao;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public boolean isMateria_conferida() {
        return materia_conferida;
    }

    public void setMateria_conferida(boolean materia_conferida) {
        this.materia_conferida = materia_conferida;
    }

    public String getSit_cad() {
        return sit_cad;
    }

    public void setSit_cad(String sit_cad) {
        this.sit_cad = sit_cad;
    }

    public boolean isPercentual() {
        return percentual;
    }

    public void setPercentual(boolean percentual) {
        this.percentual = percentual;
    }

    public String getTermo_percentual() {
        return termo_percentual;
    }

    public void setTermo_percentual(String termo_percentual) {
        this.termo_percentual = termo_percentual;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public boolean isExportado_email() {
        return exportado_email;
    }

    public void setExportado_email(boolean exportado_email) {
        this.exportado_email = exportado_email;
    }

    public boolean isSincronizado_android() {
        return sincronizado_android;
    }

    public void setSincronizado_android(boolean sincronizado_android) {
        this.sincronizado_android = sincronizado_android;
    }

    public boolean isSincronizado_iphone() {
        return sincronizado_iphone;
    }

    public void setSincronizado_iphone(boolean sincronizado_iphone) {
        this.sincronizado_iphone = sincronizado_iphone;
    }

    public boolean isExportado_pdf() {
        return exportado_pdf;
    }

    public void setExportado_pdf(boolean exportado_pdf) {
        this.exportado_pdf = exportado_pdf;
    }

    public int getId_publicacao() {
        return id_publicacao;
    }

    public void setId_publicacao(int id_publicacao) {
        this.id_publicacao = id_publicacao;
    }

    public int getId_materia() {
        return id_materia;
    }

    public void setId_materia(int id_materia) {
        this.id_materia = id_materia;
    }

    public String getTitulo_materia() {
        return titulo_materia;
    }

    public void setTitulo_materia(String titulo_materia) {
        this.titulo_materia = titulo_materia;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getPre_materia() {
        return pre_materia;
    }

    public void setPre_materia(String pre_materia) {
        this.pre_materia = pre_materia;
    }

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getMateria_hash() {
        return materia_hash;
    }

    public void setMateria_hash(String materia_hash) {
        this.materia_hash = materia_hash;
    }

    public boolean isCorte_lono() {
        return corte_lono;
    }

    public void setCorte_lono(boolean corte_lono) {
        this.corte_lono = corte_lono;
    }

    public String getDt_divulgacao() {
        return dt_divulgacao;
    }

    public void setDt_divulgacao(String dt_divulgacao) {
        this.dt_divulgacao = dt_divulgacao;
    }

    public String getDt_publicacao() {
        return dt_publicacao;
    }

    public void setDt_publicacao(String dt_publicacao) {
        this.dt_publicacao = dt_publicacao;
    }

    public String getEdicao_publicacao() {
        return edicao_publicacao;
    }

    public void setEdicao_publicacao(String edicao_publicacao) {
        this.edicao_publicacao = edicao_publicacao;
    }

    public int getId_jornal() {
        return id_jornal;
    }

    public void setId_jornal(int id_jornal) {
        this.id_jornal = id_jornal;
    }

    public String getStatus_publicacao() {
        return status_publicacao;
    }

    public void setStatus_publicacao(String status_publicacao) {
        this.status_publicacao = status_publicacao;
    }

    public String getNome_pesquisa() {
        return nome_pesquisa;
    }

    public void setNome_pesquisa(String nome_pesquisa) {
        this.nome_pesquisa = nome_pesquisa;
    }

    public boolean isLiteral() {
        return literal;
    }

    public void setLiteral(boolean literal) {
        this.literal = literal;
    }

    public boolean isOab() {
        return oab;
    }

    public void setOab(boolean oab) {
        this.oab = oab;
    }

    public String getId_pauta() {
        return id_pauta;
    }

    public void setId_pauta(String id_pauta) {
        this.id_pauta = id_pauta;
    }

    public String getPagina_pauta() {
        return pagina_pauta;
    }

    public void setPagina_pauta(String pagina_pauta) {
        this.pagina_pauta = pagina_pauta;
    }

    public String getPauta() {
        return pauta;
    }

    public void setPauta(String pauta) {
        this.pauta = pauta;
    }

    public String getNome_jornal() {
        return nome_jornal;
    }

    public void setNome_jornal(String nome_jornal) {
        this.nome_jornal = nome_jornal;
    }

    public String getSigla_jornal() {
        return sigla_jornal;
    }

    public void setSigla_jornal(String sigla_jornal) {
        this.sigla_jornal = sigla_jornal;
    }

    public String getNome_orgao() {
        return nome_orgao;
    }

    public void setNome_orgao(String nome_orgao) {
        this.nome_orgao = nome_orgao;
    }

    public String getSigla_orgao() {
        return sigla_orgao;
    }

    public void setSigla_orgao(String sigla_orgao) {
        this.sigla_orgao = sigla_orgao;
    }
}
