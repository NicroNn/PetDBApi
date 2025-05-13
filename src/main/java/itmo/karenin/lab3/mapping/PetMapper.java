package itmo.karenin.lab3.mapping;

import itmo.karenin.lab3.dto.PetDTO;
import itmo.karenin.lab3.dto.PetResponseDTO;
import itmo.karenin.lab3.entity.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OwnerMapper.class})
public interface PetMapper {

    @Mapping(source = "owner.id", target = "ownerId", ignore = true)
    PetDTO toDto(Pet pet);

    @Mapping(source = "friends", target = "friendCount", qualifiedByName = "countFriends", ignore = true)
    @Mapping(source = "owner.id", target = "ownerId", ignore = true)
    PetResponseDTO toResponseDto(Pet pet);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "friends", ignore = true)
    Pet toEntity(PetDTO dto);

    @Named("countFriends")
    default int countFriends(List<Pet> friends) {
        return friends != null ? friends.size() : 0;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "friends", ignore = true)
    void updateEntity(Pet source, @MappingTarget Pet target);

    List<PetDTO> toDtoList(List<Pet> pets);
    List<PetResponseDTO> toResponseDtoList(List<Pet> pets);
}
