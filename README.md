# FixMatch Backend - Spring Boot Gig Management System

## 📋 Project Overview
FixMatch is a comprehensive gig management system backend built with Spring Boot, demonstrating advanced database relationships and JPA functionalities.

## 🎯 Assessment Requirements Coverage

### ✅ 1. Entity Relationship Diagram (ERD) - 6 Tables

**Entities Created:**
1. **Location** - Single table with complete hierarchy (Province, District, Sector, Cell, Village)
2. **User** - Both clients and service providers
3. **ProviderProfile** - Extended profile for providers
4. **ServiceCategory** - Service categories (Plumbing, Cleaning, etc.)
5. **Job** - Job postings and bookings
6. **Skill** - Skills that providers possess

**Relationships:**
- Location → User (One-to-Many)
- Location → Job (One-to-Many)
- User → ProviderProfile (One-to-One)
- ProviderProfile ↔ Skill (Many-to-Many)
- User → Job (One-to-Many as client)
- User → Job (One-to-Many as provider)
- ServiceCategory → Job (One-to-Many)

### ✅ 2. Implementation of Saving Location (2 Marks)

**File:** `Location.java`

**Logic:**
- Single Location entity stores complete hierarchical data
- Contains Province (code, name), District, Sector, Cell, and Village information
- Users and Jobs reference locations via `location_id` foreign key
- Supports both complete hierarchy and partial location data

**Example:**
```java
// Complete hierarchy
Location kigaliGasabo = new Location("KGL", "Kigali", "Gasabo", "Kimisagara", "Rugenge", "Kiyovu");
locationRepository.save(kigaliGasabo);

// Partial hierarchy (District level only)
Location northernMusanze = new Location("NTH", "Northern Province", "Musanze");
locationRepository.save(northernMusanze);
```

**Database Structure:**
```sql
CREATE TABLE locations (
    id BIGSERIAL PRIMARY KEY,
    province_code VARCHAR(10) NOT NULL,
    province_name VARCHAR(100) NOT NULL,
    district_name VARCHAR(100) NOT NULL,
    sector_name VARCHAR(100),
    cell_name VARCHAR(100),
    village_name VARCHAR(100)
);
```

### ✅ 3. Sorting & Pagination (5 Marks)

**Files:** All Repository interfaces

**Sorting Implementation:**
```java
// Sort by name ascending
Sort sort = Sort.by("name").ascending();
List<User> users = userRepository.findAll(sort);

// Sort by rating descending
Sort sort = Sort.by("rating").descending();
```

**Pagination Implementation:**
```java
// Page 0, Size 10, Sort by name
Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
Page<User> userPage = userRepository.findByUserType(UserType.PROVIDER, pageable);

// Access page data
List<User> users = userPage.getContent();
int totalPages = userPage.getTotalPages();
long totalElements = userPage.getTotalElements();
```

**Performance Benefits:**
- Reduces memory usage by loading data in chunks
- Improves query performance (LIMIT and OFFSET in SQL)
- Better user experience (load as you scroll)
- Reduces network bandwidth

### ✅ 4. Many-to-Many Relationship (3 Marks)

**Files:** `ProviderProfile.java`, `Skill.java`

**Relationship:** ProviderProfile ↔ Skill

**Join Table:** `provider_skills`
```sql
CREATE TABLE provider_skills (
    provider_id BIGINT,
    skill_id BIGINT,
    PRIMARY KEY (provider_id, skill_id),
    FOREIGN KEY (provider_id) REFERENCES provider_profiles(id),
    FOREIGN KEY (skill_id) REFERENCES skills(id)
);
```

**Mapping:**
```java
@ManyToMany
@JoinTable(
    name = "provider_skills",
    joinColumns = @JoinColumn(name = "provider_id"),
    inverseJoinColumns = @JoinColumn(name = "skill_id")
)
private Set<Skill> skills = new HashSet<>();
```

**Logic:**
- A provider can have multiple skills (Plumbing, Electrical, etc.)
- A skill can belong to multiple providers
- Join table stores the relationships
- Cascade operations manage the relationship automatically

