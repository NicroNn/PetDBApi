package itmo.karenin.lab3;

import itmo.karenin.lab3.entity.Pet;
import itmo.karenin.lab3.exception.PetNotFoundException;
import itmo.karenin.lab3.repository.PetRepository;
import itmo.karenin.lab3.service.PetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

    @Test
    void getPetById_ExistingId_ReturnsPet() {
        Pet pet = new Pet();
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));

        Pet result = petService.getPetById(1L);
        assertNotNull(result);
    }

    @Test
    void getPetById_NonExistingId_ThrowsException() {
        when(petRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(PetNotFoundException.class, () -> petService.getPetById(999L));
    }

    @Test
    void createPet_ValidEntity_ReturnsSavedPet() {
        Pet pet = new Pet();
        when(petRepository.save(pet)).thenReturn(pet);

        Pet result = petService.createPet(pet);
        assertNotNull(result);
    }

    @Test
    void addFriend_ValidIds_UpdatesFriends() {
        Pet pet = new Pet();
        Pet friend = new Pet();
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(petRepository.findById(2L)).thenReturn(Optional.of(friend));

        petService.addFriend(1L, 2L);

        assertTrue(pet.getFriends().contains(friend));
        verify(petRepository).saveAll(List.of(pet, friend));
    }
}