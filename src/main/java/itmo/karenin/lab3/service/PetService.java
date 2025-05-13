package itmo.karenin.lab3.service;

import itmo.karenin.lab3.entity.Color;
import itmo.karenin.lab3.entity.Pet;
import itmo.karenin.lab3.exception.PetNotFoundException;
import itmo.karenin.lab3.mapping.PetMapper;
import itmo.karenin.lab3.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final OwnerService ownerService;

    @Transactional(readOnly = true)
    public Pet getPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));
    }

    @Transactional(readOnly = true)
    public Page<Pet> getPetsByColor(Color color, Pageable pageable) {
        return petRepository.findAllByColor(color, pageable);
    }

    @Transactional
    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    @Transactional
    public Pet updatePet(Long id, Pet pet) {
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));
        petMapper.updateEntity(pet, existingPet);
        return petRepository.save(existingPet);
    }

    @Transactional
    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    @Transactional
    public Pet addFriend(Long petId, Long friendId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));
        Pet friend = petRepository.findById(friendId)
                .orElseThrow(() -> new PetNotFoundException("Friend not found"));
        pet.addFriend(friend);
        friend.addFriend(pet);
        petRepository.saveAll(List.of(pet, friend));
        return petRepository.save(pet);
    }

    @Transactional
    public Pet removeFriend(Long petId, Long friendId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));
        Pet friend = petRepository.findById(friendId)
                .orElseThrow(() -> new PetNotFoundException("Friend not found"));

        pet.getFriends().remove(friend);
        friend.getFriends().remove(pet);
        petRepository.saveAll(List.of(pet, friend));
        return petRepository.save(pet);
    }

    @Transactional(readOnly = true)
    public Page<Pet> getFriends(Long petId, Pageable pageable) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));
        return petRepository.findAllFriends(pet.getId(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<Pet> searchPets(
            String breed,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        return petRepository.findByBreedContainingAndBirthDateBetween(breed, startDate, endDate, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Pet> getPetsByOwnerId(Long ownerId, Pageable pageable) {
        return petRepository.findByOwnerId(ownerId, pageable);
    }

    public boolean isOwner(Long petId, Long ownerId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet not found"));
        return pet.getOwner().getId().equals(ownerId);
    }
}