package com.partflow.service;

import com.partflow.model.Part;
import com.partflow.repository.PartRepository;
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
public class PartServiceTest {

    @InjectMocks
    private PartService partService;

    @Mock
    private PartRepository partRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllParts() {
        Part part1 = new Part();
        part1.setId(1L);
        part1.setPartName("Test Part 1");

        Part part2 = new Part();
        part2.setId(2L);
        part2.setPartName("Test Part 2");

        List<Part> parts = Arrays.asList(part1, part2);

        when(partRepository.findAll()).thenReturn(parts);

        List<Part> result = partService.getAllParts();

        assertEquals(2, result.size());
        assertEquals("Test Part 1", result.get(0).getPartName());
    }

    @Test
    public void testGetPartById() {
        Part part = new Part();
        part.setId(1L);
        part.setPartName("Test Part");

        when(partRepository.findById(1L)).thenReturn(Optional.of(part));

        Optional<Part> result = partService.getPartById(1L);

        assertEquals(true, result.isPresent());
        assertEquals("Test Part", result.get().getPartName());
    }

    @Test
    public void testSavePart() {
        Part part = new Part();
        part.setPartName("Test Part");

        when(partRepository.save(part)).thenReturn(part);

        Part result = partService.savePart(part);

        assertEquals("Test Part", result.getPartName());
    }

    @Test
    public void testDeletePart() {
        partService.deletePart(1L);
        verify(partRepository, times(1)).deleteById(1L);
    }
}
