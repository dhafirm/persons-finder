package nz.co.dhafir.supplier.service;

import lombok.extern.slf4j.Slf4j;
import nz.co.dhafir.supplier.domain.Email;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {
    public void sendEmail(Email email) {
        log.info("EMail successfully sent. Email Id : {}", email.getId());
    }
}
