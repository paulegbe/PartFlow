package com.partflow.service;

import com.partflow.model.Vendor;
import com.partflow.repository.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendorServiceTest {

    @InjectMocks
    private VendorService vendorService;

    @Mock
    private VendorRepository vendorRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllVendors() {
        Vendor vendor1 = new Vendor();
        vendor1.setId(1L);
        vendor1.setName("Test Vendor 1");

        Vendor vendor2 = new Vendor();
        vendor2.setId(2L);
        vendor2.setName("Test Vendor 2");

        List<Vendor> vendors = Arrays.asList(vendor1, vendor2);

        when(vendorRepository.findAll()).thenReturn(vendors);

        List<Vendor> result = vendorService.getAllVendors();

        assertEquals(2, result.size());
        assertEquals("Test Vendor 1", result.get(0).getName());
    }

    @Test
    public void testGetVendorById() {
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setName("Test Vendor");

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(vendor));

        Optional<Vendor> result = vendorService.getVendorById(1L);

        assertEquals(true, result.isPresent());
        assertEquals("Test Vendor", result.get().getName());
    }

    @Test
    public void testSaveVendor() {
        Vendor vendor = new Vendor();
        vendor.setName("Test Vendor");

        when(vendorRepository.save(vendor)).thenReturn(vendor);

        Vendor result = vendorService.saveVendor(vendor);

        assertEquals("Test Vendor", result.getName());
    }

    @Test
    public void testDeleteVendor() {
        vendorService.deleteVendor(1L);
        verify(vendorRepository, times(1)).deleteById(1L);
    }
}
