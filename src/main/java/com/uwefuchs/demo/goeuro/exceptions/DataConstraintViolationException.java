package com.uwefuchs.demo.goeuro.exceptions;

public class DataConstraintViolationException
    extends BusRouteServiceRuntimeException {

  private static final long serialVersionUID = 1L;

  public DataConstraintViolationException() {
  }

  public DataConstraintViolationException(final String message) {
    super(message);
  }

  public DataConstraintViolationException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
