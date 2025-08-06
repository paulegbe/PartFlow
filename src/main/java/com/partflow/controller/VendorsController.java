// VendorsController.java
package com.partflow.controller;

import com.partflow.model.Vendor;
import com.partflow.repository.VendorRepository;
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
            Vendor matchByName = vendorRepository.findByName(query);
            Vendor matchByContact = vendorRepository.findByContactPerson(query);
            vendorList.clear();
            if (matchByName != null) vendorList.add(matchByName);
            else if (matchByContact != null) vendorList.add(matchByContact);
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
                Vendor vendor = new Vendor();
                vendor.setName(parts[0].trim());
                vendor.setContactPerson(parts[1].trim());
                vendor.setPhone(parts[2].trim());
                vendor.setEmail(parts[3].trim());
                Vendor saved = vendorRepository.save(vendor);
                vendorList.add(saved);
            } else {
                showAlert("Invalid input format.");
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
}
