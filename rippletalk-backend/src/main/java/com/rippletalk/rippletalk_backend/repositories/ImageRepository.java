package com.rippletalk.rippletalk_backend.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rippletalk.rippletalk_backend.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

  Optional<Image> findByImageName(String imageName);

}
