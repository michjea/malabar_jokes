package ch.hearc.malabar_jokes.jokes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ch.hearc.malabar_jokes.jokes.service.JokesService;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ch.hearc.malabar_jokes.jokes.model.Joke;
import ch.hearc.malabar_jokes.jokes.model.User;
import ch.hearc.malabar_jokes.jokes.repository.UserRepository;

import org.springframework.validation.BindingResult;

// import random
import java.util.Random;

@Controller
public class JokesController {

    @Autowired
    JokesService jokesService;

    @Autowired
    UserDetailsService userService;

    @PostMapping(value = "/save-joke")
    public String saveJoke(@ModelAttribute Joke joke, BindingResult errors, Model model, String username) {
        User user = (User) userService.loadUserByUsername(username);
        joke.setUser(user);
        joke.setDate(Date.valueOf(java.time.LocalDate.now()).toString());
        jokesService.addJoke(joke);
        return "redirect:/home";
    }

    @GetMapping(value = "/new-joke")
    public String showNewJokeForm(Model model) {
        model.addAttribute("joke", new Joke());
        return "new-joke";
    }

    @GetMapping(value = { "/", "", "/home" })
    public String showHomePage(Model model) {
        Joke joke = jokesService.getRandomJoke();

        System.out.println(joke.getUserName());

        model.addAttribute("joke", joke);
        // add username attribute
        model.addAttribute("username", joke.getUserName());

        // model.addAttribute("jokes", jokesService.getJokes());
        return "home";
    }
}
