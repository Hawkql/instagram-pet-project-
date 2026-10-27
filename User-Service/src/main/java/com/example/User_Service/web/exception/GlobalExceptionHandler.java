package com.example.User_Service.web.exception;

import com.example.User_Service.web.dto.ExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(exception = UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleNotFoundUserException(UserNotFoundException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Пользователь не найден",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    @ExceptionHandler(exception = UnsupportedFileExtensionException.class)
    public ResponseEntity<ExceptionDto> handleUnsupportedFileExtensionException(
            UnsupportedFileExtensionException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Не поддерживается расширение такого файла",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);

    }
    @ExceptionHandler(exception = UserAllReadyException.class)
    public ResponseEntity<ExceptionDto> handleUserAllReadyException(
            UserAllReadyException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Пользователь уже создан с такими параметрами ",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);

    }
    @ExceptionHandler(exception = ObjectNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleObjectNotFoundException(
            ObjectNotFoundException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Аватарки нет в базе данных",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);

    }
    @ExceptionHandler(exception = UserAuthenticationTokenException.class)
    public ResponseEntity<ExceptionDto> handleUserAuthenticationTokenException(
            UserAuthenticationTokenException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Ошибка авторизации",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);

    }
    @ExceptionHandler(exception = AccessDeniedException.class)
    public ResponseEntity<ExceptionDto> handleAccessDeniedException(
            AccessDeniedException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Доступ запрещен",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);

    }
    @ExceptionHandler(exception = IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handleIllegalArgumentException(
            IllegalArgumentException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Некорректные данные запроса",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);

    }
    @ExceptionHandler(exception = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex){
        logger.error("Error = "+ ex);

        String detailMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ExceptionDto error= mappingToExceptionDto(
                "Ошибка валидации полей запроса",
                detailMessage
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);

    }
    @ExceptionHandler(exception = Exception.class)
    public ResponseEntity<ExceptionDto> handleException(Exception ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Внутренняя ошибка сервера",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);

    }

    private ExceptionDto mappingToExceptionDto(String exceptionMessage,String ex){
        return  new ExceptionDto(
                exceptionMessage,
                ex,
                LocalDateTime.now()
        );
    }

}
