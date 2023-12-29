package com.example.empmanager.exception;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public @ResponseBody ExceptionResponse handleException(
      final Exception exception, final HttpServletRequest request) {
    ExceptionResponse error = new ExceptionResponse();
    error.setErrorMessage(exception.getMessage());
    error.setRequestedURI(request.getRequestURI());
    return error;
  }

  @ExceptionHandler(DataNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public @ResponseBody ExceptionResponse handleException(
      final DataNotFoundException exception, final HttpServletRequest request) {
    ExceptionResponse error = new ExceptionResponse();
    error.setErrorMessage(exception.getMessage());
    error.setRequestedURI(request.getRequestURI());
    return error;
  }
}
