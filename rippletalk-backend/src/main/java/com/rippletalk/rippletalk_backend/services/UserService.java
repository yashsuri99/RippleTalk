package com.rippletalk.rippletalk_backend.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.rippletalk.rippletalk_backend.exceptions.EmailAlreadyTakenException;
import com.rippletalk.rippletalk_backend.exceptions.EmailFailedToSendException;
import com.rippletalk.rippletalk_backend.exceptions.FollowException;
import com.rippletalk.rippletalk_backend.exceptions.IncorrectVerificationCodeException;
import com.rippletalk.rippletalk_backend.exceptions.UnableToSavePhotoException;
import com.rippletalk.rippletalk_backend.exceptions.UserDoesNotExistException;
import com.rippletalk.rippletalk_backend.models.ApplicationUser;
import com.rippletalk.rippletalk_backend.models.Image;
import com.rippletalk.rippletalk_backend.models.RegistrationObject;
import com.rippletalk.rippletalk_backend.models.Role;
import com.rippletalk.rippletalk_backend.repositories.RoleRepository;
import com.rippletalk.rippletalk_backend.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepo;
  private final RoleRepository roleRepo;
  private final MailService mailService;
  private final PasswordEncoder passwordEncoder;
  private final ImageService imageService;

  public UserService(UserRepository userRepo, RoleRepository roleRepo, MailService mailService,
      PasswordEncoder passwordEncoder, ImageService imageService) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    this.mailService = mailService;
    this.passwordEncoder = passwordEncoder;
    this.imageService = imageService;
  }

  public ApplicationUser registerUser(RegistrationObject ro) throws EmailAlreadyTakenException {

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
    roles.add(roleRepo.findByAuthority("USER").get());
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

  public ApplicationUser getUserByUsername(String username) throws UserDoesNotExistException {

    return userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);
  }

  public ApplicationUser updateUser(ApplicationUser user) throws EmailAlreadyTakenException {
    try {
      return userRepo.save(user);
    } catch (Exception e) {
      throw new EmailAlreadyTakenException();
    }
  }

  public void generateEmailVerififcation(String username) throws UserDoesNotExistException, EmailFailedToSendException {
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

  public ApplicationUser verifyEmail(String username, Long code)
      throws UserDoesNotExistException, IncorrectVerificationCodeException {

    ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

    if (code.equals(user.getVerification())) {
      user.setEnabled(true);
      user.setVerification(null);
      return userRepo.save(user);
    } else {
      throw new IncorrectVerificationCodeException();
    }

  }

  public ApplicationUser setPassword(String username, String password) throws UserDoesNotExistException {

    ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

    String encodedPassword = passwordEncoder.encode(password);

    user.setPassword(encodedPassword);

    return userRepo.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    ApplicationUser u = userRepo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    Set<GrantedAuthority> authorities = u.getAuthorities()
        .stream()
        .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
        .collect(Collectors.toSet());

    UserDetails ud = new User(u.getUsername(), u.getPassword(), authorities);

    return ud;
  }

  public ApplicationUser setProfileOrBannerPicture(String username, MultipartFile file, String prefix)
      throws UserDoesNotExistException, UnableToSavePhotoException {

    ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

    Image photo = imageService.uploadImage(file, prefix);

    try {
      if (prefix.equals("pfp")) {
        if (user.getProfilePicture() != null || user.getProfilePicture().getImageName().equals("defaultpfp.png")) {
          Path p = Paths.get(user.getProfilePicture().getImagePath());
          Files.deleteIfExists(p);
        }
        user.setProfilePicture(photo);
      } else {
        if (user.getBannerPicture() != null || user.getBannerPicture().getImageName().equals("defaultbnr.png")) {
          Path p = Paths.get(user.getBannerPicture().getImagePath());
          Files.deleteIfExists(p);
        }
        user.setBannerPicture(photo);
      }
    } catch (IOException e) {
      throw new UnableToSavePhotoException();
    }

    return userRepo.save(user);

  }

  public Set<ApplicationUser> followUser(String username, String followee)
      throws UserDoesNotExistException, FollowException {

    if (username.equals(followee))
      throw new FollowException();

    ApplicationUser loggedInUser = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

    Set<ApplicationUser> followingList = loggedInUser.getFollowing();

    ApplicationUser followedUser = userRepo.findByUsername(followee).orElseThrow(UserDoesNotExistException::new);

    Set<ApplicationUser> followersList = followedUser.getFollowers();

    // Add the followed user to following list
    followingList.add(followedUser);
    loggedInUser.setFollowing(followingList);

    // Add the current user to the follower list of the followee
    followersList.add(loggedInUser);
    followedUser.setFollowers(followersList);

    // Update both users
    userRepo.save(loggedInUser);
    userRepo.save(followedUser);

    return loggedInUser.getFollowing();
  }

  public Set<ApplicationUser> retrieveFollowingList(String username) throws UserDoesNotExistException {
    ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

    return user.getFollowing();

  }

  public Set<ApplicationUser> retrieveFollowersList(String username) throws UserDoesNotExistException {
    ApplicationUser user = userRepo.findByUsername(username).orElseThrow(UserDoesNotExistException::new);

    return user.getFollowers();
  }

}
