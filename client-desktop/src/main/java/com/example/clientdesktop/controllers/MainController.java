package com.example.clientdesktop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;


public class MainController extends JavaFxController {

    @FXML
    public Button bttnVotacoes;

    @FXML
    public Button bttnProjetos;


    @FXML
    public void mudaParaVotacoesEmCurso(ActionEvent event){
        carregarScene(event, votacoesEmCursoFxml);
    }

    @FXML
    public void mudaParaProjetosNaoExpirados(ActionEvent event) {
        carregarScene(event, listarProjetosFxml);
    }


}
