package pt.ul.fc.css.example.democracia2.exception;

import java.io.IOException;

public class PDFException extends Exception {
    public PDFException(String erro, IOException ex) {
        super(erro);
    }

    public PDFException(String erro) {
        super(erro);
    }
}
