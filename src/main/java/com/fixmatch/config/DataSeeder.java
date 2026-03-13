package com.fixmatch.config;

import com.fixmatch.entity.*;
import com.fixmatch.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataSeeder - Populates database with sample data on application startup
 * 
 * This class demonstrates:
 * 1. Hierarchical location data seeding
 * 2. Sample user creation with location relationships
 * 3. Service category initialization
 */
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProviderProfileRepository providerProfileRepository;

    @Autowired
    private ServiceCategoryRepository categoryRepository;

    @Autowired
    private JobRepository jobRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (locationRepository.count() > 0) {
            System.out.println("✅ Database already seeded. Skipping...");
            return;
        }

        System.out.println("🌱 Seeding database with sample data...");

        // 1. Seed Hierarchical Locations
        seedHierarchicalLocations();

        // 2. Seed Service Categories - only if empty
        if (categoryRepository.count() == 0) {
            seedServiceCategories();
        }

        // 3. Seed Sample Users with hierarchical locations
        seedSampleUsers();

        System.out.println("\n🎉 Database seeding completed successfully!");
        System.out.println("📊 Summary:");
        System.out.println("   - Locations: " + locationRepository.count());
        System.out.println("   - Categories: " + categoryRepository.count());
        System.out.println("   - Users: " + userRepository.count());
        System.out.println("   - Jobs: " + jobRepository.count());
    }

    /**
     * Seed hierarchical locations using the Adjacency List Model
     */
    private void seedHierarchicalLocations() {
        System.out.println("🏛️ Seeding hierarchical locations...");
        
        // Create Rwanda administrative hierarchy
        // Level 0: Provinces (Root nodes)
        Location kigaliCity = locationRepository.save(
            new Location("Kigali City", "KGL", LocationType.PROVINCE)
        );
        
        Location easternProvince = locationRepository.save(
            new Location("Eastern Province", "EST", LocationType.PROVINCE)
        );
        
        Location westernProvince = locationRepository.save(
            new Location("Western Province", "WST", LocationType.PROVINCE)
        );

        // Level 1: Districts
        Location gasabo = locationRepository.save(
            new Location("Gasabo", "GAS", LocationType.DISTRICT, kigaliCity)
        );
        
        Location kicukiro = locationRepository.save(
            new Location("Kicukiro", "KIC", LocationType.DISTRICT, kigaliCity)
        );
        
        Location nyarugenge = locationRepository.save(
            new Location("Nyarugenge", "NYA", LocationType.DISTRICT, kigaliCity)
        );
        
        Location rwamagana = locationRepository.save(
            new Location("Rwamagana", "RWA", LocationType.DISTRICT, easternProvince)
        );

        // Level 2: Sectors
        Location kimironko = locationRepository.save(
            new Location("Kimironko", "KIM", LocationType.SECTOR, gasabo)
        );
        
        Location kimisagara = locationRepository.save(
            new Location("Kimisagara", "KMS", LocationType.SECTOR, gasabo)
        );

        // Level 3: Cells
        Location bibare = locationRepository.save(
            new Location("Bibare", "BIB", LocationType.CELL, kimironko)
        );
        
        Location rugenge = locationRepository.save(
            new Location("Rugenge", "RUG", LocationType.CELL, kimisagara)
        );

        // Level 4: Villages (Leaf nodes)
        Location nyagatovu = locationRepository.save(
            new Location("Nyagatovu", "NYG", LocationType.VILLAGE, bibare)
        );
        
        Location kiyovu = locationRepository.save(
            new Location("Kiyovu", "KIY", LocationType.VILLAGE, rugenge)
        );

        System.out.println("✅ Hierarchical locations seeded: " + locationRepository.count());
    }
    /**
     * Seed service categories
     */
    private void seedServiceCategories() {
        System.out.println("🔧 Seeding service categories...");
        
        ServiceCategory plumbing = new ServiceCategory();
        plumbing.setName("Plumbing");
        plumbing.setDescription("Water pipe installation, repair, and maintenance");
        categoryRepository.save(plumbing);

        ServiceCategory electrical = new ServiceCategory();
        electrical.setName("Electrical");
        electrical.setDescription("Electrical wiring, installation, and repair services");
        categoryRepository.save(electrical);

        ServiceCategory cleaning = new ServiceCategory();
        cleaning.setName("Cleaning");
        cleaning.setDescription("House cleaning, office cleaning, and maintenance");
        categoryRepository.save(cleaning);

        ServiceCategory gardening = new ServiceCategory();
        gardening.setName("Gardening");
        gardening.setDescription("Garden maintenance, landscaping, and plant care");
        categoryRepository.save(gardening);

        System.out.println("✅ Service categories seeded: " + categoryRepository.count());
    }

    /**
     * Seed sample users with hierarchical locations
     */
    private void seedSampleUsers() {
        System.out.println("👥 Seeding sample users...");

        // Get hierarchical locations for user assignment
        Location kiyovu = locationRepository.findByNameAndType("Kiyovu", LocationType.VILLAGE).orElse(null);
        Location nyagatovu = locationRepository.findByNameAndType("Nyagatovu", LocationType.VILLAGE).orElse(null);

        // Create sample clients
        User client1 = new User();
        client1.setName("Alice Uwimana");
        client1.setEmail("alice@example.com");
        client1.setPassword("password123");
        client1.setPhone("0781234567");
        client1.setUserType(UserType.CLIENT);
        client1.setLocation(kiyovu);
        userRepository.save(client1);

        User client2 = new User();
        client2.setName("Bob Nkurunziza");
        client2.setEmail("bob@example.com");
        client2.setPassword("password123");
        client2.setPhone("0782345678");
        client2.setUserType(UserType.CLIENT);
        client2.setLocation(nyagatovu);
        userRepository.save(client2);

        // Create sample provider
        User provider1 = new User();
        provider1.setName("Charles Mugisha");
        provider1.setEmail("charles@example.com");
        provider1.setPassword("password123");
        provider1.setPhone("0783456789");
        provider1.setUserType(UserType.PROVIDER);
        provider1.setLocation(kiyovu);
        User savedProvider = userRepository.save(provider1);

        // Create provider profile
        ProviderProfile profile = new ProviderProfile();
        profile.setUser(savedProvider);
        profile.setBio("Experienced plumber with 10+ years of experience");
        profile.setYearsExperience(10);
        profile.setHourlyRate(new java.math.BigDecimal("15000.0"));
        profile.setVerificationStatus(true);
        profile.setRating(new java.math.BigDecimal("4.8"));
        providerProfileRepository.save(profile);
        providerProfileRepository.save(profile);

        System.out.println("✅ Sample users seeded: " + userRepository.count());
    }
}