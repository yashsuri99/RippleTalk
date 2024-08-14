package com.rippletalk.rippletalk_backend.exceptions;

public class EmailFailedToSendException extends RuntimeException {

  public EmailFailedToSendException() {
    super("The email failed to send");
  }

}
