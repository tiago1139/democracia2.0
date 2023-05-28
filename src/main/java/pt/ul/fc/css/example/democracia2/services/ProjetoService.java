package pt.ul.fc.css.example.democracia2.services;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.democracia2.dto.ProjetoLeiDTO;
import pt.ul.fc.css.example.democracia2.entities.*;
import pt.ul.fc.css.example.democracia2.repositories.ProjetoRepository;
import pt.ul.fc.css.example.democracia2.repositories.VotacaoPublicaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepo;

    @Autowired
    private ProjetoRepository<PropostaLei> propostaRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CidadaoService cidadaoService;

    @Autowired
    private VotacaoPublicaRepository votacaoPublicaRepository;

    public ProjetoLei apresentarProjetoLei(ProjetoLei proj){

        if(proj.getTitulo().equals(null)
                || proj.getProponente().equals(null)
                || proj.getTema().equals(null)
                || proj.getValidade().equals(null)
                || proj.getDescricao().equals(null)) {

            throw new IllegalStateException("O projeto nao eh valido.");
        }

        if(LocalDateTime.now().plusYears(1L).isBefore(proj.getValidade())){
            proj.setValidade(LocalDateTime.now().plusYears(1L));
        }


        Optional<ProjetoLei> projetoEncontrado = projetoRepo.findByTituloAndProponente(proj.getTitulo(), proj.getProponente());

        if(projetoEncontrado.isPresent()) {
            throw new IllegalStateException("Este projeto ja existe.");
        }
        ProjetoLei projCriado = (ProjetoLei) projetoRepo.save(proj);
        Delegado delegado = projCriado.getProponente();
        delegado.addProjetoapoiado(projCriado);

        cidadaoService.atualizarDelegado(delegado);

        return projCriado;
    }

    public ProjetoLeiDTO apresentarProjetoLeiDTO(ProjetoLei proj){

        ProjetoLei projCriado = apresentarProjetoLei(proj);
        System.out.println("PROJETO CRIADO: "+projCriado);
        return modelMapper.map(projCriado, ProjetoLeiDTO.class);
    }

    public Optional<ProjetoLei> getProjetoById(Long id) {

        Optional<ProjetoLei> projetoById = projetoRepo.findById(id);

        if(projetoById.isPresent()) {
            return projetoById;
        } else {
            throw new IllegalStateException("O projeto com o id "+id+" nao existe.");
        }
    }
    public ProjetoLeiDTO dtofyProjeto(ProjetoLei p) {
        return modelMapper.map(p, ProjetoLeiDTO.class);
    }

    public List<ProjetoLei> getAllProjetos(){
        return projetoRepo.findAll();
    }

    public List<ProjetoLei> getProjetosNaoFechados(){
        return projetoRepo.findProjetosNaoExpirados();
    }

    public List<ProjetoLei> getProjetosNaoExpirados(){
        List<ProjetoLei> projetos = getProjetosNaoFechados();

        return projetos.stream()
                .filter(p -> p.getValidade().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

    }


    public List<PropostaLei> getPropostasAtivas(){
        return projetoRepo.findAllActivePropostas();
    }

    public List<PropostaLei> getVotacoesEmCurso() {
        List<PropostaLei> propostas = getPropostasAtivas();
        return propostas.stream()
                .filter(p -> p.getValidade().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public List<ProjetoLei> fecharProjetosExpirados(){
        List<ProjetoLei> projetos = getProjetosNaoFechados();

        projetos = projetos.stream()
                .filter(p -> p.getValidade().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());

        for(ProjetoLei p : projetos){
            p.setFechado(true);
        }

        projetoRepo.saveAll(projetos);

        return projetos;

    }

    @Transactional
    public void fechaProjetosExpirados(){
        projetoRepo.fechaPropostasExpiradas(LocalDateTime.now());
    }

    @Transactional
    public void apoiarProjetoLei(long cidadao_id,long projeto_id){
        Optional<Cidadao> cd = cidadaoService.getCidadao(cidadao_id);
        Optional<ProjetoLei> pl = this.getProjetoById(projeto_id);


        if(cd != null && pl != null){
            Cidadao cidadao = cd.get();
            ProjetoLei projetoLei = pl.get();
            boolean estahFechado = projetoLei.getFechado();
            boolean verifica = cidadaoService.verificaSeJaApoiou(cidadao,projetoLei);
            if(!verifica && !estahFechado ){
                cidadao = cidadaoService.adicionaListaApoio(cidadao,projetoLei);
                projetoLei.adicionaApoio();
                this.projetoRepo.save(projetoLei);
                if(projetoLei.verificaApoios()) {
                    LocalDateTime dataProposta = verificaDataProjeto(projetoLei.getValidade());
                    PropostaLei propostaLei = new PropostaLei(projetoLei, dataProposta);

                    Long delegado_id = propostaLei.getProponente().getId();
                    Delegado d = (Delegado) cidadaoService.getCidadao(delegado_id).get();

                    VotacaoPublica votacaoPublica = new VotacaoPublica(d, Voto.FAVORAVEL, propostaLei);
                    votacaoPublicaRepository.save(votacaoPublica);

                    propostaLei = incrementaVotos(propostaLei, Voto.FAVORAVEL);

                    this.propostaRepo.save(propostaLei);

                    d = cidadaoService.atualizaVotosDelegado(d, propostaLei, votacaoPublica);
                    cidadaoService.atualizarCidadao(d);
                }
                cidadaoService.atualizarCidadao(cidadao);
            }
        }
    }

    public void fechaVotacoes(){
        List<PropostaLei> ppls = projetoRepo.findPropostasExpiradas(LocalDateTime.now());
        for(PropostaLei p:ppls){
            fechaVotacao(p);
        }
    }


    public boolean fechaVotacao(@NonNull PropostaLei propostaLei){
        if(propostaLei.verificaValidade() == false){
            PropostaLei propExpirada = propostaLei;

            List<Cidadao> cidadaosSemVoto = cidadaoService.getCidadaosSemVoto(propExpirada.getId());

            if(!cidadaosSemVoto.isEmpty()){

                propExpirada = votaComVotosdeDelegado(propExpirada, cidadaosSemVoto);
            }

            propExpirada.fechaProjeto(); //fechamos a proposta

            if(propExpirada.getVotosFavoraveis() > propExpirada.getVotosDesfavoraveis()){
                propExpirada.setAprovada(true);
            }else{
                propExpirada.setAprovada(false);
            }
            projetoRepo.save(propExpirada);

            return true;

        }
        return false;
    }

    /*
    *
    * METODOS AUXILIARES
    *
    * */

    private PropostaLei votaComVotosdeDelegado(PropostaLei propostaLei, List<Cidadao> cidadaosSemVoto) {
        Tema t = propostaLei.getTema();
        for(Cidadao cSemVoto: cidadaosSemVoto){
            if(cSemVoto.getDelegados().isEmpty()){continue;}
            Delegado d = this.getDelegadoComVotacao(cSemVoto, propostaLei, t);
            if(d == null){continue;}
            VotacaoPublica votacaoDelegado = this.getVotacaoByProposta(d.getId(), propostaLei.getId());
            if(votacaoDelegado==null){continue;}
            Voto voto = votacaoDelegado.getVoto();
            propostaLei = incrementaVotos(propostaLei, voto);
            cidadaoService.adicionaProjetoVotado(cSemVoto,propostaLei);
        }
        return propostaLei;

    }

    private boolean containsProposta(List<VotacaoPublica> list, PropostaLei proposta){
        return list.stream().filter(o -> o.getPropostaLei().equals(proposta)).findFirst().isPresent();
    }

    private LocalDateTime verificaDataProjeto(LocalDateTime validade) {
        LocalDateTime agora = LocalDateTime.now();
        long tempo = DAYS.between(agora, validade);

        if(tempo < 15L){
            System.out.println(tempo);
            return(validade.plusDays(15-1-tempo));
        } else if(tempo > 61L){// 30+31 = 2 meses +-
            return validade.minusDays(tempo-61L);
        } else {
            return validade;
        }
    }


    private Delegado getDelegadoComVotacao(Cidadao cidadao, PropostaLei proposta, Tema tema) {
        Delegado d = cidadao.getDelegados().get(tema);
        while((d == null || !containsProposta(d.getVotacoes(),proposta) ) && tema != null){
            Tema tpai = tema.getTemaPai();
            tema = tpai;
            if(tpai != null){
                d = cidadao.getDelegados().get(tpai);
            }

        }
        return d;
    }

    private VotacaoPublica getVotacaoByProposta(Long delegadoId, Long propostaId) {
        return votacaoPublicaRepository.findVotobyDelegadoAndProjeto(delegadoId, propostaId);
    }

    protected PropostaLei incrementaVotos(PropostaLei propostaLei, Voto voto) {
        if(voto.equals(Voto.FAVORAVEL)){
            propostaLei.setVotosFavoraveis(propostaLei.getVotosFavoraveis()+1);
        }else if(voto.equals(Voto.DESFAVORAVEL)){
            propostaLei.setVotosDesfavoraveis(propostaLei.getVotosDesfavoraveis()+1);
        }
        return propostaLei;
    }




    public Optional<PropostaLei> getPropostaById(long id_proposta) {
        Optional<PropostaLei> propostaById = projetoRepo.findPropostabyId(id_proposta);

        if(propostaById.isPresent()) {
            return propostaById;
        } else {
            throw new IllegalStateException("A proposta com o id "+id_proposta+" nao existe.");
        }

    }

    public Tema findTemabyProposta(long id_proposta) {
        return projetoRepo.findTemabyProposta(id_proposta);
    }
}
