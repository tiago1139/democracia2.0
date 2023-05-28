package pt.ul.fc.css.example.democracia2.RestControllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.After;
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
import pt.ul.fc.css.example.democracia2.dto.ProjetoLeiDTO;
import pt.ul.fc.css.example.democracia2.entities.Delegado;
import pt.ul.fc.css.example.democracia2.entities.ProjetoLei;
import pt.ul.fc.css.example.democracia2.entities.PropostaLei;
import pt.ul.fc.css.example.democracia2.entities.Tema;
import pt.ul.fc.css.example.democracia2.repositories.ProjetoRepository;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Democracia2Application.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude= SecurityAutoConfiguration.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase
public class ListarEConsultarProjetosNaoExpiradosRESTTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private ProjetoService projetoService;

    @BeforeEach
    public void resetDb() {
        projetoRepository.deleteAll();
    }

    @Test
    public void lista_projetos_nao_expirados_vazia()
            throws Exception {

        BDDMockito.given(projetoService.getProjetosNaoExpirados()).willReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projetos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

    }
    @Test
    public void lista_projetos_nao_expirados_mocked()
            throws Exception {

        ProjetoLei p1 = new ProjetoLei();
        ProjetoLei p2 = new ProjetoLei();



        BDDMockito.given(projetoService.getProjetosNaoExpirados()).willReturn(List.of(p1,p2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projetos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

    }

    @Test
    public void consultar_projeto() throws Exception {
        Delegado d1 = new Delegado("D1","Apelido1",null,null);
        Tema t1 = new Tema("Geral", null);
        ProjetoLei p = criaProjetoTeste("titulo","descricao",d1,t1,LocalDateTime.now(),5L);

        BDDMockito.given(projetoService.getProjetoById(1L)).willReturn(Optional.of(p));

        ProjetoLeiDTO pDTO = modelMapper.map(p, ProjetoLeiDTO.class);

        String inputJson = this.objectMapper.writeValueAsString(pDTO);


        String output = mockMvc.perform(MockMvcRequestBuilders.get("/api/projetos/1").contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(output).isEqualTo(inputJson);
    }

    @Test
    public void consultar_projeto_nulo() throws Exception {
        BDDMockito.given(projetoService.getProjetoById(1L)).willReturn(Optional.empty());

        String output = mockMvc.perform(MockMvcRequestBuilders.get("/api/projetos/1").contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Assertions.assertThat(output).isEqualTo("null");
    }

    private ProjetoLei criaProjetoTeste(String titulo, String descricao,
                                         Delegado proponente, Tema tema, LocalDateTime validade,
                                         Long apoios) {

        ProjetoLei p = new PropostaLei();
        p.setTitulo(titulo);
        p.setDescricao(descricao);
        p.setProponente(proponente);
        p.setTema(tema);
        p.setValidade(validade);
        p.setApoios(apoios);

        return p;
    }

}
