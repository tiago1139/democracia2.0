package pt.ul.fc.css.example.democracia2.UseCases;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
public class ApoiarProjetosLeiTests {

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
    public void apoiar_com_sucesso() {
        drop();
        Tema t = new Tema("Geral",null);
        temaRepository.save(t);
        Delegado d = new Delegado("tomas","delegado",null,null);
        cidadaoRepository.save(d);
        Cidadao c = new Cidadao("manuel","tomas");
        cidadaoRepository.save(c);
        ProjetoLei pl = new ProjetoLei("titulo","descricao",LocalDateTime.now().minusYears(10),d,t);
        pl.setApoios(1L);
        pl = (ProjetoLei) projetoRepository.save(pl);

        assertEquals(1,pl.getApoios());

        projetoService.apoiarProjetoLei(c.getId(),pl.getId());
        ProjetoLei plNovo = (ProjetoLei) projetoRepository.findById(pl.getId()).get();

        System.out.println(plNovo.getId());
        assertEquals(2, plNovo.getApoios());
    }

    @Test
    //@Sql(scripts={"classpath:sql/ApoiarProjetosLei/apoiarComSucesso.sql"})
    public void cidadao_ja_tinha_apoiado(){
        drop();
        Tema t = new Tema("Geral",null);
        temaRepository.save(t);
        Delegado d = new Delegado("tomas","delegado",null,null);
        cidadaoRepository.save(d);
        Cidadao c = new Cidadao("manuel","tomas");
        cidadaoRepository.save(c);
        ProjetoLei pl = new ProjetoLei("titulo","descricao",LocalDateTime.now().minusYears(10),d,t);
        pl.setApoios(1L);
        pl = (ProjetoLei) projetoRepository.save(pl);


        projetoService.apoiarProjetoLei(c.getId(),pl.getId());
        projetoService.apoiarProjetoLei(c.getId(),pl.getId());

        ProjetoLei plNovo = (ProjetoLei) projetoRepository.findById(pl.getId()).get();

        assertEquals(2,plNovo.getApoios());
    }

    @Test
    public void apoio_leva_a_criar_proposta(){
        drop();
        Cidadao c = new Cidadao("Tiago", "Pinto");

        Delegado d = new Delegado("Tiago", "Pinto", null, null);

        Tema t = new Tema("Tema", null);

        temaRepository.save(t);
        cidadaoRepository.save(c);
        cidadaoRepository.save(d);

        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                LocalDateTime.of(2023,10,13,13,00),d,t);

        pl.setApoios(9999L);
        projetoRepository.save(pl);


        projetoService.apoiarProjetoLei(c.getId(), pl.getId());

        PropostaLei pp = (PropostaLei) projetoRepository.findPropostabyId(pl.getId()+1).get();
        d = cidadaoRepository.findDelegadobyID(d.getId());

        assertEquals(1,projetoRepository.findAllActivePropostas().size());
        assertEquals(2,projetoRepository.findAll().size());
        assertEquals(pp,d.getVotacoes().get(0).getPropostaLei());


    }

    @Test
    public void data_validade_maior_que_dois_meses(){
        Cidadao c = new Cidadao("Tiago", "Pinto");

        Delegado d = new Delegado("Tiago", "Pinto", null, null);

        Tema t = new Tema("Tema", null);

        temaRepository.save(t);
        cidadaoRepository.save(c);
        cidadaoRepository.save(d);

        LocalDateTime validade = LocalDateTime.now().plusMonths(5);

        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                validade,d,t);

        pl.setApoios(9999L);
        projetoRepository.save(pl);
        projetoService.apoiarProjetoLei(c.getId(), pl.getId());

        PropostaLei pp = (PropostaLei) projetoRepository.findPropostabyId(pl.getId()+1L).get();
        assertEquals(1,pp.getVotosFavoraveis()); //lancado automaticamente o voto do delegado
        assertNotEquals(validade,pp.getValidade());

    }

    @Test
    public void data_validade_menor_que_quinze_dias(){
        Cidadao c = new Cidadao("Tiago", "Pinto");

        Delegado d = new Delegado("Tiago", "Pinto", null, null);

        Tema t = new Tema("Tema", null);

        temaRepository.save(t);
        cidadaoRepository.save(c);
        cidadaoRepository.save(d);

        LocalDateTime validade = LocalDateTime.now().plusDays(10);

        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                validade,d,t);

        pl.setApoios(9999L);
        projetoRepository.save(pl);
        projetoService.apoiarProjetoLei(c.getId(), pl.getId());

        PropostaLei pp = (PropostaLei) projetoRepository.findPropostabyId(pl.getId()+1L).get();
        assertEquals(1,pp.getVotosFavoraveis());
        assertNotEquals(validade,pp.getValidade());

    }



}
