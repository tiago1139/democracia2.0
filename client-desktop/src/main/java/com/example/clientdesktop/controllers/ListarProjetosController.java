package com.example.clientdesktop.controllers;

import com.example.clientdesktop.models.ProjetoLei;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;


public class ListarProjetosController extends JavaFxController implements Initializable {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @FXML
    private Button bttnVotacoes;

    @FXML
    private Button bttn_inicio;

    @FXML
    private Button bttnProjetos;

    private long projeto_id;


    @FXML
    private TableColumn<ProjetoLei, Void> detalhes;


    @FXML
    private TableColumn<ProjetoLei, String> proponente;

    @FXML
    private TableColumn<ProjetoLei, String> tema;

    @FXML
    private TableColumn<ProjetoLei, String> titulo;

    @FXML
    private TableColumn<ProjetoLei, String> validade;

    @FXML
    private TableView<ProjetoLei> projetos;

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
    void mudaParaVotacoesEmCurso(ActionEvent event) {
        carregarScene(event, votacoesEmCursoFxml);
    }

    @FXML
    void mudaParaDetalhesProjeto(ActionEvent event){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(consultaProjetoFxml));
        ConsultaProjetoController consultaProjetoController = new ConsultaProjetoController();
        consultaProjetoController.setprojetoID(get_projeto_id());
        loader.setController(consultaProjetoController);
        carregarScene(event, consultaProjetoFxml);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream response = getRequest("http://localhost:8080/api/projetos");
            mapper.registerModule(new JavaTimeModule());
            List<ProjetoLei> projetosDTOs = mapper.readValue(response, new TypeReference<List<ProjetoLei>>() {});
            projetos.getItems().setAll(projetosDTOs);

            proponente.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getProponente().getNome()));
            tema.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getTema().getNome()));
            titulo.setCellValueFactory(new PropertyValueFactory<ProjetoLei,String>("titulo"));
            validade.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getValidade().format(formatter)));
            detalhes.setCellFactory(param -> new TableCell<ProjetoLei, Void>() {
                private final Button detalhesbttn = new Button("Detalhes");
                {

                    detalhesbttn.setOnAction(event -> {
                        setProjeto_id(getTableRow().getItem().getId());
                        System.out.println(get_projeto_id());
                        mudaParaDetalhesProjeto(event);
                    });
                }
                @Override // TO UPDATE THE LIST WITH THE BUTTON -> from ChatGPT

                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(detalhesbttn);
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }




    }






}
