package itmo.karenin.lab3.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import itmo.karenin.lab3.entity.Color;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PetDTO {
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Birth date is mandatory")
    @Past(message = "Birth date must be in the past")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    @NotBlank(message = "Breed is mandatory")
    private String breed;

    @NotNull(message = "Color is mandatory")
    private Color color;

    @NotNull(message = "Owner ID is mandatory")
    private Long ownerId;

    @PositiveOrZero(message = "Tail length must be positive or zero")
    private Integer tailLength;
}
