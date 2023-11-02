package nz.co.dhafir.supplier.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private long id;
    private String subject;
    private String body;
    private String sender;
    private List<String> recipients = new ArrayList<>();
    private boolean draft = true;
}
