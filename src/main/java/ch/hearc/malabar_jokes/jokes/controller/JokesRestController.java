package ch.hearc.malabar_jokes.jokes.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.hearc.malabar_jokes.jokes.model.Joke;
import ch.hearc.malabar_jokes.jokes.model.User;
import ch.hearc.malabar_jokes.jokes.service.JokesService;

@RestController
@RequestMapping("/api")
public class JokesRestController {
    @Autowired
    JokesService jokesService;

    @Autowired
    UserDetailsService userService;

    @PostMapping(value = "/joke")
    public ResponseEntity<?> saveJoke(@RequestBody Joke joke, String username) {
        User user = (User) userService.loadUserByUsername(username);
        joke.setUser(user);
        joke.setDate(Date.valueOf(java.time.LocalDate.now()).toString());
        jokesService.addJoke(joke);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/joke")
    public ResponseEntity<?> getJokes() {
        return ResponseEntity.ok(jokesService.getJokes());
    }

    @GetMapping(value = "/random-joke")
    public ResponseEntity<?> getJoke() {
        Joke joke = jokesService.getRandomJoke();
        return ResponseEntity.ok(joke);
    }

}
