package pt.ul.fc.css.example.democracia2.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.*;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Cidadao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty("id")
    private Long id;

    @NonNull
    @JsonProperty("nome")
    private String nome;

    @NonNull
    @JsonProperty("apelido")
    private String apelido;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty("projApoiados")
    private List<ProjetoLei> projApoiados;


    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty("projVotados")
    private List<PropostaLei> projVotados;

    /**
     * Ideia retirada do seguinte site:
     * https://www.baeldung.com/hibernate-persisting-maps
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cidadao_tema_delegado", joinColumns = {
            @JoinColumn(name = "cidadao_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "delegado_id", referencedColumnName = "id") })
    @MapKeyJoinColumn(name = "tema_id")
    private Map<Tema,Delegado> delegados;

    public Cidadao(@NonNull String nome, @NonNull String apelido) {
        this.nome = nome;
        this.apelido = apelido;
        this.projApoiados = new ArrayList<ProjetoLei>();
        this.projVotados = new ArrayList<PropostaLei>();
        this.delegados = new HashMap<Tema, Delegado>();
    }

    public Cidadao() {}

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getApelido() {
        return apelido;
    }

    @JsonIgnore
    public List<ProjetoLei> getProjApoiados() {
        return projApoiados;
    }

    public void setProjApoiados(List<ProjetoLei> projApoiados) {
        this.projApoiados = projApoiados;
    }

    @JsonIgnore
    public List<PropostaLei> getProjVotados() {
        return projVotados;
    }

    public void setProjVotados(List<PropostaLei> projVotados) {
        this.projVotados = projVotados;
    }

    @JsonIgnore
    public Map<Tema, Delegado> getDelegados() {
        return delegados;
    }

    public void setDelegados(Map<Tema, Delegado> delegados) {
        this.delegados = delegados;
    }

    public void addProjetoapoiado(ProjetoLei projeto) {
        this.projApoiados.add(projeto);
    }

    public void addPropostavotada(PropostaLei proposta) {
        this.projVotados.add(proposta);
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    public void setApelido(@NonNull String apelido) {
        this.apelido = apelido;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (Cidadao) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.nome, that.nome) &&
                Objects.equals(this.apelido, that.apelido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, apelido);
    }

    @Override
    public String toString() {
        return getNome();
    }


}
