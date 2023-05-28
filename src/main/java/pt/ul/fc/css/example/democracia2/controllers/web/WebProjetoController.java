package pt.ul.fc.css.example.democracia2.controllers.web;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pt.ul.fc.css.example.democracia2.dto.DelegadoDTO;
import pt.ul.fc.css.example.democracia2.dto.ProjetoFormDTO;
import pt.ul.fc.css.example.democracia2.dto.ProjetoLeiDTO;
import pt.ul.fc.css.example.democracia2.dto.TemaDTO;
import pt.ul.fc.css.example.democracia2.entities.*;
import pt.ul.fc.css.example.democracia2.services.CidadaoService;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;
import pt.ul.fc.css.example.democracia2.services.TemaService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class WebProjetoController {

    Logger logger = LoggerFactory.getLogger(WebProjetoController.class);

    @Autowired
    private ProjetoService projetoServ;

    @Autowired
    private TemaService temaServ;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CidadaoService cidadaoServ;

    public WebProjetoController() {
        super();
    }

    @GetMapping({ "/", "/votacoes-em-curso" })
    public String index(final Model model) {
        model.addAttribute("votacoes", projetoServ.getVotacoesEmCurso());
        return "votacoes_em_curso_lista";
    }

    @GetMapping("/votacoes/{id}")
    public String votacaoDetail(final Model model, @PathVariable Long id) {
        Optional<PropostaLei> p = projetoServ.getPropostaById(id);
        if(p.isPresent()){
            System.out.print(p.get().getId());
            logger.error("CID:", p.get().getId());
            model.addAttribute("proposta", p.get());
            model.addAttribute("cidadaos", cidadaoServ.getAllCidadaos());
            model.addAttribute("escolhido", 0L);
            return "proposta_detail";
        }else{
            return "error/404";
        }
    }

    @PostMapping("/votacoes/{id}")
    public String votacaoDetailPost(final Model model, @PathVariable Long id,
                                    @RequestParam(value = "c_escolhido") Long cidadao) {

        return "redirect:/votar-proposta/cidadao/" + cidadao + "/proposta/"+ id;
    }

    @GetMapping("/projetos/{id}")
    public String projetoDetail(final Model model, @PathVariable Long id) {
        Optional<ProjetoLei> p = projetoServ.getProjetoById(id);
        if(p.isPresent()){
            model.addAttribute("cidadaos", cidadaoServ.getAllCidadaos());
            model.addAttribute("projeto", p.get());
            model.addAttribute("delegado",p.get().getProponente().getNome());
            model.addAttribute("tema",p.get().getTema().getNome());
            return "projeto_detail";
        }else{
            return "error/404";
        }
    }

    @GetMapping({ "/projetos" })
    public String projetosEmCurso(final Model model) {
        model.addAttribute("projetos", projetoServ.getProjetosNaoExpirados());
        return "projetos";
    }


    @GetMapping("/projetos/new")
    public String newProjetoLei(final Model model) {
        List<TemaDTO> temasDTO =  temaServ.getTemas().stream().map(tema -> modelMapper.map(tema, TemaDTO.class))
                .collect(Collectors.toList());

        List<DelegadoDTO> delegadosDTO = cidadaoServ.getAllDelegados().stream().map(delegado -> modelMapper.map(delegado, DelegadoDTO.class))
                .collect(Collectors.toList());

        model.addAttribute("projeto", new ProjetoFormDTO());
        model.addAttribute("temas", temasDTO);
        model.addAttribute("delegados", delegadosDTO);


        return "projeto_add";
    }


    @PostMapping("/projetos/new")
    public String newProjetoLeiAction(final Model model, @ModelAttribute ProjetoFormDTO p) {
        ProjetoLeiDTO p2;
        Tema t = temaServ.getTema(p.getTema()).get();
        Delegado d = (Delegado) cidadaoServ.getCidadao(p.getDelegado()).get();


        ProjetoLei pl = new ProjetoLei(p.getTitulo(), p.getDescricao(), p.getValidade(), d, t);
        try {
            p2 = projetoServ.apresentarProjetoLeiDTO(pl);
            return "redirect:/projetos/" + p2.getId();

        } catch (Exception e) {
            p2 = new ProjetoLeiDTO();
            model.addAttribute("projeto", p2);
            model.addAttribute("error", e.getMessage());
            return "projeto_add";
        }
    }


    @PostMapping("/projetos/{id}/apoiar")
    public String apoiarProjeto(final Model model, @PathVariable Long id, @RequestParam(value = "c_apoia") Long cidadao ) {
        try{
            Cidadao c = cidadaoServ.getCidadao(cidadao).get();
            System.out.println(c.getNome());
            System.out.println(c.getApelido());

            projetoServ.apoiarProjetoLei(cidadao,id);

            return "redirect:/projetos";
        }catch(Exception e){
            return "projeto_detail";
        }
    }

}
