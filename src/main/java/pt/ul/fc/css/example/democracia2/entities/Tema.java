package pt.ul.fc.css.example.democracia2.entities;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

@Entity
public class Tema {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NonNull
	private String nome;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<Tema> subtemas;

	@ManyToOne
	private Tema temaPai;

	public Tema() {}

	public Tema(@NonNull String nome, List<Tema> subtemas) {
		this.nome = nome;
		this.subtemas = subtemas;
	}
	public Tema getTemaPai() {
		return temaPai;
	}

	public void setTemaPai(Tema temaPai) {
		this.temaPai = temaPai;
	}



	public Long getId() {
		return id;
	}


	@NonNull
	public String getNome() {
		return nome;
	}

	public void setNome(@NonNull String nome) {
		this.nome = nome;
	}

	public List<Tema> getSubtemas() {
		return subtemas;
	}

	public void setSubtemas(List<Tema> subtemas) {
		this.subtemas = subtemas;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Tema tema = (Tema) o;
		return Objects.equals(id, tema.id) && Objects.equals(nome, tema.nome);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, nome);
	}

	@Override
	public String toString() {
		return "Tema{" +
				"id=" + id +
				", nome='" + nome + '\'' +
				", subtemas=" + subtemas +
				'}';
	}
}
