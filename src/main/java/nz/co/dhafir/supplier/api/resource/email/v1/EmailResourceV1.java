package nz.co.dhafir.supplier.api.resource.email.v1;

import nz.co.dhafir.supplier.api.dto.EmailDTO;
import nz.co.dhafir.supplier.api.dto.EmailListResponse;
import nz.co.dhafir.supplier.service.EmailServiceStub;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Creates and updates single email
 */
@RestController
@RequestMapping("/api/v1/suppliers/{supplierId}/emails")
public class EmailResourceV1 {

    private EmailServiceStub emailService;
    /**
     * Retrieves all emails for the given supplier Id
     * @param supplierId
     * @return
     */
    @GetMapping
    public EmailListResponse getSupplierEmails(@PathVariable long supplierId) {
        EmailDTO email1 = EmailDTO.builder().
                id(1000L).
                subject("subject1").
                sender("here@there.com").
                recipients(List.of("they@there.com")).
                body("Hi 1!").build();
        EmailDTO email2 = EmailDTO.builder().
                id(1001L).
                subject("subject 2").
                sender("here@there.com").
                recipients(List.of("they@there.com")).
                body("Hi 2!").build();
        return EmailListResponse.builder().emails(List.of(email1, email2)).build();
    }

    /**
     * Retrieves a single email identified by emailId for the given supplier Id.
     * @param supplierId
     * @param emailId
     * @return
     */
    @GetMapping("/{emailId}")
    public EmailDTO getSupplierEmails(@PathVariable long supplierId, @PathVariable long emailId) {
        return EmailDTO.builder().
                id(emailId).
                subject("subject1").
                sender("here@there.com").
                recipients(List.of("they@there.com")).
                body("Hi!").build();
    }

    @PostMapping("/draft")
    public String createEmail(@PathVariable long supplierId, @RequestBody EmailDTO emailDto) {
        // Implementation to create a new user

        return "Email created successfully";
    }

    @PostMapping("/{emailId}/send")
    public String sendEmail(@PathVariable long supplierId,  @RequestBody EmailDTO emailDto) {
        // Implementation to create a new user

        return "Email created successfully";
    }

    @PutMapping("/{emailId}/recipients")
    public String updateRecipients(@PathVariable long emailId, @RequestBody List<String> recipients) {
        // Implementation to update user by ID
        return "Email with ID " + emailId + " updated recipients successfully";
    }

}
