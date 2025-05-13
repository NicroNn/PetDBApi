package itmo.karenin.lab3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pet")
public class Pet {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    @Column(name = "birthdate")
    private LocalDate birthDate;

    @Setter
    @Getter
    private String breed;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private Color color;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Setter
    @Getter
    @ManyToMany
    @JoinTable(
            name = "pet_friends",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Pet> friends = new ArrayList<>();

    public void addFriend(Pet pet) {
        this.friends.add(pet);
    }
}
