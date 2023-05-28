package pt.ul.fc.css.example.democracia2.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Esta entidade foi pensada com base na leitura da seguinte pagina:
 * https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/
 */

@Entity
public class PDF {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String nomeFicheiro;

    @NonNull
    private String tipoFicheiro;
    @Lob
    @NonNull
    private byte[] conteudo;

    public PDF(){}

    public PDF(String nomeFicheiro, String tipoFicheiro, byte[] conteudo) {
        this.nomeFicheiro = nomeFicheiro;
        this.tipoFicheiro = tipoFicheiro;
        this.conteudo = conteudo;
    }

    public Long getId() {
        return id;
    }

    public String getNomeFicheiro() {
        return nomeFicheiro;
    }

    public void setNomeFicheiro(String nomeFicheiro) {
        this.nomeFicheiro = nomeFicheiro;
    }

    public String getTipoFicheiro() {
        return tipoFicheiro;
    }

    public void setTipoFicheiro(String tipoFicheiro) {
        this.tipoFicheiro = tipoFicheiro;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PDF pdf = (PDF) o;
        return Objects.equals(id, pdf.id) && Objects.equals(nomeFicheiro, pdf.nomeFicheiro) && Objects.equals(tipoFicheiro, pdf.tipoFicheiro) && Arrays.equals(conteudo, pdf.conteudo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, nomeFicheiro, tipoFicheiro);
        result = 31 * result + Arrays.hashCode(conteudo);
        return result;
    }

    @Override
    public String toString() {
        return "PDF{" +
                "id=" + id +
                ", nomeFicheiro='" + nomeFicheiro + '\'' +
                ", tipoFicheiro='" + tipoFicheiro + '\'' +
                '}';
    }
}
