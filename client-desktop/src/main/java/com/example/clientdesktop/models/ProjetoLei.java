package com.example.clientdesktop.models;

import java.time.LocalDateTime;

public class ProjetoLei {

    private Long id;
    private Long apoios;
    private String titulo;
    private boolean fechado;
    private String descricao;
    private String pdfUrl;
    private LocalDateTime validade;
    private boolean passa;
    private Delegado proponente;

    private Tema tema;



    public ProjetoLei(){}

    public ProjetoLei(Long id, Long apoios, String titulo, boolean fechado, String descricao, String pdfUrl, LocalDateTime validade, boolean passa,Delegado proponente,Tema tema) {
        this.id = id;
        this.apoios = apoios;
        this.titulo = titulo;
        this.fechado = fechado;
        this.descricao = descricao;
        this.pdfUrl = pdfUrl;
        this.validade = validade;
        this.passa = passa;
        this.tema = tema;
        this.proponente = proponente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApoios() {
        return apoios;
    }

    public void setApoios(Long apoios) {
        this.apoios = apoios;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isFechado() {
        return fechado;
    }

    public void setFechado(boolean fechado) {
        this.fechado = fechado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public LocalDateTime getValidade() {
        return validade;
    }

    public void setValidade(LocalDateTime validade) {
        this.validade = validade;
    }

    public boolean isPassa() {
        return passa;
    }

    public void setPassa(boolean passa) {
        this.passa = passa;
    }

    public Delegado getProponente() {
        return proponente;
    }

    public void setProponente(Delegado proponente) {
        this.proponente = proponente;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
    }
}
