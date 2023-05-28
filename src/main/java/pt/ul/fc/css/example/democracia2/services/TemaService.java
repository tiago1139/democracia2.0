package pt.ul.fc.css.example.democracia2.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.example.democracia2.dto.TemaDTO;
import pt.ul.fc.css.example.democracia2.entities.Tema;
import pt.ul.fc.css.example.democracia2.repositories.TemaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TemaService {

    @Autowired
    private TemaRepository temaRepo;

    @Autowired
    private ModelMapper modelMapper;

    public Tema createTema(Tema tema){
        return temaRepo.save(tema);
    }



    public List<Tema> getTemas(){
        return temaRepo.findAll();
    }

    public List<TemaDTO> getTemasDTO(){

        return getTemas().stream().map(post -> modelMapper.map(post, TemaDTO.class))
                .collect(Collectors.toList());
    }

    public Optional<Tema> getTema (Long id) {
        Optional<Tema> tema = temaRepo.findById(id);

        if (tema.isPresent()) {
            return tema;
        } else {
            return null;
        }
    }
}
