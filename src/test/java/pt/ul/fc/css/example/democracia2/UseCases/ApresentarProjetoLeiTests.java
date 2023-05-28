package pt.ul.fc.css.example.democracia2.UseCases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import pt.ul.fc.css.example.democracia2.entities.Delegado;
import pt.ul.fc.css.example.democracia2.entities.ProjetoLei;
import pt.ul.fc.css.example.democracia2.entities.PropostaLei;
import pt.ul.fc.css.example.democracia2.entities.Tema;
import pt.ul.fc.css.example.democracia2.repositories.CidadaoRepository;
import pt.ul.fc.css.example.democracia2.repositories.ProjetoRepository;
import pt.ul.fc.css.example.democracia2.repositories.TemaRepository;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApresentarProjetoLeiTests {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @BeforeEach
    public void drop(){
        projetoRepository.deleteAll();
        cidadaoRepository.deleteAll();
        temaRepository.deleteAll();

    }

    @Test
    public void guarda_projeto_com_sucesso(){
        Tema t = new Tema("Geral",null);
        Delegado d = new Delegado("Tomas","Delegado",null,null);
        cidadaoRepository.save(d);
        temaRepository.save(t);
        ProjetoLei pl = new ProjetoLei("Projeto1", "Descricao do projeto1", LocalDateTime.of(2023,10,10,20,10),d,t);
        ProjetoLei projGuardado = projetoService.apresentarProjetoLei(pl);

        assertNotEquals(null, projGuardado);
        assertEquals(pl, projGuardado);
    }


    @Test
    public void altera_data_validade_projeto_criado(){
        Tema t = new Tema("Geral",null);
        Delegado d = new Delegado("Tomas","Delegado",null,null);
        cidadaoRepository.save(d);
        temaRepository.save(t);
        LocalDateTime validadeMaiorQueOLimite = LocalDateTime.of(2025,10,10,20,10);
        ProjetoLei pl = new ProjetoLei("Projeto1", "Descricao do projeto1",validadeMaiorQueOLimite ,d,t);
        ProjetoLei projGuardado = projetoService.apresentarProjetoLei(pl);

        assertNotEquals(null, projGuardado);
        assertNotEquals(validadeMaiorQueOLimite,projGuardado.getValidade());
    }
}
