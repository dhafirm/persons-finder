package nz.co.dhafir.supplier.datastore.dao.impl;

import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import nz.co.dhafir.supplier.datastore.dao.EmailDAO;
import nz.co.dhafir.supplier.domain.Email;
import nz.co.dhafir.supplier.error.EmailException;
import nz.co.dhafir.supplier.types.SupplierId;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmailDAOStub implements EmailDAO {
    private static long emailId = 1000;

    private Map<SupplierId, List<Email>> emails;

    public EmailDAOStub() {
        emails = new HashMap<SupplierId, List<Email>>();
    }
    @Override
    public List<Email> findSupplierEmails(long supplierId) {
        return emails.get(SupplierId.of(supplierId));
    }

    @Override
    public Optional<Email> findSupplierEmailByEmailId(long supplierId, long emailId) {
        List<Email> supplierEmails = findSupplierEmails(supplierId);
        if (CollectionUtils.isEmpty(supplierEmails)) {
            return Optional.empty();
        }

       return supplierEmails.stream().filter(email -> email.getId() == emailId).findAny();
    }

    @Override
    public Email saveDraftEmail(long supplierId, Email email) {
        Email draftEmail = Email.builder().
                id(emailId++)
                .body(email.getBody())
                .subject(email.getSubject())
                .sender(email.getSender())
                .recipients(email.getRecipients())
                .body(email.getBody())
                .build();
        List<Email> supplierEmails = findSupplierEmails(supplierId);
        if (CollectionUtils.isEmpty(supplierEmails)) {
            supplierEmails = new ArrayList<>();
            emails.put( new SupplierId(supplierId), new ArrayList<>());
        }
        supplierEmails.add(draftEmail)
        return draftEmail;
    }

    @Override
    public Email updateEmailRecipients(long supplierId, long emailId, List<String> recipients) {
        Optional<Email> supplierEmail =  findSupplierEmailByEmailId(supplierId, emailId);
        if (supplierEmail.isEmpty()) {
            // error email doesn't exists
            throw new EmailException("Email not found exception");
        }

        Email emailToUpdate = supplierEmail.get();
        emailToUpdate.getRecipients().clear();
        emailToUpdate.getRecipients().addAll(recipients);

        log.info("Updated email recipients for supplier {} and email wit emailId {}", supplierId, emailId);
        return emailToUpdate;
    }
}
