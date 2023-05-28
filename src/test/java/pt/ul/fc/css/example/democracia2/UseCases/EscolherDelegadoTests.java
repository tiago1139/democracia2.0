package pt.ul.fc.css.example.democracia2.UseCases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import pt.ul.fc.css.example.democracia2.entities.*;

import pt.ul.fc.css.example.democracia2.entities.PropostaLei;
import pt.ul.fc.css.example.democracia2.repositories.CidadaoRepository;
import pt.ul.fc.css.example.democracia2.repositories.ProjetoRepository;
import pt.ul.fc.css.example.democracia2.repositories.TemaRepository;
import pt.ul.fc.css.example.democracia2.services.CidadaoService;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.util.List;

//import static org.junit.jupiter.api.AssertThrows.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class EscolherDelegadoTests {

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private CidadaoService cidadaoService;

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private CidadaoRepository cidadaoRepository;
    @BeforeEach
    public void dropBefore() {
        projetoRepository.deleteAll();
        cidadaoRepository.deleteAll();
        temaRepository.deleteAll();
    }




    @Test
    public void escolhe_delegado_com_sucesso() {
        Cidadao c = new Cidadao("Tiago", "Pinto");

        Delegado d = new Delegado("Alberto", "Pinto", null, null);

        Tema t = new Tema("Tema", null);

        temaRepository.save(t);
        cidadaoRepository.save(c);
        cidadaoRepository.save(d);


        Cidadao c1 = cidadaoService.escolheDelegado(c.getId(), t.getId(), d.getId());

        assertEquals(d, c1.getDelegados().get(t));
    }


    @Test
    public void substituir_delegado(){
        Cidadao c2 = new Cidadao("Vasco", "Pinto");

        Delegado d1 = new Delegado("Jose", "Pinto", null, null);
        Delegado d2 = new Delegado("Xavier", "Pinto", null, null);

        Tema t1 = new Tema("Saude", null);

        temaRepository.save(t1);
        cidadaoRepository.save(c2);
        cidadaoRepository.save(d1);
        cidadaoRepository.save(d2);


        Cidadao c1 = cidadaoService.escolheDelegado(c2.getId(), t1.getId(), d1.getId());
        Delegado dTema = c1.getDelegados().get(t1);
        assertEquals(d1,dTema);
        c1 = cidadaoService.escolheDelegado(c2.getId(), t1.getId(), d2.getId());
        dTema = c1.getDelegados().get(t1);

        assertEquals(d2,dTema);



    }

}
