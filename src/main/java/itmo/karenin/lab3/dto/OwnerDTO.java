package itmo.karenin.lab3.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class OwnerDTO {
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Birth date is mandatory")
    @Past(message = "Birth date must be in the past")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    private List<PetDTO> pets = new ArrayList<>();
}