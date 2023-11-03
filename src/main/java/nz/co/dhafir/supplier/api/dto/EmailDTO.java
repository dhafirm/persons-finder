package nz.co.dhafir.supplier.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDTO {
    private long id;
    private String subject;
    private String body;
    private String sender;
    private List<String> recipients;

    // can add attachments later on
}
