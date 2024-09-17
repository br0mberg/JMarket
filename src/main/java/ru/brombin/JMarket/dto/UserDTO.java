package ru.brombin.JMarket.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.brombin.JMarket.entity.User;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    @NotEmpty(message="Name should not be empty")
    @Size(min=2, max=30, message="Name should be between 2 and 30 characters")
    private String username;

    @NotEmpty(message="Password can't be empty")
    private String password;

    @Min(value=0, message="Age should be greater than 0")
    private int age;

    @NotEmpty(message="Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message="Role should be set")
    private User.UserRole role = User.UserRole.ROLE_USER;

    @NotEmpty(message="Date of Birth should not be empty")
    @Pattern(regexp = "\\d{4}/\\d{2}/\\d{2}", message = "Date of Birth should be in the format YYYY/MM/DD")
    private String dateOfBirth;
}
