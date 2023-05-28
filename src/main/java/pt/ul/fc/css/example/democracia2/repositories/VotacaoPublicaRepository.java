package pt.ul.fc.css.example.democracia2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.example.democracia2.entities.VotacaoPublica;

@Repository
public interface VotacaoPublicaRepository extends JpaRepository<VotacaoPublica,Long> {

    @Query("SELECT vp FROM VotacaoPublica vp WHERE vp.delegado.id = ?1 AND vp.propostaLei.id = ?2")
    VotacaoPublica findVotobyDelegadoAndProjeto(Long delegadoId, Long propostaId);
}
