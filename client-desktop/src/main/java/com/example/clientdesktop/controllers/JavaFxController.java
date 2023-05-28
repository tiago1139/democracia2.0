package com.example.clientdesktop.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class JavaFxController {

    public ObjectMapper objectMapper = new ObjectMapper();

    Gson gson = new Gson();

    String prefix = "/com/example/clientdesktop/";

    String inicioFxml = prefix + "MainController.fxml";
    String consultaProjetoFxml = prefix + "ConsultaProjetoController.fxml";
    String listarProjetosFxml = prefix + "ListarProjetosController.fxml";
    String votacoesEmCursoFxml = prefix + "VotacoesEmCursoController.fxml";
    String votarPropostaFxml = prefix + "VotarPropostaController.fxml";


    private Stage stage;

    @FXML
    public void initialize() {
        this.stage = new Stage();

    }


    public void carregarScene(Event parent, String path) {
        Node node = (Node) parent.getSource();
        // Step 3
        Stage stage = (Stage) node.getScene().getWindow();
        try {
            // Step 4
            Parent root = FXMLLoader.load(getClass().getResource(path));

            // Step 6
            Scene scene = new Scene(root);
            stage.setScene(scene);
            // Step 7
            stage.show();
        } catch (IOException e) {
            System.err.println(String.format("Error: %s", e.getMessage()));
        }


    }

    /*
    Código adaptado dos seguintes sites:
    https://www.twilio.com/pt-br/blog/5-maneiras-de-fazer-uma-chamada-http-em-java
    https://www.baeldung.com/java-http-request
     */

    public InputStream getRequest(String urlString) throws IOException {
        URL url = new URL(urlString);

    // Open a connection(?) on the URL(??) and cast the response(???)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    // Now it's "open", we can set the request method, headers etc.
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestMethod("GET");

    // This line makes the request
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return connection.getInputStream();
        } else {
            throw new IOException("Falha na solicitação HTTP. Código de resposta: " + responseCode);
        }
    }

    public InputStream postRequest(String urlString, String jsonBody) throws IOException {
        URL url = new URL(urlString);

        // Open a connection(?) on the URL(??) and cast the response(???)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Now it's "open", we can set the request method, headers etc.
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // This line makes the request
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_CREATED) {
            return connection.getInputStream();
        } else {
            throw new IOException("Falha na solicitação HTTP. Código de resposta: " + responseCode);
        }
    }

    protected void show() {
        this.stage.show();
    }
}
