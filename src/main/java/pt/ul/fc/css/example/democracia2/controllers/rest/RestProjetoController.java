package pt.ul.fc.css.example.democracia2.controllers.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.democracia2.dto.ProjetoLeiDTO;
import pt.ul.fc.css.example.democracia2.dto.PropostaLeiDTO;
import pt.ul.fc.css.example.democracia2.entities.Cidadao;
import pt.ul.fc.css.example.democracia2.entities.ProjetoLei;
import pt.ul.fc.css.example.democracia2.entities.PropostaLei;
import pt.ul.fc.css.example.democracia2.services.CidadaoService;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("api")
public class RestProjetoController {

    @Autowired
    private ProjetoService projetoServ;

    @Autowired
    private CidadaoService cidadaoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/projetos/{id}")
    public Optional<ProjetoLeiDTO> getProjeto(@PathVariable Long id) {
        Optional<ProjetoLei> p = projetoServ.getProjetoById(id);
        if(p.isPresent()){
            ProjetoLei pl = p.get();
            return Optional.ofNullable(modelMapper.map(pl, ProjetoLeiDTO.class));
        }
        return Optional.empty();
    }

    @GetMapping("/proposta/{id}")
    public Optional<PropostaLeiDTO> getPropostaById(@PathVariable Long id) {
        PropostaLei p = projetoServ.getPropostaById(id).get();
        return Optional.ofNullable(modelMapper.map(p, PropostaLeiDTO.class));
    }

    @GetMapping("/projetos")
    public List<ProjetoLeiDTO> getProjetos() {
        List<ProjetoLei> l=  projetoServ.getProjetosNaoExpirados();
        List<ProjetoLeiDTO> listaDTO =  l.stream().map(projeto -> modelMapper.map(projeto, ProjetoLeiDTO.class))
                .collect(Collectors.toList());
        return listaDTO;
    }



    @GetMapping("/votacoes-em-curso")
    public List<PropostaLeiDTO> getVotacoesEmCurso() {
        List<PropostaLei> l = projetoServ.getVotacoesEmCurso();
        System.out.println(l);
        List<PropostaLeiDTO> listaDTO =  l.stream().map(proposta -> modelMapper.map(proposta, PropostaLeiDTO.class))
                .collect(Collectors.toList());

        System.out.println(listaDTO);
        return listaDTO;
    }


    @PostMapping("/projetos")
    public ResponseEntity<ProjetoLei> criaProjeto(@NonNull @RequestBody ProjetoLei projeto) {
        ProjetoLei projetoCriado = projetoServ.apresentarProjetoLei(projeto);
        System.out.println("TITULO");
        System.out.println(projetoCriado.getTitulo());

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(projetoCriado);
    }

    @PostMapping("/projetos/{id}/apoiar")
    public ResponseEntity<ProjetoLei> apoiarProjeto(@NonNull @PathVariable Long id, @RequestBody Long cidadaoId) {
        Optional<Cidadao> c = cidadaoService.getCidadao(cidadaoId);
        Optional<ProjetoLei> p = projetoServ.getProjetoById(id);

        if(!c.isPresent() || !p.isPresent()){
            return ResponseEntity.badRequest().build();
        }

        projetoServ.apoiarProjetoLei(cidadaoId, id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, "application/json;charset=UTF-8");

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(p.get());
    }
}
