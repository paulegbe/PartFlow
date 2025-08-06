# PartFlow Project Plan

## Project Overview
PartFlow is a desktop application designed to streamline and automate operations for auto parts retail businesses. The system enables shop managers to manage inventory, vendors, sales, restocking alerts, and performance analytics, eliminating manual processes while improving visibility and decision-making.

**Primary Users:** Shop Manager, Supplier (no public-facing component)

**Tech Stack:** Java, JavaFX, Spring Boot, FXML, CSS, SceneBuilder, MVC Architecture

---

## Core Functionalities

1. **Authentication & Navigation**
   - Secure login system (LoginController)
   - Post-login routing to dashboard (MainController)
   - Navigation panel for different views: Dashboard, Inventory, Vendors, Restocking, Sales Reports, Settings

2. **Dashboard Module**
   - **File:** Dashboard.fxml | DashboardController.java
   - **Features:**
     - Displays 6 key KPIs:
       - Total parts
       - Low stock alerts
       - Sales today
       - Sales this month
       - Total sales (all time)
       - Out of stock count
     - Shows two tables:
       - Low Stock Table: Lists parts under restock threshold
       - Sales Report Table: Displays recent sales (last 7 days)

3. **Inventory Management**
   - **Controller:** InventoryController.java
   - **Features:**
     - Add, edit, delete parts
     - Search parts by name or category
     - View all inventory records
     - Displays individual part details, including:
       - ID, Name, Quantity, Category, Price, Restock threshold
     - **Fixed Issues:**
       - ✅ No more duplicate entries when adding parts
       - ✅ Proper error handling for delete operations (foreign key constraints)
       - ✅ Null vendor safety

4. **Vendor Management**
   - **Controller:** VendorController.java
   - **Features:**
     - Add and manage vendor information
     - Track parts supplied by each vendor
     - Contact information and supply frequency data stored

5. **Sales Tracking**
   - **Controller:** SalesController.java
   - **Features:**
     - Record new sales
     - Associate parts sold with their quantities
     - Calculate total sale price
     - Automatically reduce inventory stock levels

6. **Restocking Module** ⚠️ **NEEDS ENHANCEMENT**
   - **Controller:** RestockingController.java
   - **Current Features:**
     - Basic table showing parts below restock threshold
     - Checkbox selection for parts
     - Simple "Reorder Selected" button that adds restock amount
     - Search functionality
   - **Issues:**
     - No restock order tracking
     - No vendor communication
     - No restock history
     - No order status management
     - No cost tracking
     - No delivery date management

7. **Reports Module** ✅ **COMPLETED**
   - **Controller:** ReportsController.java
   - **Features:**
     - 4 Report Types: Sales Report, Inventory Report, Low Stock Report, Revenue Summary
     - 6 Time Periods: Today, This Week, This Month, Last Month, This Year, All Time
     - Real-time summary statistics
     - Report history table
     - Export functionality to .txt files
     - Null safety for all data operations

---

## System Architecture
- **Frontend:** JavaFX with FXML for UI, styled via CSS
- **Backend:** Spring Boot used to manage business logic and services
- **Pattern Used:** MVC (Model-View-Controller)
- **Data Models:**
  - Part
  - Vendor
  - Sale
  - User

---

## Use Cases
- **UC1:** Login - Shop Manager enters credentials, system validates and grants access
- **UC2:** Inventory Management - Add, edit, delete parts with vendor tracking
- **UC3:** Sales Recording - Record sales transactions with automatic inventory updates
- **UC4:** Dashboard Overview - View KPIs and recent activity
- **UC5:** Report Generation - Generate and export business reports
- **UC6:** Restocking Management - **NEEDS IMPLEMENTATION**

---

## Current Progress
- ✅ **Authentication & Navigation** - Complete
- ✅ **Dashboard** - Complete with all KPIs working
- ✅ **Inventory Management** - Complete with all fixes applied
- ✅ **Vendor Management** - Complete
- ✅ **Sales Tracking** - Complete
- ⚠️ **Restocking Module** - Basic functionality, needs enhancement
- ✅ **Reports Module** - Complete with full functionality

---

## Restocking Enhancement Plan

