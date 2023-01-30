package ch.hearc.malabar_jokes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import ch.hearc.malabar_jokes.jokes.controller.JokesController;
import ch.hearc.malabar_jokes.jokes.model.Joke;
import ch.hearc.malabar_jokes.jokes.model.User;
import ch.hearc.malabar_jokes.jokes.repository.UserRepository;
import ch.hearc.malabar_jokes.jokes.service.JokesService;
import org.junit.runner.RunWith;

//@RunWith(SpringRunner.class)
@SpringBootTest
class JokesControllerTest {

    @Autowired
    private JokesService jokesService;

    @Autowired
    private JokesController jokesController;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveJokeTest() {
        User user = new User("testUsername", "testPassword");
        userRepository.save(user);

        Joke joke = new Joke("testJoke", "testJoke", "testDate", user);

        jokesService.addJoke(joke);

        Joke savedJoke = jokesService.getJokeById(joke.getId());
        assertEquals(joke.getId(), savedJoke.getId());
        System.out.println("save joke test passed");
    }

    @Test
    public void showNewJokeFormTest() {
        Model model = new ExtendedModelMap();
        String result = jokesController.showNewJokeForm(model);
        assertEquals("new-joke", result);

        Joke joke = (Joke) model.asMap().get("joke");
        assertNotNull(joke);
        System.out.println("show new joke form test passed");
    }

    @Test
    public void showHomePageTest() {
        Model model = new ExtendedModelMap();
        String result = jokesController.showHomePage(model);
        assertEquals("home", result);

        Joke joke = (Joke) model.asMap().get("joke");
        assertNotNull(joke);
        System.out.println("show home page test passed");
    }

}
