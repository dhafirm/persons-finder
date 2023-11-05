package nz.co.dhafir.supplier.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailResponseDTO {
    private long id;

    private String subject;
    private String body;
    private String sender;
    private List<String> recipients;
    private boolean draft;

    // can add attachments later on
}
