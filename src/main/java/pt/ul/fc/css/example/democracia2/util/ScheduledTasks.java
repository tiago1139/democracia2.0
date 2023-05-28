package pt.ul.fc.css.example.democracia2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.ul.fc.css.example.democracia2.services.ProjetoService;

@Component
public class ScheduledTasks {

    @Autowired
    ProjetoService projetoService;

    @Scheduled(fixedDelay = 4000)
    public void fecharProjetosExpirados(){
        projetoService.fechaProjetosExpirados();
    }

    @Scheduled(fixedDelay = 4000)
    public void fecharVotacoesExpiradas(){
        projetoService.fechaVotacoes();
    }

}
