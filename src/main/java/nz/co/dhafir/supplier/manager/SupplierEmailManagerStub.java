package nz.co.dhafir.supplier.manager;

import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nz.co.dhafir.supplier.datastore.dao.impl.EmailDAOStub;
import nz.co.dhafir.supplier.domain.Email;
import nz.co.dhafir.supplier.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
        return emailDAOStub.saveDraftEmail(supplierId, email);
    }

    @Override
    public void sendEmail(long supplierId, @NotNull final Email emailToSend) {
        // update/save the email first
        emailToSend.setDraft(false);
        if (emailToSend.getId() == 0) {
            // new Email, save
            saveDraftEmail(supplierId, emailToSend);
        }

        emailSender.sendEmail(emailToSend);
    }

    @Override
    public void updateEmailRecipients(long supplierId, long emailId, List<String> recipients) {
        if (CollectionUtils.isEmpty(recipients)) {
            return ;
        }

        emailDAOStub.updateEmailRecipients(supplierId, emailId, recipients);
    }
}
