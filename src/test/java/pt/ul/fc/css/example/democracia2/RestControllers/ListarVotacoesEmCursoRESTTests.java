package pt.ul.fc.css.example.democracia2.RestControllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pt.ul.fc.css.example.democracia2.Democracia2Application;
import pt.ul.fc.css.example.democracia2.dto.PropostaLeiDTO;
import pt.ul.fc.css.example.democracia2.entities.Delegado;
import pt.ul.fc.css.example.democracia2.entities.PropostaLei;
import pt.ul.fc.css.example.democracia2.entities.Tema;
import pt.ul.fc.css.example.democracia2.repositories.ProjetoRepository;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Democracia2Application.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude=SecurityAutoConfiguration.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase
public class ListarVotacoesEmCursoRESTTests {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjetoService projetoService;

    @BeforeEach
    public void resetDb() {
        projetoRepository.deleteAll();
    }

    @Test
    public void lista_votacoes_em_curso_vazia()
            throws Exception {

        BDDMockito.given(projetoService.getVotacoesEmCurso()).willReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/votacoes-em-curso")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

    }

    @Test
    public void lista_votacoes_em_curso_mocked()
            throws Exception {

        PropostaLei p1 = new PropostaLei();
        PropostaLei p2 = new PropostaLei();



        BDDMockito.given(projetoService.getVotacoesEmCurso()).willReturn(List.of(p1,p2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/votacoes-em-curso")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

    }

    @Test
    public void lista_com_2_votacoes() throws Exception {
        Delegado d1 = new Delegado("D1","Apelido1",null,null);
        Delegado d2 = new Delegado("D2","Apelido2",null,null);
        Tema t1 = new Tema("Geral", null);
        Tema t2 = new Tema("Saude", null);
        LocalDateTime v1 = LocalDateTime.of(2023,6,10,15,00);
        LocalDateTime v2 = LocalDateTime.of(2023,10,14,15,00);
        PropostaLei p1 = criaVotacaoTeste("P1", "Teste1", d1,t1,v1,15L,5L);
        PropostaLei p2 = criaVotacaoTeste("P2", "Teste2", d2,t2,v2,20L,10L);

        PropostaLeiDTO pd1 = mapper.map(p1,PropostaLeiDTO.class);
        PropostaLeiDTO pd2 = mapper.map(p2,PropostaLeiDTO.class);

        String inputJson = this.objectMapper.writeValueAsString(List.of(pd1,pd2));

        BDDMockito.given(projetoService.getVotacoesEmCurso()).willReturn(List.of(p1,p2));


        String output = mockMvc.perform(MockMvcRequestBuilders.get("/api/votacoes-em-curso").contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(output).isEqualTo(inputJson);

    }

    private PropostaLei criaVotacaoTeste(String titulo, String descricao,
                                  Delegado proponente, Tema tema, LocalDateTime validade,
                                  Long favoraveis, Long desfavoraveis) {

        PropostaLei p = new PropostaLei();
        p.setTitulo(titulo);
        p.setDescricao(descricao);
        p.setProponente(proponente);
        p.setTema(tema);
        p.setValidade(validade);
        p.setVotosFavoraveis(favoraveis);
        p.setVotosDesfavoraveis(desfavoraveis);

        return p;
    }


}
