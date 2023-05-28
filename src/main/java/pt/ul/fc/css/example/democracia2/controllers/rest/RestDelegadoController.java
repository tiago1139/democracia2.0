package pt.ul.fc.css.example.democracia2.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.example.democracia2.dto.DelegadoDTO;
import pt.ul.fc.css.example.democracia2.entities.Delegado;
import pt.ul.fc.css.example.democracia2.services.CidadaoService;

@RestController()
@RequestMapping("api")
public class RestDelegadoController {

    @Autowired
    private CidadaoService cidadaoServ;

    @PostMapping("/delegados")
    public ResponseEntity<DelegadoDTO> criaDelegado(@RequestBody Delegado delegadoDTO) {
        System.out.println("Entra");
        //DelegadoDTO delegadoCriado = cidadaoServ.criaDelegadoDTO(delegadoDTO);
        System.out.println("Sai");



        return (ResponseEntity<DelegadoDTO>) ResponseEntity.status(HttpStatus.CREATED);
    }

}
