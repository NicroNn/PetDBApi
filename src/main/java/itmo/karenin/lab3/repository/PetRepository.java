package itmo.karenin.lab3.repository;

import itmo.karenin.lab3.entity.Color;
import itmo.karenin.lab3.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Page<Pet> findAllByColor(Color color, Pageable pageable);

    @Query("select p.friends from Pet p where p.id = :petId")
    Page<Pet> findAllFriends(@Param("petId") Long petId, Pageable pageable);

    @Query("select p from Pet p where p.owner.id = :ownerId")
    Page<Pet> findByOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);

    Page<Pet> findByBreedContainingAndBirthDateBetween(
            String breed,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
}
