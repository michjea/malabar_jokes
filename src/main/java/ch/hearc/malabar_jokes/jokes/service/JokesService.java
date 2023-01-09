package ch.hearc.malabar_jokes.jokes.service;

import ch.hearc.malabar_jokes.jokes.model.Joke;
import java.util.List;

public interface JokesService {

    public void addJoke(Joke joke);

    public List<Joke> getJokes();

    public Joke getRandomJoke();
}
