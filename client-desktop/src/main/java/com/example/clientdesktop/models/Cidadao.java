package com.example.clientdesktop.models;

import java.util.List;
import java.util.Map;

public class Cidadao {

    private Long id;
    private String nome;
    private String apelido;
    private List<ProjetoLei> projApoiados;
    private List<PropostaLei> projVotados;
    private Map<Long, Long> delegados;

    public Cidadao(){}

    public Cidadao(Long id, String nome, String apelido, List<ProjetoLei> projApoiados, List<PropostaLei> projVotados, Map<Long, Long> delegados) {
        this.id = id;
        this.nome = nome;
        this.apelido = apelido;
        this.projApoiados = projApoiados;
        this.projVotados = projVotados;
        this.delegados = delegados;
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

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public List<ProjetoLei> getProjApoiados() {
        return projApoiados;
    }

    public void setProjApoiados(List<ProjetoLei> projApoiados) {
        this.projApoiados = projApoiados;
    }

    public List<PropostaLei> getProjVotados() {
        return projVotados;
    }

    public void setProjVotados(List<PropostaLei> projVotados) {
        this.projVotados = projVotados;
    }

    public Map<Long, Long> getDelegados() {
        return delegados;
    }

    public void setDelegados(Map<Long, Long> delegados) {
        this.delegados = delegados;
    }

    @Override
    public String toString() {
        return nome + " "+ apelido;
    }
}
