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
        if (locationRepository.count() > 0) {
            System.out.println("✅ Database already seeded. Skipping...");
            return;
        }

        System.out.println("🌱 Seeding database with sample data...");

        // 1. Seed Locations (Province, District, Sector, Cell, Village hierarchy)
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
        
        Location westernKarongi = locationRepository.save(new Location(
            "WST", "Western Province", "Karongi", "Bwishyura", "Bwishyura", "Bwishyura"
        ));

        // Add some locations with partial hierarchy (District level only)
        Location northernMusanze = locationRepository.save(new Location(
            "NTH", "Northern Province", "Musanze"
        ));
        
        Location southernHuye = locationRepository.save(new Location(
            "STH", "Southern Province", "Huye"
        ));

        System.out.println("✅ Locations seeded: 7");

        // 2. Seed Service Categories
        ServiceCategory plumbing = categoryRepository.save(
            new ServiceCategory("Plumbing", "Wrench", "Professional plumbing services")
        );
        ServiceCategory cleaning = categoryRepository.save(
            new ServiceCategory("Cleaning", "Sparkles", "Home and office cleaning")
        );
        ServiceCategory electrical = categoryRepository.save(
            new ServiceCategory("Electrical", "Zap", "Electrical installation and repair")
        );
        ServiceCategory painting = categoryRepository.save(
            new ServiceCategory("Painting", "Paintbrush", "Interior and exterior painting")
        );
        ServiceCategory moving = categoryRepository.save(
            new ServiceCategory("Moving", "Truck", "Moving and relocation services")
        );

        System.out.println("✅ Service Categories seeded: 5");

        // 3. Seed Skills
        Skill pipeRepair = skillRepository.save(new Skill("Pipe Repair", "Fix leaking and broken pipes"));
        Skill houseCleaning = skillRepository.save(new Skill("House Cleaning", "Deep cleaning services"));
        Skill electricalWiring = skillRepository.save(new Skill("Electrical Wiring", "Install and repair wiring"));
        Skill wallPainting = skillRepository.save(new Skill("Wall Painting", "Interior and exterior painting"));
        Skill furniture = skillRepository.save(new Skill("Furniture Assembly", "Assemble and install furniture"));

        System.out.println("✅ Skills seeded: 5");

        // 4. Seed Users (Clients)
        User client1 = new User();
        client1.setName("Jean Uwimana");
        client1.setEmail("jean@example.com");
        client1.setPassword("password123");
        client1.setPhone("0781234567");
        client1.setUserType(UserType.CLIENT);
        client1.setLocation(kigaliGasabo);
        userRepository.save(client1);

        User client2 = new User();
        client2.setName("Marie Mukamana");
        client2.setEmail("marie@example.com");
        client2.setPassword("password123");
        client2.setPhone("0782345678");
        client2.setUserType(UserType.CLIENT);
        client2.setLocation(kigaliKicukiro);
        userRepository.save(client2);

        System.out.println("✅ Clients seeded: 2");

        // 5. Seed Users (Providers)
        User provider1 = new User();
        provider1.setName("Patrick Nkurunziza");
        provider1.setEmail("patrick@example.com");
        provider1.setPassword("password123");
        provider1.setPhone("0783456789");
        provider1.setUserType(UserType.PROVIDER);
        provider1.setLocation(kigaliGasabo);
        userRepository.save(provider1);

        User provider2 = new User();
        provider2.setName("Grace Uwase");
        provider2.setEmail("grace@example.com");
        provider2.setPassword("password123");
        provider2.setPhone("0784567890");
        provider2.setUserType(UserType.PROVIDER);
        provider2.setLocation(kigaliNyarugenge);
        userRepository.save(provider2);

        User provider3 = new User();
        provider3.setName("Emmanuel Habimana");
        provider3.setEmail("emmanuel@example.com");
        provider3.setPassword("password123");
        provider3.setPhone("0785678901");
        provider3.setUserType(UserType.PROVIDER);
        provider3.setLocation(kigaliKicukiro);
        userRepository.save(provider3);

        System.out.println("✅ Providers seeded: 3");

        // 6. Seed Provider Profiles (One-to-One relationship)
        ProviderProfile profile1 = new ProviderProfile();
        profile1.setUser(provider1);
        profile1.setBio("Experienced plumber with 10 years of experience");
        profile1.setHourlyRate(new BigDecimal("5000"));
        profile1.setYearsExperience(10);
        profile1.setVerificationStatus(true);
        profile1.setRating(new BigDecimal("4.8"));
        profile1.setTotalJobsCompleted(45);
        providerProfileRepository.save(profile1);

        // Add skills to provider1 (Many-to-Many relationship)
        profile1.addSkill(pipeRepair);
        profile1.addSkill(furniture);
        providerProfileRepository.save(profile1);

        ProviderProfile profile2 = new ProviderProfile();
        profile2.setUser(provider2);
        profile2.setBio("Professional house cleaner, reliable and thorough");
        profile2.setHourlyRate(new BigDecimal("3000"));
        profile2.setYearsExperience(5);
        profile2.setVerificationStatus(true);
        profile2.setRating(new BigDecimal("4.9"));
        profile2.setTotalJobsCompleted(78);
        providerProfileRepository.save(profile2);

        // Add skills to provider2
        profile2.addSkill(houseCleaning);
        providerProfileRepository.save(profile2);

        ProviderProfile profile3 = new ProviderProfile();
        profile3.setUser(provider3);
        profile3.setBio("Licensed electrician, fast and efficient");
        profile3.setHourlyRate(new BigDecimal("6000"));
        profile3.setYearsExperience(8);
        profile3.setVerificationStatus(true);
        profile3.setRating(new BigDecimal("4.7"));
        profile3.setTotalJobsCompleted(32);
        providerProfileRepository.save(profile3);

        // Add skills to provider3
        profile3.addSkill(electricalWiring);
        providerProfileRepository.save(profile3);

        System.out.println("✅ Provider Profiles seeded: 3");

        // 7. Seed Jobs
        Job job1 = new Job();
        job1.setTitle("Fix leaking kitchen sink");
        job1.setDescription("My kitchen sink has been leaking for 2 days. Need urgent repair.");
        job1.setBudget(new BigDecimal("15000"));
        job1.setStatus(JobStatus.OPEN);
        job1.setClient(client1);
        job1.setCategory(plumbing);
        job1.setLocation(kigaliGasabo);
        jobRepository.save(job1);

        Job job2 = new Job();
        job2.setTitle("Deep cleaning for 3-bedroom house");
        job2.setDescription("Need thorough cleaning including windows, floors, and bathrooms.");
        job2.setBudget(new BigDecimal("25000"));
        job2.setStatus(JobStatus.IN_PROGRESS);
        job2.setClient(client2);
        job2.setProvider(provider2);
        job2.setCategory(cleaning);
        job2.setLocation(kigaliKicukiro);
        jobRepository.save(job2);

        Job job3 = new Job();
        job3.setTitle("Install ceiling lights");
        job3.setDescription("Need to install 5 ceiling lights in living room and bedrooms.");
        job3.setBudget(new BigDecimal("20000"));
        job3.setStatus(JobStatus.OPEN);
        job3.setClient(client1);
        job3.setCategory(electrical);
        job3.setLocation(kigaliGasabo);
        jobRepository.save(job3);

        Job job4 = new Job();
        job4.setTitle("Paint bedroom walls");
        job4.setDescription("Need to repaint 2 bedroom walls. Paint will be provided.");
        job4.setBudget(new BigDecimal("30000"));
        job4.setStatus(JobStatus.COMPLETED);
        job4.setClient(client2);
        job4.setProvider(provider1);
        job4.setCategory(painting);
        job4.setLocation(kigaliKicukiro);
        jobRepository.save(job4);

        System.out.println("✅ Jobs seeded: 4");

        System.out.println("\n🎉 Database seeding completed successfully!");
        System.out.println("📊 Summary:");
        System.out.println("   - Locations: 7 (with complete hierarchy)");
        System.out.println("   - Categories: 5");
        System.out.println("   - Skills: 5");
        System.out.println("   - Clients: 2");
        System.out.println("   - Providers: 3");
        System.out.println("   - Provider Profiles: 3");
        System.out.println("   - Jobs: 4");
        System.out.println("\n✅ You can now test all API endpoints!");
    }
}
