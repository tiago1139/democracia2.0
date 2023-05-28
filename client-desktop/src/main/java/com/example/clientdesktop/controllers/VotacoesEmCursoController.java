package com.example.clientdesktop.controllers;

import com.example.clientdesktop.models.PropostaLei;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;


public class VotacoesEmCursoController extends JavaFxController implements Initializable {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    private Button bttnVotacoes;

    @FXML
    private Button bttn_inicio;
    @FXML
    private Button bttnProjetos;

    private long projeto_id;


    @FXML
    private TableColumn<PropostaLei, Long> desfavoraveis;

    @FXML
    private TableColumn<PropostaLei, Long> favoraveis;

    @FXML
    private TableColumn<PropostaLei, String> proponente;

    @FXML
    private TableColumn<PropostaLei, String> tema;

    @FXML
    private TableColumn<PropostaLei, String> titulo;

    @FXML
    private TableColumn<PropostaLei, String> validade;

    @FXML
    private TableView<PropostaLei> votacoes;



    public void setProjeto_id(Long id){
        this.projeto_id = id;
    }
    public Long get_projeto_id(){
        return this.projeto_id;
    }

    @FXML
    void mudaParaInicio(ActionEvent event) {
        carregarScene(event, inicioFxml);
    }

    @FXML
    public void mudaParaProjetosNaoExpirados(ActionEvent event) {
        carregarScene(event, listarProjetosFxml);
    }


    @FXML
    void propostaDetails(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(votarPropostaFxml));
        VotarPropostaController votarPropostaController = new VotarPropostaController();
        setProjeto_id(votacoes.getSelectionModel().getSelectedItem().getId());
        votarPropostaController.setprojetoID(get_projeto_id());
        loader.setController(votarPropostaController);

        carregarScene(event, votarPropostaFxml);
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        desfavoraveis.setCellValueFactory(new PropertyValueFactory<PropostaLei, Long>("votosDesfavoraveis"));
        favoraveis.setCellValueFactory(new PropertyValueFactory<PropostaLei, Long>("votosFavoraveis"));
        proponente.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProponente().getNome()));
        tema.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTema().getNome()));
        titulo.setCellValueFactory(new PropertyValueFactory<PropostaLei, String>("titulo"));
        validade.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getValidade().format(formatter)));



        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream response = getRequest("http://localhost:8080/api/votacoes-em-curso");

            mapper.registerModule(new JavaTimeModule());
            List<PropostaLei> propostas = mapper.readValue(response, new TypeReference<List<PropostaLei>>() {});
            System.out.println(propostas);
            votacoes.getItems().setAll(propostas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
