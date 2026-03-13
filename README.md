# FixMatch Backend - Unified Hierarchical Location System

## 📋 Project Overview
FixMatch is a comprehensive gig management system backend built with Spring Boot, featuring a **unified hierarchical location system** using the **Adjacency List Model**. This demonstrates sophisticated database relationships, tree data structures, and modern JPA functionalities with a single Location table architecture.

## 🌳 **Unified Hierarchical Location Architecture**

### **Design Pattern: Adjacency List Model (Single Table)**
```
Province (Root)
└── District
    └── Sector
        └── Cell
            └── Village (Leaf)
```

**Example Hierarchy:**
```
Kigali City → Gasabo → Kimironko → Bibare → Nyagatovu
```

### **Key Features**
- ✅ **Single Location table** with self-referencing relationships
- ✅ **Tree data structure** with recursive operations
- ✅ **Full path generation** and navigation
- ✅ **Village-based user registration** with automatic hierarchy mapping
- ✅ **Hierarchical queries** at any level (province, district, sector, cell, village)
- ✅ **Scalable design** supporting unlimited depth
- ✅ **Clean architecture** with unified location system

## 🎯 Assessment Requirements Coverage

### ✅ 1. Entity Relationship Diagram (ERD) - 7 Tables

**Entities Created:**
1. **Location** - Unified hierarchical location system (single table for all levels)
2. **User** - Both clients and service providers with hierarchical location support
3. **ProviderProfile** - Extended profile for providers
4. **ServiceCategory** - Service categories (Plumbing, Cleaning, etc.)
5. **Job** - Job postings and bookings
6. **Skill** - Skills that providers possess
7. **LocationType** - Enum for location types (PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE)

**Advanced Relationships:**
- **Location → Location** (Self-referencing, Parent-Child) 🌳
- **Location → User** (One-to-Many)
- **Location → Job** (One-to-Many)
- **User → ProviderProfile** (One-to-One)
- **ProviderProfile ↔ Skill** (Many-to-Many)
- **User → Job** (One-to-Many as client/provider)
- **ServiceCategory → Job** (One-to-Many)

### ✅ 2. Unified Hierarchical Location System
**Database Structure:**
```sql
CREATE TABLE locations (
    location_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10),
    type VARCHAR(20) NOT NULL CHECK (type IN ('PROVINCE','DISTRICT','SECTOR','CELL','VILLAGE')),
    parent_location_id BIGINT REFERENCES locations(location_id)
);
```

**Features:**
- `POST /api/locations/initialize` - Initialize Rwanda administrative hierarchy
- `POST /api/locations` - Create locations with parent-child relationships
- Automatic tree structure validation and path generation
- Village-based user registration with complete hierarchy linking

### ✅ 3. Pagination & Sorting
**All list endpoints support:**
- `?page=0&size=10` - Pagination parameters
- `?sortBy=name&direction=asc` - Sorting parameters
- Applied to: Users, Providers, Jobs, Categories, Locations

**Examples:**
```http
GET /api/users?page=0&size=10&sortBy=name&direction=asc
GET /api/providers?page=0&size=5&sortBy=rating&direction=desc
GET /api/jobs?page=0&size=20&sortBy=createdAt&direction=desc
```

### ✅ 4. Many-to-Many Relationship
**ProviderProfile ↔ Skill:**
- `POST /api/providers/{id}/skills?skillName=Plumbing` - Add skill to provider
- `GET /api/providers/skill/Plumbing` - Get providers by skill
- Junction table: `provider_skills`
- Bidirectional relationship with cascade operations

### ✅ 5. One-to-Many Relationships
**Multiple One-to-Many relationships:**
- **Location → User** (One location has many users)
- **Location → Job** (One location has many jobs)
- **User → Job** (One user can have many jobs as client)
- **ServiceCategory → Job** (One category has many jobs)

### ✅ 6. One-to-One Relationship
**User → ProviderProfile:**
- `POST /api/providers?userId=1` - Create provider profile for user
- `GET /api/providers/user/1` - Get provider profile by user ID
- Bidirectional mapping with cascade operations
- Only providers have profiles (conditional relationship)

### ✅ 7. existsBy() Methods
**Validation endpoints:**
```http
GET /api/users/exists/email?email=john@example.com
GET /api/users/exists/phone?phone=0781234567
```