### ✅ 5. One-to-Many Relationship (2 Marks)

**Files:** `Location.java`, `User.java`, `Job.java`

**Relationship:** Location → User, Location → Job (One location has many users and jobs)

**Mapping:**
```java
// In User.java
@ManyToOne
@JoinColumn(name = "location_id")
private Location location;

// In Job.java
@ManyToOne
@JoinColumn(name = "location_id", nullable = false)
private Location location;
```

**Foreign Keys:** `location_id` in `users` and `jobs` tables

**Logic:**
- Multiple users can belong to one location
- Multiple jobs can be posted in one location
- Foreign keys are stored in the User and Job tables
- Single location table eliminates complex joins

### ✅ 6. One-to-One Relationship (2 Marks)

**Files:** `User.java`, `ProviderProfile.java`

**Relationship:** User → ProviderProfile (One user has one provider profile)

**Mapping:**
```java
// In User.java
@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
private ProviderProfile providerProfile;

// In ProviderProfile.java
@OneToOne
@JoinColumn(name = "user_id", nullable = false, unique = true)
private User user;
```

**Logic:**
- ProviderProfile owns the relationship (has the foreign key)
- `unique = true` ensures one user can only have one profile
- Only users with userType = PROVIDER have a profile

### ✅ 7. existsBy() Method (2 Marks)

**Files:** `UserRepository.java`, `LocationRepository.java`

**Implementation:**
```java
// In UserRepository
boolean existsByEmail(String email);
boolean existsByPhone(String phone);

// In LocationRepository
boolean existsByProvinceCode(String provinceCode);
boolean existsByProvinceCodeAndDistrictName(String provinceCode, String districtName);
```

**Logic:**
- Spring Data JPA automatically generates the query
- SQL: `SELECT COUNT(*) > 0 FROM users WHERE email = ?`
- More efficient than `findBy()` when only checking existence
- Returns boolean (true/false)

**Use Cases:**
- Check if email exists during registration
- Validate province codes and district combinations
- Prevent duplicate entries

### ✅ 8. Retrieve Users by Province (4 Marks)

**File:** `UserRepository.java`

**By Province Code:**
```java
@Query("SELECT u FROM User u JOIN u.location l WHERE l.provinceCode = :provinceCode")
List<User> findUsersByProvinceCode(@Param("provinceCode") String provinceCode);
```

**By Province Name:**
```java
@Query("SELECT u FROM User u JOIN u.location l WHERE l.provinceName = :provinceName")
List<User> findUsersByProvinceName(@Param("provinceName") String provinceName);
```

**Query Logic:**
1. Start from User entity
2. JOIN with Location (User → Location)
3. Filter by province code or name

**SQL Equivalent:**
```sql
SELECT u.* FROM users u
JOIN locations l ON u.location_id = l.id
WHERE l.province_code = 'KGL';
```

**Performance Benefits:**
- Single JOIN instead of multiple JOINs (User → District → Province)
- Faster query execution with simplified relationship
- Direct access to province data in location table

## 🚀 How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (or 14+)

### Database Setup
```bash
# Using psql
psql -U postgres
CREATE DATABASE Gig_db;
\q
```

### Configuration
Edit `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/Gig_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### Run Application
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Server will start on: `http://localhost:8080`

## 📚 API Endpoints

### Core Endpoints
- `GET /api` - API documentation and endpoint listing
- `GET /api/health` - Health check endpoint

