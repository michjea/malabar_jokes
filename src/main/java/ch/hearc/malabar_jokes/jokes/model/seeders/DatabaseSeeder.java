package ch.hearc.malabar_jokes.jokes.model.seeders;

import java.sql.Date;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ch.hearc.malabar_jokes.jokes.model.Joke;
import ch.hearc.malabar_jokes.jokes.model.User;
import ch.hearc.malabar_jokes.jokes.repository.JokeRepository;
import ch.hearc.malabar_jokes.jokes.repository.UserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

        @Autowired
        UserRepository userRepository;

        @Autowired
        JokeRepository jokeRepository;

        @Override
        public void run(String... args) throws Exception {
                // loadData();
        }

        private void loadData() {
                String date = new Date(System.currentTimeMillis()).toString();

                if (userRepository.count() > 0 || jokeRepository.count() > 0) {
                        System.out.println("Database already seeded");
                        return;
                }

                // Password encoder
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

                User user1 = new User("user1", passwordEncoder.encode("password1"));
                User user2 = new User("user2", passwordEncoder.encode("password2"));
                User user3 = new User("user3", passwordEncoder.encode("password3"));

                userRepository.save(user1);
                userRepository.save(user2);
                userRepository.save(user3);

                jokeRepository.save(new Joke(jokes[0][0], jokes[0][1], date, user1));
                jokeRepository.save(new Joke(jokes[1][0], jokes[1][1], date, user1));
                jokeRepository.save(new Joke(jokes[2][0], jokes[2][1], date, user1));

                jokeRepository.save(new Joke(jokes[3][0], jokes[3][1], date, user2));
                jokeRepository.save(new Joke(jokes[4][0], jokes[4][1], date, user2));
                jokeRepository.save(new Joke(jokes[5][0], jokes[5][1], date, user2));

                jokeRepository.save(new Joke(jokes[6][0], jokes[6][1], date, user3));
                jokeRepository.save(new Joke(jokes[7][0], jokes[7][1], date, user3));
                jokeRepository.save(new Joke(jokes[8][0], jokes[8][1], date, user3));
                jokeRepository.save(new Joke(jokes[9][0], jokes[9][1], date, user3));

                System.out.println("Database seeded");
        }

        private String[][] jokes = {
                        { "La blague la plus nulle au monde",
                                        "Pourquoi les giraffes ont-elles un très long cou ? parce qu'elles puent des fesses" },
                        { "Chèvres", "Quel club de football les chèvres aiment-t-elles ? Beeeeee-nfica" },
                        { "Dark humour", "I was digging in our garden and found a chest full of gold coins. I wanted to run straight home to tell my wife about it. Then I remembered why I was digging in our garden." },
                        { "Adoption", "Do you know the phrase “One man’s trash is another man’s treasure”? Wonderful saying, horrible way to find out that you were adopted." },
                        { "La magie de Noël",
                                        "Pourquoi les petits chinois ne croient pas au Père Noël ? Parce que c'est eux qui fabriquent les jouets." },
                        { "Boomers", "My grandfather said my generation relies too much on the latest technology. So I unplugged his life support." },
                        { "Web dev", "I made a website for orphans. It doesn’t have a home page." },
                        { "Eat your veggies", "What’s the hardest part of a vegetable to eat? The wheelchair." },
                        { "Programmer pro tip", "Code Java underwater, so nobody could see you crying." },
                        { "Au plat", "La famille Deuf on un fils, comment s'appelle-t-il ? John. John Deuf." },
        };
}