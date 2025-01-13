package io.fervo.takecost.projectestimation.vendor;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VendorMapper {
    VendorDTO toDTO(Vendor vendor);

    Vendor toEntity(VendorDTO vendorDTO);
}
