package com.example.Post_Service.web.exception;

import com.example.Post_Service.web.dto.ExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(exception = PostNotFoundException.class)
    public ResponseEntity<ExceptionDto> handlePostNotFoundException(PostNotFoundException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Пост не найден",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    @ExceptionHandler(exception = PostNotFoundByAuthorException.class)
    public ResponseEntity<ExceptionDto> handlePostNotFoundByAuthorException(PostNotFoundByAuthorException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "У автора нет постов",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    @ExceptionHandler(exception = PostOwnerMismatchException.class)
    public ResponseEntity<ExceptionDto> handlePostOwnerMismatchException(PostOwnerMismatchException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Пользователь не имеет прав на этот пост",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }
    @ExceptionHandler(exception = UserDoesntHavePermissionsException.class)
    public ResponseEntity<ExceptionDto> handleUserDoesntHavePermissionsException(UserDoesntHavePermissionsException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Пользователь не имеет прав на это действие",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }
    @ExceptionHandler(exception = CommentsNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleCommentsNotFoundException(CommentsNotFoundException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Комментарий не найден",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    @ExceptionHandler(exception = PostAlreadyLikedException.class)
    public ResponseEntity<ExceptionDto> handlePostAlreadyLikedException(PostAlreadyLikedException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Пост уже лайкнут",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }
    @ExceptionHandler(exception = PostNotLikedException.class)
    public ResponseEntity<ExceptionDto> handlePostNotLikedException(PostNotLikedException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Пост ещё не лайкнут",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    @ExceptionHandler(exception = ObjectNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleObjectNotFoundException(ObjectNotFoundException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Объект не найден в хранилище",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }
    @ExceptionHandler(exception = UnsupportedFileExtensionException.class)
    public ResponseEntity<ExceptionDto> handleUnsupportedFileExtensionException(UnsupportedFileExtensionException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Не поддерживается расширение такого файла",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    @ExceptionHandler(exception = AccessDeniedException.class)
    public ResponseEntity<ExceptionDto> handleAccessDeniedException(AccessDeniedException ex){
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
    public ResponseEntity<ExceptionDto> handleIllegalArgumentException(IllegalArgumentException ex){
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
    public ResponseEntity<ExceptionDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
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
    @ExceptionHandler(exception = ConstraintViolationException.class)
    public ResponseEntity<ExceptionDto> handleConstraintViolationException(ConstraintViolationException ex){
        logger.error("Error = "+ ex);

        String detailMessage = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));

        ExceptionDto error= mappingToExceptionDto(
                "Ошибка валидации полей запроса",
                detailMessage
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
    @ExceptionHandler(exception = HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex){
        logger.error("Error = "+ ex);

        ExceptionDto error= mappingToExceptionDto(
                "Некорректное или отсутствующее тело запроса",
                ex.getMessage()
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
