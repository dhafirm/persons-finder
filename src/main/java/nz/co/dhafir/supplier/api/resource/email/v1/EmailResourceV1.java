package nz.co.dhafir.supplier.api.resource.email.v1;

import nz.co.dhafir.supplier.api.dto.EmailDTO;
import nz.co.dhafir.supplier.api.ResponseWrapper;
import nz.co.dhafir.supplier.domain.Email;
import nz.co.dhafir.supplier.api.mapper.EmailBeanMapper;
import nz.co.dhafir.supplier.manager.SupplierEmailManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Creates and updates single email
 */
@RestController
@RequestMapping("/api/v1/suppliers/{supplierId}/emails")
public class EmailResourceV1 {

    @Autowired
    private SupplierEmailManager emailServiceStub;

    @Autowired
    private EmailBeanMapper emailBeanMapper;

    /**
     * Retrieves all emails for the given supplier Id
     * @param supplierId
     * @return
     */
    @GetMapping
    public ResponseWrapper getSupplierEmails(@PathVariable long supplierId) {
        List<Email> emails = emailServiceStub.findSupplierEmails(supplierId);
        ResponseWrapper response = new ResponseWrapper();
        if (CollectionUtils.isEmpty(emails)) {
            response.setMessage("No emails were found for supplier with Id: " + supplierId);
        } else {
            response.setData(emailBeanMapper.fromEmailToEmailDTO(emails));
        }

        return response;
    }

    /**
     * Retrieves a single email identified by emailId for the given supplier Id.
     * @param supplierId
     * @param emailId
     * @return
     */
    @GetMapping("/{emailId}")
    public ResponseWrapper getSupplierEmailByEmailId(@PathVariable long supplierId, @PathVariable long emailId) {
        return ResponseWrapper.builder()
                .data(emailServiceStub.findSupplierEmailByEmailId(supplierId, emailId))
                .build();
    }

    @PostMapping("/draft")
    public ResponseWrapper createEmail(@PathVariable long supplierId, @RequestBody EmailDTO emailDto) {
        // Implementation to save a draft email
        Email email = emailServiceStub.saveDraftEmail(supplierId, emailBeanMapper.fromEmailDtoToEmail(emailDto));
        return ResponseWrapper.builder()
                .status(ResponseWrapper.STATUS.SUCCESS)
                .message("Email created successfully")
                .data(emailBeanMapper.fromEmailToEmailDTO(email))
                .build();
    }

    @PostMapping("/{emailId}/send")
    public ResponseWrapper sendEmail(@PathVariable long supplierId,  @RequestBody EmailDTO emailDto) {
        // Implementation to send an email
        emailServiceStub.sendEmail(supplierId, emailBeanMapper.fromEmailDtoToEmail(emailDto));
        return  ResponseWrapper.builder()
                .status(ResponseWrapper.STATUS.SUCCESS)
                .message("Email sent successfully")
                .build();
    }

    @PutMapping("/{emailId}/recipients")
    public ResponseWrapper updateRecipients(@PathVariable long supplierId, @PathVariable long emailId, @RequestBody List<String> recipients) {
        emailServiceStub.updateEmailRecipients(supplierId, emailId, recipients);
        return  ResponseWrapper.builder()
                .status(ResponseWrapper.STATUS.SUCCESS)
                .message( "Email with ID " + emailId + " updated recipients successfully")
                .build();
    }

}
