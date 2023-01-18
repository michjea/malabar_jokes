package ch.hearc.malabar_jokes.jokes.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ch.hearc.malabar_jokes.jokes.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    public User getUserByUsername(@Param("username") String username);

    public User getUserById(@Param("id") int id);
}
