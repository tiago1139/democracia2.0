package pt.ul.fc.css.example.democracia2.UseCases;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import pt.ul.fc.css.example.democracia2.entities.Cidadao;
import pt.ul.fc.css.example.democracia2.entities.Delegado;
import pt.ul.fc.css.example.democracia2.entities.ProjetoLei;
import pt.ul.fc.css.example.democracia2.entities.Tema;
import pt.ul.fc.css.example.democracia2.repositories.CidadaoRepository;
import pt.ul.fc.css.example.democracia2.repositories.ProjetoRepository;
import pt.ul.fc.css.example.democracia2.repositories.TemaRepository;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FecharProjetosExpiradosTests {

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
    public void fechar_projetos_sucesso() {
        valoresTeste();
        List<ProjetoLei> projetosNaoFechados = projetoService.fecharProjetosExpirados();

        assertEquals(4, projetosNaoFechados.size());
        for(ProjetoLei p : projetosNaoFechados){
            assertTrue(p.getFechado());
        }
    }

    public Long[] valoresTeste(){
        Long[] r = new Long[2];
        Tema t = new Tema("Geral",null);
        temaRepository.save(t);
        Delegado d = new Delegado("tomas","delegado",null,null);
        cidadaoRepository.save(d);
        Cidadao c = new Cidadao("manuel","tomas");
        cidadaoRepository.save(c);
        long last_id =0;
        for(int i = 1;i < 5;i++){
            ProjetoLei pl = new ProjetoLei("titulo"+i,"descricao"+i, LocalDateTime.now().minusYears(10),d,t);
            pl.setApoios(1L);
            projetoRepository.save(pl);
            last_id = pl.getId();
        }
        r[0] = c.getId();
        r[1] = last_id;
        return r;
    }


    @Test
    //@Sql(scripts={"classpath:sql/FecharProjetosExpirados/fechaProjetoSucesso.sql"})
    public void nao_aceita_mais_assinaturas(){
        Long[] id = valoresTeste();
        ProjetoLei pl = (ProjetoLei) projetoRepository.getById(id[1]);
        Long apoiosPl = pl.getApoios();
        boolean estahFechado = pl.getFechado();

        List<ProjetoLei> projetosNaoFechados = projetoService.fecharProjetosExpirados();

        projetoService.apoiarProjetoLei(id[0], id[1]);

        pl = (ProjetoLei) projetoRepository.getById(id[1]);
        Long apoiosPosTentativaApoio = pl.getApoios();
        boolean estahPosFechado = pl.getFechado();

        assertEquals(apoiosPl, apoiosPosTentativaApoio);
        assertEquals(true,estahPosFechado);
        assertEquals(false, estahFechado);

    }
}
