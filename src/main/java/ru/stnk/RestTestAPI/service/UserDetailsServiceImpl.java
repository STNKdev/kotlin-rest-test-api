package ru.stnk.RestTestAPI.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.stnk.RestTestAPI.entity.User;
import ru.stnk.RestTestAPI.repository.UserRepository;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional userFromDB = userRepository.findByEmail(email);

        if (userFromDB.isPresent()) {

            return new UserDetailsImpl( (User) userFromDB.get() );

        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}
