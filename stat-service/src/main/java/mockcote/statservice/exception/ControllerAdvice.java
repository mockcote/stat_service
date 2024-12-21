package mockcote.statservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    // Not Found
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResult handleNotFoundException(NotFoundException e) {
        log.error("404 Error : {}", e.getMessage());
        return new ErrorResult("404", e.getMessage());
    }

    // Bad Request
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResult handleBadRequestException(BadRequestException e) {
        log.error("400 Error : {}", e.getMessage());
        return new ErrorResult("400", e.getMessage());
    }

    // DataAccessException 처리
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataAccessException.class)
    public ErrorResult handleDataAccessException(DataAccessException e) {
        log.error("DataAccessError : {}", e.getMessage());
        return new ErrorResult("500", "데이터베이스 요류");
    }

    // 서버 내부 에러
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResult exceptionHandler(Exception e) {
        log.error("InternalServerError : {}", e.getMessage());
        return new ErrorResult("500", "서버 내부 오류");
    }

}
