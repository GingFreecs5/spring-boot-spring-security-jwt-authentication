package com.eaisign.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.eaisign.models.ERole;
import com.eaisign.models.Role;
import com.eaisign.models.User;
import com.eaisign.payload.message.ResponseMessage;
import com.eaisign.payload.request.CheckEmailRequest;
import com.eaisign.payload.request.LoginRequest;
import com.eaisign.payload.request.SignupRequest;
import com.eaisign.payload.response.JwtResponse;
import com.eaisign.payload.response.MessageResponse;
import com.eaisign.repository.RoleRepository;
import com.eaisign.repository.UserRepository;
import com.eaisign.security.jwt.JwtUtils;
import com.eaisign.security.services.UserDetailsImpl;
import com.eaisign.services.FileStorageService;

import com.eaisign.services.implementations.UserServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private static final Logger logger=LoggerFactory.getLogger(AuthController.class);
  static final String ROOT = "C:/Users/yassi/OneDrive/Documents/EAI_Docs/";


AuthenticationManager authenticationManager;

  
  UserRepository userRepository;
  UserServiceImp userServiceImp;
  RoleRepository roleRepository;

  PasswordEncoder encoder;

  JwtUtils jwtUtils;

  FileStorageService fileStorageService;
  public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
		RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils,
		FileStorageService fileStorageService,UserServiceImp userServiceImp) {
	super();
	this.authenticationManager = authenticationManager;
    this.userServiceImp=userServiceImp;
	this.userRepository = userRepository;
	this.roleRepository = roleRepository;
	this.encoder = encoder;
	this.jwtUtils = jwtUtils;
	this.fileStorageService = fileStorageService;
}
  
  
  @PostMapping("/checkEmail")
  public  ResponseEntity<?> checkEmail(@Valid @RequestBody CheckEmailRequest checkEmailRequest){
  logger.info(checkEmailRequest.getEmail());


    if (userRepository.existsByEmail(checkEmailRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }else {
      return ResponseEntity.ok(new MessageResponse("Step Passed"));
    }
  }
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {

    String passwordEncrypted=loginRequest.getPassword();
    String passwordDecrypted= userServiceImp.desEncrypt(passwordEncrypted);

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), passwordDecrypted));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());
    return ResponseEntity.ok(new JwtResponse(jwt,
                         userDetails.getId(),
                         userDetails.getUsername(),
                         userDetails.getPrenom(),
                         userDetails.getEmail(),
                         userDetails.getNum_telephone(),
                         userDetails.getPiece_justicatif(),
                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

	  // Create new user's account
    User user = new User(signUpRequest.getNom(),signUpRequest.getPrenom(),
               signUpRequest.getEmail(),signUpRequest.getPiece_justicatif(),signUpRequest.getNum_telephone(),encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();
    
   if (strRoles == null) {
   Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    	roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    } 
    
    
    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(user.getId());
  }
  @PostMapping("/createfolder/{id}")
	public ResponseEntity<ResponseMessage> createFolder(@PathVariable("id") Long id) {
        fileStorageService.CreateDirectory(ROOT+id+"/Enveloppes");
		String msg = fileStorageService.CreateDirectory(ROOT+id+"/Rapports");
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
	}

    @PostMapping("/decrypt/{password}")
    public ResponseEntity<ResponseMessage> Decrypt(@PathVariable("password") String password) throws Exception {
      String passwordDec=userServiceImp.desEncrypt(password);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(passwordDec));

    }
}
