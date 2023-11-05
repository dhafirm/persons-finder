package nz.co.dhafir.supplier.datastore;

import nz.co.dhafir.supplier.domain.Email;

import java.util.List;
import java.util.Optional;

public interface EmailDAO {
    List<Email> findSupplierEmails(long supplierId);

    Optional<Email> findSupplierEmailByEmailId(long supplierId, long emailId);

    Email createDraftEmail(long supplierId, Email email);

    Email updateEmailRecipients(long supplierId, long emailId, List<String> recipients);
}
