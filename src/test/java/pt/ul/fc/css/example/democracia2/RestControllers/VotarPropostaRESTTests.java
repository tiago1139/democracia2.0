package pt.ul.fc.css.example.democracia2.RestControllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
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
import pt.ul.fc.css.example.democracia2.Democracia2Application;
import pt.ul.fc.css.example.democracia2.dto.CidadaoDTO;
import pt.ul.fc.css.example.democracia2.entities.*;
import pt.ul.fc.css.example.democracia2.services.CidadaoService;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Democracia2Application.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude= SecurityAutoConfiguration.class)
// @TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase
public class VotarPropostaRESTTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private ProjetoService projetoService;

    @MockBean
    private CidadaoService cidadaoService;

    @Test
    public void vota_com_sucesso() throws Exception {
        Delegado d1 = new Delegado("D1","Apelido1",null,null);
        Delegado d2 = new Delegado("D2","Apelido2",null,null);

        Tema t1 = new Tema("Geral", null);
        Tema t2 = new Tema("Saude", null);

        LocalDateTime v1 = LocalDateTime.of(2023,6,10,15,00);
        LocalDateTime v2 = LocalDateTime.of(2023,10,14,15,00);
        Cidadao c = new Cidadao("Manel", "Roscas");
        PropostaLei p1 = criaVotacaoTeste("P1", "Teste1", d1,t1,v1,15L,5L);
        PropostaLei p2 = criaVotacaoTeste("P2", "Teste2", d2,t2,v2,20L,10L);

        c.setProjVotados(List.of(p1,p2));

        CidadaoDTO cDTO = modelMapper.map(c, CidadaoDTO.class);

        String inputJson = this.objectMapper.writeValueAsString(cDTO);

        given(cidadaoService.votarProposta(1, 3, Voto.FAVORAVEL)).willReturn(true);
        given(cidadaoService.getCidadao(1L)).willReturn(Optional.of(c));

        String output = mockMvc.perform(post("/api/votar-proposta/proposta/"+3+"/cidadao/"+1)
                        .content(this.objectMapper.writeValueAsString(Voto.FAVORAVEL))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(output).isEqualTo(inputJson);

    }

    @Test
    public void ja_votou_na_proposta() throws Exception {

        given(cidadaoService.votarProposta(1, 3, Voto.FAVORAVEL)).willReturn(false);
        given(projetoService.getPropostaById(3)).willReturn(Optional.of(new PropostaLei()));
        given(cidadaoService.getCidadao(1L)).willReturn(Optional.of(new Cidadao()));

        mockMvc.perform(post("/api/votar-proposta/proposta/"+3+"/cidadao/"+1)
                        .content(this.objectMapper.writeValueAsString(Voto.FAVORAVEL))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotModified());


    }

    @Test
    public void cidadao_invalido_bad_request() throws Exception {

        given(cidadaoService.votarProposta(15, 3, Voto.FAVORAVEL)).willReturn(false);
        given(projetoService.getPropostaById(3)).willReturn(Optional.of(new PropostaLei()));
        given(cidadaoService.getCidadao(15L)).willReturn(Optional.empty());

        mockMvc.perform(post("/api/votar-proposta/proposta/"+3+"/cidadao/"+15)
                        .content(this.objectMapper.writeValueAsString(Voto.FAVORAVEL))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


    }

    @Test
    public void proposta_invalida_bad_request() throws Exception {

        given(cidadaoService.votarProposta(1, 30, Voto.FAVORAVEL)).willReturn(false);
        given(projetoService.getPropostaById(30)).willReturn(Optional.empty());
        given(cidadaoService.getCidadao(1L)).willReturn(Optional.of(new Cidadao()));

        mockMvc.perform(post("/api/votar-proposta/proposta/"+30+"/cidadao/"+1)
                        .content(this.objectMapper.writeValueAsString(Voto.FAVORAVEL))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


    }

    @Test
    public void voto_invalido_bad_request() throws Exception {

        given(cidadaoService.votarProposta(1, 3, Voto.SEMVOTO)).willReturn(false);
        given(projetoService.getPropostaById(3)).willReturn(Optional.of(new PropostaLei()));
        given(cidadaoService.getCidadao(1L)).willReturn(Optional.of(new Cidadao()));

        mockMvc.perform(post("/api/votar-proposta/proposta/"+3+"/cidadao/"+1)
                        .content(this.objectMapper.writeValueAsString(Voto.SEMVOTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());


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
