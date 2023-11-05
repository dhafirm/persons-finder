package nz.co.dhafir.supplier.error;

public class EmailNotFoundException extends EmailException {
    public EmailNotFoundException() {
        super("Email not found");
    }
}
