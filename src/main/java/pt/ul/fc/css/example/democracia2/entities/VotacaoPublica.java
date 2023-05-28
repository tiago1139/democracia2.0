package pt.ul.fc.css.example.democracia2.entities;

import jakarta.persistence.*;


@Entity
public class VotacaoPublica {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Delegado delegado;

	@ManyToOne
	private PropostaLei propostaLei;

	@Enumerated(EnumType.STRING)
	private Voto voto;


	public VotacaoPublica(){

	}

	public VotacaoPublica(Delegado d, Voto voto, PropostaLei proposta){
		this.delegado = d;
		this.propostaLei = proposta;
		setVoto(voto);
	}



	public Long getId() {
		return id;
	}


	public Delegado getDelegado() {
		return delegado;
	}

	public void setDelegado(Delegado delegado) {
		this.delegado = delegado;
	}

	public PropostaLei getPropostaLei() {
		return this.propostaLei;
	}

	public void setPropostaLei(PropostaLei propostaLei) {
		this.propostaLei = propostaLei;
	}

	public Voto getVoto() {
		return voto;
	}

	public void setVoto(Voto voto) {
		this.voto = voto;
	}


}
