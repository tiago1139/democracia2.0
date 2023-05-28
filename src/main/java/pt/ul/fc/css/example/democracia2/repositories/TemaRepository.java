package pt.ul.fc.css.example.democracia2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ul.fc.css.example.democracia2.entities.ProjetoLei;
import pt.ul.fc.css.example.democracia2.entities.Tema;

@Repository
public interface TemaRepository extends JpaRepository<Tema,Long> {


}
