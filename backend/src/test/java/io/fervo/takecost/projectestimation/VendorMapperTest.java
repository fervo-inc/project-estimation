package io.fervo.takecost.projectestimation;

import io.fervo.takecost.projectestimation.vendor.Vendor;
import io.fervo.takecost.projectestimation.vendor.VendorDTO;
import io.fervo.takecost.projectestimation.vendor.VendorMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VendorMapperTest {
    private final VendorMapper mapper = Mappers.getMapper(VendorMapper.class);

    @Test
    public void testToDTO() {
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setName("Vendor A");
        vendor.setEmail("vendorA@example.com");
        vendor.setPhone("123-456-7890");
        vendor.setAddress("123 Main St");

        VendorDTO dto = mapper.toDTO(vendor);

        assertNotNull(dto);
        assertEquals(vendor.getId(), dto.id());
        assertEquals(vendor.getName(), dto.name());
        assertEquals(vendor.getEmail(), dto.email());
        assertEquals(vendor.getPhone(), dto.phone());
        assertEquals(vendor.getAddress(), dto.address());
    }
}
