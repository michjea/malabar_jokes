package ch.hearc.malabar_jokes.jokes.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.relation.Role;
import javax.validation.Valid;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.malabar_jokes.JmsLogger;
import ch.hearc.malabar_jokes.jokes.model.Log;
import ch.hearc.malabar_jokes.jokes.model.User;
import ch.hearc.malabar_jokes.jokes.payload.request.LoginRequest;
import ch.hearc.malabar_jokes.jokes.payload.request.SignupRequest;
import ch.hearc.malabar_jokes.jokes.payload.response.JwtResponse;
import ch.hearc.malabar_jokes.jokes.payload.response.MessageResponse;
import ch.hearc.malabar_jokes.jokes.repository.UserRepository;
import ch.hearc.malabar_jokes.jokes.security.jwt.JwtUtils;
import ch.hearc.malabar_jokes.jokes.service.impl.UserDetailsImpl;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    JmsLogger logger;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        // Obtenir les erreurs de validation
        List<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        // Créer une réponse d'erreur avec les messages de validation
        MessageResponse errorResponse = new MessageResponse("Validation error :" + errors.toString());

        logger.log(new Log("ERROR", "Validation error :" + errors.toString()));

        // Renvoyer une réponse avec le code d'état 400 Bad Request
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        logger.log(new Log("SUCCESS", "User " + userDetails.getUsername() + " logged in"));

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                jwt, roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        logger.log(new Log("SUCCESS", "User " + user.getUsername() + " registered"));

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
