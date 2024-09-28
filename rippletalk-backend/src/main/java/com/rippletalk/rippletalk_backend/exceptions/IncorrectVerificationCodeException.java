package com.rippletalk.rippletalk_backend.exceptions;

public class IncorrectVerificationCodeException extends Exception {

  public IncorrectVerificationCodeException() {
    super("The code passed did not match the users verification code");
  }

}
