package ru.brombin.JMarket.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.repositorie.UserRepository;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class RegistrationService {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public void register(User user) {
        logger.info("Starting registration for new user");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setNanos(0);

        user.setRegistrationDate(timestamp.toLocalDateTime());
        user.setRole(User.UserRole.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
        logger.info("User with id '{}' registered successfully at {}", user.getId(), user.getRegistrationDate());
    }
}