### ✅ 8. Users by Province and Complete Location Hierarchy
**Hierarchical System Queries:**
```http
GET /api/users/province/name/Kigali%20City
GET /api/users/province/code/KGL
GET /api/users/district/name/Gasabo
GET /api/users/sector/name/Kimironko
GET /api/users/cell/name/Bibare
GET /api/users/village/name/Kiyovu
GET /api/users/location?provinceName=Kigali%20City&districtName=Gasabo&villageName=Kiyovu
```

## 🚀 **Quick Start Guide**

### **Prerequisites**
- Java 17+
- Maven 3.6+
- PostgreSQL 12+ (Database: `Gig_db`)

### **Setup & Run**
```bash
# 1. Clone repository
git clone https://github.com/Marvin1ngabo/midterm_26591_E.git
cd midterm_26591_E

# 2. Configure database (application.properties)
spring.datasource.url=jdbc:postgresql://localhost:5432/Gig_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# 3. Run application
mvn spring-boot:run

# 4. Application automatically initializes hierarchical location system
# Check logs for: "Database seeding completed successfully!"
```

### **Test Village-Based Registration**
```bash
# Get available villages
curl http://localhost:8080/api/locations/villages

# Register user with village
curl -X POST "http://localhost:8080/api/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com", 
    "password": "password123",
    "phone": "0786789012",
    "userType": "CLIENT",
    "villageName": "Kiyovu"
  }'

# Verify user appears in hierarchy queries
curl "http://localhost:8080/api/users/province/name/Kigali%20City"
curl "http://localhost:8080/api/users/village/name/Kiyovu"
```

## 🎓 **Computer Science Concepts Demonstrated**

### **1. Tree Data Structure**
- **Nodes**: Each location is a node in the tree
- **Edges**: Parent-child relationships between locations  
- **Root**: Provinces (no parent)
- **Leaves**: Villages (no children)
- **Depth**: Distance from root (Province=0, Village=4)
- **Path**: Complete route from root to any node

### **2. Adjacency List Model**
- Each node stores reference to its parent (`parent_location_id`)
- Enables efficient tree traversal and recursive operations
- Supports unlimited hierarchy depth
- Common pattern for representing hierarchical data

### **3. Self-Referencing Relationships**
- Entity references itself via foreign key
- `@ManyToOne` for parent relationship
- `@OneToMany` for children relationship  
- Demonstrates advanced JPA mapping techniques

### **4. Recursive Algorithms**
- **Path Building**: Traverse from node to root to build full path
- **Tree Navigation**: Find ancestors and descendants
- **Depth Calculation**: Count levels from root
- **Hierarchy Validation**: Ensure tree structure integrity

## 📊 **API Endpoints Overview**

### **🌳 Hierarchical Locations**
```http
POST   /api/locations/initialize                 # Initialize Rwanda hierarchy
GET    /api/locations/provinces                  # Get all provinces (roots)
GET    /api/locations/villages                   # Get all villages (leaves)
GET    /api/locations/{id}                       # Get location with full path
GET    /api/locations/{id}/children              # Get child locations
GET    /api/locations/{id}/path                  # Get full hierarchy path
GET    /api/locations/type/{type}                # Get locations by type
GET    /api/locations/search?name={name}         # Search locations by name
GET    /api/locations/village/{villageName}      # Find village by name
POST   /api/locations                            # Create new location
DELETE /api/locations/{id}                       # Delete location (if no children)
GET    /api/locations/statistics                 # Get hierarchy statistics
```

### **👤 Users with Village-Based Registration**
```http
POST   /api/users/register                       # Register with village in body
POST   /api/users/register/village               # Register with village param
POST   /api/users/register/dto                   # Register with DTO
GET    /api/users                                # Get all users (shows full location)
GET    /api/users/{id}                           # Get user by ID
GET    /api/users/province/name/{name}           # Get users by province (hierarchical)
GET    /api/users/province/code/{code}           # Get users by province code
GET    /api/users/district/name/{name}           # Get users by district (hierarchical)
GET    /api/users/sector/name/{name}             # Get users by sector (hierarchical)
GET    /api/users/cell/name/{name}               # Get users by cell (hierarchical)
GET    /api/users/village/name/{name}            # Get users by village (direct)
GET    /api/users/location                       # Get users by hierarchy (flexible)
GET    /api/users/exists/email                   # Check email exists
GET    /api/users/exists/phone                   # Check phone exists
GET    /api/users/statistics                     # Get user statistics
```

