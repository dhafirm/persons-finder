package nz.co.dhafir.supplier.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseWrapper {

    /**
     * message (optional)
     */
    private String message;
    private Object data;
    private List<ErrorInfo> errors = new ArrayList<>();

    public void addError(String code, String message) {
        errors.add(new ErrorInfo(code, message));
    }


    @Getter
    @AllArgsConstructor
    private static class ErrorInfo {
        private String code;
        private String message;
    }
}
