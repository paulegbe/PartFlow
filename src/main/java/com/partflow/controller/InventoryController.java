package com.partflow.controller;

import com.partflow.model.Part;
import com.partflow.model.Vendor;
import com.partflow.service.PartService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.*;
import javafx.util.Callback;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;


@Component
public class InventoryController {

    @FXML private TextField partNameField;
    @FXML private TextField partNumberField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private TextField vendorField;
    @FXML private CheckBox inStockCheckBox;
    @FXML private TableView<Part> partsTable;
    @FXML private Button addPartButton;


    @FXML private TableColumn<Part, String> nameColumn;
    @FXML private TableColumn<Part, Double> priceColumn;
    @FXML private TableColumn<Part, String> numberColumn;
    @FXML private TableColumn<Part, Integer> quantityColumn;
    @FXML private TableColumn<Part, Boolean> statusColumn;
    @FXML private TableColumn<Part, String> vendorColumn;
    @FXML private TableColumn<Part, Void> actionColumn;

    private ObservableList<Part> partList;
    private Part editingPart = null;

    @Autowired
    private PartService partService;

    @Autowired
    private com.partflow.service.VendorService vendorService;

    @FXML
    public void initialize() {
        try {
            nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPartName()));
            priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
            numberColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPartNumber()));
            quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
            statusColumn.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isInStock()).asObject());
            vendorColumn.setCellValueFactory(data -> {
                Vendor vendor = data.getValue().getVendor();
                return new SimpleStringProperty(vendor != null ? vendor.getName() : "No Vendor");
            });

            actionColumn.setCellFactory(col -> new TableCell<Part, Void>() {
                private final Button editBtn = new Button("Edit");
                private final Button deleteBtn = new Button("Delete");
                private final HBox box = new HBox(10, editBtn, deleteBtn);

                {
                    editBtn.setOnAction(e -> {
                        Part selected = getTableView().getItems().get(getIndex());
                        populateFormForEdit(selected);
                    });

                    deleteBtn.setOnAction(e -> {
                        Part selected = getTableView().getItems().get(getIndex());
                        try {
                            partService.deletePart(selected.getId());
                            partList.remove(selected);
                        } catch (Exception ex) {
                            showAlert("Delete Error", "Cannot delete this part because it has associated sales records. Please delete the sales first.");
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : box);
                }
            });

            partList = FXCollections.observableArrayList(partService.getAllParts());
            partsTable.setItems(partList);
            
        } catch (Exception e) {
            System.err.println("Error initializing Inventory view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddPart() {
        try {
            if (editingPart == null) {
                // Adding new part
                Part newPart = new Part();
                setPartFields(newPart);
                Part saved = partService.savePart(newPart);
                partList.add(saved);
            } else {
                // Editing existing part
                setPartFields(editingPart);
                partService.savePart(editingPart);
                partsTable.refresh();  // force update
                editingPart = null;
                addPartButton.setText("Add Parts");
            }

            clearForm();
        } catch (Exception e) {
            showAlert("Error", "Invalid input. Please check your fields.");
        }
    }

    private void setPartFields(Part part) {
        part.setPartName(partNameField.getText());
        part.setPartNumber(partNumberField.getText());
        part.setPrice(Double.parseDouble(priceField.getText()));
        part.setQuantity(Integer.parseInt(quantityField.getText()));
        part.setInStock(inStockCheckBox.isSelected());
        com.partflow.model.Vendor vendor = vendorService.findByName(vendorField.getText());
        part.setVendor(vendor);
    }

    private void populateFormForEdit(Part part) {
        partNameField.setText(part.getPartName());
        partNumberField.setText(part.getPartNumber());
        priceField.setText(String.valueOf(part.getPrice()));
        quantityField.setText(String.valueOf(part.getQuantity()));
        inStockCheckBox.setSelected(part.isInStock());
        vendorField.setText(part.getVendor() != null ? part.getVendor().getName() : "");

        editingPart = part;
        addPartButton.setText("Save Changes");

    }

    private void clearForm() {
        partNameField.clear();
        partNumberField.clear();
        priceField.clear();
        quantityField.clear();
        vendorField.clear();
        inStockCheckBox.setSelected(false);
        addPartButton.setText("Add Parts");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}