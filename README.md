# FixMatch Backend - Spring Boot Gig Management System

## 📋 Project Overview
FixMatch is a comprehensive gig management system backend built with Spring Boot, demonstrating advanced database relationships and JPA functionalities.

## 🎯 Assessment Requirements Coverage

### ✅ 1. Entity Relationship Diagram (ERD) - 7 Tables

**Entities Created:**
1. **Province** - Stores province data (code, name)
2. **District** - Districts within provinces
3. **User** - Both clients and service providers
4. **ProviderProfile** - Extended profile for providers
5. **ServiceCategory** - Service categories (Plumbing, Cleaning, etc.)
6. **Job** - Job postings and bookings
7. **Skill** - Skills that providers possess

**Relationships:**
- Province → District (One-to-Many)
- District → User (One-to-Many)
- User → ProviderProfile (One-to-One)
- ProviderProfile ↔ Skill (Many-to-Many)
- User → Job (One-to-Many as client)
- User → Job (One-to-Many as provider)
- ServiceCategory → Job (One-to-Many)

### ✅ 2. Implementation of Saving Location (2 Marks)

**Files:** `Province.java`, `District.java`

**Logic:**
- Province entity stores province code and name
- District entity has a Many-to-One relationship with Province
- When saving a District, the foreign key `province_id` is automatically managed by JPA
- Cascade operations ensure related entities are saved together

**Example:**
```java
Province kigali = new Province("KGL", "Kigali");
District gasabo = new District("Gasabo");
gasabo.setProvince(kigali);
districtRepository.save(gasabo); // Saves both if cascade is enabled
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

**Files:** `Province.java`, `District.java`

**Relationship:** Province → District (One province has many districts)

**Mapping:**
```java
// In Province.java
@OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
private List<District> districts;

// In District.java
@ManyToOne
@JoinColumn(name = "province_id", nullable = false)
private Province province;
```

**Foreign Key:** `province_id` in `districts` table

**Logic:**
- `mappedBy` indicates District owns the relationship
- Foreign key is stored in the District table
- Cascade operations propagate from Province to Districts

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

**Files:** `UserRepository.java`, `ProvinceRepository.java`

**Implementation:**
```java
boolean existsByEmail(String email);
boolean existsByPhone(String phone);
boolean existsByCode(String code);
```

**Logic:**
- Spring Data JPA automatically generates the query
- SQL: `SELECT COUNT(*) > 0 FROM users WHERE email = ?`
- More efficient than `findBy()` when only checking existence
- Returns boolean (true/false)

**Use Cases:**
- Check if email exists during registration
- Validate province codes
- Prevent duplicate entries

### ✅ 8. Retrieve Users by Province (4 Marks)

**File:** `UserRepository.java`

**By Province Code:**
```java
@Query("SELECT u FROM User u JOIN u.district d JOIN d.province p WHERE p.code = :provinceCode")
List<User> findUsersByProvinceCode(@Param("provinceCode") String provinceCode);
```

**By Province Name:**
```java
@Query("SELECT u FROM User u JOIN u.district d JOIN d.province p WHERE p.name = :provinceName")
List<User> findUsersByProvinceName(@Param("provinceName") String provinceName);
```

**Query Logic:**
1. Start from User entity
2. JOIN with District (User → District)
3. JOIN with Province (District → Province)
4. Filter by province code or name

**SQL Equivalent:**
```sql
SELECT u.* FROM users u
JOIN districts d ON u.district_id = d.id
JOIN provinces p ON d.province_id = p.id
WHERE p.code = 'KGL';
```

## 🚀 How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (or 14+)

### Database Setup
```bash
# Using psql
psql -U postgres
CREATE DATABASE fixmatch_db;
\q
```

### Configuration
Edit `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fixmatch_db
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

## 📚 API Endpoints (To be implemented in Controllers)

### Users
- `GET /api/users` - Get all users (with pagination)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `GET /api/users/province/{code}` - Get users by province code

### Providers
- `GET /api/providers` - Get all providers (with pagination & sorting)
- `GET /api/providers/skill/{skillName}` - Get providers by skill
- `GET /api/providers/province/{code}` - Get providers by province

### Jobs
- `GET /api/jobs` - Get all jobs (with pagination)
- `POST /api/jobs` - Create new job
- `GET /api/jobs/status/{status}` - Get jobs by status

### Locations
- `GET /api/provinces` - Get all provinces
- `GET /api/districts` - Get all districts
- `POST /api/provinces` - Create new province

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
│   │   ├── Province.java
│   │   ├── District.java
│   │   ├── ServiceCategory.java
│   │   ├── Job.java
│   │   └── Skill.java
│   └── repository/
│       ├── UserRepository.java
│       ├── ProviderProfileRepository.java
│       ├── ProvinceRepository.java
│       ├── DistrictRepository.java
│       ├── ServiceCategoryRepository.java
│       ├── JobRepository.java
│       └── SkillRepository.java
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
