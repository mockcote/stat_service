package mockcote.statservice.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {
    private HttpStatus code;
    private String message;
}
