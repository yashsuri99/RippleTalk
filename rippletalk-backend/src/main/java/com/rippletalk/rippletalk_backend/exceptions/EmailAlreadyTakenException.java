package com.rippletalk.rippletalk_backend.exceptions;

public class EmailAlreadyTakenException extends Exception {

  public EmailAlreadyTakenException() {
    super("The email provided is already taken");
  }

}
