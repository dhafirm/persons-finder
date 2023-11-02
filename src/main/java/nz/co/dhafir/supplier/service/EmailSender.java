package nz.co.dhafir.supplier.service;

import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import nz.co.dhafir.supplier.domain.Email;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSender {

    public void sendEmail(Email email) {
        log.info("EMail successfully sent. Email Id : {}", email.getId());
    }
}
