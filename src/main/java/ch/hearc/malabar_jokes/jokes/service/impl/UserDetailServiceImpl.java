package ch.hearc.malabar_jokes.jokes.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import antlr.collections.List;
import ch.hearc.malabar_jokes.jokes.model.User;
import ch.hearc.malabar_jokes.jokes.repository.UserRepository;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return UserDetailsImpl.build(user);
    }

    // get user by id
    public UserDetails loadUserById(int id) {
        User user = userRepository.getUserById(id);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return UserDetailsImpl.build(user);
    }
}
