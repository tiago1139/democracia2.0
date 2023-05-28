package com.example.clientdesktop.models;

public class VotacaoPublica {


    private Long id;
    private Long delegadoId;
    private Long propostaLeiId;
    private String voto;

    public VotacaoPublica(){}

    public VotacaoPublica(Long id, Long delegadoId, Long propostaLeiId, String voto) {
        this.id = id;
        this.delegadoId = delegadoId;
        this.propostaLeiId = propostaLeiId;
        this.voto = voto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDelegadoId() {
        return delegadoId;
    }

    public void setDelegadoId(Long delegadoId) {
        this.delegadoId = delegadoId;
    }

    public Long getPropostaLeiId() {
        return propostaLeiId;
    }

    public void setPropostaLeiId(Long propostaLeiId) {
        this.propostaLeiId = propostaLeiId;
    }

    public String getVoto() {
        return voto;
    }

    public void setVoto(String voto) {
        this.voto = voto;
    }
}
