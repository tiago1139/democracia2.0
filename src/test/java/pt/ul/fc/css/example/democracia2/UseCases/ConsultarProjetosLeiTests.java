package pt.ul.fc.css.example.democracia2.UseCases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import pt.ul.fc.css.example.democracia2.entities.*;
import pt.ul.fc.css.example.democracia2.repositories.CidadaoRepository;
import pt.ul.fc.css.example.democracia2.repositories.ProjetoRepository;
import pt.ul.fc.css.example.democracia2.repositories.TemaRepository;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ConsultarProjetosLeiTests {

    @Autowired
    private ProjetoService projetoService;

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
    @AfterEach
    public void drop(){
        projetoRepository.deleteAll();
        cidadaoRepository.deleteAll();
        temaRepository.deleteAll();

    }

    @Test
    public void consultar_projetos_com_sucesso() {
        Tema t = new Tema("Geral",null);
        temaRepository.save(t);
        Delegado d = new Delegado("tomas","delegado",null,null);
        cidadaoRepository.save(d);
        Cidadao c = new Cidadao("manuel","tomas");
        cidadaoRepository.save(c);
        for(int i = 1;i < 3;i++){
            ProjetoLei pl = new ProjetoLei("titulo"+i,"descricao"+i, LocalDateTime.now().plusMonths(8),d,t);
            pl.setApoios(1L);
            projetoRepository.save(pl);
        }
        List<ProjetoLei> projetos = projetoService.getProjetosNaoExpirados();

        assertEquals(2, projetos.size());
        assertTrue(projetos.stream().filter(o -> o.getTitulo().equals("titulo1") &&
                o.getTema().getNome().equals("Geral") && o.getDescricao().equals("descricao1") &&
                o.getProponente().getNome().equals("tomas")).findFirst().isPresent());
        assertTrue(projetos.stream().filter(o -> o.getTitulo().equals("titulo2") &&
                o.getTema().getNome().equals("Geral") && o.getDescricao().equals("descricao2") &&
                o.getProponente().getNome().equals("tomas")).findFirst().isPresent());

    }


}
