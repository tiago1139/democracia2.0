package pt.ul.fc.css.example.democracia2.UseCases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class VotarEmPropostaTests {

    @Autowired
    private CidadaoService cidadaoService;

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
    public void vota_com_sucesso_cidadao() {
        Cidadao c = new Cidadao("Tiago", "Pinto");

        Delegado d = new Delegado("Tiago", "Pinto", null, null);

        Tema t = new Tema("Tema", null);

        temaRepository.save(t);
        cidadaoRepository.save(c);
        cidadaoRepository.save(d);

        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                LocalDateTime.of(2023, 10, 13, 13, 00), d, t);

        pl.setApoios(9999L);
        projetoRepository.save(pl);


        projetoService.apoiarProjetoLei(c.getId(), pl.getId());

        List<PropostaLei> propostaLeiList = projetoService.getPropostasAtivas();
        PropostaLei pp = propostaLeiList.get(0);
        Long propostaId = pp.getId();
        Long VotosTotais = pp.getVotosFavoraveis() + pp.getVotosDesfavoraveis();

        cidadaoService.getVotoDelegado(c.getId(),propostaId);
        cidadaoService.votarProposta(c.getId(),propostaId,Voto.FAVORAVEL);

        pp = (PropostaLei) projetoRepository.findPropostabyId(propostaId).get();
        Long NovosVotosTotais = pp.getVotosFavoraveis() + pp.getVotosDesfavoraveis();
        c = (Cidadao) cidadaoRepository.findById(c.getId()).get();
        assertEquals(VotosTotais+1,NovosVotosTotais);
        assertEquals(pp,c.getProjVotados().get(0));
    }

    @Test
    public void nao_consegue_votar_na_mesma_proposta() {
        Cidadao c = new Cidadao("Tiago", "Pinto");

        Delegado d = new Delegado("Tiago", "Pinto", null, null);

        Tema t = new Tema("Tema", null);

        temaRepository.save(t);
        cidadaoRepository.save(c);
        cidadaoRepository.save(d);

        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                LocalDateTime.of(2023, 10, 13, 13, 00), d, t);

        pl.setApoios(9999L);
        projetoRepository.save(pl);


        projetoService.apoiarProjetoLei(c.getId(), pl.getId());

        List<PropostaLei> propostaLeiList = projetoService.getPropostasAtivas();
        PropostaLei pp = propostaLeiList.get(0);
        Long propostaId = pp.getId();

        cidadaoService.getVotoDelegado(c.getId(), propostaId);
        cidadaoService.votarProposta(c.getId(), propostaId, Voto.FAVORAVEL);

        pp = (PropostaLei) projetoRepository.findPropostabyId(propostaId).get();
        Long VotosPrimeira = pp.getVotosFavoraveis() + pp.getVotosDesfavoraveis();

        cidadaoService.votarProposta(c.getId(),propostaId,Voto.DESFAVORAVEL);
        pp = (PropostaLei) projetoRepository.findPropostabyId(propostaId).get();
        Long VotosSegunda = pp.getVotosFavoraveis() + pp.getVotosDesfavoraveis();

        assertEquals(VotosPrimeira,VotosSegunda);
    }

    @Test
    public void ver_voto_delegado_quando_tem_delegado(){
        drop();
        Cidadao c = new Cidadao("Tiago", "Pinto");

        Delegado d = new Delegado("Tiago", "Pinto", null, null);

        Tema t = new Tema("Tema", null);

        temaRepository.save(t);
        cidadaoRepository.save(c);
        cidadaoRepository.save(d);

        c = cidadaoService.escolheDelegado(c.getId(),t.getId(),d.getId());

        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                LocalDateTime.of(2023, 10, 13, 13, 00), d, t);

        pl.setApoios(9999L);
        projetoRepository.save(pl);


        projetoService.apoiarProjetoLei(c.getId(), pl.getId());

        List<PropostaLei> propostaLeiList = projetoService.getPropostasAtivas();
        PropostaLei pp = propostaLeiList.get(0);
        Long propostaId = pp.getId();

        Voto v = cidadaoService.getVotoDelegado(c.getId(), propostaId);
        assertEquals(Voto.FAVORAVEL,v);

    }

    @Test
    public void ver_voto_delegado_quando_nao_tem_delegado(){
        Cidadao c = new Cidadao("Tiago", "Pinto");

        Delegado d = new Delegado("Tiago", "Pinto", null, null);

        Tema t = new Tema("Tema", null);

        temaRepository.save(t);
        cidadaoRepository.save(c);
        cidadaoRepository.save(d);


        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                LocalDateTime.of(2023, 10, 13, 13, 00), d, t);

        pl.setApoios(9999L);
        projetoRepository.save(pl);


        projetoService.apoiarProjetoLei(c.getId(), pl.getId());

        List<PropostaLei> propostaLeiList = projetoService.getPropostasAtivas();
        PropostaLei pp = propostaLeiList.get(0);
        Long propostaId = pp.getId();

        Voto v = cidadaoService.getVotoDelegado(c.getId(), propostaId);
        assertEquals(Voto.SEMVOTO,v);
    }

    @Test
    public void voto_proposta_delegado_com_sucesso(){
        Cidadao c = new Cidadao("Tiago", "Pinto");

        Delegado d = new Delegado("Tiago", "Pinto", null, null);

        Delegado d2 = new Delegado("Miguel", "Reis", null,null);

        Tema t = new Tema("Tema", null);

        temaRepository.save(t);
        cidadaoRepository.save(c);
        cidadaoRepository.save(d);
        cidadaoRepository.save(d2);


        ProjetoLei pl = new ProjetoLei("Projeto", "Teste do Projeto",
                LocalDateTime.of(2023, 10, 13, 13, 00), d, t);

        pl.setApoios(9999L);
        projetoRepository.save(pl);


        projetoService.apoiarProjetoLei(c.getId(), pl.getId());

        List<PropostaLei> propostaLeiList = projetoService.getPropostasAtivas();
        PropostaLei pp = propostaLeiList.get(0);
        Long propostaId = pp.getId();
        Long votosDesfavoraveisPreVotacao = pp.getVotosDesfavoraveis();

        Voto v = cidadaoService.getVotoDelegado(d2.getId(), propostaId);
        assertEquals(Voto.SEMVOTO,v);

        cidadaoService.votarProposta(d2.getId(),propostaId,Voto.DESFAVORAVEL);
        pp = (PropostaLei) projetoRepository.findPropostabyId(propostaId).get();
        Long votosDesfavoraveisPosVotacao = pp.getVotosDesfavoraveis();
        d2 = cidadaoRepository.findDelegadobyID(d2.getId());
        assertEquals(votosDesfavoraveisPreVotacao+1,votosDesfavoraveisPosVotacao);
        assertEquals(false,d2.getProjVotados().isEmpty());
        assertEquals(Voto.DESFAVORAVEL,d2.getVotacoes().get(0).getVoto());


    }
}
