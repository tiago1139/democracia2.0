package com.example.clientdesktop.models;

import java.util.List;
import java.util.Map;

public class Delegado extends Cidadao{


    public Delegado(){}

    public Delegado(Long id, String nome, String apelido, List<ProjetoLei> projApoiados, List<PropostaLei> projVotados, Map<Long, Long> delegados) {
        super(id, nome, apelido, projApoiados, projVotados, delegados);
    }

}
