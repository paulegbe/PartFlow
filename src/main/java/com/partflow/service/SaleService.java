package com.partflow.service;

import com.partflow.model.Sale;
import com.partflow.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale saveSale(Sale sale) {
        if (sale.getSaleDate() == null) {
            sale.setSaleDate(LocalDateTime.now());
        }
        return saleRepository.save(sale);
    }

    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    // NEW: Get sales within a specific month and year
    public List<Sale> getSalesByMonth(int month, int year) {
        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);
        return saleRepository.findBySaleDateBetween(startOfMonth, endOfMonth);
    }

    // NEW: Get sales on a specific day
    public List<Sale> getSalesByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return saleRepository.findBySaleDateBetween(startOfDay, endOfDay);
    }

    // NEW: Get recent sales within a number of days
    public List<Sale> getRecentSales(int daysBack) {
        LocalDateTime since = LocalDateTime.now().minusDays(daysBack);
        return saleRepository.findBySaleDateAfter(since);
    }
}
