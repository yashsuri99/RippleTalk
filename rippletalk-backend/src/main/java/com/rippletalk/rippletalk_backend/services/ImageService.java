package com.rippletalk.rippletalk_backend.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.rippletalk.rippletalk_backend.exceptions.UnableToResolvePhotoException;
import com.rippletalk.rippletalk_backend.exceptions.UnableToSavePhotoException;
import com.rippletalk.rippletalk_backend.models.Image;
import com.rippletalk.rippletalk_backend.repositories.ImageRepository;

@Service
@Transactional
public class ImageService {

  private final ImageRepository imageRepository;

  private static final File DIRECTORY = new File(
      "C:\\Users\\Lenovo\\OneDrive\\Desktop\\RippleTalk\\rippletalk-backend\\img");
  private static final String URL = "http://localhost:8000/images/";

  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  public Image uploadImage(MultipartFile file, String prefix) throws UnableToSavePhotoException {
    try {
      String extension = "." + file.getContentType().split("/")[1];

      File img = File.createTempFile(prefix, extension, DIRECTORY);

      file.transferTo(img);

      String imageUrl = URL + img.getName();

      Image i = new Image(img.getName(), file.getContentType(), img.getPath(), imageUrl);

      Image saved = imageRepository.save(i);

      return saved;
    } catch (IOException e) {
      throw new UnableToSavePhotoException();
    }
  }

  public byte[] downloadImage(String filename) throws UnableToResolvePhotoException {
    try {
      Image image = imageRepository.findByImageName(filename).get();

      String filePath = image.getImagePath();

      byte[] imageBytes = Files.readAllBytes(new File(filePath).toPath());

      return imageBytes;
    } catch (IOException e) {
      throw new UnableToResolvePhotoException();
    }
  }

  public String getImageType(String fileName) {
    Image image = imageRepository.findByImageName(fileName).get();

    return image.getImageType();
  }

}
