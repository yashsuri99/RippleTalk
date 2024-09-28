package com.rippletalk.rippletalk_backend.exceptions;

public class FollowException extends Exception {

  public FollowException() {
    super("User cannot follow themselves");
  }

}
