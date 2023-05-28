package pt.ul.fc.css.example.democracia2.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pt.ul.fc.css.example.democracia2.entities.Cidadao;
import pt.ul.fc.css.example.democracia2.services.CidadaoService;
import pt.ul.fc.css.example.democracia2.services.TemaService;

@Controller
public class WebDelegadoController {

    @Autowired
    private CidadaoService cidadaoServ;

    @Autowired
    private TemaService temaServ;

    public WebDelegadoController() {
        super();
    }


    @GetMapping("/escolher-delegado")
    public String escolherDelegado(final Model model) {
        model.addAttribute("cidadaos", cidadaoServ.getAllCidadaos());
        model.addAttribute("temas", temaServ.getTemas());

        model.addAttribute("delegados", cidadaoServ.getAllDelegados());
        return "escolher_delegado";
    }

    @PostMapping("/escolher-delegado")
    public String escolherDelegadoAction(Model model, @RequestParam(value = "c_escolhido") Long cidadao,
                                         @RequestParam(value = "t_escolhido") Long tema,
                                         @RequestParam(value = "del_escolhido") Long delegado){
        try{
            Cidadao cTeste = cidadaoServ.getCidadao(cidadao).get();
            System.out.println("antes da função " + cTeste.getDelegados());

            cTeste = cidadaoServ.escolheDelegado(cidadao, tema, delegado);

            System.out.println("depois da função " + cTeste.getDelegados());

            return "redirect:/";

        }catch(Exception e){
            System.out.println("entrei no erro");
            return "error/404";
        }

    }


}
