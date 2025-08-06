package com.partflow.service;

import com.partflow.model.Part;
import com.partflow.model.RestockOrder;
import com.partflow.model.RestockOrderItem;
import com.partflow.model.Vendor;
import com.partflow.repository.RestockOrderRepository;
import com.partflow.repository.RestockOrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RestockOrderService {

    @Autowired
    private RestockOrderRepository restockOrderRepository;

    @Autowired
    private RestockOrderItemRepository restockOrderItemRepository;

    @Autowired
    private PartService partService;

    @Autowired
    private VendorService vendorService;

    // Create a new restock order
    public RestockOrder createRestockOrder(Vendor vendor, LocalDateTime expectedDelivery, String notes) {
        RestockOrder order = new RestockOrder();
        order.setVendor(vendor);
        order.setExpectedDelivery(expectedDelivery);
        order.setNotes(notes);
        order.setStatus("PENDING");
        return restockOrderRepository.save(order);
    }

    // Add item to restock order
    public RestockOrderItem addItemToOrder(RestockOrder order, Part part, int quantity, double unitCost) {
        RestockOrderItem item = new RestockOrderItem(part, quantity, unitCost);
        order.addItem(item);
        restockOrderRepository.save(order);
        return item;
    }

    // Get all restock orders
    public List<RestockOrder> getAllRestockOrders() {
        return restockOrderRepository.findAll();
    }

    // Get restock order by ID
    public Optional<RestockOrder> getRestockOrderById(Long id) {
        return restockOrderRepository.findById(id);
    }

    // Get orders by status
    public List<RestockOrder> getOrdersByStatus(String status) {
        return restockOrderRepository.findByStatus(status);
    }

    // Get pending orders
    public List<RestockOrder> getPendingOrders() {
        return restockOrderRepository.findByStatusOrderByOrderDateDesc("PENDING");
    }

    // Get orders by vendor
    public List<RestockOrder> getOrdersByVendor(Long vendorId) {
        return restockOrderRepository.findByVendorId(vendorId);
    }

    // Update order status
    public RestockOrder updateOrderStatus(Long orderId, String newStatus) {
        Optional<RestockOrder> orderOpt = restockOrderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            RestockOrder order = orderOpt.get();
            order.setStatus(newStatus);
            
            // If order is delivered, update inventory
            if ("DELIVERED".equals(newStatus)) {
                processDelivery(order);
            }
            
            return restockOrderRepository.save(order);
        }
        return null;
    }

    // Process delivery - update inventory quantities
    private void processDelivery(RestockOrder order) {
        for (RestockOrderItem item : order.getItems()) {
            Part part = item.getPart();
            part.setQuantity(part.getQuantity() + item.getQuantity());
            partService.savePart(part);
        }
    }

    // Cancel order
    public RestockOrder cancelOrder(Long orderId) {
        return updateOrderStatus(orderId, "CANCELLED");
    }

    // Mark order as delivered
    public RestockOrder markAsDelivered(Long orderId) {
        return updateOrderStatus(orderId, "DELIVERED");
    }

    // Get orders with expected delivery in next X days
    public List<RestockOrder> getOrdersWithExpectedDeliveryInDays(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(days);
        return restockOrderRepository.findOrdersWithExpectedDeliveryBetween(now, endDate);
    }

    // Calculate total cost of all pending orders
    public double getTotalCostOfPendingOrders() {
        List<RestockOrder> pendingOrders = getPendingOrders();
        return pendingOrders.stream()
            .mapToDouble(RestockOrder::getTotalCost)
            .sum();
    }

    // Get order statistics
    public RestockOrderStats getOrderStatistics() {
        long pendingCount = restockOrderRepository.countByStatus("PENDING");
        long orderedCount = restockOrderRepository.countByStatus("ORDERED");
        long deliveredCount = restockOrderRepository.countByStatus("DELIVERED");
        long cancelledCount = restockOrderRepository.countByStatus("CANCELLED");
        
        return new RestockOrderStats(pendingCount, orderedCount, deliveredCount, cancelledCount);
    }

    // Inner class for order statistics
    public static class RestockOrderStats {
        private final long pendingCount;
        private final long orderedCount;
        private final long deliveredCount;
        private final long cancelledCount;

        public RestockOrderStats(long pendingCount, long orderedCount, long deliveredCount, long cancelledCount) {
            this.pendingCount = pendingCount;
            this.orderedCount = orderedCount;
            this.deliveredCount = deliveredCount;
            this.cancelledCount = cancelledCount;
        }

        // Getters
        public long getPendingCount() { return pendingCount; }
        public long getOrderedCount() { return orderedCount; }
        public long getDeliveredCount() { return deliveredCount; }
        public long getCancelledCount() { return cancelledCount; }
        public long getTotalCount() { return pendingCount + orderedCount + deliveredCount + cancelledCount; }
    }
} 