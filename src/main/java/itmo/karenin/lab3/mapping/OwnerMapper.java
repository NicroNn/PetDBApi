package itmo.karenin.lab3.mapping;

import itmo.karenin.lab3.dto.OwnerDTO;
import itmo.karenin.lab3.entity.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    @Mapping(target = "pets", ignore = true)
    OwnerDTO toDto(Owner owner);

    @Mapping(target = "pets", ignore = true)
    Owner toEntity(OwnerDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pets", ignore = true)
    void updateEntity(Owner source, @MappingTarget Owner target);
}