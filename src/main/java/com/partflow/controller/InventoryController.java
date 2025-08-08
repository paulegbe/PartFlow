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
    @FXML private javafx.scene.control.ComboBox<Vendor> vendorComboBox;
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

    private ObservableList<Part> masterPartList;
    private ObservableList<Part> filteredPartList;
    private Part editingPart = null;

    @Autowired
    private PartService partService;

    @Autowired
    private com.partflow.service.VendorService vendorService;

    @FXML private TextField searchField;

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
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Delete Part");
                        confirm.setHeaderText(null);
                        confirm.setContentText("Are you sure you want to delete '" + selected.getPartName() + "'? This action cannot be undone.");
                        confirm.showAndWait().ifPresent(btn -> {
                            if (btn == ButtonType.OK) {
                                try {
                                    partService.deletePart(selected.getId());
                                    masterPartList.remove(selected);
                                    handleSearch();
                                } catch (Exception ex) {
                                    showAlert("Delete Error", "Cannot delete this part because it has associated sales records. Please delete the sales first.");
                                }
                            }
                        });
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : box);
                }
            });

            // Populate vendors into combo box
            vendorComboBox.setItems(FXCollections.observableArrayList(vendorService.getAllVendors()));

            // Load parts and setup filtering lists
            masterPartList = FXCollections.observableArrayList(partService.getAllParts());
            filteredPartList = FXCollections.observableArrayList(masterPartList);
            partsTable.setItems(filteredPartList);
            
        } catch (Exception e) {
            System.err.println("Error initializing Inventory view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddPart() {
        try {
            if (!validatePartForm()) {
                return;
            }
            if (editingPart == null) {
                // Adding new part
                Part newPart = new Part();
                setPartFields(newPart);
                Part saved = partService.savePart(newPart);
                masterPartList.add(saved);
                handleSearch();
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
        part.setVendor(vendorComboBox.getValue());
    }

    private void populateFormForEdit(Part part) {
        partNameField.setText(part.getPartName());
        partNumberField.setText(part.getPartNumber());
        priceField.setText(String.valueOf(part.getPrice()));
        quantityField.setText(String.valueOf(part.getQuantity()));
        inStockCheckBox.setSelected(part.isInStock());
        vendorComboBox.setValue(part.getVendor());

        editingPart = part;
        addPartButton.setText("Save Changes");

    }

    private void clearForm() {
        partNameField.clear();
        partNumberField.clear();
        priceField.clear();
        quantityField.clear();
        vendorComboBox.getSelectionModel().clearSelection();
        inStockCheckBox.setSelected(false);
        addPartButton.setText("Add Parts");
    }

    @FXML
    public void handleSearch() {
        String searchText = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
        filteredPartList.setAll(masterPartList.filtered(part ->
                part.getPartName().toLowerCase().contains(searchText) ||
                part.getPartNumber().toLowerCase().contains(searchText)
        ));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean validatePartForm() {
        String name = partNameField.getText() == null ? "" : partNameField.getText().trim();
        String number = partNumberField.getText() == null ? "" : partNumberField.getText().trim();
        String priceText = priceField.getText() == null ? "" : priceField.getText().trim();
        String qtyText = quantityField.getText() == null ? "" : quantityField.getText().trim();

        if (name.isEmpty() || number.isEmpty() || priceText.isEmpty() || qtyText.isEmpty()) {
            showAlert("Validation Error", "All fields are required.");
            return false;
        }
        try {
            double price = Double.parseDouble(priceText);
            if (price < 0) {
                showAlert("Validation Error", "Price cannot be negative.");
                return false;
            }
        } catch (NumberFormatException ex) {
            showAlert("Validation Error", "Price must be a valid number.");
            return false;
        }
        try {
            int qty = Integer.parseInt(qtyText);
            if (qty < 0) {
                showAlert("Validation Error", "Quantity cannot be negative.");
                return false;
            }
        } catch (NumberFormatException ex) {
            showAlert("Validation Error", "Quantity must be a whole number.");
            return false;
        }
        // Vendor selection optional; if required, uncomment:
        // if (vendorComboBox.getValue() == null) { showAlert("Validation Error", "Please select a vendor."); return false; }
        return true;
    }
}