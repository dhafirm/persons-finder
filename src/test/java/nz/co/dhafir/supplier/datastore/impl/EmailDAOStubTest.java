package nz.co.dhafir.supplier.datastore.impl;

import nz.co.dhafir.supplier.domain.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class EmailDAOStubTest {

    private EmailDAOStub emailDAOStub ;

    private static Email generateEmail(long seq) {
        return Email.builder().subject("Subj " + seq).body("Body " + seq).sender("sender " + seq)
                .recipients(List.of("Rec "  + seq)).build();
    }


    @Test
    void findSupplierEmails() {
        emailDAOStub = new EmailDAOStub();
        Email email1 = emailDAOStub.createDraftEmail(100L, generateEmail(1000L));
        Email email2 = emailDAOStub.createDraftEmail(100L, generateEmail(1001L));
        Email email3 = emailDAOStub.createDraftEmail(100L, generateEmail(1002L));
        Email email4 = emailDAOStub.createDraftEmail(102L, generateEmail(1003L));
        Email email5 = emailDAOStub.createDraftEmail(102L, generateEmail(1004L));

        assertThat(emailDAOStub.findSupplierEmails(100L).size()).isEqualTo(3);
        assertThat(emailDAOStub.findSupplierEmails(102L).size()).isEqualTo(2);
    }

    @Test
    void findSupplierEmailByEmailId_shouldReturnValidEmail() {
        emailDAOStub = new EmailDAOStub();
        Email email1 = emailDAOStub.createDraftEmail(100L, generateEmail(1000L));
        Email email2 = emailDAOStub.createDraftEmail(100L, generateEmail(1001L));
        Email email3 = emailDAOStub.createDraftEmail(100L, generateEmail(1002L));
        Email email4 = emailDAOStub.createDraftEmail(102L, generateEmail(1003L));
        Email email5 = emailDAOStub.createDraftEmail(102L, generateEmail(1004L));

        assertThat(emailDAOStub.findSupplierEmailByEmailId(100L, email1.getId() ).get().getId()).isEqualTo(email1.getId());
        assertThat(emailDAOStub.findSupplierEmailByEmailId(102L, email4.getId() ).get().getId()).isEqualTo(email4.getId());
    }

    @Test
    void findSupplierEmailByEmailId_shouldReturnEmptyEmail() {
        emailDAOStub = new EmailDAOStub();
        Email email1 = emailDAOStub.createDraftEmail(100L, generateEmail(1000L));
        Email email2 = emailDAOStub.createDraftEmail(100L, generateEmail(1001L));
        Email email3 = emailDAOStub.createDraftEmail(100L, generateEmail(1002L));
        Email email4 = emailDAOStub.createDraftEmail(102L, generateEmail(1003L));
        Email email5 = emailDAOStub.createDraftEmail(102L, generateEmail(1004L));

        assertTrue(emailDAOStub.findSupplierEmailByEmailId(100L, 1004L ).isEmpty());
        assertTrue(emailDAOStub.findSupplierEmailByEmailId(102L, 1001L ).isEmpty());
    }

    @Test
    void saveDraftEmail() {
        emailDAOStub = new EmailDAOStub();
        Email email = emailDAOStub.createDraftEmail(100L, generateEmail(1000L));
        assertThat(emailDAOStub.findSupplierEmailByEmailId(100L, email.getId())
                .get().getId()).isEqualTo(email.getId());
    }

    @Test
    void updateEmailRecipients() {
        emailDAOStub = new EmailDAOStub();
        Email email1 = emailDAOStub.createDraftEmail(100L, generateEmail(1000L));
        Email email2 = emailDAOStub.createDraftEmail(100L, generateEmail(1001L));
        Email email3 = emailDAOStub.createDraftEmail(100L, generateEmail(1002L));
        Email email4 = emailDAOStub.createDraftEmail(102L, generateEmail(1003L));
        Email email5 = emailDAOStub.createDraftEmail(102L, generateEmail(1004L));

        email1.setRecipients(List.of("Old@gmail.com"));
        emailDAOStub.createDraftEmail(100L, email1);

        Email updatedEmail = emailDAOStub.updateEmailRecipients(100L, email1.getId(), List.of("new@yahoo.com"));

        assertThat(updatedEmail.getRecipients().size()).isEqualTo(1);
        assertTrue(updatedEmail.getRecipients().contains("new@yahoo.com"));
    }
}