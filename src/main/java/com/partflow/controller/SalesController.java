package com.partflow.controller;

import com.partflow.model.Part;
import com.partflow.model.Sale;
import com.partflow.repository.PartRepository;
import com.partflow.repository.SaleRepository;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class SalesController {

    @FXML private ComboBox<Part> partComboBox;
    @FXML private TextField quantityField;
    @FXML private TableView<Sale> salesTable;
    @FXML private TableColumn<Sale, String> colPart;
    @FXML private TableColumn<Sale, Integer> colQuantity;
    @FXML private TableColumn<Sale, Double> colPrice;
    @FXML private TableColumn<Sale, Double> colTotal;
    @FXML private TableColumn<Sale, LocalDateTime> colDate;

    @Autowired private com.partflow.service.PartService partService;
    @Autowired private com.partflow.service.SaleService saleService;

    private ObservableList<Sale> saleData;

    @FXML
    public void initialize() {
        // Load parts
        partComboBox.setItems(FXCollections.observableArrayList(partService.getAllParts()));
        partComboBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Part item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getPartName());
            }
        });
        partComboBox.setButtonCell(partComboBox.getCellFactory().call(null));

        // Setup columns
        colPart.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPart().getPartName()));
        colQuantity.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        colPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getUnitPrice()).asObject());
        colTotal.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotalPrice()).asObject());
        colDate.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getSaleDate()));

        // Load existing sales
                        saleData = FXCollections.observableArrayList(saleService.getAllSales());
        salesTable.setItems(saleData);
    }

    @FXML
    public void handleRecordSale() {
        Part selectedPart = partComboBox.getValue();
        if (selectedPart == null || quantityField.getText().isBlank()) {
            showAlert("Please select a part and enter quantity.");
            return;
        }

        int qty;
        try {
            qty = Integer.parseInt(quantityField.getText());
        } catch (NumberFormatException e) {
            showAlert("Quantity must be a number.");
            return;
        }

        if (qty <= 0) {
            showAlert("Quantity must be greater than 0.");
            return;
        }

        if (selectedPart.getQuantity() < qty) {
            showAlert("Not enough inventory.");
            return;
        }

        // Update inventory
        selectedPart.setQuantity(selectedPart.getQuantity() - qty);
                partService.savePart(selectedPart);

        // Create sale
        Sale sale = new Sale();
        sale.setPart(selectedPart);
        sale.setQuantity(qty);
        sale.setUnitPrice(selectedPart.getPrice());
        sale.setTotalPrice(qty * selectedPart.getPrice());
        sale.setSaleDate(LocalDateTime.now());

        saleService.saveSale(sale);
        saleData.add(sale);

        quantityField.clear();
        partComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Sale Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}