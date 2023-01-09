package ch.hearc.malabar_jokes.jokes.repository;

import ch.hearc.malabar_jokes.jokes.model.Joke;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface JokeRepository extends CrudRepository<Joke, Long> {
    // public List<Joke> getJokes();

    // public void addJoke(Joke joke);
    Joke findById(long id);

}