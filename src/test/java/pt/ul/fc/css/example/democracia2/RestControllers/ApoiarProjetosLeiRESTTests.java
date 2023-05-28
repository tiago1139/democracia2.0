package pt.ul.fc.css.example.democracia2.RestControllers;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
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
import pt.ul.fc.css.example.democracia2.entities.*;
import pt.ul.fc.css.example.democracia2.repositories.CidadaoRepository;
import pt.ul.fc.css.example.democracia2.repositories.ProjetoRepository;
import pt.ul.fc.css.example.democracia2.repositories.TemaRepository;
import pt.ul.fc.css.example.democracia2.services.CidadaoService;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Democracia2Application.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude= SecurityAutoConfiguration.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase
public class ApoiarProjetosLeiRESTTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProjetoService projetoService;

    @MockBean
    private CidadaoService cidadaoService;

    @Autowired
    private CidadaoRepository cidadaoRepository;

    @Autowired
    private TemaRepository temaRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Test
    public void apoia_sucesso() throws Exception {

        BDDMockito.given(cidadaoService.getCidadao(1L)).willReturn(Optional.of(new Cidadao("Ricardo", "Soares")));
        BDDMockito.given(projetoService.getProjetoById(1L)).willReturn(Optional.of(new ProjetoLei()));

        Mockito.doNothing().when(projetoService).apoiarProjetoLei(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/projetos/1/apoiar").content(objectMapper.writeValueAsString(1))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void cidadao_invalido() throws Exception{

        BDDMockito.given(cidadaoService.getCidadao(1L)).willReturn(Optional.empty());
        BDDMockito.given(projetoService.getProjetoById(1L)).willReturn(Optional.of(new ProjetoLei()));

        Mockito.doNothing().when(projetoService).apoiarProjetoLei(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/projetos/1/apoiar").content(objectMapper.writeValueAsString(1))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void projeto_invalido() throws Exception{

        BDDMockito.given(cidadaoService.getCidadao(1L)).willReturn(Optional.of(new Cidadao("Ricardo", "Soares")));
        BDDMockito.given(projetoService.getProjetoById(1L)).willReturn(Optional.empty());

        Mockito.doNothing().when(projetoService).apoiarProjetoLei(1L, 1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/projetos/1/apoiar").content(objectMapper.writeValueAsString(1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
