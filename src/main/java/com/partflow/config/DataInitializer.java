package com.partflow.config;

import com.partflow.model.Part;
import com.partflow.model.Sale;
import com.partflow.model.User;
import com.partflow.model.Vendor;
import com.partflow.repository.PartRepository;
import com.partflow.repository.SaleRepository;
import com.partflow.repository.UserRepository;
import com.partflow.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PartRepository partRepository;
    private final VendorRepository vendorRepository;
    private final SaleRepository saleRepository;

    public DataInitializer(PasswordEncoder passwordEncoder, UserRepository userRepository, PartRepository partRepository, VendorRepository vendorRepository, SaleRepository saleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.partRepository = partRepository;
        this.vendorRepository = vendorRepository;
        this.saleRepository = saleRepository;
    }

    @Bean
    public CommandLineRunner initData(){
        return  args -> {
            // Initialize Admin User
            if(userRepository.findByUsername("admin").isEmpty()){
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123")); // Encode the password
                userRepository.save(admin);
                System.out.println("Admin user created: admin / admin123");
            }

            // Initialize Vendors
            if (vendorRepository.count() == 0) {
                Vendor vendor1 = new Vendor();
                vendor1.setName("Supplier A");
                vendor1.setContactPerson("John Doe");
                vendor1.setEmail("john.doe@example.com");
                vendor1.setPhone("111-222-3333");
                vendorRepository.save(vendor1);

                Vendor vendor2 = new Vendor();
                vendor2.setName("Supplier B");
                vendor2.setContactPerson("Jane Smith");
                vendor2.setEmail("jane.smith@example.com");
                vendor2.setPhone("444-555-6666");
                vendorRepository.save(vendor2);

                System.out.println("Sample vendors created.");
            }

            // Initialize Parts
            if (partRepository.count() == 0) {
                Vendor vendor1 = vendorRepository.findByName("Supplier A");
                Vendor vendor2 = vendorRepository.findByName("Supplier B");

                Part part1 = new Part();
                part1.setPartName("Widget A");
                part1.setPartNumber("WA-001");
                part1.setPrice(10.50);
                part1.setQuantity(150);
                part1.setInStock(true);
                part1.setVendor(vendor1);
                part1.setRestockThreshold(50);
                part1.setRestockAmount(100);
                partRepository.save(part1);

                Part part2 = new Part();
                part2.setPartName("Gadget B");
                part2.setPartNumber("GB-002");
                part2.setPrice(25.00);
                part2.setQuantity(5); // Low stock
                part2.setInStock(true);
                part2.setVendor(vendor1);
                part2.setRestockThreshold(10);
                part2.setRestockAmount(50);
                partRepository.save(part2);

                Part part3 = new Part();
                part3.setPartName("Thing C");
                part3.setPartNumber("TC-003");
                part3.setPrice(5.75);
                part3.setQuantity(0); // Out of stock
                part3.setInStock(false);
                part3.setVendor(vendor2);
                part3.setRestockThreshold(20);
                part3.setRestockAmount(30);
                partRepository.save(part3);

                Part part4 = new Part();
                part4.setPartName("Doodad D");
                part4.setPartNumber("DD-004");
                part4.setPrice(12.00);
                part4.setQuantity(200);
                part4.setInStock(true);
                part4.setVendor(vendor2);
                part4.setRestockThreshold(30);
                part4.setRestockAmount(70);
                partRepository.save(part4);

                System.out.println("Sample parts created.");
            }

            // Initialize Sales
            if (saleRepository.count() == 0) {
                Part part1 = partRepository.findByPartNumber("WA-001");
                Part part2 = partRepository.findByPartNumber("GB-002");

                Sale sale1 = new Sale();
                sale1.setPart(part1);
                sale1.setQuantity(5);
                sale1.setUnitPrice(part1.getPrice());
                sale1.setTotalPrice(sale1.getQuantity() * sale1.getUnitPrice());
                sale1.setSaleDate(LocalDateTime.now().minusDays(2));
                saleRepository.save(sale1);

                Sale sale2 = new Sale();
                sale2.setPart(part2);
                sale2.setQuantity(2);
                sale2.setUnitPrice(part2.getPrice());
                sale2.setTotalPrice(sale2.getQuantity() * sale2.getUnitPrice());
                sale2.setSaleDate(LocalDateTime.now());
                saleRepository.save(sale2);

                System.out.println("Sample sales created.");
            }
        };
    }
}
