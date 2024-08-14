package com.rippletalk.rippletalk_backend.services;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rippletalk.rippletalk_backend.exceptions.EmailAlreadyTakenException;
import com.rippletalk.rippletalk_backend.exceptions.EmailFailedToSendException;
import com.rippletalk.rippletalk_backend.exceptions.IncorrectVerificationCodeException;
import com.rippletalk.rippletalk_backend.exceptions.UserDoesNotExistException;
import com.rippletalk.rippletalk_backend.models.ApplicationUser;
import com.rippletalk.rippletalk_backend.models.RegistrationObject;
import com.rippletalk.rippletalk_backend.models.Role;
import com.rippletalk.rippletalk_backend.repositories.RoleRepository;
import com.rippletalk.rippletalk_backend.repositories.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepo;
  private final RoleRepository roleRepo;
  private final MailService mailService;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepo, RoleRepository roleRepo, MailService mailService,
      PasswordEncoder passwordEncoder) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    this.mailService = mailService;
    this.passwordEncoder = passwordEncoder;
  }

  public ApplicationUser registerUser(RegistrationObject ro) {

    ApplicationUser user = new ApplicationUser();
    user.setFirstName(ro.getFirstName());
    user.setLastName(ro.getLastName());
    user.setEmail(ro.getEmail());
    user.setDateOfBirth(ro.getDob());
    String name = user.getFirstName() + user.getLastName();
    boolean nameTaken = true;

    String tempName = "";
    while (nameTaken) {
      tempName = generateUsername(name);

      if (userRepo.findByUsername(tempName).isEmpty()) {
        nameTaken = false;
      }

    }
    user.setUsername(tempName);

    Set<Role> roles = user.getAuthorities();
    roles.add(roleRepo.findByAuthority("User").get());
    user.setAuthorities(roles);

    try {
      return userRepo.save(user);
    } catch (Exception e) {
      throw new EmailAlreadyTakenException();
    }
  }

  private String generateUsername(String name) {
    long generatedNumber = (long) Math.floor(Math.random() * 1_000_000_000);
    return name + generatedNumber;
  }

  public ApplicationUser getUserByUsername(String username) {

    return userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
  }

  public ApplicationUser updateUser(ApplicationUser user) {
    try {
      return userRepo.save(user);
    } catch (Exception e) {
      throw new EmailAlreadyTakenException();
    }
  }

  public void generateEmailVerififcation(String username) {
    ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

    user.setVerification(generateVerificationNumber());

    try {
      mailService.sendEmail(user.getEmail(), "Your Verification Code",
          "Here is your verification code: " + user.getVerification());
      userRepo.save(user);
    } catch (Exception e) {
      System.out.println("UserService file");
      throw new EmailFailedToSendException();
    }

    userRepo.save(user);
  }

  private Long generateVerificationNumber() {
    return (long) Math.floor(Math.random() * 100_000_000);
  }

  public ApplicationUser verifyEmail(String username, Long code) {

    ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

    if (code.equals(user.getVerification())) {
      user.setEnabled(true);
      user.setVerification(null);
      return userRepo.save(user);
    } else {
      throw new IncorrectVerificationCodeException();
    }

  }

  public ApplicationUser setPassword(String username, String password) {

    ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

    String encodedPassword = passwordEncoder.encode(password);

    user.setPassword(encodedPassword);

    return userRepo.save(user);
  }

}
