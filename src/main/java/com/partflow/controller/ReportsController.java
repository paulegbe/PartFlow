package com.partflow.controller;

import com.partflow.model.Part;
import com.partflow.model.Sale;
import com.partflow.service.PartService;
import com.partflow.service.SaleService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ReportsController {

    @FXML private ChoiceBox<String> reportTypeChoiceBox;
    @FXML private ChoiceBox<String> periodChoiceBox;
    @FXML private Button generateButton;
    @FXML private Button exportButton;
    @FXML private TableView<ReportData> reportsTable;
    @FXML private TableColumn<ReportData, String> reportTypeColumn;
    @FXML private TableColumn<ReportData, String> periodColumn;
    @FXML private TableColumn<ReportData, String> generatedOnColumn;
    @FXML private TextArea reportContentArea;
    @FXML private Label totalSalesLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label totalPartsLabel;
    @FXML private Label lowStockLabel;

    @Autowired
    private SaleService saleService;

    @Autowired
    private PartService partService;

    private ObservableList<ReportData> reportHistory = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupChoiceBoxes();
        setupTable();
        setupButtons();
        loadSummaryData();
    }

    private void setupChoiceBoxes() {
        // Report types
        reportTypeChoiceBox.getItems().addAll(
            "Sales Report",
            "Inventory Report", 
            "Low Stock Report",
            "Revenue Summary"
        );
        reportTypeChoiceBox.setValue("Sales Report");

        // Period options
        periodChoiceBox.getItems().addAll(
            "Today",
            "This Week",
            "This Month",
            "Last Month",
            "This Year",
            "All Time"
        );
        periodChoiceBox.setValue("This Month");
    }

    private void setupTable() {
        reportTypeColumn.setCellValueFactory(new PropertyValueFactory<>("reportType"));
        periodColumn.setCellValueFactory(new PropertyValueFactory<>("period"));
        generatedOnColumn.setCellValueFactory(new PropertyValueFactory<>("generatedOn"));
        reportsTable.setItems(reportHistory);
    }

    private void setupButtons() {
        generateButton.setOnAction(e -> generateReport());
        exportButton.setOnAction(e -> exportReport());
    }

    private void loadSummaryData() {
        // Load summary statistics
        List<Sale> allSales = saleService.getAllSales();
        List<Part> allParts = partService.getAllParts();
        
        double totalRevenue = allSales.stream()
            .mapToDouble(Sale::getTotalPrice)
            .sum();
        
        long lowStockCount = allParts.stream()
            .filter(part -> part.getQuantity() <= part.getRestockThreshold())
            .count();

        totalSalesLabel.setText("Total Sales: " + allSales.size());
        totalRevenueLabel.setText("Total Revenue: $" + String.format("%.2f", totalRevenue));
        totalPartsLabel.setText("Total Parts: " + allParts.size());
        lowStockLabel.setText("Low Stock Items: " + lowStockCount);
    }

    @FXML
    private void generateReport() {
        String reportType = reportTypeChoiceBox.getValue();
        String period = periodChoiceBox.getValue();
        
        if (reportType == null || period == null) {
            showAlert("Error", "Please select both report type and period.");
            return;
        }

        System.out.println("Generating report: " + reportType + " for period: " + period);
        
        String reportContent = generateReportContent(reportType, period);
        System.out.println("Report content length: " + reportContent.length());
        System.out.println("Report content: " + reportContent);
        
        reportContentArea.setText(reportContent);
        
        // Add to report history
        ReportData newReport = new ReportData(reportType, period, 
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        reportHistory.add(newReport);
        
        showAlert("Success", "Report generated successfully!");
    }

    private String generateReportContent(String reportType, String period) {
        StringBuilder content = new StringBuilder();
        content.append("=== ").append(reportType).append(" - ").append(period).append(" ===\n");
        content.append("Generated on: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n\n");

        switch (reportType) {
            case "Sales Report":
                generateSalesReport(content, period);
                break;
            case "Inventory Report":
                generateInventoryReport(content, period);
                break;
            case "Low Stock Report":
                generateLowStockReport(content);
                break;
            case "Revenue Summary":
                generateRevenueSummary(content, period);
                break;
        }

        return content.toString();
    }

    private void generateSalesReport(StringBuilder content, String period) {
        List<Sale> sales = getSalesForPeriod(period);
        
        content.append("SALES REPORT\n");
        content.append("Total Sales: ").append(sales.size()).append("\n");
        
        double totalRevenue = sales.stream()
            .mapToDouble(Sale::getTotalPrice)
            .sum();
        content.append("Total Revenue: $").append(String.format("%.2f", totalRevenue)).append("\n\n");

        // Group by part with null safety
        Map<String, Long> salesByPart = sales.stream()
            .filter(sale -> sale.getPart() != null)
            .collect(Collectors.groupingBy(
                sale -> sale.getPart().getPartName(),
                Collectors.counting()
            ));

        content.append("Sales by Part:\n");
        if (salesByPart.isEmpty()) {
            content.append("  No sales data available\n");
        } else {
            salesByPart.forEach((partName, count) -> 
                content.append("  ").append(partName).append(": ").append(count).append(" sales\n"));
        }
    }

    private void generateInventoryReport(StringBuilder content, String period) {
        List<Part> parts = partService.getAllParts();
        
        content.append("INVENTORY REPORT\n");
        content.append("Total Parts: ").append(parts.size()).append("\n");
        
        long inStock = parts.stream().filter(Part::isInStock).count();
        content.append("In Stock: ").append(inStock).append("\n");
        content.append("Out of Stock: ").append(parts.size() - inStock).append("\n\n");

        content.append("Parts by Category:\n");
        Map<String, Long> partsByCategory = parts.stream()
            .collect(Collectors.groupingBy(
                part -> part.getPartName().contains(" ") ? 
                    part.getPartName().substring(0, part.getPartName().indexOf(" ")) : "Other",
                Collectors.counting()
            ));

        partsByCategory.forEach((category, count) -> 
            content.append("  ").append(category).append(": ").append(count).append(" parts\n"));
    }

    private void generateLowStockReport(StringBuilder content) {
        List<Part> parts = partService.getAllParts();
        List<Part> lowStockParts = parts.stream()
            .filter(part -> part.getQuantity() <= part.getRestockThreshold())
            .collect(Collectors.toList());

        content.append("LOW STOCK REPORT\n");
        content.append("Low Stock Items: ").append(lowStockParts.size()).append("\n\n");

        content.append("Items Requiring Restock:\n");
        lowStockParts.forEach(part -> 
            content.append("  ").append(part.getPartName())
                .append(" - Current: ").append(part.getQuantity())
                .append(", Threshold: ").append(part.getRestockThreshold())
                .append(", Vendor: ").append(part.getVendor() != null ? part.getVendor().getName() : "No Vendor")
                .append("\n"));
    }

    private void generateRevenueSummary(StringBuilder content, String period) {
        List<Sale> sales = getSalesForPeriod(period);
        
        content.append("REVENUE SUMMARY\n");
        content.append("Period: ").append(period).append("\n");
        
        double totalRevenue = sales.stream()
            .mapToDouble(Sale::getTotalPrice)
            .sum();
        content.append("Total Revenue: $").append(String.format("%.2f", totalRevenue)).append("\n");
        
        if (!sales.isEmpty()) {
            double avgSale = totalRevenue / sales.size();
            content.append("Average Sale: $").append(String.format("%.2f", avgSale)).append("\n");
        }
        
        // Top selling parts with null safety
        Map<String, Double> revenueByPart = sales.stream()
            .filter(sale -> sale.getPart() != null)
            .collect(Collectors.groupingBy(
                sale -> sale.getPart().getPartName(),
                Collectors.summingDouble(Sale::getTotalPrice)
            ));

        content.append("\nTop Revenue Generators:\n");
        if (revenueByPart.isEmpty()) {
            content.append("  No revenue data available\n");
        } else {
            revenueByPart.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> 
                    content.append("  ").append(entry.getKey())
                        .append(": $").append(String.format("%.2f", entry.getValue())).append("\n"));
        }
    }

    private List<Sale> getSalesForPeriod(String period) {
        LocalDate now = LocalDate.now();
        
        switch (period) {
            case "Today":
                return saleService.getSalesByDate(now);
            case "This Week":
                return saleService.getRecentSales(7);
            case "This Month":
                return saleService.getSalesByMonth(now.getMonthValue(), now.getYear());
            case "Last Month":
                LocalDate lastMonth = now.minusMonths(1);
                return saleService.getSalesByMonth(lastMonth.getMonthValue(), lastMonth.getYear());
            case "This Year":
                return saleService.getRecentSales(365);
            case "All Time":
            default:
                return saleService.getAllSales();
        }
    }

    @FXML
    private void exportReport() {
        String content = reportContentArea.getText();
        if (content.isEmpty()) {
            showAlert("Error", "No report content to export. Please generate a report first.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Report");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialFileName("partflow_report_" + 
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".txt");

        Stage stage = (Stage) exportButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
                showAlert("Success", "Report exported successfully to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Failed to export report: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Inner class for report history
    public static class ReportData {
        private final SimpleStringProperty reportType;
        private final SimpleStringProperty period;
        private final SimpleStringProperty generatedOn;

        public ReportData(String reportType, String period, String generatedOn) {
            this.reportType = new SimpleStringProperty(reportType);
            this.period = new SimpleStringProperty(period);
            this.generatedOn = new SimpleStringProperty(generatedOn);
        }

        public String getReportType() { return reportType.get(); }
        public String getPeriod() { return period.get(); }
        public String getGeneratedOn() { return generatedOn.get(); }
    }
}
