package com.rippletalk.rippletalk_backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "image_id")
  private Long imageId;

  @Column(name = "image_name")
  private String imageName;

  @Column(name = "image_type")
  private String imageType;

  @Column(name = "image_path")
  private String imagePath;

  @Column(name = "image_url")
  private String imageURL;

  public Image() {
    super();
  }

  public Image(String imageName, String imageType, String imagePath, String imageURL) {
    super();
    this.imageName = imageName;
    this.imageType = imageType;
    this.imagePath = imagePath;
    this.imageURL = imageURL;
  }

  public Image(Long imageId, String imageName, String imageType, String imagePath, String imageURL) {
    this.imageId = imageId;
    this.imageName = imageName;
    this.imageType = imageType;
    this.imagePath = imagePath;
    this.imageURL = imageURL;
  }

  public Long getImageId() {
    return imageId;
  }

  public void setImageId(Long imageId) {
    this.imageId = imageId;
  }

  public String getImageName() {
    return imageName;
  }

  public void setImageName(String imageName) {
    this.imageName = imageName;
  }

  public String getImageType() {
    return imageType;
  }

  public void setImageType(String imageType) {
    this.imageType = imageType;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
  }

  @Override
  public String toString() {
    return "Image [imageId=" + imageId + ", imageName=" + imageName + ", imageType=" + imageType + ", imagePath="
        + imagePath + ", imageURL=" + imageURL + "]";
  }
}
