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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ListarVotacoesEmCursoTests {

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
    public void encontra_duas_votacoes_em_curso_com_sucesso(){
        Tema t = new Tema("Geral",null);
        temaRepository.save(t);
        Delegado d = new Delegado("tomas","delegado",null,null);
        cidadaoRepository.save(d);
        for(int i = 1;i < 3;i++){
            PropostaLei pl = new PropostaLei("titulo"+i,"descricao"+i, LocalDateTime.now().plusMonths(2),d,t);
            projetoRepository.save(pl);
        }
        PropostaLei pl = new PropostaLei("titulo3","descricao3", LocalDateTime.now().minusMonths(2),d,t);
        pl.setFechado(true);
        projetoRepository.save(pl);

        List<PropostaLei> propostasAtivas = projetoService.getVotacoesEmCurso();

        assertEquals(2, propostasAtivas.size());
        assertTrue(propostasAtivas.stream().filter(o -> o.getTitulo().equals("titulo1")).findFirst().isPresent());
        assertTrue(propostasAtivas.stream().filter(o -> o.getTitulo().equals("titulo2")).findFirst().isPresent());
    }

    @Test
    public void nao_encontra_votacoes_com_sucesso() {
        Tema t = new Tema("Geral",null);
        temaRepository.save(t);
        Delegado d = new Delegado("tomas","delegado",null,null);
        cidadaoRepository.save(d);
        PropostaLei pl = new PropostaLei("titulo3","descricao3", LocalDateTime.now().minusMonths(2),d,t);
        pl.setFechado(true);
        projetoRepository.save(pl);

        List<PropostaLei> propostasAtivas = projetoService.getVotacoesEmCurso();

        assertEquals(0, propostasAtivas.size());
    }

}

