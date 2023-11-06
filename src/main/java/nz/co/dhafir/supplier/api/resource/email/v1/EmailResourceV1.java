package nz.co.dhafir.supplier.api.resource.email.v1;

import lombok.extern.slf4j.Slf4j;
import nz.co.dhafir.supplier.api.ResponseWrapper;
import nz.co.dhafir.supplier.api.dto.request.EmailRequestDTO;
import nz.co.dhafir.supplier.domain.Email;
import nz.co.dhafir.supplier.api.mapper.EmailBeanMapper;
import nz.co.dhafir.supplier.manager.SupplierEmailManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Creates and updates single email
 */
@Slf4j
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
            response.setData(emailBeanMapper.fromDomainToEmailResponseDTOList(emails));
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
        try {
            Optional<Email> maybeEmail = emailServiceStub.findSupplierEmailByEmailId(supplierId, emailId);

            if (maybeEmail.isEmpty()) {
                return errorResponse("Email not found with Id : " + emailId);
            }

            return ResponseWrapper.builder()
                .data(emailBeanMapper.fromDomainToEmailResponseDTO(maybeEmail.get()))
                .build();
        } catch (Exception exp) {
            return errorResponse("Couldn't get supplier email with email Id:" + emailId);
        }
    }

    @PostMapping("/draft")
    public ResponseWrapper createEmail(@PathVariable long supplierId,
                                       @RequestBody EmailRequestDTO emailDto) {
        try {
        Email email = emailServiceStub.saveDraftEmail(supplierId, emailBeanMapper.fromEmailRequestDtoToDomain(emailDto));
        return ResponseWrapper.builder()
                .status(ResponseWrapper.STATUS.SUCCESS)
                .message("Email created successfully")
                .data(emailBeanMapper.fromDomainToEmailResponseDTO(email))
                .build();
        } catch (Exception exp) {
            return errorResponse("Couldn't create draft email");
        }
    }

    @PostMapping("/send")
    public ResponseWrapper sendEmail(@PathVariable long supplierId,
                                     @RequestBody EmailRequestDTO emailDto) {
        try {

            Email email = emailBeanMapper.fromEmailRequestDtoToDomain(emailDto);
            emailServiceStub.sendEmail(supplierId, email);
            return  ResponseWrapper.builder()
                .status(ResponseWrapper.STATUS.SUCCESS)
                .message("Email sent successfully")
                .build();
        } catch (Exception exp) {
            log.error("Sending email failed.", exp);
            return errorResponse("Couldn't send email.");
        }
    }

    /**
     * Send a draft email.
     * @param supplierId
     * @param emailId
     * @return
     */
    @PostMapping("/{emailId}/send")
    public ResponseWrapper sendEmail(@PathVariable long supplierId,
                                     @PathVariable long emailId) {
        try {
            emailServiceStub.sendEmail(supplierId, emailId);
            return  ResponseWrapper.builder()
                    .status(ResponseWrapper.STATUS.SUCCESS)
                    .message("Email sent successfully")
                    .build();
        } catch (Exception exp) {
            log.error("Sending email failed.", exp);
            return errorResponse("Couldn't send email Id:");
        }
    }
    @PutMapping("/{emailId}/recipients")
    public ResponseWrapper updateRecipients(@PathVariable long supplierId,
                                            @PathVariable long emailId,
                                            @RequestBody EmailRequestDTO emailDto) {
        try {
            Email updatedEmail = emailServiceStub.updateEmailRecipients(supplierId, emailId, emailDto.getRecipients());
            return ResponseWrapper.builder()
                    .status(ResponseWrapper.STATUS.SUCCESS)
                    .data(emailBeanMapper.fromDomainToEmailResponseDTO(updatedEmail))
                    .message("Email with ID " + emailId + " updated recipients successfully")
                    .build();
        } catch (Exception exp) {
            log.error("updating email recipients failed.", exp);
            return errorResponse("Couldn't update recipients for email with Id:" + emailId);
        }
    }

    /**
     * Updates a draft email. All specified properties will be updated.
     * @param supplierId
     * @param emailId
     * @param emailDto
     * @return
     */
    @PutMapping("/{emailId}/u")
    public ResponseWrapper updateDraft(@PathVariable long supplierId,
                                            @PathVariable long emailId,
                                            @RequestBody EmailRequestDTO emailDto) {
        try {
            Email updatedEmail = emailServiceStub.updateDraft(supplierId, emailId, emailBeanMapper.fromEmailRequestDtoToDomain(emailDto));
            return ResponseWrapper.builder()
                    .status(ResponseWrapper.STATUS.SUCCESS)
                    .data(emailBeanMapper.fromDomainToEmailResponseDTO(updatedEmail))
                    .message("Email with ID " + emailId + " updated successfully")
                    .build();
        } catch (Exception exp) {
            log.error("updating draft email failed.", exp);
            return errorResponse("Couldn't update recipients for email with Id:" + emailId);
        }
    }

    private ResponseWrapper errorResponse(String errorMessage) {
        return ResponseWrapper.builder()
                .status(ResponseWrapper.STATUS.ERROR)
                .message(errorMessage)
                .build();
    }
}
