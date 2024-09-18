package ru.brombin.JMarket.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.repositories.UserRepository;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class RegistrationService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public int register(User user) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setNanos(0);
        user.setRegistrationDate(timestamp.toLocalDateTime());
        user.setRole(User.UserRole.ROLE_USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user.getId();
    }
}
