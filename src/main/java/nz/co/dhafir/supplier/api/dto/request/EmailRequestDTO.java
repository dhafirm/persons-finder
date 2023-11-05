package nz.co.dhafir.supplier.api.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailRequestDTO {
    private String subject;
    private String body;
    private String sender;
    private List<String> recipients;

    // can add attachments later on
}
