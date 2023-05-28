package pt.ul.fc.css.example.democracia2.dto;

import java.time.LocalDateTime;

public class PropostaLeiDTO extends ProjetoLeiDTO{

    private Long votosFavoraveis;
    private Long votosDesfavoraveis;
    private Boolean aprovada;

    public  PropostaLeiDTO(){}

    public PropostaLeiDTO(Long id, Long apoios, String titulo, boolean fechado, String descricao, String pdfUrl, LocalDateTime validade, boolean passa, DelegadoDTO proponente, TemaDTO tema) {
        super(id, apoios, titulo, fechado, descricao, pdfUrl, validade, passa,proponente,tema);
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
}
