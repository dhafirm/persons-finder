package nz.co.dhafir.supplier.datastore.dao.impl;

import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import nz.co.dhafir.supplier.datastore.dao.EmailDAO;
import nz.co.dhafir.supplier.domain.Email;
import nz.co.dhafir.supplier.error.EmailException;
import nz.co.dhafir.supplier.types.SupplierId;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Repository
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
        if (email.getId() == 0) {
            email.setId(emailId++);
        }

        List<Email> supplierEmails = findSupplierEmails(supplierId);
        if (CollectionUtils.isEmpty(supplierEmails)) {
            supplierEmails = new ArrayList<>();

            emails.put( SupplierId.of(supplierId), supplierEmails);
        }
        supplierEmails.add(email);
        return email;
    }

    @Override
    public Email updateEmailRecipients(long supplierId, long emailId, List<String> recipients) {
        Optional<Email> supplierEmail =  findSupplierEmailByEmailId(supplierId, emailId);
        if (supplierEmail.isEmpty()) {
            // error email doesn't exists
            throw new EmailException("Email not found exception");
        }

        Email emailToUpdate = supplierEmail.get();
       // emailToUpdate.getRecipients().clear();
        //emailToUpdate.getRecipients().addAll(recipients);
        emailToUpdate.setRecipients(new ArrayList<>(recipients));
        log.info("Updated email recipients for supplier {} and email wit emailId {}", supplierId, emailId);
        return emailToUpdate;
    }
}
