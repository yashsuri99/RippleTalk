package com.rippletalk.rippletalk_backend.exceptions;

public class UnableToSavePhotoException extends Exception {

  public UnableToSavePhotoException() {
    super("Unable to save the supplied photo");
  }

}
