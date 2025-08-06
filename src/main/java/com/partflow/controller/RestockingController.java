package com.partflow.controller;

import com.partflow.model.Part;
import com.partflow.service.PartService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestockingController {

    @Autowired
    private PartService partService;

    @FXML private TableView<Part> restockTable;
    @FXML private TableColumn<Part, String> partColumn;
    @FXML private TableColumn<Part, Integer> quantityColumn;
    @FXML private TableColumn<Part, String> supplierColumn;
    @FXML private TableColumn<Part, Boolean> selectColumn;
    @FXML private TextField searchField;

    private ObservableList<Part> partList;

    @FXML
    public void initialize() {
        restockTable.setEditable(true);

        partColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPartName()));
        quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        supplierColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVendor().getName()));

        selectColumn.setCellValueFactory(data -> {
            Part part = data.getValue();
            SimpleBooleanProperty selectedProp = new SimpleBooleanProperty(part.isSelected());
            selectedProp.addListener((obs, oldVal, newVal) -> part.setSelected(newVal));
            return selectedProp;
        });
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setEditable(true);

        partList = FXCollections.observableArrayList(partService.getAllParts());
        restockTable.setItems(partList);
    }

    @FXML
    public void handleReorderSelected() {
        boolean reordered = false;

        for (Part part : partList) {
            if (part.isSelected()) {
                part.setQuantity(part.getQuantity() + part.getRestockAmount());
                partService.savePart(part);
                reordered = true;
            }
        }

        if (!reordered) {
            showAlert("No parts were selected for restocking.");
        } else {
            restockTable.refresh();
        }
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            partList.setAll(partService.getAllParts());
        } else {
            ObservableList<Part> filtered = FXCollections.observableArrayList();
            for (Part part : partService.getAllParts()) {
                if (part.getPartName().toLowerCase().contains(query) || part.getPartNumber().toLowerCase().contains(query)) {
                    filtered.add(part);
                }
            }
            partList.setAll(filtered);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Restocking Notice");
        alert.setContentText(message);
        alert.showAndWait();
    }
}