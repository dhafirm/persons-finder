package nz.co.dhafir.supplier.error;

import lombok.AllArgsConstructor;


public class EmailException extends RuntimeException {
    public EmailException(String errorMsg) {
        super(errorMsg);
    }
}
