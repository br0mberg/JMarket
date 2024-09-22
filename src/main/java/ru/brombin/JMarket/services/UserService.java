package ru.brombin.JMarket.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.repositories.UserRepository;
import ru.brombin.JMarket.util.exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        logger.info("Fetching all users by user {}", getCurrentUser().getId());
        return userRepository.findAll();
    }

    public Optional<User> findOne(int id) {
        logger.info("Searching for user with id {} by user {}", id, getCurrentUser().getId());
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            logger.warn("User with id {} not found", id);
        }
        return user;
    }

    public List<User> findByName(String name) {
        logger.info("Searching for users with name {} by user {}", name, getCurrentUser().getId());
        return userRepository.findByUsernameOrderByAge(name);
    }

    public User findByEmail(String email){
        logger.info("Searching for user with email {} by user {}", email, getCurrentUser().getId());
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void save(User user) {
        logger.info("Saving new user by user {}", getCurrentUser().getId());
        user.setRegistrationDate(LocalDateTime.now());
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        userRepository.save(user);
        logger.info("User '{}' saved successfully", user.getId());
    }

    @Transactional
    public void update(int id, User updatedUser) {
        logger.info("Updating user with id {} by user {}", id, getCurrentUser().getId());
        try {
            userRepository.updateUserFields(id,
                    updatedUser.getUsername(),
                    updatedUser.getPassword(),
                    updatedUser.getAge(),
                    updatedUser.getEmail(),
                    updatedUser.getRole(),
                    updatedUser.getDateOfBirth().atStartOfDay());
            logger.info("User with id {} updated successfully", id);
        } catch (Exception e) {
            throw new NotFoundException("User with id " + id + " not found");
        }
    }

    @Transactional
    public void delete(int id) {
        logger.info("Deleting user with id {} by user {}", id, getCurrentUser().getId());
        userRepository.deleteById(id);
        logger.info("User with id {} deleted successfully", id);
    }

    @Transactional
    public void saveAll(List<User> people) {
        logger.info("Saving list of users, total: {} by user {}", people.size(), getCurrentUser().getId());
        for (User p : people) {
            p.setRegistrationDate(LocalDateTime.now());
        }
        userRepository.saveAll(people);
        logger.info("List of users saved successfully, total: {}", people.size());
    }

    public String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return user.get();
    }
}
