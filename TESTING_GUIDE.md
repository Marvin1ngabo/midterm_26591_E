# 🧪 FixMatch Testing Guide - Unified Hierarchical Location System

## 🚀 Quick Start Testing

### Prerequisites
1. **Java 17+** installed
2. **PostgreSQL** running on localhost:5432
3. **Database** named `Gig_db` created
4. **Maven** installed

### 1. Start the Application

```bash
# Navigate to project directory
cd backend

# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run
```

**Expected Output:**
```
✅ FixMatch Backend Started Successfully!
🚀 Server running on: http://localhost:8080
📚 API Documentation: http://localhost:8080/api
🌳 Hierarchical locations seeded: 13
👥 Sample users seeded: 3
📊 Database seeding completed successfully!
```

## 🔧 Testing Methods

### Method 1: Browser-Based Testing (Easiest)
1. Open `api-test.html` in your browser
2. Click "Check Server Status" to verify connection
3. Test each section systematically

### Method 2: Postman Collection
1. Import `FixMatch_Postman_Collection_Updated.json` into Postman
2. Follow the testing sequence in `POSTMAN_TESTING_GUIDE.md`

### Method 3: Command Line (curl)
```bash
# Health check
curl http://localhost:8080/api

# Test hierarchical locations
curl http://localhost:8080/api/locations/provinces
curl http://localhost:8080/api/locations/villages

# Test village-based user registration
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "phone": "0786789012",
    "userType": "CLIENT",
    "villageName": "Kiyovu"
  }'
```

## 📋 Assessment Requirements Testing Checklist

### ✅ Requirement #1: ERD with 5+ Tables (3 Marks)
**Test:** Get API documentation
```bash
curl http://localhost:8080/api
```
**Verify:** Response shows 7 tables with relationships

### ✅ Requirement #2: Hierarchical Location System (2 Marks)
**Test:** Create locations with parent-child relationships
```bash
# Get all provinces (root nodes)
curl http://localhost:8080/api/locations/provinces

# Get all villages (leaf nodes)  
curl http://localhost:8080/api/locations/villages

# Get location with full hierarchy path
curl http://localhost:8080/api/locations/12

# Get children of a location
curl http://localhost:8080/api/locations/1/children

# Create new location with parent
curl -X POST http://localhost:8080/api/locations \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Village",
    "code": "NV",
    "type": "VILLAGE",
    "parentId": 10
  }'
```
**Verify:** Single locations table with self-referencing parent_location_id, complete hierarchy paths

### ✅ Requirement #3: Sorting & Pagination (5 Marks)
**Test Sorting:**
```bash
# Sort users by name ascending
curl "http://localhost:8080/api/users?sortBy=name&direction=asc"

# Sort users by email descending
curl "http://localhost:8080/api/users?sortBy=email&direction=desc"
```

**Test Pagination:**
```bash
# Get page 0, size 5
curl "http://localhost:8080/api/users/type/PROVIDER?page=0&size=5"

# Get page 1, size 10
curl "http://localhost:8080/api/users/type/PROVIDER?page=1&size=10"
```

### ✅ Requirement #4: Many-to-Many (3 Marks)
**Test:** ProviderProfile ↔ Skill relationship
```bash
# Get providers by skill
curl http://localhost:8080/api/users/providers/skill/Plumbing

# Create provider with skills
curl -X POST http://localhost:8080/api/providers \
  -H "Content-Type: application/json" \
  -d '{"bio":"Expert plumber","skills":["Plumbing","Electrical"]}'
```

### ✅ Requirement #5: One-to-Many (2 Marks)
**Test:** Location → User and Location → Job relationships
```bash
# Get locations with their users
curl http://localhost:8080/api/locations

# Get users by location
curl http://localhost:8080/api/users/location/1

# Get jobs by location
curl http://localhost:8080/api/jobs/location/1
```

### ✅ Requirement #6: One-to-One (2 Marks)
**Test:** User → ProviderProfile relationship
```bash
# Get user with provider profile
curl http://localhost:8080/api/users/1

# Create user with provider profile
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"john@test.com","userType":"PROVIDER"}'
```

### ✅ Requirement #7: existsBy() Method (2 Marks)
**Test:** Email and phone existence checking
```bash
# Check if email exists
curl "http://localhost:8080/api/users/exists/email?email=test@example.com"

# Check if phone exists
curl "http://localhost:8080/api/users/exists/phone?phone=0781234567"
```

