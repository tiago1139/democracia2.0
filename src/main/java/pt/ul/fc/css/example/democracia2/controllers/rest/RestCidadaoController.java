package pt.ul.fc.css.example.democracia2.controllers.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.democracia2.dto.CidadaoDTO;
import pt.ul.fc.css.example.democracia2.entities.Cidadao;
import pt.ul.fc.css.example.democracia2.entities.ProjetoLei;
import pt.ul.fc.css.example.democracia2.entities.PropostaLei;
import pt.ul.fc.css.example.democracia2.entities.Voto;
import pt.ul.fc.css.example.democracia2.repositories.CidadaoRepository;
import pt.ul.fc.css.example.democracia2.services.CidadaoService;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController()
@RequestMapping("api")
public class RestCidadaoController {


    @Autowired
    private CidadaoService cidadaoServ;

    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/cidadaos")
    public List<CidadaoDTO> getCidadaos() {
        List<Cidadao> listaCidadaos = cidadaoServ.getAllCidadaos();
        for (Cidadao c: listaCidadaos){
            System.out.println(c.getNome()+" "+c.getId());
            System.out.println(c.getProjVotados());
        }
        List<CidadaoDTO> listaDTO =  listaCidadaos.stream().map(cidadao -> modelMapper.map(cidadao, CidadaoDTO.class))
                .collect(Collectors.toList());
        return listaDTO;
    }
    @PostMapping("/cidadaos")
    public ResponseEntity<Cidadao> criaCidadao(@NonNull @RequestBody Cidadao cidadao) {
        Cidadao cidadaoCriado = cidadaoServ.criaCidadao(cidadao);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/json;charset=UTF-8");

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers)
                .body(cidadaoCriado);
    }

    @GetMapping("/cidadaos/apoia/{cid}/{pid}")
    public Boolean verificaSeApoiou(@PathVariable long cid,@PathVariable long pid){
        ProjetoLei p = projetoService.getProjetoById(pid).get();
        return cidadaoServ.getCidadao(cid).get().getProjApoiados().contains(p);
    }

    @GetMapping("/cidadaos/votar/{cid}/{pid}")
    public Boolean verificaSeVotou(@PathVariable long cid,@PathVariable long pid){
        ProjetoLei p = projetoService.getProjetoById(pid).get();
        return cidadaoServ.getCidadao(cid).get().getProjVotados().contains(p);
    }

    @GetMapping("/votar-proposta/proposta/{pid}/cidadao/{cid}")
    public Voto getVotoDelegado(@PathVariable long pid, @PathVariable long cid){
        return cidadaoServ.getVotoDelegado(cid,pid);
    }

    @PostMapping("/votar-proposta/proposta/{pid}/cidadao/{cid}")
    public ResponseEntity<CidadaoDTO> votar(@PathVariable long pid, @PathVariable long cid, @RequestBody Voto voto){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/json;charset=UTF-8");
        boolean votou = cidadaoServ.votarProposta(cid,pid,voto);
        if(!votou) {
            Optional<PropostaLei> p = projetoService.getPropostaById(pid);
            Optional<Cidadao> c = cidadaoServ.getCidadao(cid);
            if(voto.equals(Voto.SEMVOTO) || !p.isPresent() || !c.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .body(null);
            }
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers)
                    .body(null);
        }
        Cidadao c = cidadaoServ.getCidadao(cid).get();
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(headers)
                .body(modelMapper.map(c,CidadaoDTO.class));
    }
}
