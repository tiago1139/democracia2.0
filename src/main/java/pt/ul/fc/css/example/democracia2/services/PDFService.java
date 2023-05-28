package pt.ul.fc.css.example.democracia2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pt.ul.fc.css.example.democracia2.entities.PDF;
import pt.ul.fc.css.example.democracia2.exception.PDFException;
import pt.ul.fc.css.example.democracia2.repositories.PDFRepository;

import java.io.IOException;
import java.util.Optional;

/**
 * Este Servico foi pensada com base na leitura da seguinte pagina:
 * https://www.callicoder.com/spring-boot-file-upload-download-jpa-hibernate-mysql-database-example/
 */

@Service
public class PDFService {

    @Autowired
    private PDFRepository pdfRepo;

    public PDF enviaPdf(MultipartFile ficheiro) throws PDFException {
        String nomeFicheiro = StringUtils.cleanPath(ficheiro.getOriginalFilename());

        try {
            // Verifica se o nome do ficheiro tem carateres invalidos
            if(nomeFicheiro.contains("..")) {
                throw new PDFException("Erro! O nome do ficheiro tem uma sequencia invalida" + nomeFicheiro);
            }

            PDF dbFile = new PDF(nomeFicheiro, ficheiro.getContentType(), ficheiro.getBytes());

            return pdfRepo.save(dbFile);
        } catch (IOException ex) {
            throw new PDFException("Nao foi possivel guardar o ficheiro " + nomeFicheiro + ". Por favor tente de novo!", ex);
        }
    }

    public Optional<PDF> getPDFbyId(Long id) {

        return pdfRepo.findById(id);
    }
}