### ✅ Requirement #8: Users by Province and Complete Location Hierarchy (4 Marks)
**Test:** Hierarchical location queries with automatic traversal
```bash
# By province code
curl http://localhost:8080/api/users/province/code/KGL

# By province name (hierarchical traversal)
curl "http://localhost:8080/api/users/province/name/Kigali%20City"

# By district name (includes all child locations)
curl "http://localhost:8080/api/users/district/name/Gasabo"

# By sector name (hierarchical traversal)
curl "http://localhost:8080/api/users/sector/name/Kimironko"

# By cell name (hierarchical traversal)
curl "http://localhost:8080/api/users/cell/name/Bibare"

# By village name (direct match)
curl "http://localhost:8080/api/users/village/name/Kiyovu"

# By flexible location hierarchy
curl "http://localhost:8080/api/users/location?provinceName=Kigali%20City&districtName=Gasabo&villageName=Kiyovu"
```
**Verify:** Queries use hierarchical traversal to find users at any level of the location tree

## 🗄️ Database Verification

### Check Foreign Keys in Database
```sql
-- Connect to PostgreSQL
psql -U postgres -d Gig_db

-- Check table structure
\d locations
\d users
\d provider_profiles

-- Verify foreign key constraints
SELECT 
    tc.table_name, 
    kcu.column_name, 
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name 
FROM information_schema.table_constraints AS tc 
JOIN information_schema.key_column_usage AS kcu
  ON tc.constraint_name = kcu.constraint_name
JOIN information_schema.constraint_column_usage AS ccu
  ON ccu.constraint_name = tc.constraint_name
WHERE constraint_type = 'FOREIGN KEY';
```

## 🎯 Performance Testing

### Test Pagination Performance
```bash
# Small page size
time curl "http://localhost:8080/api/users?page=0&size=5"

# Large page size
time curl "http://localhost:8080/api/users?page=0&size=100"
```

### Test Sorting Performance
```bash
# Sort by indexed field (id)
time curl "http://localhost:8080/api/users?sortBy=id&direction=asc"

# Sort by non-indexed field (name)
time curl "http://localhost:8080/api/users?sortBy=name&direction=asc"
```

## 🐛 Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   ```bash
   # Change port in application.properties
   server.port=8081
   ```

2. **Database connection failed**
   ```bash
   # Check PostgreSQL is running
   pg_ctl status
   
   # Create database if missing
   createdb -U postgres Gig_db
   ```

3. **Foreign key constraint violations**
   ```bash
   # Check data seeder has run
   curl http://localhost:8080/api/locations
   ```

## 📊 Expected Test Results

### Successful API Responses
- **Health Check:** `{"status":"UP","timestamp":"..."}`
- **User Statistics:** `{"totalUsers":X,"totalProviders":Y}`
- **Pagination:** `{"content":[...],"totalElements":X,"totalPages":Y}`
- **Sorting:** Data ordered by specified field
- **Foreign Keys:** Related data properly linked

### Database Tables Created
- `locations` (location_id, name, code, type, parent_location_id) - **Hierarchical structure**
- `users` (id, name, email, location_id) - **Links to village-level location**
- `provider_profiles` (id, user_id, bio, rating)
- `provider_skills` (provider_id, skill_id) - **Many-to-Many junction table**
- `jobs` (id, title, client_id, provider_id, category_id, location_id)
- `service_categories` (id, name, description)
- `skills` (id, name, description)

### Hierarchical Location Data
- **13 Locations** total in tree structure
- **3 Provinces** (Kigali City, Eastern Province, Western Province)
- **2 Districts** (Gasabo, Rwamagana)
- **2 Sectors** (Kimironko, Kimisagara)
- **2 Cells** (Bibare, Rugenge)
- **2 Villages** (Nyagatovu, Kiyovu)

### Village-Based User Registration
- Users register by selecting village name
- Automatically linked to complete location hierarchy
- `fullLocation` field shows: "Kigali City, Gasabo, Kimironko, Bibare, Nyagatovu"

## 🎓 Viva-Voce Preparation

### Key Points to Explain
1. **Hierarchical Location System:** Self-referencing relationships and tree data structure
2. **Adjacency List Model:** Parent-child relationships in single table
3. **Village-Based Registration:** Automatic hierarchy linking through village selection
4. **Tree Traversal:** Recursive queries for hierarchical location searches
5. **JPA Relationships:** How foreign keys are managed in hierarchical structure
6. **Pagination Benefits:** Memory usage and performance optimization
7. **existsBy() Logic:** Efficiency over findBy() methods
8. **JPQL Queries:** Complex hierarchical join syntax
9. **Cascade Operations:** How related entities are saved and updated
10. **Transaction Management:** ACID properties in hierarchical operations

### Demo Flow for Assessment
1. Start application and show hierarchical location seeding logs
2. Demonstrate village-based user registration
3. Show hierarchical location queries (province → district → village)
4. Explain tree data structure and parent-child relationships
5. Show database schema with self-referencing foreign key
6. Demonstrate pagination and sorting on hierarchical data
7. Answer theoretical questions about tree structures and JPA concepts