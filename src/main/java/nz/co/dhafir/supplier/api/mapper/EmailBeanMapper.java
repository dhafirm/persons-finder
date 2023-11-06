package nz.co.dhafir.supplier.api.mapper;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import nz.co.dhafir.supplier.api.dto.request.EmailRequestDTO;
import nz.co.dhafir.supplier.api.dto.response.EmailResponseDTO;
import nz.co.dhafir.supplier.domain.Email;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper between EmailDTO and Email.
 * TODO Use mapstruct library for easy bean mapping.
 */
@Component
@Slf4j
public class EmailBeanMapper {

   public EmailResponseDTO fromDomainToEmailResponseDTO(@NotNull Email email) {
       log.info("Email draft status:" + email.isSent());
       return EmailResponseDTO.builder()
                .id(email.getId())
                .sender(email.getSender())
                .recipients(List.copyOf(email.getRecipients()))
                .body(email.getBody())
                .subject(email.getSubject())
                .sent(email.isSent())
                .build();

   }

   public List<EmailResponseDTO> fromDomainToEmailResponseDTOList(List<Email> emails) {
       return emails.stream().map(this::fromDomainToEmailResponseDTO).collect(Collectors.toList());
   }

    public Email fromEmailRequestDtoToDomain(@NotNull EmailRequestDTO emailRequestDto) {
       return Email.builder()
               .subject(emailRequestDto.getSubject())
               .body(emailRequestDto.getBody())
               .sender(emailRequestDto.getSender())
               .recipients(emailRequestDto.getRecipients())
               .build();
    }
}
