package ru.brombin.JMarket.dto;

import jakarta.persistence.*;import jakarta.validation.constraints.NotNull;import jakarta.validation.constraints.Pattern;import jakarta.validation.constraints.PositiveOrZero;import jakarta.validation.constraints.Size;import lombok.Data;import org.springframework.format.annotation.DateTimeFormat;import ru.brombin.JMarket.model.ItemCategory;
import ru.brombin.JMarket.model.Person;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ItemDTO {

    @NotNull(message="Name should not be empty")
    @Size(min=2, max=50, message="Name should be between 2 and 50 characters")
    private String name;

    @NotNull(message="Description should not be empty")
    @Size(max=255, message="Description should be lower than 255")
    private String description;

    @NotNull(message="Category should not be empty")
    private ItemCategory category;

    @NotNull(message="Price can't be null")
    @PositiveOrZero(message="Price should be greater than 0")
    private int price;

    @NotNull(message="Quantity can't be null")
    @PositiveOrZero(message="Quantity should be greater than 0")
    private int quantity;

    @NotNull(message="Article Number can't be empty")
    @Size(min=6, max=12, message="Article Number should be between 6 and 12 characters")
    @Pattern(regexp="[A-Za-z0-9]+")
    private String articleNumber;
}