### Users
- `GET /api/users` - Get all users (with sorting)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users/register` - Register new user
- `GET /api/users/type/{userType}` - Get users by type (with pagination)
- `GET /api/users/province/code/{code}` - Get users by province code
- `GET /api/users/province/name/{name}` - Get users by province name
- `GET /api/users/providers/verified` - Get verified providers
- `GET /api/users/providers/skill/{skillName}` - Get providers by skill
- `GET /api/users/providers/top-rated` - Get top-rated providers
- `GET /api/users/statistics` - Get user statistics
- `GET /api/users/recent` - Get recent users
- `GET /api/users/search` - Search users by name
- `GET /api/users/exists/email` - Check if email exists
- `GET /api/users/exists/phone` - Check if phone exists

### Jobs
- `GET /api/jobs` - Get all jobs (with pagination)
- `GET /api/jobs/{id}` - Get job by ID
- `POST /api/jobs` - Create new job
- `GET /api/jobs/status/{status}` - Get jobs by status
- `GET /api/jobs/available` - Get available jobs (OPEN status)
- `GET /api/jobs/budget` - Get jobs by budget range
- `GET /api/jobs/search/title` - Search jobs by title
- `GET /api/jobs/statistics` - Get job statistics
- `GET /api/jobs/recent` - Get recent jobs
- `GET /api/jobs/high-budget` - Get high-budget jobs
- `PUT /api/jobs/{id}/assign` - Assign provider to job
- `PUT /api/jobs/{id}/complete` - Mark job as completed
- `PUT /api/jobs/{id}/cancel` - Cancel job

### Providers
- `GET /api/providers` - Get all providers (with pagination & sorting)
- `GET /api/providers/{id}` - Get provider by ID
- `POST /api/providers` - Create provider profile
- `GET /api/providers/skill/{skillName}` - Get providers by skill
- `GET /api/providers/province/{code}` - Get providers by province

### Locations
- `GET /api/locations` - Get all locations
- `GET /api/locations/{id}` - Get location by ID
- `POST /api/locations` - Create new location
- `GET /api/locations/provinces` - Get all unique provinces
- `GET /api/locations/province/{code}` - Get locations by province code
- `GET /api/locations/province/{code}/districts` - Get districts by province
- `GET /api/locations/province/{code}/exists` - Check if province exists
- `GET /api/locations/search` - Search locations by keyword
- `GET /api/locations/statistics` - Get location statistics

### Service Categories
- `GET /api/categories` - Get all service categories
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create new category

## 🎓 Viva-Voce Preparation

### Key Concepts to Explain:

1. **JPA vs Hibernate**
   - JPA is a specification, Hibernate is an implementation
   - JPA provides annotations, Hibernate provides the engine

2. **Lazy vs Eager Loading**
   - Lazy: Load related entities only when accessed
   - Eager: Load related entities immediately
   - Default: @OneToMany and @ManyToMany are LAZY

3. **Cascade Types**
   - PERSIST: Save child when parent is saved
   - MERGE: Update child when parent is updated
   - REMOVE: Delete child when parent is deleted
   - ALL: All operations cascade

4. **Transaction Management**
   - @Transactional ensures ACID properties
   - Rollback on exceptions
   - Commit on success

5. **N+1 Query Problem**
   - Occurs with lazy loading
   - Solution: Use JOIN FETCH or @EntityGraph

## 📁 Project Structure
```
backend/
├── src/main/java/com/fixmatch/
│   ├── FixMatchApplication.java
│   ├── entity/
│   │   ├── User.java
│   │   ├── ProviderProfile.java
│   │   ├── Location.java
│   │   ├── ServiceCategory.java
│   │   ├── Job.java
│   │   └── Skill.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── ProviderProfileRepository.java
│   │   ├── LocationRepository.java
│   │   ├── ServiceCategoryRepository.java
│   │   ├── JobRepository.java
│   │   └── SkillRepository.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── ProviderService.java
│   │   ├── LocationService.java
│   │   ├── ServiceCategoryService.java
│   │   └── JobService.java
│   ├── controller/
│   │   ├── UserController.java
│   │   ├── ProviderController.java
│   │   ├── LocationController.java
│   │   ├── ServiceCategoryController.java
│   │   ├── JobController.java
│   │   └── ApiDocController.java
│   ├── config/
│   │   ├── DataSeeder.java
│   │   └── WebConfig.java
│   └── exception/
│       └── GlobalExceptionHandler.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## ✨ Next Steps
1. Create Service layer
2. Create REST Controllers
3. Add validation
4. Implement authentication
5. Add unit tests
6. Connect with React frontend

---
**Author:** FixMatch Team  
**Date:** 2026  
**Purpose:** Spring Boot Practical Assessment
