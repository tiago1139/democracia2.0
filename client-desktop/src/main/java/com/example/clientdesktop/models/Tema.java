package com.example.clientdesktop.models;

import java.util.List;

public class Tema {

    private Long id;
    private String nome;
    private List<Long> subtemas;
    private Long temaPai;

    public Tema(){}

    public Tema(Long id, String nome, List<Long> subtemas, Long temaPai) {
        this.id = id;
        this.nome = nome;
        this.subtemas = subtemas;
        this.temaPai = temaPai;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Long> getSubtemas() {
        return subtemas;
    }

    public void setSubtemas(List<Long> subtemas) {
        this.subtemas = subtemas;
    }

    public Long getTemaPai() {
        return temaPai;
    }

    public void setTemaPai(Long temaPai) {
        this.temaPai = temaPai;
    }
}
