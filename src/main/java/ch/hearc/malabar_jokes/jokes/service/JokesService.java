package ch.hearc.malabar_jokes.jokes.service;

import ch.hearc.malabar_jokes.jokes.model.Joke;
import ch.hearc.malabar_jokes.jokes.model.User;

import java.util.List;

public interface JokesService {

    public void addJoke(Joke joke);

    public List<Joke> getJokes();

    public Joke getRandomJoke();

    public Joke getJokeById(Object id);

    public List<Joke> getJokesByUser(User user);

    public void deleteJokeById(long id);

    public void saveJoke(Joke joke);
}