### **Phase 1: Data Model Enhancements**
1. **Create RestockOrder Entity**
   ```java
   @Entity
   public class RestockOrder {
       @Id @GeneratedValue
       private Long id;
       private LocalDateTime orderDate;
       private LocalDateTime expectedDelivery;
       private String status; // PENDING, ORDERED, DELIVERED, CANCELLED
       private double totalCost;
       private String notes;
       
       @ManyToOne
       private Vendor vendor;
       
       @OneToMany(mappedBy = "restockOrder", cascade = CascadeType.ALL)
       private List<RestockOrderItem> items;
   }
   ```

2. **Create RestockOrderItem Entity**
   ```java
   @Entity
   public class RestockOrderItem {
       @Id @GeneratedValue
       private Long id;
       private int quantity;
       private double unitCost;
       
       @ManyToOne
       private RestockOrder restockOrder;
       
       @ManyToOne
       private Part part;
   }
   ```

### **Phase 2: Enhanced Restocking UI**
1. **Restocking Dashboard**
   - Summary cards: Pending Orders, Total Cost, Items to Restock
   - Quick actions: Create Order, View History, Contact Vendors

2. **Order Creation Interface**
   - Multi-select parts with quantity inputs
   - Vendor selection per part
   - Cost calculation
   - Expected delivery date
   - Order notes

3. **Order Management**
   - Order status tracking
   - Delivery date updates
   - Cost tracking
   - Order history

### **Phase 3: Business Logic Implementation**
1. **RestockOrderService**
   - Create restock orders
   - Update order status
   - Calculate costs
   - Track delivery dates

2. **Automated Restocking**
   - Daily low stock alerts
   - Automatic order suggestions
   - Vendor preference management

3. **Cost Management**
   - Track restock costs
   - Calculate profit margins
   - Budget monitoring

### **Phase 4: Advanced Features**
1. **Vendor Communication**
   - Email integration for order notifications
   - Order confirmation tracking
   - Delivery status updates

2. **Analytics & Reporting**
   - Restock cost analysis
   - Vendor performance metrics
   - Stock turnover rates

3. **Integration Features**
   - Supplier API integration (future)
   - Barcode scanning for deliveries
   - Mobile app for delivery confirmations

---

## Implementation Timeline

### **Week 1: Data Model & Backend**
- [x] Create RestockOrder and RestockOrderItem entities
- [x] Implement repositories and services
- [x] Add database migrations
- [ ] Unit tests for new functionality

### **Week 2: Enhanced UI**
- [ ] Redesign Restocking.fxml with new layout
- [ ] Add order creation interface
- [ ] Implement order management views
- [ ] Add status tracking UI

### **UI Improvements (Completed)**
- [x] Fixed sidebar UI layout and styling
- [x] Created modern, industry-standard login screen
- [x] Improved window sizing and resizing behavior
- [x] Enhanced visual design with gradients and modern styling
- [x] Added proper CSS classes for consistent theming

### **Week 3: Business Logic**
- [ ] Implement RestockOrderService
- [ ] Add cost calculation logic
- [ ] Create automated restocking rules
- [ ] Add order status management

### **Week 4: Testing & Polish**
- [ ] Integration testing
- [ ] UI/UX improvements
- [ ] Performance optimization
- [ ] Documentation updates

---

## Planned Enhancements

### **Short Term (Next 2-4 weeks)**
1. **Enhanced Restocking System** - Complete order management
2. **Improved Dashboard** - Add more analytics and charts
3. **Better Error Handling** - Comprehensive error messages
4. **Data Export** - PDF reports and Excel exports

### **Medium Term (1-3 months)**
1. **Vendor Portal** - Web interface for suppliers
2. **Mobile App** - Inventory management on mobile
3. **Advanced Analytics** - Predictive restocking
4. **Multi-location Support** - Multiple store management

### **Long Term (3-6 months)**
1. **AI Integration** - Machine learning for demand prediction
2. **Cloud Deployment** - Web-based version
3. **API Development** - Third-party integrations
4. **Advanced Reporting** - Business intelligence dashboard

---

## Technical Debt & Improvements
- [ ] Add comprehensive unit tests
- [ ] Implement proper logging
- [ ] Add input validation
- [ ] Improve error handling
- [ ] Optimize database queries
- [ ] Add data backup functionality
- [ ] Implement user roles and permissions
- [ ] Add audit trails for all operations

---

## Success Metrics
- **User Adoption:** 90% of daily operations completed through system
- **Efficiency:** 50% reduction in manual data entry
- **Accuracy:** 99% data consistency across all modules
- **Performance:** Sub-2 second response times for all operations
- **Reliability:** 99.9% uptime with proper error handling
