package nz.co.dhafir.supplier.service;

import lombok.extern.slf4j.Slf4j;
import nz.co.dhafir.supplier.datastore.dao.impl.EmailDAOStub;
import nz.co.dhafir.supplier.domain.Email;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmailServiceStub implements EmailService {

    private EmailDAOStub  emailDAOStub;

    private EmailSender emailSender;

    @Override
    public List<Email> findSupplierEmails(long supplierId) {
        return emailDAOStub.findSupplierEmails(supplierId);
    }

    @Override
    public Optional<Email> findSupplierEmailByEmailId(long supplierId, long emailId) {
        return emailDAOStub.findSupplierEmailByEmailId(supplierId, emailId);
    }

    @Override
    public Email saveDraftEmail(long supplierId, Email email) {
        return emailDAOStub.saveDraftEmail(supplierId, email);
    }

    @Override
    public void sendEmail(long supplierId, Email email) {
        emailSender.sendEmail(email);

    }

    @Override
    public Email updateEmailRecipients(long supplierId, long emailId, List<String> recipients) {
        return emailDAOStub.updateEmailRecipients(supplierId, emailId, recipients);
    }
}
