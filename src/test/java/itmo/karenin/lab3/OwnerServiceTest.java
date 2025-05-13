package itmo.karenin.lab3;

import itmo.karenin.lab3.entity.Owner;
import itmo.karenin.lab3.exception.OwnerNotFoundException;
import itmo.karenin.lab3.repository.OwnerRepository;
import itmo.karenin.lab3.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerService ownerService;

    @Test
    void getOwnerById_ExistingId_ReturnsOwner() {
        Owner owner = new Owner();
        when(ownerRepository.findById(1L)).thenReturn(Optional.of(owner));

        Owner result = ownerService.getOwnerById(1L);
        assertNotNull(result);
    }

    @Test
    void getOwnerById_NonExistingId_ThrowsException() {
        when(ownerRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(OwnerNotFoundException.class, () -> ownerService.getOwnerById(999L));
    }

    @Test
    void createOwner_ValidEntity_ReturnsSavedOwner() {
        Owner owner = new Owner();
        when(ownerRepository.save(owner)).thenReturn(owner);

        Owner result = ownerService.createOwner(owner);
        assertNotNull(result);
    }

    @Test
    void deleteOwner_ValidId_DeletesEntity() {
        ownerService.deleteOwner(1L);
        verify(ownerRepository).deleteById(1L);
    }

    @Test
    void searchOwnersByName_ValidQuery_ReturnsPage() {
        Page<Owner> page = new PageImpl<>(List.of(new Owner()));
        when(ownerRepository.findByNameContainingIgnoreCase("john", PageRequest.of(0, 10)))
                .thenReturn(page);

        Page<Owner> result = ownerService.searchOwnersByName("john", PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getAllOwners_ReturnsPagedResults() {
        Page<Owner> page = new PageImpl<>(List.of(new Owner()));
        when(ownerRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<Owner> result = ownerService.getAllOwners(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
    }
}