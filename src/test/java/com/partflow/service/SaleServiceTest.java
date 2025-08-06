package com.partflow.service;

import com.partflow.model.Sale;
import com.partflow.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @InjectMocks
    private SaleService saleService;

    @Mock
    private SaleRepository saleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllSales() {
        Sale sale1 = new Sale();
        sale1.setId(1L);
        Sale sale2 = new Sale();
        sale2.setId(2L);
        List<Sale> sales = Arrays.asList(sale1, sale2);

        when(saleRepository.findAll()).thenReturn(sales);

        List<Sale> result = saleService.getAllSales();

        assertEquals(2, result.size());
    }

    @Test
    public void testGetSaleById() {
        Sale sale = new Sale();
        sale.setId(1L);

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        Optional<Sale> result = saleService.getSaleById(1L);

        assertEquals(true, result.isPresent());
    }

    @Test
    public void testSaveSale() {
        Sale sale = new Sale();
        when(saleRepository.save(sale)).thenReturn(sale);

        Sale result = saleService.saveSale(sale);

        assertEquals(sale, result);
    }

    @Test
    public void testDeleteSale() {
        saleService.deleteSale(1L);
        verify(saleRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetSalesByDate() {
        LocalDate date = LocalDate.of(2025, 1, 1);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        Sale sale1 = new Sale();
        sale1.setSaleDate(startOfDay.plusHours(1));
        Sale sale2 = new Sale();
        sale2.setSaleDate(startOfDay.plusHours(2));
        List<Sale> sales = Arrays.asList(sale1, sale2);

        when(saleRepository.findBySaleDateBetween(startOfDay, endOfDay)).thenReturn(sales);

        List<Sale> result = saleService.getSalesByDate(date);

        assertEquals(2, result.size());
    }

    @Test
    public void testGetRecentSales() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        Sale sale1 = new Sale();
        sale1.setSaleDate(LocalDateTime.now().minusDays(1));
        Sale sale2 = new Sale();
        sale2.setSaleDate(LocalDateTime.now().minusDays(2));
        List<Sale> sales = Arrays.asList(sale1, sale2);

        when(saleRepository.findBySaleDateAfter(sevenDaysAgo)).thenReturn(sales);

        List<Sale> result = saleService.getRecentSales(7);

        assertEquals(2, result.size());
    }

    @Test
    public void testGetSalesByMonth() {
        int month = 1;
        int year = 2025;
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

        Sale sale1 = new Sale();
        sale1.setSaleDate(startOfMonth.plusDays(5));
        Sale sale2 = new Sale();
        sale2.setSaleDate(startOfMonth.plusDays(10));
        List<Sale> sales = Arrays.asList(sale1, sale2);

        when(saleRepository.findBySaleDateBetween(startOfMonth, endOfMonth)).thenReturn(sales);

        List<Sale> result = saleService.getSalesByMonth(month, year);

        assertEquals(2, result.size());
    }
}
