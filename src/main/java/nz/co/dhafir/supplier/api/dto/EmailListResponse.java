package nz.co.dhafir.supplier.api.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class EmailListResponse {
    private List<EmailDTO> emails;
}
