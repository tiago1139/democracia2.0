package pt.ul.fc.css.example.democracia2.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.example.democracia2.entities.Tema;
import pt.ul.fc.css.example.democracia2.services.TemaService;

@RestController()
@RequestMapping("api")
public class RestTemaController {


    @Autowired
    private TemaService temaServ;

    @PostMapping("/temas")
    public ResponseEntity<Tema> criaTema(@NonNull @RequestBody Tema tema) {
        Tema temaCriado = temaServ.createTema(tema);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/json;charset=UTF-8");

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers)
                .body(temaCriado);
    }
}
