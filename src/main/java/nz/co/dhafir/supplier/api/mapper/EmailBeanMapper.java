package nz.co.dhafir.supplier.api.mapper;

import jakarta.validation.constraints.NotNull;
import net.minidev.json.writer.BeansMapper;
import nz.co.dhafir.supplier.api.dto.EmailDTO;
import nz.co.dhafir.supplier.domain.Email;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper between EmailDTO and Email.
 * TODO Use mapstruct library for easy bean mapping.
 */
@Component
public class EmailBeanMapper {

   public EmailDTO fromEmailToEmailDTO(@NotNull Email email) {
        return EmailDTO.builder()
                .id(email.getId())
                .sender(email.getSender())
                .recipients(List.copyOf(email.getRecipients()))
                .body(email.getBody())
                .subject(email.getSubject())
                .build();

   }

   public List<EmailDTO>  fromEmailToEmailDTO(List<Email> emails) {
       return emails.stream().map(this::fromEmailToEmailDTO).collect(Collectors.toList());
   }

    public Email fromEmailDtoToEmail(@NotNull EmailDTO emailDto) {
       Email email = new Email();
       email.setId(emailDto.getId());
       email.setSubject(email.getSubject());
       email.setBody(emailDto.getBody());
       email.setSender(emailDto.getSender());
       email.setSubject(emailDto.getSubject());
       email.setRecipients(List.copyOf(emailDto.getRecipients()));
       return email;
    }
}
