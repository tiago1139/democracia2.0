package pt.ul.fc.css.example.democracia2.dto;

public class CidadaoDTO {

    private Long id;
    private String nome;
    private String apelido;

    public CidadaoDTO(){}

    public CidadaoDTO(Long id, String nome, String apelido) {
        this.id = id;
        this.nome = nome;
        this.apelido = apelido;
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

}
