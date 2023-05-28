package pt.ul.fc.css.example.democracia2.controllers.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pt.ul.fc.css.example.democracia2.entities.Cidadao;
import pt.ul.fc.css.example.democracia2.entities.PropostaLei;
import pt.ul.fc.css.example.democracia2.entities.Voto;
import pt.ul.fc.css.example.democracia2.services.CidadaoService;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class WebCidadaoController {

    Logger logger = LoggerFactory.getLogger(WebCidadaoController.class);

    @Autowired
    private ProjetoService projetoServ;


    @Autowired
    private CidadaoService cidadaoServ;

    public WebCidadaoController() {
        super();
    }

    @GetMapping("/votar-proposta/cidadao/{c_id}/proposta/{p_id}")
    public String votarPropostaGet(final Model model, @PathVariable Long c_id, @PathVariable Long p_id) {
        Optional<PropostaLei> p = projetoServ.getPropostaById(p_id);
        Optional<Cidadao> c = cidadaoServ.getCidadao(c_id);
        if(p.isPresent() && c.isPresent()){
            System.out.print(p.get().getId());
            logger.error("CID:", p.get().getId());
            model.addAttribute("proposta", p.get());
            model.addAttribute("cidadao", c.get());
            model.addAttribute("votos",
                    Arrays.stream(Voto.values()).
                            filter(v -> !v.equals(Voto.SEMVOTO))
                            .collect(Collectors.toList()));
            model.addAttribute("voto_delegado", cidadaoServ.getVotoDelegado(c_id, p_id));
            return "votarProposta";
        }else{
            return "error/404";
        }


    }

    @PostMapping("/votar-proposta/cidadao/{c_id}/proposta/{p_id}")
    public String votarPropostaPut(final Model model,
                                   RedirectAttributes redirectAttributes,
                                   @PathVariable Long c_id, @PathVariable Long p_id,
                                 @RequestParam(value = "voto") String voto
                                 ) {

        boolean votou = false;
        Optional<PropostaLei> p = projetoServ.getPropostaById(p_id);
        Optional<Cidadao> c = cidadaoServ.getCidadao(c_id);
        if(p.isPresent() && c.isPresent()) {
            Voto v = Voto.valueOf(voto);
            votou = cidadaoServ.votarProposta(c_id, p_id, v);

        }
        if (!votou) {
            redirectAttributes.addFlashAttribute("error",
                    "Votação falhou!\nCausas possíveis:\n1- Já votou nesta proposta\n2- Erro ao processar");
            return "redirect:/votar-proposta/cidadao/" + c_id + "/proposta/" + p_id;

        }
        redirectAttributes.addFlashAttribute("success", "Votou " + voto + " com sucesso! Obrigado");
        return "redirect:/votar-proposta/cidadao/" + c_id + "/proposta/" + p_id;

    }
}
