package com.example.clientdesktop.controllers;

import com.example.clientdesktop.models.Cidadao;

import com.example.clientdesktop.models.PropostaLei;
import com.example.clientdesktop.models.Voto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class VotarPropostaController extends JavaFxController implements Initializable {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    private Button botaoVotar;

    @FXML
    private Button bttnProjetos;

    @FXML
    private Button bttnVotacoes;

    @FXML
    private Button bttn_inicio;

    @FXML
    private ComboBox<Cidadao> cidadaosLista;

    @FXML
    private Label descricao;

    @FXML
    private Label desfavoraveis;

    @FXML
    private Label favoraveis;

    @FXML
    private Label proponente;

    @FXML
    private Label tema;

    @FXML
    private Label titulo;

    @FXML
    private Label validade;

    @FXML
    private Label omissao;

    @FXML
    private ComboBox<Voto> votos;

    private List<Cidadao> cidadaos;

    @FXML
    void mudaParaProjetosNaoExpirados(ActionEvent event) {
        carregarScene(event, listarProjetosFxml);
    }

    @FXML
    void mudaParaVotacoesEmCurso(ActionEvent event) {
        carregarScene(event, votacoesEmCursoFxml);
    }

    @FXML
    void mudaParaInicio(ActionEvent event) {
        carregarScene(event, inicioFxml);
    }

    private static Long projetoId;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            InputStream response = getRequest("http://localhost:8080/api/proposta/"+projetoId);

            PropostaLei propostaLei = mapper.readValue(response, PropostaLei.class);

            titulo.setText(propostaLei.getTitulo());
            descricao.setText(propostaLei.getDescricao());
            tema.setText(propostaLei.getTema().getNome());
            proponente.setText(propostaLei.getProponente().getNome());
            validade.setText(propostaLei.getValidade().format(formatter));
            favoraveis.setText(String.valueOf(propostaLei.getVotosFavoraveis()));
            desfavoraveis.setText(String.valueOf(propostaLei.getVotosDesfavoraveis()));

            InputStream responseCidadao = getRequest("http://localhost:8080/api/cidadaos");

            cidadaos = mapper.readValue(responseCidadao, new TypeReference<List<Cidadao>>() {});

            cidadaos = cidadaos.stream()
                    .filter(c -> !c.getId().equals(propostaLei.getProponente().getId()))
                    .collect(Collectors.toList());

            ObservableList<Cidadao> options = FXCollections.observableArrayList(
                    cidadaos);

            ObservableList<Voto> optionsVotos = FXCollections.observableArrayList(
                    Arrays.stream(Voto.values()).
                            filter(v -> !v.equals(Voto.SEMVOTO))
                            .collect(Collectors.toList()));

            cidadaosLista.setItems(options);
            votos.setItems(optionsVotos);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }




    @FXML
    void verificaVotacao(ActionEvent event) throws IOException {

        Cidadao c = cidadaosLista.getValue();
        String urlOmissao = "http://localhost:8080/api/votar-proposta/proposta/"+projetoId+"/cidadao/"+c.getId();
        InputStream response = getRequest(urlOmissao);
        Voto voto = objectMapper.readValue(response, Voto.class);
        omissao.setText(String.valueOf(voto));
        InputStream responseCheck = getRequest("http://localhost:8080/api/cidadaos/votar/"+c.getId()+"/"+projetoId);
        Boolean b = objectMapper.readValue(responseCheck, Boolean.class);
        if(b){
            botaoVotar.setDisable(true);
            botaoVotar.setText("Projeto Votado");
        } else {
            botaoVotar.setDisable(false);
            botaoVotar.setText("Votar");
        }


    }

    @FXML
    void vota(ActionEvent event) throws IOException {
        Cidadao cidadaoSelected = cidadaosLista.getValue();
        Voto votoSelected = votos.getValue();
        if(cidadaoSelected != null && votoSelected != null){
            String urlVotar = "http://localhost:8080/api/votar-proposta/proposta/"+projetoId+"/cidadao/"+cidadaoSelected.getId();
            String jsonBody = objectMapper.writeValueAsString(votoSelected);
            InputStream response = postRequest(urlVotar, jsonBody);
            mudaParaVotacoesEmCurso(event);
        }


    }

    public void setprojetoID(Long projetoId) {

        this.projetoId = projetoId;
    }
}
