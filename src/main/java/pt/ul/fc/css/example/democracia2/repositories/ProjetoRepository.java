package pt.ul.fc.css.example.democracia2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.example.democracia2.entities.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjetoRepository<T extends ProjetoLei> extends JpaRepository<T,Long> {

  /**  @Query("SELECT a FROM Cidadao a WHERE a.name LIKE %:q% OR a.surname LIKE %:q% ")
    List<ProjetoLei> findByName(@Param("q") String q);*/

  @Query("SELECT pl FROM ProjetoLei pl WHERE pl.dtype=ProjetoLei AND pl.fechado =false")
  List<T> findProjetosNaoExpirados();

  @Modifying
  @Query("UPDATE ProjetoLei pl SET pl.fechado = true WHERE pl.validade < :now")
  void fechaPropostasExpiradas(LocalDateTime now);

  @Query("SELECT pl FROM ProjetoLei pl WHERE pl.titulo=?1 AND pl.proponente =?2")
  Optional<T> findByTituloAndProponente(String t, Delegado d);

  @Query("SELECT pl FROM ProjetoLei pl WHERE pl.dtype=PropostaLei AND pl.fechado =false")
  List<PropostaLei> findAllActivePropostas();

  @Query("SELECT pl FROM ProjetoLei pl WHERE pl.dtype=PropostaLei AND pl.validade < :now")
  List<PropostaLei> findPropostasExpiradas(LocalDateTime now);

  @Query("SELECT tm FROM PropostaLei pl JOIN Tema tm WHERE pl.id= ?1")
  Tema findTemabyProposta(Long proposta_id);

  @Query("SELECT pl  FROM PropostaLei pl WHERE pl.id = ?1")
  Optional<PropostaLei> findPropostabyId(long idProposta);
}
