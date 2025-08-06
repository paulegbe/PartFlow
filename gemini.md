# PartFlow Project

## Project Description

PartFlow is a desktop application for managing inventory and sales. It is built with Java, Spring Boot, and JavaFX. The application provides a user-friendly interface for tracking parts, managing vendors, recording sales, and generating reports.

## Key Technologies

*   **Backend:** Spring Boot
*   **Frontend (GUI):** JavaFX
*   **Database:** H2 (file-based)
*   **Data Access:** Spring Data JPA
*   **Build Tool:** Apache Maven
*   **Language:** Java

## Commands

*   **Install Dependencies:** `mvnw.cmd install`
*   **Run the Application:** `mvnw.cmd javafx:run`
*   **Run Tests:** `mvnw.cmd test`

## Development Plan & Progress

**Overall Goal:** Enhance the PartFlow application with improved functionality, a modern UI/UX, and robust features for efficient inventory and sales management.

### Phased Approach:

**Phase 1: UI/UX Overhaul (IN PROGRESS)**
*   **Objective:** Implement a consistent, modern, and intuitive user interface across the entire application.
*   **Tasks:**
    *   **Global Styles (`style.css`):**
        *   Refine color palette, typography, and spacing for a cohesive look.
        *   Implement consistent styling for all common UI elements (buttons, text fields, labels, tables, etc.).
        *   Ensure responsiveness across different window sizes.
    *   **Login Screen (`Login.fxml`):**
        *   Apply new global styles.
        *   Improve layout and visual appeal.
    *   **Sidebar (`Sidebar.fxml`):**
        *   Apply new global styles.
        *   Ensure consistent navigation button styling and active states.
    *   **Dashboard (`Dashboard.fxml`):**
        *   Apply new global styles to cards, labels, and tables.
        *   Enhance visual presentation of key metrics.
    *   **Inventory Management (`Inventory.fxml`):**
        *   Apply new global styles to input forms, buttons, and tables.
        *   Improve layout for better usability.
    *   **Sales Management (`Sales.fxml`):
        *   Apply new global styles to input forms, buttons, and tables.
        *   Optimize layout for efficient sale recording.
    *   **Restocking Management (`Restocking.fxml`):**
        *   Apply new global styles to search bars, tables, and buttons.
        *   Improve clarity of restocking information.
    *   **Vendor Management (`Vendors.fxml`):**
        *   Apply new global styles to input forms, tables, and buttons.
        *   Streamline vendor information display.
    *   **Reports (`Reports.fxml`):**
        *   Apply new global styles to report generation controls and display area.
        *   Improve readability of generated reports.

**Phase 2: Core Functionality Enhancements (TODO)**
*   **Objective:** Implement key features to enhance the core inventory and sales management capabilities.
*   **Tasks:**
    *   **Part Management:**
        *   Implement functionality to edit and delete parts.
        *   Add validation for part input fields (e.g., price must be numeric, quantity non-negative).
        *   Implement search and filter capabilities for parts.
    *   **Vendor Management:**
        *   Implement functionality to edit and delete vendors.
        *   Add validation for vendor input fields.
        *   Implement search and filter capabilities for vendors.
    *   **Sales Management:**
        *   Implement functionality to view sales history in detail.
        *   Add ability to edit or void sales (with appropriate permissions/logging).
        *   Implement sales reporting with customizable date ranges.
    *   **Restocking:**
        *   Implement a proper restock order creation and tracking system.
        *   Generate purchase orders for selected restock items.
        *   Update inventory automatically upon receipt of restock orders.
    *   **User Management:**
        *   Implement user creation, editing, and deletion (for admin users).
        *   Implement role-based access control (e.g., admin, sales, inventory).

**Phase 3: Advanced Features & Polish (TODO)**
*   **Objective:** Introduce advanced features and polish the application for a production-ready state.
*   **Tasks:**
    *   **Notifications & Alerts:**
        *   Implement real-time notifications for low stock, new sales, etc.
        *   Configurable alert thresholds.
    *   **Data Visualization:**
        *   Integrate charts and graphs on the dashboard for sales trends, inventory levels, etc.
    *   **Export/Import:**
        *   Allow exporting data to common formats (CSV, PDF).
        *   Allow importing data from CSV for initial setup.
    *   **Settings & Configuration:**
        *   Implement a settings screen for application-wide configurations (e.g., currency, tax rates).
    *   **Error Handling & Logging:**
        *   Improve user-friendly error messages.
        *   Implement comprehensive logging for debugging and auditing.
    *   **Deployment:**
        *   Package the application for easy deployment (e.g., native installer).