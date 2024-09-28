package com.rippletalk.rippletalk_backend.controllers;

import java.util.LinkedHashMap;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rippletalk.rippletalk_backend.exceptions.EmailAlreadyTakenException;
import com.rippletalk.rippletalk_backend.exceptions.FollowException;
import com.rippletalk.rippletalk_backend.exceptions.UnableToSavePhotoException;
import com.rippletalk.rippletalk_backend.exceptions.UserDoesNotExistException;
import com.rippletalk.rippletalk_backend.models.ApplicationUser;
import com.rippletalk.rippletalk_backend.services.TokenService;
import com.rippletalk.rippletalk_backend.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;
  private final TokenService tokenService;

  public UserController(UserService userService, TokenService tokenService) {
    this.userService = userService;
    this.tokenService = tokenService;
  }

  @GetMapping("/verify")
  public ApplicationUser verifyIdentity(@RequestHeader(HttpHeaders.AUTHORIZATION) String token)
      throws UserDoesNotExistException {
    String username = tokenService.getUsernameFromToken(token);

    return userService.getUserByUsername(username);
  }

  @PostMapping("pfp")
  public ApplicationUser uploadProfilePicture(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam("image") MultipartFile file)
      throws UnableToSavePhotoException, UserDoesNotExistException {

    String username = tokenService.getUsernameFromToken(token);

    return userService.setProfileOrBannerPicture(username, file, "pfp");
  }

  @PostMapping("/banner")
  public ApplicationUser uploadBannerPicture(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam("image") MultipartFile file)
      throws UnableToSavePhotoException, UserDoesNotExistException {

    String username = tokenService.getUsernameFromToken(token);

    return userService.setProfileOrBannerPicture(username, file, "bnr");
  }

  @PutMapping("/")
  public ApplicationUser updateUser(@RequestBody ApplicationUser u) throws EmailAlreadyTakenException {

    return userService.updateUser(u);
  }

  @ExceptionHandler({ FollowException.class })
  public ResponseEntity<String> handleFollowException() {
    return new ResponseEntity<String>("Users cannot follow themselves", HttpStatus.FORBIDDEN);
  }

  @PutMapping("/follow")
  public Set<ApplicationUser> followUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody LinkedHashMap<String, String> body) throws UserDoesNotExistException, FollowException {

    String loggedInUser = tokenService.getUsernameFromToken(token);
    String followedUser = body.get("followedUser");

    return userService.followUser(loggedInUser, followedUser);
  }

  @GetMapping("/following/{username}")
  public Set<ApplicationUser> getFollowingList(@PathVariable("username") String username)
      throws UserDoesNotExistException {
    return userService.retrieveFollowingList(username);
  }

  @GetMapping("/followers/{username}")
  public Set<ApplicationUser> getFollowersList(@PathVariable("username") String username)
      throws UserDoesNotExistException {
    return userService.retrieveFollowersList(username);
  }

}
