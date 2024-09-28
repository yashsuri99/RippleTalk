package com.rippletalk.rippletalk_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rippletalk.rippletalk_backend.exceptions.EmailAlreadyTakenException;
import com.rippletalk.rippletalk_backend.exceptions.EmailFailedToSendException;
import com.rippletalk.rippletalk_backend.exceptions.IncorrectVerificationCodeException;
import com.rippletalk.rippletalk_backend.exceptions.UserDoesNotExistException;
import com.rippletalk.rippletalk_backend.models.ApplicationUser;
import com.rippletalk.rippletalk_backend.models.LoginResponse;
import com.rippletalk.rippletalk_backend.models.RegistrationObject;
import com.rippletalk.rippletalk_backend.services.TokenService;
import com.rippletalk.rippletalk_backend.services.UserService;

import java.util.LinkedHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

  private final UserService userService;
  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationController(UserService userService, TokenService tokenService,
      AuthenticationManager authenticationManager) {
    this.userService = userService;
    this.tokenService = tokenService;
    this.authenticationManager = authenticationManager;
  }

  @ExceptionHandler({ EmailAlreadyTakenException.class })
  public ResponseEntity<String> handleEmailTaken() {
    return new ResponseEntity<String>("The email you provided is already taken", HttpStatus.CONFLICT);
  }

  @PostMapping("/register")
  public ApplicationUser registerUser(@RequestBody RegistrationObject ro) throws EmailAlreadyTakenException {
    return userService.registerUser(ro);
  }

  @ExceptionHandler({ UserDoesNotExistException.class })
  public ResponseEntity<String> handleUserDoesNotExist() {
    return new ResponseEntity<String>("The user you are looking for does not exist", HttpStatus.NOT_FOUND);
  }

  @PutMapping("/update/phone")
  public ApplicationUser updatePhoneNumber(@RequestBody LinkedHashMap<String, String> body)
      throws UserDoesNotExistException, EmailAlreadyTakenException {
    String username = body.get("username");
    String phone = body.get("phone");

    ApplicationUser user = userService.getUserByUsername(username);

    user.setPhone(phone);

    return userService.updateUser(user);
  }

  @ExceptionHandler({ EmailFailedToSendException.class })
  public ResponseEntity<String> handleFailedEmail() {
    return new ResponseEntity<String>("Email failed to send, try again in a moment", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @PostMapping("/email/code")
  public ResponseEntity<String> createEmailVerfication(@RequestBody LinkedHashMap<String, String> body)
      throws UserDoesNotExistException, EmailFailedToSendException {

    userService.generateEmailVerififcation(body.get("username"));

    return new ResponseEntity<String>("Verification Code generated, email sent", HttpStatus.OK);

  }

  @ExceptionHandler({ IncorrectVerificationCodeException.class })
  public ResponseEntity<String> incorrectCodeHandler() {
    return new ResponseEntity<String>("The code provided does'nt match the users code", HttpStatus.CONFLICT);
  }

  @PostMapping("/email/verify")
  public ApplicationUser verifyEmail(@RequestBody LinkedHashMap<String, String> body)
      throws UserDoesNotExistException, IncorrectVerificationCodeException {

    Long code = Long.parseLong(body.get("code"));

    String username = body.get("username");

    return userService.verifyEmail(username, code);
  }

  @PutMapping("/update/password")
  public ApplicationUser updatePassword(@RequestBody LinkedHashMap<String, String> body)
      throws UserDoesNotExistException {

    String username = body.get("username");
    String password = body.get("password");

    return userService.setPassword(username, password);

  }

  @PostMapping("/login")
  public LoginResponse login(@RequestBody LinkedHashMap<String, String> body) throws UserDoesNotExistException {

    String username = body.get("username");
    String password = body.get("password");

    try {
      Authentication auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));

      String token = tokenService.generateToken(auth);
      return new LoginResponse(userService.getUserByUsername(username), token);
    } catch (AuthenticationException e) {
      return new LoginResponse(null, "");
    }

  }
}
