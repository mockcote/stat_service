package mockcote.statservice.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
	
	// 사용자 에러
	@ExceptionHandler(CustomException.class)
    public ErrorResult handleCostumerException(CustomException e) {
        log.error("{} : {}", e.getErrorCode(), e.getErrorMessage());
        return new ErrorResult(e.getErrorCode(), e.getErrorMessage());
    }

    // DataAccessException 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public ErrorResult handleDataAccessException(DataAccessException e) {
        log.error("DataAccessError : {}", e.getMessage());
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 요류");
    }

}
