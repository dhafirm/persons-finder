package nz.co.dhafir.supplier.manager;

import nz.co.dhafir.supplier.datastore.impl.EmailDAOStub;
import nz.co.dhafir.supplier.domain.Email;
import nz.co.dhafir.supplier.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
class SupplierEmailManagerStubTest {
    @Mock
    private EmailService emailService;

    @Mock
    private EmailDAOStub emailDAOStub;

    @InjectMocks
    private  SupplierEmailManagerStub supplierEmailManagerStub;

    @Test
    void findSupplierEmails() {
        when(emailDAOStub.findSupplierEmails(100L)).thenReturn(List.of(new Email(), new Email()));
        List<Email> emails = supplierEmailManagerStub.findSupplierEmails(100L);
        verify(emailDAOStub).findSupplierEmails(100L);
    }

    @Test
    void findSupplierEmailByEmailId() {
        when(emailDAOStub.findSupplierEmailByEmailId(100L, 1000L)).thenReturn(Optional.of(new Email()));
        Optional<Email> email = supplierEmailManagerStub.findSupplierEmailByEmailId(100L, 1000L);
        verify(emailDAOStub).findSupplierEmailByEmailId(100L, 1000L);
    }

    @Test
    void saveDraftEmail() {
        Email email = new Email();
        supplierEmailManagerStub.saveDraftEmail(100L, email);
        verify(emailDAOStub).createDraftEmail(100L, email);
    }

    @Test
    void sendEmail_shouldSaveAndSendNewEmail() {
        Email email = new Email();
        when(emailDAOStub.createDraftEmail(100L, email)).thenReturn(email);
        supplierEmailManagerStub.sendEmail(100L, email);
        verify(emailService).sendEmail(email);
        assertFalse(email.isDraft());
    }

    @Test
    void updateEmailRecipients() {
        Email email = new Email();
        email.setId(1000L);
        email.setRecipients(List.of("old@hotmail.com"));
        List<String> recipients = List.of("new1@hotmail.com", "new2@gmail.com");
        Email updatedEmail = supplierEmailManagerStub.updateEmailRecipients(100L, 1000L, recipients);
        verify(emailDAOStub).updateEmailRecipients(100L, 1000L, recipients);
    }
}