package io.fervo.takecost.projectestimation.material;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MaterialCatalogMapper {
    MaterialCatalogMapper INSTANCE = Mappers.getMapper(MaterialCatalogMapper.class);

    //    @Mapping(target = "id", source = "id")
//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "category", source = "category")
//    @Mapping(target = "unitType", source = "unitType")
//    @Mapping(target = "unitPrice", source = "unitPrice")
//    @Mapping(target = "vendorId", source = "vendor.id")
//    @Mapping(target = "description", source = "description")
//    @Mapping(target = "subCategory", source = "subCategory")
//    @Mapping(target = "inStock", source = "inStock")
//    @Mapping(target = "leadTimeDays", source = "leadTimeDays")
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
    MaterialCatalogDTO toDTO(MaterialCatalog materialCatalog);

    //    @Mapping(target = "id", source = "id")
//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "category", source = "category")
//    @Mapping(target = "unitType", source = "unitType")
//    @Mapping(target = "unitPrice", source = "unitPrice")
//    @Mapping(target = "vendor.id", source = "vendorId")
//    @Mapping(target = "description", source = "description")
//    @Mapping(target = "subCategory", source = "subCategory")
//    @Mapping(target = "inStock", source = "inStock")
//    @Mapping(target = "leadTimeDays", source = "leadTimeDays")
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
    MaterialCatalog toEntity(MaterialCatalogDTO materialCatalogDTO);
}
