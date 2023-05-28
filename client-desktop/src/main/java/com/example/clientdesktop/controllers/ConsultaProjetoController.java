package com.example.clientdesktop.controllers;

import com.example.clientdesktop.models.Cidadao;
import com.example.clientdesktop.models.ProjetoLei;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class ConsultaProjetoController extends JavaFxController implements Initializable {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @FXML
    private ComboBox<Cidadao> cidadaosLista;

    private static Long projetoId;

    @FXML
    private Button botaoApoiar;

    @FXML
    private Button bttnVotacoes;

    @FXML
    private Button bttn_inicio;

    @FXML
    private Button bttnProjetos;

    @FXML
    private Label titulo;

    @FXML
    private Label descricao;

    @FXML
    private Label tema;

    @FXML
    private Label delegado;

    @FXML
    private Label apoios;

    @FXML
    private Label validade;

    private List<Cidadao> cidadaos;

    @FXML
    void mudaParaInicio(ActionEvent event) {
        carregarScene(event, inicioFxml);
    }

    @FXML
    void mudaParaVotacoesEmCurso(ActionEvent event) {
        carregarScene(event, votacoesEmCursoFxml);
    }
    @FXML
    void mudaParaProjetosNaoExpirados(ActionEvent event) {
        carregarScene(event, listarProjetosFxml);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(projetoId);
        ObjectMapper mapper = new ObjectMapper();
        InputStream response = null;
        try {
            response = getRequest("http://localhost:8080/api/projetos/"+projetoId);
            mapper.registerModule(new JavaTimeModule());

            ProjetoLei p = mapper.readValue(response, ProjetoLei.class);
            titulo.setText(p.getTitulo());
            descricao.setText(p.getDescricao());
            tema.setText(p.getTema().getNome());
            delegado.setText(p.getProponente().getNome());
            apoios.setText(String.valueOf(p.getApoios()));
            validade.setText(p.getValidade().format(formatter));

            InputStream responseCidadao = getRequest("http://localhost:8080/api/cidadaos");

            cidadaos = mapper.readValue(responseCidadao, new TypeReference<List<Cidadao>>() {});

            cidadaos = cidadaos.stream()
                    .filter(c -> !c.getId().equals(p.getProponente().getId()))
                    .collect(Collectors.toList());


            ObservableList<Cidadao> options = FXCollections.observableArrayList(
                    cidadaos);

            cidadaosLista.setItems(options);

            botaoApoiar.setOnAction(event -> {
                System.out.println("botao apoiar funciona");
                Cidadao selected = cidadaosLista.getValue();
                if(selected != null){
                    try {
                        apoia(selected.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mudaParaProjetosNaoExpirados(event);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void verificaApoio(ActionEvent event) throws IOException {
        Cidadao c = cidadaosLista.getValue();
        InputStream response = getRequest("http://localhost:8080/api/cidadaos/apoia/"+c.getId()+"/"+projetoId);
        ObjectMapper mapper = new ObjectMapper();
        Boolean b = mapper.readValue(response, Boolean.class);
        if(b){
            botaoApoiar.setDisable(true);
            botaoApoiar.setText("Projeto Apoiado");
        } else {
            botaoApoiar.setDisable(false);
            botaoApoiar.setText("Apoiar");
        }
        System.out.println(b);

        /*for(ProjetoLei p: c.getProjApoiados()){
            if(p.getId() == projetoId){
                System.out.println("APOIADO");
                botaoApoiar.setDisable(true);
                botaoApoiar.setText("Projeto Apoiado");
                return;
            }
        }
        botaoApoiar.setDisable(false);
        botaoApoiar.setText("Apoiar");*/
    }
    void apoia(Long cidadaoID) throws IOException{
        String jsonBody = objectMapper.writeValueAsString(cidadaoID);
        InputStream response = postRequest("http://localhost:8080/api/projetos/"+projetoId+"/apoiar",jsonBody);

    }



    public void setprojetoID(Long projetoId) {
        this.projetoId = projetoId;
    }
}
