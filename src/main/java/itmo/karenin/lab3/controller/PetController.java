package itmo.karenin.lab3.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import itmo.karenin.lab3.dto.PetDTO;
import itmo.karenin.lab3.dto.PetResponseDTO;
import itmo.karenin.lab3.entity.Color;
import itmo.karenin.lab3.entity.Pet;
import itmo.karenin.lab3.mapping.PetMapper;
import itmo.karenin.lab3.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "API для работы с питомцами")
public class PetController {
    private final PetService petService;
    private final PetMapper petMapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Получить питомца по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Питомец найден"),
                    @ApiResponse(responseCode = "404", description = "Питомец не найден")
            })
    public PetResponseDTO getPet(@PathVariable Long id) {
        return petMapper.toResponseDto(petService.getPetById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "Получить питомцев с фильтрацией и пагинацией",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Питомцы найдены"),
                    @ApiResponse(responseCode = "404", description = "Питомцы не найдены")
            })
    public Page<PetResponseDTO> getPets(
            @RequestParam(required = false)
            @Parameter(description = "Цвет питомца") Color color,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return petService.getPetsByColor(color, pageable)
                .map(petMapper::toResponseDto);
    }

    @PreAuthorize("hasRole('ADMIN') or #dto.ownerId == authentication.principal.id")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать нового питомца",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Питомец создан"),
                    @ApiResponse(responseCode = "400", description = "Питомец не создан")
            })
    public PetResponseDTO createPet(@Valid @RequestBody PetDTO dto) {
        Pet pet = petMapper.toEntity(dto);
        Pet savedPet = petService.createPet(pet);
        return petMapper.toResponseDto(savedPet);
    }

    @PreAuthorize("hasRole('ADMIN') or @petService.isOwner(#id, authentication.principal.owner.id)")
    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные питомца",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные питомца обновлены"),
                    @ApiResponse(responseCode = "400", description = "Данные питомца не обновлены")
            })
    public PetResponseDTO updatePet(
            @PathVariable Long id,
            @Valid @RequestBody PetDTO petDTO
    ) {
        return petMapper.toResponseDto(petService.updatePet(id, petMapper.toEntity(petDTO)));
    }

    @PreAuthorize("hasRole('ADMIN') or @petService.isOwner(#id, authentication.principal.owner.id)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить питомца по ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Питомца удалён"),
                    @ApiResponse(responseCode = "400", description = "Питомца не удалён")
            })
    public void deletePet(@PathVariable Long id) {
        petService.deletePet(id);
    }

    @PreAuthorize("@petService.isOwner(#petId, authentication.principal.owner.id)")
    @PostMapping("/{petId}/friends/{friendId}")
    @Operation(summary = "Добавить друга питомцу",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Друг питомца добавлен"),
                    @ApiResponse(responseCode = "400", description = "Друг питомца не добавлен")
            })
    public PetResponseDTO addFriend(
            @PathVariable Long petId,
            @PathVariable Long friendId
    ) {
        return petMapper.toResponseDto(petService.addFriend(petId, friendId));
    }

    @PreAuthorize("@petService.isOwner(#petId, authentication.principal.owner.id)")
    @DeleteMapping("/{petId}/friends/{friendId}")
    @Operation(summary = "Удалить друга у питомца",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Друг питомца удалён"),
                    @ApiResponse(responseCode = "400", description = "Друг питомца не удалён")
            })
    public PetResponseDTO removeFriend(
            @PathVariable Long petId,
            @PathVariable Long friendId
    ) {
        return petMapper.toResponseDto(petService.removeFriend(petId, friendId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{petId}/friends")
    @Operation(summary = "Получить друзей питомца",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Друзья питомца получены"),
                    @ApiResponse(responseCode = "404", description = "Друзья питомца не получены")
            })
    public Page<PetResponseDTO> getFriends(
            @PathVariable Long petId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return petService.getFriends(petId, pageable)
                .map(petMapper::toResponseDto);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    @Operation(summary = "Расширенный поиск питомцев",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Питомец найден"),
                    @ApiResponse(responseCode = "404", description = "Питомец не найден")
            })
    public Page<PetResponseDTO> searchPets(
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthdayStartDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthdayEndDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return petService.searchPets(breed, birthdayStartDate, birthdayEndDate, pageable)
                .map(petMapper::toResponseDto);
    }
}