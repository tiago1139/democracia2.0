package pt.ul.fc.css.example.democracia2.dto;

import java.time.LocalDateTime;

public class ProjetoFormDTO {
    private String titulo;

    private String descricao;

    private LocalDateTime validade;

    private Long delegado;

    private Long tema;

    public ProjetoFormDTO(){}

    public ProjetoFormDTO(String titulo, String descricao, LocalDateTime validade, Long delegado, Long tema) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.validade = validade;
        this.delegado = delegado;
        this.tema = tema;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getValidade() {
        return validade;
    }

    public void setValidade(LocalDateTime validade) {
        this.validade = validade;
    }

    public Long getDelegado() {
        return delegado;
    }

    public void setDelegado(Long delegado) {
        this.delegado = delegado;
    }

    public Long getTema() {
        return tema;
    }

    public void setTema(Long tema) {
        this.tema = tema;
    }
}
