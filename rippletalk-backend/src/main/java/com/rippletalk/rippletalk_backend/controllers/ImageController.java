package com.rippletalk.rippletalk_backend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rippletalk.rippletalk_backend.exceptions.UnableToResolvePhotoException;
import com.rippletalk.rippletalk_backend.exceptions.UnableToSavePhotoException;
import com.rippletalk.rippletalk_backend.services.ImageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/images")
@CrossOrigin("*")
public class ImageController {

  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @ExceptionHandler({ UnableToSavePhotoException.class, UnableToResolvePhotoException.class })
  public ResponseEntity<String> handlePhotoException() {
    return new ResponseEntity<String>("Unable to process the photo", HttpStatus.NOT_ACCEPTABLE);
  }

  @GetMapping("/{fileName}")
  public ResponseEntity<byte[]> downloadImage(@PathVariable String fileName) throws UnableToResolvePhotoException {

    byte[] imageBytes = imageService.downloadImage(fileName);

    return ResponseEntity
        .status(HttpStatus.OK)
        .contentType(MediaType.valueOf(imageService.getImageType(fileName)))
        .body(imageBytes);
  }

}
