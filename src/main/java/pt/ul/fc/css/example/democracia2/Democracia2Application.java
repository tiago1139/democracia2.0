package pt.ul.fc.css.example.democracia2;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import pt.ul.fc.css.example.democracia2.entities.*;
import pt.ul.fc.css.example.democracia2.repositories.CidadaoRepository;
import pt.ul.fc.css.example.democracia2.repositories.ProjetoRepository;
import pt.ul.fc.css.example.democracia2.repositories.TemaRepository;

import java.time.LocalDateTime;
import java.util.List;

@EntityScan("pt.ul.fc.css.example.democracia2")
@SpringBootApplication
@EnableScheduling
public class Democracia2Application {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }



    private static final Logger log = LoggerFactory.getLogger(Democracia2Application.class);

    public static void main(String[] args) {

        SpringApplication.run(Democracia2Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(CidadaoRepository cidadaoRepository,
                                  TemaRepository temaRepository,
                                  ProjetoRepository projetoRepository) {
        return (args) -> {

            Tema t = new Tema();
            t.setNome("Geral");
            temaRepository.save(t);

            // save a few customers
           Delegado d = new Delegado();
           d.setNome("Tiago");
           d.setApelido("Pinto");


           Cidadao c = new Cidadao("Ricardo","Osga");
           Cidadao c1 = new Cidadao("Miguel","Pastel");

           cidadaoRepository.saveAll(List.of(c,c1,d));

           Delegado d1 = new Delegado();
           d1.setNome("Tiago1");
           d1.setApelido("Pinto1");

            Delegado d2 = new Delegado();
            d2.setNome("Tiago2");
            d2.setApelido("Pinto2");

           Cidadao c2 = new Cidadao("Cidadao", "Apelido");

           Cidadao cTeste = new Cidadao("Ricardo", "Soares");

           Cidadao cTeste1 = new Cidadao("Ricardo1", "Soares1");

           cidadaoRepository.save(d);
           cidadaoRepository.save(d1);
           cidadaoRepository.save(d2);
           cidadaoRepository.save(c2);
           cidadaoRepository.save(cTeste);
           cidadaoRepository.save(cTeste1);

            System.out.println("teste");
            ProjetoLei projetoLei = new ProjetoLei();
            projetoLei.setTitulo("Projeto 1");
            projetoLei.setTema(t);
            projetoLei.setProponente(d);
            projetoLei.setApoios(5L);
            projetoLei.setDescricao("Este é um teste de um projeto de lei.");
            projetoLei.setValidade(LocalDateTime.of(2023,5,30,15,0));
            projetoRepository.save(projetoLei);
            ProjetoLei projetoLei2 = new ProjetoLei();
            projetoLei2.setTitulo("Projeto 2");
            projetoLei2.setTema(t);
            projetoLei2.setProponente(d);
            projetoLei2.setApoios(9999L);
            projetoLei2.setDescricao("Este é um teste de um projeto de lei.");
            projetoLei2.setValidade(LocalDateTime.now().plusSeconds(20));
            projetoRepository.save(projetoLei2);

            PropostaLei p = new PropostaLei();
            p.setTitulo("Proposta 1");
            p.setDescricao("Teste");
            p.setProponente(d);
            p.setTema(t);
            p.setValidade(LocalDateTime.of(2023,8,15,10,00));
            p.setVotosFavoraveis(30L);
            p.setVotosDesfavoraveis(15L);

            projetoRepository.save(p);

            PropostaLei p2 = new PropostaLei();
            p2.setTitulo("Proposta Dois");
            p2.setDescricao("Teste Dois");
            p2.setProponente(d);
            p2.setTema(t);
            p2.setValidade(LocalDateTime.now().plusSeconds(20));
            p2.setVotosFavoraveis(30L);
            p2.setVotosDesfavoraveis(15L);

            projetoRepository.save(p2);






        };
    }

}
