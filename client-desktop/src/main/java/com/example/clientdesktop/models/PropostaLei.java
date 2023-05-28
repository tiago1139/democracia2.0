package com.example.clientdesktop.models;

import java.time.LocalDateTime;

public class PropostaLei extends ProjetoLei {

    private Long votosFavoraveis;
    private Long votosDesfavoraveis;
    private Boolean aprovada;

    public PropostaLei(){}

    public PropostaLei(Long id, Long apoios, String titulo, boolean fechado, String descricao, String pdfUrl, LocalDateTime validade, boolean passa, Delegado proponente, Tema tema) {
        super(id, apoios, titulo, fechado, descricao, pdfUrl, validade, passa,proponente, tema);
    }

    public Long getVotosFavoraveis() {
        return votosFavoraveis;
    }

    public void setVotosFavoraveis(Long votosFavoraveis) {
        this.votosFavoraveis = votosFavoraveis;
    }

    public Long getVotosDesfavoraveis() {
        return votosDesfavoraveis;
    }

    public void setVotosDesfavoraveis(Long votosDesfavoraveis) {
        this.votosDesfavoraveis = votosDesfavoraveis;
    }

    public Boolean getAprovada() {
        return aprovada;
    }

    public void setAprovada(Boolean aprovada) {
        this.aprovada = aprovada;
    }

    @Override
    public String toString() {
        return "PropostaLei{" +
                "votosFavoraveis=" + votosFavoraveis +
                ", votosDesfavoraveis=" + votosDesfavoraveis +
                ", aprovada=" + aprovada +
                '}';
    }
}
