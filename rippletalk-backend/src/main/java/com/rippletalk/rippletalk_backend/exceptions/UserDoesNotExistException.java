package com.rippletalk.rippletalk_backend.exceptions;

public class UserDoesNotExistException extends RuntimeException {

  public UserDoesNotExistException() {
    super("The user you are looking for does not exist.");
  }

}
