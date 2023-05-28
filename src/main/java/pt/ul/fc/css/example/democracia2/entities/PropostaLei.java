package pt.ul.fc.css.example.democracia2.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Entity
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id")
public class PropostaLei extends ProjetoLei {

	private Long votosFavoraveis;

	private Long votosDesfavoraveis;

	private Boolean aprovada;

	public PropostaLei(){

	}

	public PropostaLei(ProjetoLei pl, LocalDateTime validade) {
		super(pl.getTitulo(),pl.getDescricao(),validade,pl.getProponente(),pl.getTema());
		this.setPdf(pl.getPdf());
		this.setApoios(pl.getApoios());
		this.setVotos();
	}

	public PropostaLei(@NonNull String titulo, @NonNull String descricao, @NonNull LocalDateTime validade,
					   Delegado proponente, Tema tema) {
		super(titulo, descricao, validade, proponente, tema);
		this.setVotos();
	}

	public Long getVotosFavoraveis() {
		return votosFavoraveis;
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

	public void setVotosFavoraveis(Long votosFavoraveis) {
		this.votosFavoraveis = votosFavoraveis;
	}





	private void setVotos() {
		this.votosDesfavoraveis = 0L;
		this.votosFavoraveis = 0L;
	}


	public boolean aumentaVotos(Long l, boolean favoravel){
		boolean b = !this.getFechado();

		if(b){
			if(favoravel){
				this.votosFavoraveis += l;
			}else{
				this.votosDesfavoraveis += l;
			}
		}
		return b;
	}

}
