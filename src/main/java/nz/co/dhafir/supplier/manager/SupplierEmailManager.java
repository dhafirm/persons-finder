package nz.co.dhafir.supplier.manager;

import nz.co.dhafir.supplier.domain.Email;

import java.util.List;
import java.util.Optional;

public interface SupplierEmailManager {

    List<Email> findSupplierEmails(long supplierId);


    Optional<Email> findSupplierEmailByEmailId(long supplierId, long emailId);

    Email saveDraftEmail(long supplierId, Email email);

    void sendEmail(long supplierId, Email email);

    void updateEmailRecipients(long supplierId, long emailId, List<String> recipients);

}
