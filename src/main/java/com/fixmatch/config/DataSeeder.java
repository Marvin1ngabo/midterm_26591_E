package com.fixmatch.config;

import com.fixmatch.entity.*;
import com.fixmatch.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * DataSeeder - Populates database with sample data on application startup
 * 
 * This runs automatically when the application starts
 * Useful for testing and demonstration
 */
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private HierarchicalLocationRepository hierarchicalLocationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProviderProfileRepository providerProfileRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private ServiceCategoryRepository categoryRepository;

    @Autowired
    private JobRepository jobRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (locationRepository.count() > 0 && hierarchicalLocationRepository.count() > 0) {
            System.out.println("✅ Database already seeded. Skipping...");
            return;
        }

        System.out.println("🌱 Seeding database with sample data...");

        // 1. Seed Hierarchical Locations (NEW SYSTEM)
        seedHierarchicalLocations();

        // 2. Seed Legacy Locations (for backward compatibility)
        seedLegacyLocations();

        // 3. Seed Service Categories
        seedServiceCategories();

        // 4. Seed Skills
        seedSkills();

        // 5. Seed Users with Hierarchical Locations
        seedUsers();

        // 6. Seed Jobs
        seedJobs();

        System.out.println("\n🎉 Database seeding completed successfully!");
        System.out.println("📊 Summary:");
        System.out.println("   - Hierarchical Locations: " + hierarchicalLocationRepository.count());
        System.out.println("   - Legacy Locations: " + locationRepository.count());
        System.out.println("   - Categories: " + categoryRepository.count());
        System.out.println("   - Skills: " + skillRepository.count());
        System.out.println("   - Users: " + userRepository.count());
        System.out.println("   - Jobs: " + jobRepository.count());
        System.out.println("\n✅ You can now test all API endpoints!");
    }

    private void seedHierarchicalLocations() {
        System.out.println("🌳 Seeding hierarchical locations...");

        // Create Rwanda administrative hierarchy
        // Level 0: Provinces (Root nodes)
        HierarchicalLocation kigaliCity = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Kigali City", "KGL", LocationType.PROVINCE)
        );
        
        HierarchicalLocation easternProvince = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Eastern Province", "EST", LocationType.PROVINCE)
        );
        
        HierarchicalLocation westernProvince = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Western Province", "WST", LocationType.PROVINCE)
        );

        // Level 1: Districts
        HierarchicalLocation gasabo = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Gasabo", "GAS", LocationType.DISTRICT, kigaliCity)
        );
        
        HierarchicalLocation kicukiro = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Kicukiro", "KIC", LocationType.DISTRICT, kigaliCity)
        );
        
        HierarchicalLocation nyarugenge = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Nyarugenge", "NYA", LocationType.DISTRICT, kigaliCity)
        );
        
        HierarchicalLocation rwamagana = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Rwamagana", "RWA", LocationType.DISTRICT, easternProvince)
        );

        // Level 2: Sectors
        HierarchicalLocation kimironko = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Kimironko", "KIM", LocationType.SECTOR, gasabo)
        );
        
        HierarchicalLocation kimisagara = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Kimisagara", "KMS", LocationType.SECTOR, gasabo)
        );

        // Level 3: Cells
        HierarchicalLocation bibare = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Bibare", "BIB", LocationType.CELL, kimironko)
        );
        
        HierarchicalLocation rugenge = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Rugenge", "RUG", LocationType.CELL, kimisagara)
        );

        // Level 4: Villages (Leaf nodes)
        HierarchicalLocation nyagatovu = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Nyagatovu", "NYG", LocationType.VILLAGE, bibare)
        );
        
        HierarchicalLocation kiyovu = hierarchicalLocationRepository.save(
            new HierarchicalLocation("Kiyovu", "KIY", LocationType.VILLAGE, rugenge)
        );

        System.out.println("✅ Hierarchical locations seeded: " + hierarchicalLocationRepository.count());
    }

    private void seedLegacyLocations() {
        System.out.println("📍 Seeding legacy locations for backward compatibility...");

        // Legacy flat locations (for backward compatibility)
        Location kigaliGasabo = locationRepository.save(new Location(
            "KGL", "Kigali", "Gasabo", "Kimisagara", "Rugenge", "Kiyovu"
        ));
        
        Location kigaliKicukiro = locationRepository.save(new Location(
            "KGL", "Kigali", "Kicukiro", "Nyamirambo", "Nyakabanda", "Kimihurura"
        ));
        
        Location kigaliNyarugenge = locationRepository.save(new Location(
            "KGL", "Kigali", "Nyarugenge", "Muhima", "Rugenge", "Kacyiru"
        ));
        
        Location easternRwamagana = locationRepository.save(new Location(
            "EST", "Eastern Province", "Rwamagana", "Rwamagana", "Nyakariro", "Nyakariro"
        ));

        System.out.println("✅ Legacy locations seeded: " + locationRepository.count());
    }

    private void seedServiceCategories() {
        System.out.println("🏷️ Seeding service categories...");

        categoryRepository.save(new ServiceCategory("Plumbing", "Wrench", "Professional plumbing services"));
        categoryRepository.save(new ServiceCategory("Cleaning", "Sparkles", "Home and office cleaning"));
        categoryRepository.save(new ServiceCategory("Electrical", "Zap", "Electrical installation and repair"));
        categoryRepository.save(new ServiceCategory("Painting", "Paintbrush", "Interior and exterior painting"));
        categoryRepository.save(new ServiceCategory("Moving", "Truck", "Moving and relocation services"));

        System.out.println("✅ Service categories seeded: " + categoryRepository.count());
    }

    private void seedSkills() {
        System.out.println("🛠️ Seeding skills...");

        skillRepository.save(new Skill("Pipe Repair", "Fix leaking and broken pipes"));
        skillRepository.save(new Skill("House Cleaning", "Deep cleaning services"));
        skillRepository.save(new Skill("Electrical Wiring", "Install and repair wiring"));
        skillRepository.save(new Skill("Wall Painting", "Interior and exterior painting"));
        skillRepository.save(new Skill("Furniture Assembly", "Assemble and install furniture"));

        System.out.println("✅ Skills seeded: " + skillRepository.count());
    }

    private void seedUsers() {
        System.out.println("👥 Seeding users with hierarchical locations...");

        // Get hierarchical locations for user assignment
        HierarchicalLocation kiyovu = hierarchicalLocationRepository.findByNameAndType("Kiyovu", LocationType.VILLAGE).orElse(null);
        HierarchicalLocation nyagatovu = hierarchicalLocationRepository.findByNameAndType("Nyagatovu", LocationType.VILLAGE).orElse(null);

        // Get legacy locations for backward compatibility
        Location legacyKigaliGasabo = locationRepository.findByVillageName("Kiyovu").stream().findFirst().orElse(null);
        Location legacyKigaliKicukiro = locationRepository.findByVillageName("Kimihurura").stream().findFirst().orElse(null);

        // Seed Clients with hierarchical locations
        User client1 = new User();
        client1.setName("Jean Uwimana");
        client1.setEmail("jean@example.com");
        client1.setPassword("password123");
        client1.setPhone("0781234567");
        client1.setUserType(UserType.CLIENT);
        client1.setHierarchicalLocation(kiyovu);
        userRepository.save(client1);

        User client2 = new User();
        client2.setName("Marie Mukamana");
        client2.setEmail("marie@example.com");
        client2.setPassword("password123");
        client2.setPhone("0782345678");
        client2.setUserType(UserType.CLIENT);
        client2.setHierarchicalLocation(nyagatovu);
        userRepository.save(client2);

        // Seed Providers with hierarchical locations
        User provider1 = new User();
        provider1.setName("Patrick Nkurunziza");
        provider1.setEmail("patrick@example.com");
        provider1.setPassword("password123");
        provider1.setPhone("0783456789");
        provider1.setUserType(UserType.PROVIDER);
        provider1.setHierarchicalLocation(kiyovu);
        userRepository.save(provider1);

        User provider2 = new User();
        provider2.setName("Grace Uwase");
        provider2.setEmail("grace@example.com");
        provider2.setPassword("password123");
        provider2.setPhone("0784567890");
        provider2.setUserType(UserType.PROVIDER);
        provider2.setHierarchicalLocation(nyagatovu);
        userRepository.save(provider2);

        // Create provider profiles
        createProviderProfiles(provider1, provider2);

        System.out.println("✅ Users seeded: " + userRepository.count());
    }

    private void createProviderProfiles(User provider1, User provider2) {
        // Get skills
        Skill pipeRepair = skillRepository.findByName("Pipe Repair").orElse(null);
        Skill houseCleaning = skillRepository.findByName("House Cleaning").orElse(null);

        // Provider 1 Profile
        ProviderProfile profile1 = new ProviderProfile();
        profile1.setUser(provider1);
        profile1.setBio("Experienced plumber with 10 years of experience");
        profile1.setHourlyRate(new BigDecimal("5000"));
        profile1.setYearsExperience(10);
        profile1.setVerificationStatus(true);
        profile1.setRating(new BigDecimal("4.8"));
        profile1.setTotalJobsCompleted(45);
        if (pipeRepair != null) profile1.addSkill(pipeRepair);
        providerProfileRepository.save(profile1);

        // Provider 2 Profile
        ProviderProfile profile2 = new ProviderProfile();
        profile2.setUser(provider2);
        profile2.setBio("Professional house cleaner, reliable and thorough");
        profile2.setHourlyRate(new BigDecimal("3000"));
        profile2.setYearsExperience(5);
        profile2.setVerificationStatus(true);
        profile2.setRating(new BigDecimal("4.9"));
        profile2.setTotalJobsCompleted(78);
        if (houseCleaning != null) profile2.addSkill(houseCleaning);
        providerProfileRepository.save(profile2);
    }

    private void seedJobs() {
        System.out.println("💼 Seeding jobs...");

        // Get entities for job creation
        User client1 = userRepository.findByEmail("jean@example.com").orElse(null);
        User client2 = userRepository.findByEmail("marie@example.com").orElse(null);
        User provider1 = userRepository.findByEmail("patrick@example.com").orElse(null);
        ServiceCategory plumbing = categoryRepository.findByName("Plumbing").orElse(null);
        ServiceCategory cleaning = categoryRepository.findByName("Cleaning").orElse(null);
        Location legacyLocation = locationRepository.findByVillageName("Kiyovu").stream().findFirst().orElse(null);

        if (client1 != null && plumbing != null && legacyLocation != null) {
            Job job1 = new Job();
            job1.setTitle("Fix leaking kitchen sink");
            job1.setDescription("My kitchen sink has been leaking for 2 days. Need urgent repair.");
            job1.setBudget(new BigDecimal("15000"));
            job1.setStatus(JobStatus.OPEN);
            job1.setClient(client1);
            job1.setCategory(plumbing);
            job1.setLocation(legacyLocation);
            jobRepository.save(job1);
        }

        if (client2 != null && cleaning != null && provider1 != null && legacyLocation != null) {
            Job job2 = new Job();
            job2.setTitle("Deep cleaning for 3-bedroom house");
            job2.setDescription("Need thorough cleaning including windows, floors, and bathrooms.");
            job2.setBudget(new BigDecimal("25000"));
            job2.setStatus(JobStatus.IN_PROGRESS);
            job2.setClient(client2);
            job2.setProvider(provider1);
            job2.setCategory(cleaning);
            job2.setLocation(legacyLocation);
            jobRepository.save(job2);
        }

        System.out.println("✅ Jobs seeded: " + jobRepository.count());
    }
}
