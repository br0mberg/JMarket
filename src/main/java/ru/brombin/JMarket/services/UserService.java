package ru.brombin.JMarket.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.repositories.UserRepository;
import ru.brombin.JMarket.util.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(int id) {
        Optional<User> User = userRepository.findById(id);
        return User.orElseThrow(UserNotFoundException::new);
    }

    public List<User> findByName(String name) {
        return userRepository.findByUsernameOrderByAge(name);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public void save(User User) {
        User.setRegistrationDate(LocalDateTime.now());
        userRepository.save(User);
    }

    @Transactional
    public void update(int id, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(id);

        if (existingUserOpt.isEmpty()) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }

        User existingUser = existingUserOpt.get();

        updatedUser.setId(existingUser.getId());
        updatedUser.setRegistrationDate(existingUser.getRegistrationDate());

        userRepository.save(updatedUser);
    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void saveAll(List<User> people) {
        for (User p : people) {
            p.setRegistrationDate(LocalDateTime.now());
        }
        userRepository.saveAll(people);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return user.get();
    }
}
