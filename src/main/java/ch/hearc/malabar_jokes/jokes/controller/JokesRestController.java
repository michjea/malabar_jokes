package ch.hearc.malabar_jokes.jokes.controller;

import java.sql.Date;
import java.util.List;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.event.AuthenticationFailureCredentialsExpiredEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.malabar_jokes.JmsLogger;
import ch.hearc.malabar_jokes.jokes.model.Joke;
import ch.hearc.malabar_jokes.jokes.model.Log;
import ch.hearc.malabar_jokes.jokes.model.User;
import ch.hearc.malabar_jokes.jokes.service.JokesService;
import ch.hearc.malabar_jokes.jokes.service.impl.UserService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class JokesRestController {
    @Autowired
    JokesService jokesService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @Autowired
    JmsLogger jmsLogger;

    @PostMapping(value = "/joke")
    public ResponseEntity<?> saveJoke(@RequestBody Joke joke, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            jokesService.addJoke(new Joke(joke.getTitle(), joke.getContent(),
                    Date.valueOf(java.time.LocalDate.now()).toString(), user));

            jmsLogger.log(new Log("SUCCESS", "Joke added by " + user.getUsername()));

            return ResponseEntity.ok().build();
        } else {
            jmsLogger.log(new Log("ERROR", "Joke not added"));
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/joke")
    public ResponseEntity<?> getJokes() {
        return ResponseEntity.ok(jokesService.getJokes());
    }

    @GetMapping(value = "/joke/{id}")
    public ResponseEntity<?> getJokeById(@PathVariable("id") Long id) {
        jmsLogger.log(new Log("INFO", "Joke retrieved"));
        return ResponseEntity.ok(jokesService.getJokeById(id));
    }

    @GetMapping(value = "/random-joke")
    public ResponseEntity<?> getJoke() {
        Joke joke = jokesService.getRandomJoke();
        jmsLogger.log(new Log("INFO", "Random joke retrieved"));
        return ResponseEntity.ok(joke);
    }

    @GetMapping(value = "/my-jokes")
    public ResponseEntity<?> getMyJokes(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            List<Joke> jokes = jokesService.getJokesByUser(user);
            jmsLogger.log(new Log("INFO", "Jokes retrieved by " + user.getUsername()));
            return ResponseEntity.ok(jokes);
        } else {
            jmsLogger.log(new Log("ERROR", "Jokes not retrieved"));
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/joke/{id}")
    public ResponseEntity<?> deleteJoke(@PathVariable("id") Long id, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            Joke joke = jokesService.getJokeById(id);

            if (joke.getUserId() == user.getId()) {
                jokesService.deleteJokeById(id);
                jmsLogger.log(new Log("INFO", "Joke deleted by " + user.getUsername()));

                return ResponseEntity.ok().build();
            }
        }
        jmsLogger.log(new Log("ERROR", "Joke not deleted"));

        return ResponseEntity.badRequest().build();
    }

    @PutMapping(value = "/joke/{id}")
    public ResponseEntity<?> updateJoke(@PathVariable("id") Long id, @RequestBody Joke joke,
            Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getUserByUsername(authentication.getName());
            Joke joke_ = jokesService.getJokeById(id).get();

            if (joke_.getUserId() == user.getId()) {
                joke_.setContent(joke.getContent());
                joke_.setTitle(joke.getTitle());
                joke_.setDate(Date.valueOf(java.time.LocalDate.now()).toString());
                jokesService.saveJoke(joke_);
                jmsLogger.log(new Log("INFO", "Joke updated by " + user.getUsername()));

                return ResponseEntity.ok().build();
            }
        }
        jmsLogger.log(new Log("ERROR", "Joke not updated"));
        return ResponseEntity.badRequest().build();
    }
}
