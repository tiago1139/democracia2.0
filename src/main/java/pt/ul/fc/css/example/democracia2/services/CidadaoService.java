package pt.ul.fc.css.example.democracia2.services;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.democracia2.dto.DelegadoDTO;
import pt.ul.fc.css.example.democracia2.entities.*;
import pt.ul.fc.css.example.democracia2.repositories.CidadaoRepository;
import pt.ul.fc.css.example.democracia2.repositories.TemaRepository;
import pt.ul.fc.css.example.democracia2.repositories.VotacaoPublicaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CidadaoService {

    @Autowired
    private CidadaoRepository cidadaoRepo;

    @Autowired
    private TemaRepository temaRepo;

    @Lazy
    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VotacaoPublicaRepository votacaoPublicaRepository;

    public Cidadao criaCidadao(Cidadao cidadao){
        return (Cidadao) cidadaoRepo.save(cidadao);
    }

    public Delegado criaDelegado (Delegado delegado){
        System.out.println(delegado);
        return (Delegado) cidadaoRepo.save(delegado);
    }


    public Cidadao atualizarCidadao(Cidadao cidadao){

        Cidadao cidadaoAdicionado = (Cidadao) this.cidadaoRepo.save(cidadao);
        return cidadaoAdicionado;
    }

    public List<Cidadao> getAllCidadaos() {
        return cidadaoRepo.findAll();
    }

    public List<Delegado> getAllDelegados(){
        return cidadaoRepo.findAllDelegados();
    }

    public List<DelegadoDTO> getAllDelegadosDTO() {
        return getAllDelegados().stream().map(post -> modelMapper.map(post, DelegadoDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<Cidadao> getCidadao (Long id) {
        Optional<Cidadao> cidadao = cidadaoRepo.findById(id);

        if (cidadao.isPresent()) {
            return cidadao;
        } else {
            return null;
        }
    }

    public List<Cidadao> getCidadaosComVoto(Long propostaId) {
        return cidadaoRepo.findCidadaosComVoto(propostaId);
    }

    public List<Cidadao> getCidadaosSemVoto(Long propostaId) {
        List<Cidadao> listaCidadaos = this.getAllCidadaos();
        List<Cidadao> listaCidadaoComVotos = this.getCidadaosComVoto(propostaId);
        List<Cidadao> listaCidadaoSemVotos = listaCidadaos.stream()
                .filter(l->!listaCidadaoComVotos.contains(l)).collect(Collectors.toList());

        return listaCidadaoSemVotos;
    }

    public boolean verificaSeJaApoiou(Cidadao cidadao, ProjetoLei projetoLei) {
        Cidadao c = this.cidadaoRepo.findCidadaosSemApoio(cidadao.getId(),projetoLei.getId());
        return c != null;
    }

    public Cidadao adicionaListaApoio(Cidadao cidadao, ProjetoLei projetoLei) {
        cidadao.addProjetoapoiado(projetoLei);
        return cidadao;
    }

    public Cidadao adicionaProjetoVotado(Cidadao cidadao, PropostaLei propostaLei){
        List<PropostaLei> lpl = cidadao.getProjVotados();
        lpl.add(propostaLei);
        cidadao.setProjVotados(lpl);
        return this.atualizarCidadao(cidadao);
    }

    public Delegado atualizaVotosDelegado(Delegado d, PropostaLei propostaLei, VotacaoPublica votacaoPublica) {
        d.addPropostavotada(propostaLei);
        d.adicionaVotacaoPublica(votacaoPublica);
        return d;
    }

    public Cidadao escolheDelegado(Long cidadaoId, Long temaId, Long delegadoId){
        Optional<Cidadao> cidadaoById = cidadaoRepo.findById(cidadaoId);
        Optional<Tema> temaById = temaRepo.findById(temaId);
        Optional<Delegado> delegadoById = cidadaoRepo.findById(delegadoId);
        
        if(!cidadaoById.isPresent()) {
            throw new IllegalStateException("Cidadao nao existe.");
        }
        if(!temaById.isPresent()) {
            throw new IllegalStateException("Tema escolhido nao existe!");
        }
        if (!delegadoById.isPresent()) {
            throw new IllegalStateException("Delegado escolhido nao existe!");
        }

        Cidadao c = cidadaoById.get();
        Tema t = temaById.get();
        Delegado d = delegadoById.get();
        Map<Tema, Delegado> delegadoMap = c.getDelegados();
        if(c.getDelegados().containsKey(t)) {
            delegadoMap.replace(t, delegadoMap.get(t), d);
        } else {
            delegadoMap.put(t,d);
        }
        c.setDelegados(delegadoMap);

        return (Cidadao) cidadaoRepo.save(c);
    }

    @Transactional
     public boolean votarProposta(long idCidadao, long idProposta,Voto voto) {
        if(voto != Voto.SEMVOTO){
            Optional<Cidadao> c = getCidadao(idCidadao);
            Optional<PropostaLei> ppl = projetoService.getPropostaById(idProposta);
            if(ppl.isPresent() && c.isPresent()){
                Cidadao cGet = c.get();
                PropostaLei pGet = ppl.get();
                boolean jaVotou = cGet.getProjVotados().contains(pGet);
                if(!jaVotou){
                    cGet.addPropostavotada(pGet);
                    pGet = projetoService.incrementaVotos(pGet,voto);
                    cGet = (Cidadao) cidadaoRepo.save(cGet);
                    if(cGet == null) {
                        return false;
                    }
                    if(cGet.getClass().equals(Delegado.class)){
                        Delegado d = getDelegadoById(idCidadao);
                        VotacaoPublica vp = new VotacaoPublica(d,voto,pGet);
                        votacaoPublicaRepository.save(vp);
                        d.adicionaVotacaoPublica(vp);
                        cidadaoRepo.save(d);
                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public void atualizarDelegado(Delegado delegado){
        cidadaoRepo.save(delegado);
    }

    private Delegado getDelegadoById(long idCidadao) {
       return cidadaoRepo.findDelegadobyID(idCidadao);
    }

    public Voto getVotoDelegado(long idCidadao, long propostaid) {
        Cidadao c = getCidadao(idCidadao).get();
        if (c.getDelegados().isEmpty()) {
            return Voto.SEMVOTO;
        } else {
            Tema t = projetoService.findTemabyProposta(propostaid);
            Delegado d = c.getDelegados().get(t);
            VotacaoPublica vp = votacaoPublicaRepository.findVotobyDelegadoAndProjeto(d.getId(),propostaid);
            if (vp != null) {
                return vp.getVoto();

        }
        return Voto.SEMVOTO;
    }

    }
}
