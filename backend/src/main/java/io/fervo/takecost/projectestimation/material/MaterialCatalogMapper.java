package io.fervo.takecost.projectestimation.material;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaterialCatalogMapper {
    MaterialCatalogDTO toDTO(MaterialCatalog materialCatalog);

    MaterialCatalog toEntity(MaterialCatalogDTO materialCatalogDTO);
}
