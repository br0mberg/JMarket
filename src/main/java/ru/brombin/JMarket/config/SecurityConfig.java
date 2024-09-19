package ru.brombin.JMarket.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.brombin.JMarket.entity.User;
import ru.brombin.JMarket.services.UserService;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    @Bean
    @Autowired
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           UserService userService) throws Exception{
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/static/**", "/images/**", "/item/**").permitAll()
                        .requestMatchers("/items", "/items/{id}").hasAnyRole("USER", "SELLER", "ADMIN")
                        .requestMatchers("/items/new").hasAnyRole("SELLER", "ADMIN")
                        .requestMatchers("/items/*/edit").hasRole("ADMIN")
                        .requestMatchers("/people", "/people/**", "/items/**").hasRole("ADMIN")
                        .requestMatchers("/auth/**", "/error").permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN", "SELLER")
                )
                .formLogin(this::configureFormLogin)
                .authenticationProvider(authenticationProvider(userService))
                .logout(this::configureLogout);
        return http.build();
    }

    private void configureFormLogin(FormLoginConfigurer<HttpSecurity> form) {
        form.loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/items", true)
                .failureUrl("/auth/login?error");
    }

    private void configureLogout(LogoutConfigurer<HttpSecurity> logout) {
        logout.logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(getPasswordEncoder());

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
