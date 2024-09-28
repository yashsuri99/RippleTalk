package com.rippletalk.rippletalk_backend.exceptions;

public class EmailFailedToSendException extends Exception {

  public EmailFailedToSendException() {
    super("The email failed to send");
  }

}
