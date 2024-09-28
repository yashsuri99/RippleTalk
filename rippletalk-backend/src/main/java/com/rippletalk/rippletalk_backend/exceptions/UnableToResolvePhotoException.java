package com.rippletalk.rippletalk_backend.exceptions;

public class UnableToResolvePhotoException extends Exception {

  public UnableToResolvePhotoException() {
    super("The photo you are looking for cannot be found");
  }

}
