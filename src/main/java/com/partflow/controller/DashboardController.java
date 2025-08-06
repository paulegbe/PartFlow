package com.partflow.controller;

import com.partflow.model.Part;
import com.partflow.model.Sale;
import com.partflow.service.PartService;
import com.partflow.service.SaleService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DashboardController {

    @FXML private Label totalPartsLabel;
    @FXML private Label lowStockLabel;
    @FXML private Label salesTodayLabel;
    @FXML private Label outOfStockLabel;

    @FXML private TableView<Part> lowStockTable;
    @FXML private TableColumn<Part, String> lowStockPartColumn;
    @FXML private TableColumn<Part, Integer> lowStockQtyColumn;

    @FXML private TableView<Sale> salesReportTable;
    @FXML private TableColumn<Sale, String> saleDateColumn;
    @FXML private TableColumn<Sale, String> salePartColumn;
    @FXML private TableColumn<Sale, String> saleQuantityColumn;
    @FXML private TableColumn<Sale, String> saleAmountColumn;

    @Autowired private PartService partService;
    @Autowired private SaleService saleService;

    @FXML
    public void initialize() {
        updateSummaryMetrics();
        loadLowStockParts();
        loadSalesReport();
    }

    private void updateSummaryMetrics() {
        List<Part> allParts = partService.getAllParts();

        long lowStockCount = allParts.stream().filter(p -> p.getQuantity() < p.getRestockThreshold()).count();
        long outOfStockCount = allParts.stream().filter(p -> p.getQuantity() == 0).count();

        int salesToday = saleService.getSalesByDate(LocalDate.now()).stream()
                .mapToInt(sale -> (int) sale.getTotalPrice()).sum();

        totalPartsLabel.setText(String.valueOf(allParts.size()));
        lowStockLabel.setText(String.valueOf(lowStockCount));
        outOfStockLabel.setText(String.valueOf(outOfStockCount));
        salesTodayLabel.setText("$" + salesToday);
    }

    private void loadLowStockParts() {
        List<Part> lowStock = partService.getAllParts().stream()
                .filter(p -> p.getQuantity() < p.getRestockThreshold())
                .toList();

        ObservableList<Part> partData = FXCollections.observableArrayList(lowStock);

        lowStockPartColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPartName()));
        lowStockQtyColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());

        lowStockTable.setItems(partData);
    }

    private void loadSalesReport() {
        List<Sale> recentSales = saleService.getRecentSales(7); // e.g., last 7 days

        ObservableList<Sale> salesData = FXCollections.observableArrayList(recentSales);

        saleDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSaleDate().toLocalDate().toString()));
        salePartColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPart().getPartName()));
        saleQuantityColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getQuantity())));
        saleAmountColumn.setCellValueFactory(data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getTotalPrice())));

        salesReportTable.setItems(salesData);
    }
}