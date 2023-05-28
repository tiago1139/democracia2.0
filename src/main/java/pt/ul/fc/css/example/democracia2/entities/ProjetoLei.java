package pt.ul.fc.css.example.democracia2.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public class ProjetoLei {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column
	private Long apoios;
	
	@NonNull
	@Column
	private String titulo;

	private boolean fechado;

	@Column(insertable = false,updatable = false)
	private String dtype;
	
	@NonNull
	@Column
	private String descricao;

	@OneToOne
	@JsonBackReference
	private PDF pdf;


	@NonNull
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime validade;

	@Column
	private boolean passa = true;


	@ManyToOne
	@JsonBackReference(value="delegado-projeto")
	@JsonIgnoreProperties({"projetosLei", "votacoes", "projApoiados", "projVotados"})
	private Delegado proponente;

	@ManyToOne
	@NonNull
	@JsonProperty("tema")
	private Tema tema;
	
	public ProjetoLei() {}

	public ProjetoLei(@NonNull String titulo, @NonNull String descricao, @NonNull LocalDateTime validade,
					  Delegado proponente, Tema tema) {
		this.titulo = titulo;
		this.descricao = descricao;
		this.validade = validade;
		this.proponente = proponente;
		this.fechado = false;
		this.tema = tema;
		this.passa = true;

		this.apoios = 1L;
	}


	public boolean verificaValidade(){
		if(validade.isAfter(LocalDateTime.now())){
			return true;
		}else{
			return false;
		}
	}

	public void fechaProjeto(){
		this.fechado = true;
	}

	public boolean adicionaApoio(){
		if(!fechado){
			this.apoios += 1;
		}
		return !fechado;
	}


	public Long getApoios() {
		return apoios;
	}

	public void setApoios(Long apoios) {
		this.apoios = apoios;
	}

	@NonNull
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(@NonNull String titulo) {
		this.titulo = titulo;
	}

	@NonNull
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(@NonNull String descricao) {
		this.descricao = descricao;
	}

	@NonNull
	public LocalDateTime getValidade() {
		return validade;
	}

	public void setValidade(@NonNull LocalDateTime validade) {
		this.validade = validade;
	}

	public Delegado getProponente() {
		return proponente;
	}

	public void setProponente(Delegado proponente) {
		this.proponente = proponente;
	}

	public Tema getTema() {
		return tema;
	}

	public void setTema(Tema tema) {
		this.tema = tema;
	}

	@NonNull
	public PDF getPdf() {
		return pdf;
	}

	public void setPdf(@NonNull PDF pdf) {
		this.pdf = pdf;
	}

	public Long getId(){
		return id;
	}

	public boolean getFechado(){
		return fechado;
	}

	public void setFechado(boolean fechado){
		this.fechado = fechado;
	}

	@Override
	public String toString() {
		return "ProjetoLei{" +
				"id=" + id +
				", apoios=" + apoios +
				", titulo='" + titulo + '\'' +
				", descricao='" + descricao + '\'' +
				", validade=" + validade +
				", proponente=" + proponente +
				", tema=" + tema +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProjetoLei that = (ProjetoLei) o;
		return fechado == that.fechado && Objects.equals(id, that.id) && Objects.equals(apoios, that.apoios) && Objects.equals(titulo, that.titulo) && Objects.equals(dtype, that.dtype) && Objects.equals(descricao, that.descricao) && Objects.equals(validade, that.validade) && Objects.equals(proponente, that.proponente) && Objects.equals(tema, that.tema);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, apoios, titulo, fechado, dtype, descricao, validade, proponente, tema);
	}

	public boolean verificaApoios() {
		return this.apoios >= 10000L && !fechado;
	}
}
