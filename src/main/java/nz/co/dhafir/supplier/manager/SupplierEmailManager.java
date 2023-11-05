package nz.co.dhafir.supplier.manager;

import nz.co.dhafir.supplier.domain.Email;

import java.util.List;
import java.util.Optional;

public interface SupplierEmailManager {

    List<Email> findSupplierEmails(long supplierId);


    Optional<Email> findSupplierEmailByEmailId(long supplierId, long emailId);

    Email saveDraftEmail(long supplierId, Email email);

    /**
     * Save and send the new email.
     * @param supplierId
     * @param email
     */
    Email sendEmail(long supplierId, Email email);

    /**
     * Send the email with the given email ID
     * @param supplierId
     * @param emailId
     */
    Email sendEmail(long supplierId, long emailId);

    Email updateEmailRecipients(long supplierId, long emailId, List<String> recipients);

    Email updateDraft(long supplierId, long emailId, Email email);

}
