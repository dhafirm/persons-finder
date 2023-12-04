package nz.co.dhafir.supplier.api.resource.email.v1;

import lombok.extern.slf4j.Slf4j;
import nz.co.dhafir.supplier.api.ApiResponseWrapper;
import nz.co.dhafir.supplier.api.dto.request.EmailRequestDTO;
import nz.co.dhafir.supplier.domain.Email;
import nz.co.dhafir.supplier.api.mapper.EmailBeanMapper;
import nz.co.dhafir.supplier.manager.SupplierEmailManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Creates and updates single email
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/suppliers/{supplierId}/")
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
    @GetMapping("emails")
    public ResponseEntity<ApiResponseWrapper> getSupplierEmails(@PathVariable long supplierId) {
        List<Email> emails = emailServiceStub.findSupplierEmails(supplierId);
        ApiResponseWrapper responseWrapper = new ApiResponseWrapper();
        if (CollectionUtils.isEmpty(emails)) {
            responseWrapper.setMessage("No emails were found for supplier with Id: " + supplierId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseWrapper);
        }

        responseWrapper.setData(emailBeanMapper.fromDomainToEmailResponseDTOList(emails));
        return ResponseEntity.ok().body(responseWrapper);

    }

    /**
     * Retrieves a single email identified by emailId for the given supplier Id.
     * @param supplierId
     * @param emailId
     * @return
     */
    @GetMapping("/emails/{emailId}")
    public ResponseEntity<ApiResponseWrapper> getSupplierEmailByEmailId(@PathVariable long supplierId, @PathVariable long emailId) {
        try {
            Optional<Email> maybeEmail = emailServiceStub.findSupplierEmailByEmailId(supplierId, emailId);

            if (maybeEmail.isEmpty()) {
                return errorResponse("Email not found with Id : " + emailId);
            }

            ApiResponseWrapper responseWrapper = ApiResponseWrapper.builder()
                .data(emailBeanMapper.fromDomainToEmailResponseDTO(maybeEmail.get()))
                .build();

            return ResponseEntity.ok().body(responseWrapper);
        } catch (Exception exp) {
            return errorResponse("Couldn't get supplier email with email Id:" + emailId);
        }
    }

    @PostMapping("emails")
    public ResponseEntity<ApiResponseWrapper> createEmail(@PathVariable long supplierId,
                                          @RequestBody EmailRequestDTO emailDto) {
        try {
        Email email = emailServiceStub.saveDraftEmail(supplierId, emailBeanMapper.fromEmailRequestDtoToDomain(emailDto));
        ApiResponseWrapper responseWrapper =  ApiResponseWrapper.builder()
                .message("Email created successfully")
                .data(emailBeanMapper.fromDomainToEmailResponseDTO(email))
                .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(responseWrapper);
        } catch (Exception exp) {
            return errorResponse("Couldn't create draft email");
        }
    }

    @PostMapping("/emails/{emailId}")
    public ResponseEntity<ApiResponseWrapper> sendEmail(@PathVariable long supplierId, @PathVariable long emailId) {
        try {
            log.info("Sending email with emailId: " + emailId);
            emailServiceStub.sendEmail(supplierId, emailId);
            ApiResponseWrapper responseWrapper =  ApiResponseWrapper.builder()
                .message("Email sent successfully")
                .build();
            return ResponseEntity.ok().body(responseWrapper);
        } catch (Exception exp) {
            log.error("Sending email failed.", exp);
            return errorResponse("Couldn't send email.");
        }
    }


    /**
     * Updates a draft email. All specified properties will be updated.
     * @param supplierId
     * @param emailId
     * @param emailDto
     * @return
     */
    @PutMapping("/emails/{emailId}")
    public ResponseEntity<ApiResponseWrapper> updateDraft(@PathVariable long supplierId,
                                          @PathVariable long emailId,
                                          @RequestBody EmailRequestDTO emailDto) {
        try {
            Email updatedEmail = emailServiceStub.updateDraft(supplierId, emailId, emailBeanMapper.fromEmailRequestDtoToDomain(emailDto));
            ApiResponseWrapper responseWrapper = ApiResponseWrapper.builder()
                    .data(emailBeanMapper.fromDomainToEmailResponseDTO(updatedEmail))
                    .message("Email with ID " + emailId + " updated successfully")
                    .build();
            return ResponseEntity.ok().body(responseWrapper);
        } catch (Exception exp) {
            log.error("updating draft email failed.", exp);
            return errorResponse("Couldn't update recipients for email with Id:" + emailId);
        }
    }

    private ResponseEntity<ApiResponseWrapper> errorResponse(String errorMessage) {
        ApiResponseWrapper apiResponseWrapper =  ApiResponseWrapper.builder()
                .message(errorMessage)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponseWrapper);
    }
}
