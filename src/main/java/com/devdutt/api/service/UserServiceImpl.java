package com.devdutt.api.service;

import com.devdutt.api.entity.UserEntity;
import com.devdutt.api.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService,UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Integer saveUser(UserEntity user) {
        String password = user.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);
        user= userRepository.save(user);
        return user.getId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<UserEntity> optUser = userRepository.findUserByEmail(email);
        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException("User with email:- " + email + " not found !");
        } else {
            UserEntity user = optUser.get();
            return new User(user.getEmail(),
                    user.getPassword(),
                    user.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role))
                            .collect(Collectors.toSet()));
        }//else
    }//method

    //Other Approach: Without Using Lambda & Stream API Of Java 8

    /**
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<UserEntity> opt = userRepository.findUserByEmail(email);

        org.springframework.security.core.userdetails.User springUser = null;

        if (opt.isEmpty()) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        UserEntity user = opt.get();
        List<String> roles = user.getRoles();
        Set<GrantedAuthority> ga = new HashSet<>();
        for (String role : roles) {
            ga.add(new SimpleGrantedAuthority(role));
        }

        springUser = new org.springframework.security.core.userdetails.User(
                email,
                user.getPassword(),
                ga);
        return springUser;
    }  **/
}
