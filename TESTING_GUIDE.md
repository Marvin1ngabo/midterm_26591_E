# 🧪 FixMatch Testing Guide

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
```

## 🔧 Testing Methods

### Method 1: Browser-Based Testing (Easiest)
1. Open `api-test.html` in your browser
2. Click "Check Server Status" to verify connection
3. Test each section systematically

### Method 2: Postman Collection
1. Import `FixMatch_Postman_Collection.json` into Postman
2. Run the collection to test all endpoints

### Method 3: Command Line (curl)
```bash
# Health check
curl http://localhost:8080/api/health

# Get API documentation
curl http://localhost:8080/api

# Test user endpoints
curl http://localhost:8080/api/users
```

## 📋 Assessment Requirements Testing Checklist

### ✅ Requirement #1: ERD with 5+ Tables (3 Marks)
**Test:** Get API documentation
```bash
curl http://localhost:8080/api
```
**Verify:** Response shows 7 tables with relationships

### ✅ Requirement #2: Location Saving (2 Marks)
**Test:** Create Province and District
```bash
# Create Province
curl -X POST http://localhost:8080/api/provinces \
  -H "Content-Type: application/json" \
  -d '{"code":"TST","name":"Test Province"}'

# Create District (with foreign key)
curl -X POST http://localhost:8080/api/districts \
  -H "Content-Type: application/json" \
  -d '{"name":"Test District","provinceId":1}'
```
**Verify:** District has `province_id` foreign key

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
**Test:** Province → District relationship
```bash
# Get all provinces (shows districts list)
curl http://localhost:8080/api/provinces

# Get districts by province
curl http://localhost:8080/api/districts/province/1
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

### ✅ Requirement #8: Province Queries (4 Marks)
**Test:** Users by province code and name
```bash
# Get users by province code
curl http://localhost:8080/api/users/province/code/KGL

# Get users by province name
curl http://localhost:8080/api/users/province/name/Kigali
```

## 🗄️ Database Verification

### Check Foreign Keys in Database
```sql
-- Connect to PostgreSQL
psql -U postgres -d Gig_db

-- Check table structure
\d districts
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
   curl http://localhost:8080/api/provinces
   ```

## 📊 Expected Test Results

### Successful API Responses
- **Health Check:** `{"status":"UP","timestamp":"..."}`
- **User Statistics:** `{"totalUsers":X,"totalProviders":Y}`
- **Pagination:** `{"content":[...],"totalElements":X,"totalPages":Y}`
- **Sorting:** Data ordered by specified field
- **Foreign Keys:** Related data properly linked

### Database Tables Created
- `provinces` (id, code, name)
- `districts` (id, name, province_id)
- `users` (id, name, email, district_id)
- `provider_profiles` (id, user_id, bio, rating)
- `provider_skills` (provider_id, skill_id)
- `jobs` (id, title, client_id, provider_id, category_id)
- `service_categories` (id, name, description)
- `skills` (id, name, description)

## 🎓 Viva-Voce Preparation

### Key Points to Explain
1. **JPA Relationships:** How foreign keys are managed
2. **Pagination Benefits:** Memory usage and performance
3. **Sorting Implementation:** Using Sort and Pageable
4. **existsBy() Logic:** Efficiency over findBy()
5. **JPQL Queries:** Join syntax for province queries
6. **Cascade Operations:** How related entities are saved
7. **Transaction Management:** ACID properties

### Demo Flow for Assessment
1. Start application and show startup logs
2. Open browser tester and demonstrate each requirement
3. Show database tables and foreign key constraints
4. Explain code implementation for each requirement
5. Answer theoretical questions about JPA concepts