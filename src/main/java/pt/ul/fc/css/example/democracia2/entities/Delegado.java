package pt.ul.fc.css.example.democracia2.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity

public class Delegado extends Cidadao {

	@OneToMany(mappedBy = "proponente", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonManagedReference(value="delegado-projeto")
	private List<ProjetoLei> projetosLei;

	@OneToMany(mappedBy = "delegado", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonManagedReference(value = "delegado-votacoes")
	@JsonIgnore
	private List<VotacaoPublica> votacoes;

	public Delegado() {}

	public Delegado(String nome, String apelido, List<ProjetoLei> projetosLei, List<VotacaoPublica> votacoes) {
		super(nome, apelido);
		this.projetosLei = projetosLei;
		this.votacoes = votacoes;
	}

	@JsonIgnore
	public List<ProjetoLei> getProjetosLei() {
		return projetosLei;
	}

	public void setProjetosLei(List<ProjetoLei> projetosLei) {
		this.projetosLei = projetosLei;
	}

	@JsonIgnore
	public List<VotacaoPublica> getVotacoes() {
		return votacoes;
	}

	public void setVotacoes(List<VotacaoPublica> votacoes) {
		this.votacoes = votacoes;
	}

	public void adicionaVotacaoPublica(VotacaoPublica votacaoPublica) {
		this.votacoes.add(votacaoPublica);
	}

	@Override
	public String toString() {
		return getNome();
	}


}
