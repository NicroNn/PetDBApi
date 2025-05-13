package itmo.karenin.lab3.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import itmo.karenin.lab3.dto.OwnerDTO;
import itmo.karenin.lab3.dto.PetResponseDTO;
import itmo.karenin.lab3.entity.Owner;
import itmo.karenin.lab3.mapping.OwnerMapper;
import itmo.karenin.lab3.mapping.PetMapper;
import itmo.karenin.lab3.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import itmo.karenin.lab3.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/owners")
@RequiredArgsConstructor
@Tag(name = "Owners", description = "API для работы с владельцами питомцев")
public class OwnerController {
    private final OwnerService ownerService;
    private final PetService petService;
    private final OwnerMapper ownerMapper;
    private final PetMapper petMapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Получить владельца по ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Владелец найден"),
                    @ApiResponse(responseCode = "404", description = "Владелец не найден")
            })
    public OwnerDTO getOwner(@PathVariable Long id) {
        return ownerMapper.toDto(ownerService.getOwnerById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "Получить всех владельцев с пагинацией",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Владельцы найдены"),
                    @ApiResponse(responseCode = "404", description = "Владельцы не найдены")
            })
    public Page<OwnerDTO> getAllOwners(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sort));
        Page<Owner> owners = ownerService.getAllOwners(pageable);
        return owners.map(ownerMapper::toDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать нового владельца",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Владелец создан"),
                    @ApiResponse(responseCode = "400", description = "Владелец не создан")
            })
    public OwnerDTO createOwner(@Valid @RequestBody OwnerDTO ownerDTO) {
        Owner owner = ownerMapper.toEntity(ownerDTO);
        Owner savedOwner = ownerService.createOwner(owner);
        return ownerMapper.toDto(savedOwner);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.owner.id")
    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные владельца",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные владельца обновлены"),
                    @ApiResponse(responseCode = "400", description = "Данные владельца не обновлены")
            })
    public OwnerDTO updateOwner(@PathVariable Long id, @Valid @RequestBody OwnerDTO ownerDTO) {
        Owner owner = ownerMapper.toEntity(ownerDTO);
        Owner updatedOwner = ownerService.updateOwner(id, owner);
        return ownerMapper.toDto(updatedOwner);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.owner.id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить владельца по ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Владелец удалён"),
                    @ApiResponse(responseCode = "400", description = "Владелец не удалён")
            })
    public void deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    @Operation(summary = "Поиск владельцев по имени",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Владелец найден"),
                    @ApiResponse(responseCode = "404", description = "Владелец не найден")
            })
    public Page<OwnerDTO> searchOwnersByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ownerService.searchOwnersByName(name, pageable)
                .map(ownerMapper::toDto);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{ownerId}/pets")
    @Operation(summary = "Получить всех питомцев владельца",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Питомцы найдены"),
                    @ApiResponse(responseCode = "404", description = "Питомцы не найдены")
            })
    public Page<PetResponseDTO> getPetsByOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return petService.getPetsByOwnerId(ownerId, pageable)
                .map(petMapper::toResponseDto);
    }
}