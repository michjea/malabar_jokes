package ch.hearc.malabar_jokes.jokes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.hearc.malabar_jokes.jokes.model.Joke;
import ch.hearc.malabar_jokes.jokes.repository.JokeRepository;
import ch.hearc.malabar_jokes.jokes.service.JokesService;
import java.util.List;

@Service
public class JokesServiceImpl implements JokesService {

    @Autowired
    JokeRepository jokeRepository;

    public void addJoke(Joke joke) {
        jokeRepository.save(joke);
    }

    public List<Joke> getJokes() {
        return (List<Joke>) jokeRepository.findAll();
    }

    public Joke getRandomJoke() {

        List<Joke> jokes = getJokes();

        // List<Joke> jokes = jokeRepository.getJokes();

        // if jokes size is not empty
        if (jokes.size() == 0) {
            return null;
        }

        int randomIndex = (int) (Math.random() * jokes.size());
        return jokes.get(randomIndex);
    }
}
