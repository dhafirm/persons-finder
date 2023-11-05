package nz.co.dhafir.supplier.manager;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import nz.co.dhafir.supplier.datastore.impl.EmailDAOStub;
import nz.co.dhafir.supplier.domain.Email;
import nz.co.dhafir.supplier.error.EmailNotFoundException;
import nz.co.dhafir.supplier.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component("supplierEmailManagerStub")
public class SupplierEmailManagerStub implements SupplierEmailManager {


    @Autowired
    private EmailDAOStub  emailDAOStub;

    @Autowired
    private EmailService emailSender;

    @Override
    public List<Email> findSupplierEmails(long supplierId) {
        return emailDAOStub.findSupplierEmails(supplierId);
    }

    @Override
    public Optional<Email> findSupplierEmailByEmailId(long supplierId, long emailId) {
        return emailDAOStub.findSupplierEmailByEmailId(supplierId, emailId);
    }

    @Override
    public Email saveDraftEmail(long supplierId, @NotNull final Email email) {
        return emailDAOStub.createDraftEmail(supplierId, email);
    }

    /**
     * Saves the email. If the email doesn't exist then it will add it to the datastore first.
     * @param supplierId
     * @param emailId
     */
    @Override
    public Email sendEmail(long supplierId, long emailId) {
        // find draft email
        Optional<Email> maybeEmail = findSupplierEmailByEmailId(supplierId, emailId);

        if (maybeEmail.isEmpty()) {
            // new Email, save it
            throw new EmailNotFoundException();
        }

        Email email = maybeEmail.get();
        emailSender.sendEmail(email);
        email.setDraft(false);
        return email;
    }

    @Override
    public Email sendEmail(long supplierId, @NotNull final Email emailToSend) {
        // update/save the email first
        Email email = saveDraftEmail(supplierId, emailToSend);

        emailSender.sendEmail(email);
        email.setDraft(false);
        return email;
    }

    @Override
    public Email updateEmailRecipients(long supplierId, long emailId,  List<String> recipients) {
        return emailDAOStub.updateEmailRecipients(supplierId, emailId, recipients);
    }

    @Override
    public Email updateDraft(long supplierId, long emailId, Email email) {
        // find draft email
        return emailDAOStub.updateDraftEmail(supplierId, emailId, email);
    }
}
