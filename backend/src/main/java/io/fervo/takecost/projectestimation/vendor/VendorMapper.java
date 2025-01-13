package io.fervo.takecost.projectestimation.vendor;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    VendorMapper INSTANCE = Mappers.getMapper(VendorMapper.class);

    VendorDTO toDTO(Vendor vendor);

    Vendor toEntity(VendorDTO vendorDTO);
}