### **🔧 Providers, Jobs, Categories**
```http
POST   /api/providers                            # Create provider profile
POST   /api/providers/{id}/skills                # Add skill to provider
GET    /api/providers/skill/{skill}              # Get providers by skill
POST   /api/jobs                                 # Create job (use village location ID)
GET    /api/jobs/statistics/province             # Get job statistics by province
POST   /api/categories                           # Create category
```

## 📱 **Testing**

### **Postman Collection**
1. Import `FixMatch_Postman_Collection_Updated.json`
2. Follow testing sequence in `POSTMAN_TESTING_GUIDE.md`
3. Test both hierarchical and legacy systems

### **Key Test Scenarios**
- ✅ Initialize hierarchical system
- ✅ Village-based user registration  
- ✅ Tree navigation (parent/child queries)
- ✅ Full path generation
- ✅ Multi-level location queries
- ✅ Backward compatibility with legacy system

## 📚 **Documentation Files**

- **`API_ENDPOINTS.md`** - Complete API documentation with unified hierarchical system
- **`POSTMAN_TESTING_GUIDE.md`** - Step-by-step testing instructions for unified system
- **`HIERARCHICAL_SYSTEM_COMPLETION.md`** - Technical completion summary
- **`FixMatch_Postman_Collection_Updated.json`** - Updated Postman collection
- **`TESTING_GUIDE.md`** - General testing information

## 🏗️ **Architecture Highlights**

### **Database Design**
- **Single Location Table**: Unified hierarchical structure with self-referencing relationships
- **Clean Architecture**: No dual systems, single source of truth for locations
- **Data Seeding**: Automatic initialization with Rwanda administrative hierarchy
- **Tree Structure**: Efficient parent-child relationships using Adjacency List Model

### **Spring Boot Features**
- **JPA Relationships**: All relationship types demonstrated with hierarchical structure
- **Repository Patterns**: Custom queries with hierarchical traversal
- **Service Layer**: Business logic for tree operations and village-based registration
- **Controller Layer**: RESTful API design with hierarchical endpoints
- **Exception Handling**: Global exception management
- **Data Validation**: Input validation and tree structure constraints

### **Advanced Features**
- **Tree Traversal**: Recursive path building and navigation
- **Hierarchical Queries**: Multi-level location-based searches with automatic traversal
- **Village Registration**: Automatic linking to complete location hierarchy
- **Scalable Design**: Supports unlimited hierarchy depth
- **Performance**: Efficient queries with proper indexing and DTO mapping

---

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

### Hierarchical Locations (NEW)
- `POST /api/hierarchical-locations/initialize` - Initialize Rwanda hierarchy
- `GET /api/hierarchical-locations/provinces` - Get all provinces (root nodes)
- `GET /api/hierarchical-locations/villages` - Get all villages (leaf nodes)
- `GET /api/hierarchical-locations/{id}` - Get location with full path
- `GET /api/hierarchical-locations/{id}/children` - Get child locations
- `GET /api/hierarchical-locations/{id}/path` - Get full hierarchy path
- `GET /api/hierarchical-locations/type/{type}` - Get locations by type
- `GET /api/hierarchical-locations/search?name=X` - Search locations
- `GET /api/hierarchical-locations/village/{name}` - Find village by name
- `POST /api/hierarchical-locations` - Create new location
- `GET /api/hierarchical-locations/statistics` - Get hierarchy statistics

### Users (Enhanced with Hierarchical Locations)
- `GET /api/users` - Get all users (with sorting)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users/register` - Register new user
- `POST /api/users/register/village?villageName=X` - **Village-based registration**
- `GET /api/users/type/{userType}` - Get users by type (with pagination)
- `GET /api/users/province/code/{code}` - Get users by province code
- `GET /api/users/province/name/{name}` - Get users by province name
- `GET /api/users/district/name/{name}` - Get users by district name
- `GET /api/users/sector/name/{name}` - Get users by sector name
- `GET /api/users/cell/name/{name}` - Get users by cell name
- `GET /api/users/village/name/{name}` - **Get users by village name**
- `GET /api/users/location?provinceCode=X&districtName=Y` - Hierarchical location query
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
**Author:** RUTATO NGABONZIZA Didace  
**Student ID:** 26591  
**Date:** March 2026  
**Purpose:** Spring Boot Practical Assessment - Unified Hierarchical Location System
