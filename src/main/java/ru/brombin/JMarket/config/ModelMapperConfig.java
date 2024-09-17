package ru.brombin.JMarket.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.brombin.JMarket.dto.UserDTO;
import ru.brombin.JMarket.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Converter for String to LocalDate
        Converter<String, LocalDate> stringToLocalDate = context ->
                LocalDate.parse(context.getSource(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        // Converter for LocalDate to String
        Converter<LocalDate, String> localDateToString = context ->
                context.getSource().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        modelMapper.addConverter(stringToLocalDate);
        modelMapper.addConverter(localDateToString);

        modelMapper.typeMap(UserDTO.class, User.class).addMappings(mapper -> {
            mapper.using(stringToLocalDate).map(UserDTO::getDateOfBirth, User::setDateOfBirth);
        });

        modelMapper.typeMap(User.class, UserDTO.class).addMappings(mapper -> {
            mapper.using(localDateToString).map(User::getDateOfBirth, UserDTO::setDateOfBirth);
        });

        return modelMapper;
    }
}
