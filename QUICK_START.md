# FixMatch Backend - Quick Start Guide

## 🚀 Getting Started in 5 Minutes

### Prerequisites
- ✅ Java 17 or higher
- ✅ Maven 3.6+
- ✅ PostgreSQL 12+ (or 14+)
- ✅ IDE (IntelliJ IDEA, Eclipse, or VS Code)

---

## Step 1: Setup PostgreSQL Database

### Option A: Using psql Command Line
```bash
psql -U postgres
CREATE DATABASE fixmatch_db;
\q
```

### Option B: Using pgAdmin
1. Open pgAdmin
2. Right-click on Databases
3. Create → Database
4. Name: `fixmatch_db`
5. Click Save

---

## Step 2: Configure Database Connection

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fixmatch_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD_HERE
```

**Important:** Replace `YOUR_PASSWORD_HERE` with your PostgreSQL password!

---

## Step 3: Run the Application

### Option A: Using Maven Command Line
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

### Option B: Using IDE
1. Open `backend` folder in your IDE
2. Wait for Maven to download dependencies
3. Run `FixMatchApplication.java`

### Option C: Using JAR file
```bash
mvn clean package
java -jar target/fixmatch-backend-1.0.0.jar
```

---

## Step 4: Verify It's Running

You should see:
```
==============================================
✅ FixMatch Backend Started Successfully!
🚀 Server running on: http://localhost:8080
📚 API Documentation: http://localhost:8080/api
==============================================

🌱 Seeding database with sample data...
✅ Provinces seeded: 5
✅ Districts seeded: 5
✅ Service Categories seeded: 5
✅ Skills seeded: 5
✅ Clients seeded: 2
✅ Providers seeded: 3
✅ Provider Profiles seeded: 3
✅ Jobs seeded: 4

🎉 Database seeding completed successfully!
```

---

## Step 5: Test the API

### Using Browser
Open: `http://localhost:8080/api/provinces`

You should see JSON response with provinces.

### Using cURL
```bash
# Get all provinces
curl http://localhost:8080/api/locations/provinces

# Get all users
curl http://localhost:8080/api/users

# Get providers with pagination
curl http://localhost:8080/api/providers?page=0&size=10
```

### Using Postman
1. Import `API_ENDPOINTS.md`
2. Test endpoints
3. Verify responses

---

## 📊 Sample Data Included

The application automatically seeds the database with:

### Provinces (5)
- Kigali (KGL)
- Eastern Province (EST)
- Western Province (WST)
- Northern Province (NTH)
- Southern Province (STH)

### Districts (5)
- Gasabo (Kigali)
- Kicukiro (Kigali)
- Nyarugenge (Kigali)
- Rwamagana (Eastern)
- Karongi (Western)

### Users (5)
**Clients:**
- Jean Uwimana (jean@example.com)
- Marie Mukamana (marie@example.com)

**Providers:**
- Patrick Nkurunziza (patrick@example.com) - Plumber
- Grace Uwase (grace@example.com) - Cleaner
- Emmanuel Habimana (emmanuel@example.com) - Electrician

### Service Categories (5)
- Plumbing
- Cleaning
- Electrical
- Painting
- Moving

### Jobs (4)
- Fix leaking kitchen sink (OPEN)
- Deep cleaning for 3-bedroom house (IN_PROGRESS)
- Install ceiling lights (OPEN)
- Paint bedroom walls (COMPLETED)

---

## 🧪 Quick Tests

### Test 1: Get All Provinces
```bash
curl http://localhost:8080/api/locations/provinces
```

### Test 2: Get Users by Province (Requirement #8)
```bash
curl http://localhost:8080/api/users/province/code/KGL
```

### Test 3: Check if Email Exists (Requirement #7)
```bash
curl http://localhost:8080/api/users/exists/email?email=jean@example.com
```

### Test 4: Get Providers with Pagination (Requirement #3)
```bash
curl http://localhost:8080/api/providers?page=0&size=10&sortBy=rating&direction=desc
```

### Test 5: Get Providers by Skill (Many-to-Many - Requirement #4)
```bash
curl http://localhost:8080/api/providers/skill/Plumbing
```

---

## 🐛 Troubleshooting

### Error: "FATAL: password authentication failed for user 'postgres'"
**Solution:** Check your PostgreSQL password in `application.properties`

### Error: "Unknown database 'fixmatch_db'"
**Solution:** Create the database:
```sql
CREATE DATABASE fixmatch_db;
```

### Error: "Port 8080 is already in use"
**Solution:** Change port in `application.properties`:
```properties
server.port=8081
```

### Error: "Could not find or load main class"
**Solution:** Run Maven clean install:
```bash
mvn clean install
```

### Database tables not created
**Solution:** Check `application.properties`:
```properties
spring.jpa.hibernate.ddl-auto=update
```

---

## 📁 Project Structure

```
backend/
├── src/main/java/com/fixmatch/
│   ├── FixMatchApplication.java       # Main class
│   ├── entity/                        # 7 entities
│   ├── repository/                    # 7 repositories
│   ├── service/                       # 5 services
│   ├── controller/                    # 5 controllers
│   └── config/
│       └── DataSeeder.java           # Sample data
├── src/main/resources/
│   └── application.properties        # Configuration
├── pom.xml                           # Maven dependencies
├── README.md                         # Full documentation
├── API_ENDPOINTS.md                  # API documentation
└── QUICK_START.md                    # This file
```

---

## ✅ Verification Checklist

- [ ] MySQL is running
- [ ] Database `fixmatch_db` is created
- [ ] `application.properties` is configured
- [ ] Application starts without errors
- [ ] Sample data is seeded
- [ ] API endpoints respond correctly
- [ ] All 7 tables are created in database

---

## 🎯 Next Steps

1. ✅ Test all API endpoints
2. ✅ Verify pagination and sorting
3. ✅ Test relationships (One-to-One, One-to-Many, Many-to-Many)
4. ✅ Prepare for viva-voce
5. ✅ Connect with React frontend (later)

---

## 📚 Additional Resources

- **Full Documentation:** `README.md`
- **API Endpoints:** `API_ENDPOINTS.md`
- **Assessment Checklist:** `ASSESSMENT_CHECKLIST.md`
- **Spring Boot Docs:** https://spring.io/projects/spring-boot
- **JPA Documentation:** https://spring.io/projects/spring-data-jpa

---

## 🎉 You're Ready!

Your FixMatch backend is now running with:
- ✅ 7 entities with all relationships
- ✅ Complete CRUD operations
- ✅ Pagination and sorting
- ✅ Sample data for testing
- ✅ All assessment requirements covered

**Happy coding!** 🚀
