package pt.ul.fc.css.example.democracia2.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import pt.ul.fc.css.example.democracia2.entities.Cidadao;
import pt.ul.fc.css.example.democracia2.entities.Delegado;
import pt.ul.fc.css.example.democracia2.entities.PropostaLei;
import pt.ul.fc.css.example.democracia2.entities.VotacaoPublica;

@Repository
public interface CidadaoRepository<T extends Cidadao> extends JpaRepository<T, Long> {

    @Query("SELECT a FROM Cidadao a WHERE a.nome LIKE %:q% OR a.apelido LIKE %:q% ")
    List<T> findByName(@Param("q") String q);


    @Query("SELECT c FROM Cidadao c JOIN c.projVotados  pv WHERE pv.id = ?1")
    List<T> findCidadaosComVoto(Long projId);

    @Query("SELECT c FROM Cidadao c JOIN c.projApoiados pv WHERE c.id = ?1 AND pv.id = ?2")
    Cidadao findCidadaosSemApoio(Long cidID,Long projId);


    @Query("SELECT d FROM Delegado d WHERE d.id = ?1")
    Delegado findDelegadobyID(long idCidadao);

    @Query("SELECT d FROM Delegado d")
    List<Delegado> findAllDelegados();
}

