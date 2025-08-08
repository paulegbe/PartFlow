// VendorsController.java
package com.partflow.controller;

import com.partflow.model.Vendor;
import com.partflow.repository.VendorRepository;
import com.partflow.service.VendorService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VendorsController {

    @FXML private TextField vendorSearchField;
    @FXML private TableView<Vendor> vendorTable;
    @FXML private TableColumn<Vendor, String> nameColumn;
    @FXML private TableColumn<Vendor, String> contactColumn;
    @FXML private TableColumn<Vendor, Void> actionColumn;
    @FXML private Button addVendorButton;

    private ObservableList<Vendor> vendorList;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorService vendorService;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        contactColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContactPerson()));

        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox box = new HBox(10, editBtn, deleteBtn);

            {
                editBtn.setOnAction(e -> editVendor(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(e -> {
                    Vendor selected = getTableView().getItems().get(getIndex());
                    vendorRepository.deleteById(selected.getId());
                    vendorList.remove(selected);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });

        vendorList = FXCollections.observableArrayList(vendorRepository.findAll());
        vendorTable.setItems(vendorList);
    }

    @FXML
    public void handleSearch() {
        String query = vendorSearchField.getText().trim();
        if (query.isEmpty()) {
            vendorList.setAll(vendorRepository.findAll());
        } else {
            vendorList.clear();
            // Name contains (case-insensitive)
            vendorList.addAll(vendorRepository.findByNameContainingIgnoreCase(query));
            // Also consider exact match on contact person if not already present
            Vendor matchByContact = vendorRepository.findByContactPerson(query);
            if (matchByContact != null && vendorList.stream().noneMatch(v -> v.getId().equals(matchByContact.getId()))) {
                vendorList.add(matchByContact);
            }
        }
    }

    @FXML
    public void handleAddVendor() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter vendor details (comma separated):");
        dialog.setContentText("Format: Name, Contact Person, Phone, Email");

        dialog.showAndWait().ifPresent(input -> {
            String[] parts = input.split(",");
            if (parts.length == 4) {
                String name = parts[0].trim();
                String contactPerson = parts[1].trim();
                String phone = parts[2].trim();
                String email = parts[3].trim();

                if (name.isEmpty() || contactPerson.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    showAlert("Validation Error", "All fields must be filled.");
                    return;
                }
                // Basic email validation
                if (!email.matches(".+@.+\\..+")) {
                    showAlert("Validation Error", "Invalid email format.");
                    return;
                }
                // Basic phone validation (allows +, digits, spaces, dashes, parentheses)
                if (!phone.matches("^[+\\d][\\d\\s\\-()]{6,}$")) {
                    showAlert("Validation Error", "Invalid phone format.");
                    return;
                }

                Vendor vendor = new Vendor();
                vendor.setName(name);
                vendor.setContactPerson(contactPerson);
                vendor.setPhone(phone);
                vendor.setEmail(email);
                Vendor saved = vendorService.saveVendor(vendor);
                vendorList.add(saved);
            } else {
                showAlert("Invalid input format. Please use: Name, Contact Person, Phone, Email");
            }
        });
    }

    private void editVendor(Vendor vendor) {
        TextInputDialog dialog = new TextInputDialog(
                vendor.getName() + ", " + vendor.getContactPerson() + ", " + vendor.getPhone() + ", " + vendor.getEmail());
        dialog.setHeaderText("Update vendor details (comma separated):");

        dialog.showAndWait().ifPresent(input -> {
            String[] parts = input.split(",");
            if (parts.length == 4) {
                vendor.setName(parts[0].trim());
                vendor.setContactPerson(parts[1].trim());
                vendor.setPhone(parts[2].trim());
                vendor.setEmail(parts[3].trim());
                vendorRepository.save(vendor);
                vendorTable.refresh();
            } else {
                showAlert("Invalid input format.");
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
