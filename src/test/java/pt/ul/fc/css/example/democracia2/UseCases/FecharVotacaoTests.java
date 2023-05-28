package pt.ul.fc.css.example.democracia2.UseCases;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.test.context.jdbc.Sql;
import pt.ul.fc.css.example.democracia2.entities.*;
import pt.ul.fc.css.example.democracia2.repositories.CidadaoRepository;
import pt.ul.fc.css.example.democracia2.repositories.ProjetoRepository;
import pt.ul.fc.css.example.democracia2.repositories.TemaRepository;
import pt.ul.fc.css.example.democracia2.services.CidadaoService;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FecharVotacaoTests {

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


    @AfterEach
    public void drop(){
        projetoRepository.deleteAll();
        cidadaoRepository.deleteAll();
        temaRepository.deleteAll();


    }
    @BeforeEach
    public void dropBefore() {
        projetoRepository.deleteAll();
        cidadaoRepository.deleteAll();
        temaRepository.deleteAll();
    }

    @Test
    public void proposta_ainda_valida(){
        Delegado d1 = new Delegado("Jose", "Pinto", null, null);

        Tema t1 = new Tema("Saude", null);

        temaRepository.save(t1);
        cidadaoRepository.save(d1);

        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                LocalDateTime.of(2023,10,13,13,00),d1,t1);

        PropostaLei pp = new PropostaLei(pl, LocalDateTime.of(2023,11,15,15,00));

        projetoRepository.save(pl);
        projetoRepository.save(pp);

        boolean b = projetoService.fechaVotacao(pp);

        pp = (PropostaLei) projetoRepository.findPropostabyId(pp.getId()).get();

        assertEquals(false, pp.getFechado());

    }

    @Test
    public void fecha_votacao_com_sucesso() {

        Cidadao c1 = new Cidadao("Vasco", "Pinto");
        Cidadao c2 = new Cidadao ("Mario", "Manuel");
        Cidadao c3 = new Cidadao("Leonardo", "Pinto");

        Delegado d1 = new Delegado("Jose", "Pinto", null, null);

        Tema t1 = new Tema("Saude", null);

        temaRepository.save(t1);
        cidadaoRepository.save(c1);
        cidadaoRepository.save(c2);
        cidadaoRepository.save(c3);
        cidadaoRepository.save(d1);

        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                LocalDateTime.of(2023,10,13,13,00),d1,t1);

        PropostaLei pp = new PropostaLei(pl, LocalDateTime.of(2023,3,15,15,00));

        projetoRepository.save(pl);
        projetoRepository.save(pp);

        cidadaoService.votarProposta(c1.getId(), pp.getId(), Voto.FAVORAVEL);
        cidadaoService.votarProposta(c2.getId(), pp.getId(), Voto.FAVORAVEL);
        cidadaoService.votarProposta(c3.getId(), pp.getId(), Voto.DESFAVORAVEL);

        pp = (PropostaLei) projetoRepository.findPropostabyId(pp.getId()).get();

        boolean b = projetoService.fechaVotacao(pp);

        pp = (PropostaLei) projetoRepository.findPropostabyId(pp.getId()).get();

        long votosFavoraveis = pp.getVotosFavoraveis();
        long votosDesfavoraveis = pp.getVotosDesfavoraveis();

        boolean estadoProposta = pp.getAprovada();

        assertEquals(2, votosFavoraveis);
        assertEquals(1, votosDesfavoraveis);
        assertEquals(true, estadoProposta);
        assertEquals(true, pp.getFechado());
    }

    @Test
    public void fecha_votacao_com_2_delegados(){
        Cidadao c1 = new Cidadao("Vasco", "Pinto");
        Cidadao c2 = new Cidadao ("Mario", "Manuel");
        Cidadao c3 = new Cidadao("Leonardo", "Pinto");

        Delegado d1 = new Delegado("Jose", "Pinto", null, null);
        Delegado d2 = new Delegado("Josefina", "Pinto", null, null);

        Tema t1 = new Tema("Saude", null);

        temaRepository.save(t1);

        cidadaoRepository.save(c1);
        cidadaoRepository.save(c2);
        cidadaoRepository.save(c3);
        cidadaoRepository.save(d1);
        cidadaoRepository.save(d2);

        cidadaoService.escolheDelegado(c1.getId(), t1.getId(), d1.getId());
        cidadaoService.escolheDelegado(c2.getId(), t1.getId(), d1.getId());
        cidadaoService.escolheDelegado(c3.getId(), t1.getId(), d2.getId());


        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                LocalDateTime.of(2023,10,13,13,00),d1,t1);

        PropostaLei pp = new PropostaLei(pl, LocalDateTime.of(2023,3,15,15,00));

        projetoRepository.save(pl);
        projetoRepository.save(pp);

        cidadaoService.votarProposta(d2.getId(), pp.getId(), Voto.FAVORAVEL);
        cidadaoService.votarProposta(d1.getId(), pp.getId(), Voto.DESFAVORAVEL);

        pp = (PropostaLei) projetoRepository.findPropostabyId(pp.getId()).get();

        boolean b = projetoService.fechaVotacao(pp);

        pp = (PropostaLei) projetoRepository.findPropostabyId(pp.getId()).get();

        long f = pp.getVotosFavoraveis();
        long df = pp.getVotosDesfavoraveis();

        boolean estadoProp = pp.getAprovada();

        assertEquals(2, f);
        assertEquals(3, df);
        assertEquals(false, estadoProp);
        assertEquals(true, pp.getFechado());
    }


    @Test
    public void fecha_votacao_com_hierarquia_temas(){
        Cidadao c1 = new Cidadao("Vasco", "Pinto");
        Cidadao c2 = new Cidadao ("Mario", "Manuel");
        Cidadao c3 = new Cidadao("Leonardo", "Pinto");

        Delegado d1 = new Delegado("Jose", "Pinto", null, null);
        Delegado d2 = new Delegado("Josefina", "Pinto", null, null);
        Delegado d3 = new Delegado("Daniel", "Pinto", null, null);

        Tema t1 = new Tema("Saude", null);

        temaRepository.save(t1);

        Tema t2 = new Tema("Geral", List.of(t1));

        temaRepository.save(t2);
        t1.setTemaPai(t2);
        temaRepository.save(t1);

        cidadaoRepository.save(c1);
        cidadaoRepository.save(c2);
        cidadaoRepository.save(c3);
        cidadaoRepository.save(d1);
        cidadaoRepository.save(d2);
        cidadaoRepository.save(d3);

        cidadaoService.escolheDelegado(c1.getId(), t1.getId(), d1.getId());
        cidadaoService.escolheDelegado(c1.getId(), t2.getId(), d2.getId());

        cidadaoService.escolheDelegado(c2.getId(), t1.getId(), d3.getId());

        cidadaoService.escolheDelegado(c3.getId(), t1.getId(), d2.getId());

        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                LocalDateTime.of(2023,10,13,13,00),d1,t1);

        PropostaLei pp = new PropostaLei(pl, LocalDateTime.of(2023,3,15,15,00));

        projetoRepository.save(pl);
        projetoRepository.save(pp);

        cidadaoService.votarProposta(d2.getId(), pp.getId(), Voto.DESFAVORAVEL);
        cidadaoService.votarProposta(d3.getId(), pp.getId(), Voto.FAVORAVEL);

        pp = (PropostaLei) projetoRepository.findPropostabyId(pp.getId()).get();

        boolean b = projetoService.fechaVotacao(pp);

        pp = (PropostaLei) projetoRepository.findPropostabyId(pp.getId()).get();

        long f = pp.getVotosFavoraveis();
        long df = pp.getVotosDesfavoraveis();

        boolean estadoProp = pp.getAprovada();

        assertEquals(2, f);
        assertEquals(3, df);
        assertEquals(false, estadoProp);
        assertEquals(true, pp.getFechado());

    }



    @Test
    public void cidadao_com_delegados_nao_votam(){
        Cidadao c1 = new Cidadao("Vasco", "Pinto");
        Cidadao c2 = new Cidadao ("Mario", "Manuel");
        Cidadao c3 = new Cidadao("Leonardo", "Pinto");

        Delegado d1 = new Delegado("Jose", "Pinto", null, null);
        Delegado d2 = new Delegado("Josefina", "Pinto", null, null);
        Delegado d3 = new Delegado("Daniel", "Pinto", null, null);

        Tema t1 = new Tema("Saude", null);

        temaRepository.save(t1);

        Tema t2 = new Tema("Geral", List.of(t1));

        temaRepository.save(t2);
        t1.setTemaPai(t2);
        temaRepository.save(t1);

        cidadaoRepository.save(c1);
        cidadaoRepository.save(c2);
        cidadaoRepository.save(c3);
        cidadaoRepository.save(d1);
        cidadaoRepository.save(d2);
        cidadaoRepository.save(d3);

        cidadaoService.escolheDelegado(c1.getId(), t1.getId(), d1.getId());
        cidadaoService.escolheDelegado(c1.getId(), t2.getId(), d1.getId());

        cidadaoService.escolheDelegado(c2.getId(), t1.getId(), d3.getId());

        cidadaoService.escolheDelegado(c3.getId(), t1.getId(), d2.getId());

        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                LocalDateTime.of(2023,10,13,13,00),d1,t1);

        PropostaLei pp = new PropostaLei(pl, LocalDateTime.of(2023,3,15,15,00));

        projetoRepository.save(pl);
        projetoRepository.save(pp);

        cidadaoService.votarProposta(d2.getId(), pp.getId(), Voto.DESFAVORAVEL);
        cidadaoService.votarProposta(d3.getId(), pp.getId(), Voto.FAVORAVEL);

        pp = (PropostaLei) projetoRepository.findPropostabyId(pp.getId()).get();

        boolean b = projetoService.fechaVotacao(pp);

        pp = (PropostaLei) projetoRepository.findPropostabyId(pp.getId()).get();

        long f = pp.getVotosFavoraveis();
        long df = pp.getVotosDesfavoraveis();

        boolean estadoProp = pp.getAprovada();

        assertEquals(2, f);
        assertEquals(2, df);
        assertEquals(false, estadoProp);
        assertEquals(true, pp.getFechado());

    }

}
