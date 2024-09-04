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
                        // Разрешаем доступ к списку товаров и отдельному товару для роли USER
                        .requestMatchers("/items").hasAnyRole("USER", "SELLER", "ADMIN")
                        .requestMatchers("/items/{id}").hasAnyRole("USER", "SELLER", "ADMIN")
                        // Разрешаем доступ к созданию новых товаров только для SELLER и ADMIN
                        .requestMatchers("/items/new").hasAnyRole("SELLER", "ADMIN")
                        // Разрешаем доступ к редактированию товаров только для ADMIN
                        .requestMatchers("/items/*/edit").hasRole("ADMIN")
                        // Полный доступ к людям и товарам только для ADMIN
                        .requestMatchers("/people", "/people/**", "/items/**").hasRole("ADMIN")
                        // Доступ ко всем страницам авторизации и ошибкам для всех
                        .requestMatchers("/auth/**", "/error").permitAll()
                        // Все остальные запросы доступны для ролей USER, ADMIN, SELLER
                        .anyRequest().hasAnyRole("USER", "ADMIN", "SELLER")
                )
                .formLogin(form -> form.
                        loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/items", true)
                        .failureUrl("/auth/login?error")
                )
                .authenticationProvider(authenticationProvider(userService))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login")
                );

        // отдаем страницу, и указываем куда данные с формы отправлять (нам самим их обрабатывать не надо)
        // поля в форме обязательно username и password иначе spring security не увидит

        return http.build();
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
