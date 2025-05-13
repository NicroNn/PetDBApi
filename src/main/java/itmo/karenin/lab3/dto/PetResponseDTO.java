package itmo.karenin.lab3.dto;

import itmo.karenin.lab3.entity.Color;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PetResponseDTO {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private String breed;
    private Color color;
    private Integer tailLength;
    private Long ownerId;
    private int friendCount;
}
