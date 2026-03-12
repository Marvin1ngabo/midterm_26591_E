# Postman Testing Guide for FixMatch API

## 🚀 Quick Setup

### Step 1: Import Collection into Postman

1. Open Postman
2. Click **Import** button (top left)
3. Select **File** tab
4. Choose `FixMatch_Postman_Collection_Updated.json`
5. Click **Import**

You'll see a collection called **"FixMatch API - Complete with Location Hierarchy"** with 5 folders!

---

## 📋 Test Sequence (Follow This Order)

### ✅ Test 1: Get All Village Locations (For Registration)
**Folder:** 1. Locations → Get All Village Locations  
**Method:** GET  
**URL:** `http://localhost:8080/api/locations/villages`

**Expected Response:**
```json
[
  {
    "id": 1,
    "provinceCode": "KGL",
    "provinceName": "Kigali",
    "districtName": "Gasabo",
    "sectorName": "Kimisagara",
    "cellName": "Rugenge",
    "villageName": "Kiyovu"
  },
  ...
]
```

---

### ✅ Test 2: Get All Village Names Only
**Folder:** 1. Locations → Get All Village Names Only  
**Method:** GET  
**URL:** `http://localhost:8080/api/locations/villages/names`

**Expected Response:**
```json
[
  "Kiyovu",
  "Kimihurura",
  "Nyarutarama",
  ...
]
```

---

### ✅ Test 3: Register User by Village Name (RECOMMENDED)
**Folder:** 2. Users → Register User by Village Name  
**Method:** POST  
**URL:** `http://localhost:8080/api/users/register/village?villageName=Kiyovu`  
**Body:**
```json
{
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "password": "password123",
  "phone": "0782345678",
  "userType": "PROVIDER"
}
```

**Expected Response:**
```json
{
  "id": 6,
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "phone": "0782345678",
  "userType": "PROVIDER",
  "location": {
    "id": 1,
    "provinceCode": "KGL",
    "provinceName": "Kigali",
    "districtName": "Gasabo",
    "sectorName": "Kimisagara",
    "cellName": "Rugenge",
    "villageName": "Kiyovu"
  }
}
```

**Note:** The user is automatically linked to the complete location hierarchy!

---

### ✅ Test 4: Register User with Location ID
**Folder:** 2. Users → Register User with Location ID  
**Method:** POST  
**URL:** `http://localhost:8080/api/users/register?locationId=1`  
**Body:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "phone": "0781234567",
  "userType": "CLIENT"
}
```

**Expected Response:** User with location linked by ID

---

### ✅ Test 5: Register User (Basic - No Location)
**Folder:** 2. Users → Register User (Basic)  
**Method:** POST  
**URL:** `http://localhost:8080/api/users/register`  
**Body:**
```json
{
  "name": "Test User",
  "email": "test@example.com",
  "password": "password123",
  "phone": "0789999999",
  "userType": "CLIENT"
}
```

**Expected Response:** User without location (location field will be null)

---

### ✅ Test 6: Check Location Exists (Requirement #7)
**Folder:** 1. Locations → Check Province Exists  
**Method:** GET  
**URL:** `http://localhost:8080/api/locations/province/KGL/exists`

**Expected Response:**
```json
true
```

---

### ✅ Test 7: Get Users by Province CODE (Requirement #8)
**Folder:** 2. Users → Get Users by Province CODE  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/province/code/KGL`

**Expected Response:**
```json
[
  {
    "id": 1,
    "name": "Jean Uwimana",
    "email": "jean@example.com",
    "userType": "CLIENT",
    "location": {
      "provinceCode": "KGL",
      "provinceName": "Kigali"
    }
  },
  ...
]
```

---

### ✅ Test 8: Get Users by Village Name (NEW)
**Folder:** 2. Users → Get Users by Village Name  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/village/name/Kiyovu`

**Expected Response:** Users living in Kiyovu village

---

### ✅ Test 9: Get Users by Location Hierarchy (NEW)
**Folder:** 2. Users → Get Users by Location Hierarchy  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/location?provinceCode=KGL&districtName=Gasabo&sectorName=Kimisagara`

**Expected Response:** Users matching the specific location hierarchy

---

### ✅ Test 10: Check Email Exists (Requirement #7)
**Folder:** 2. Users → Check Email Exists  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/exists/email?email=jean@example.com`

**Expected Response:**
```json
true
```

---

### ✅ Test 6: Get Users with Pagination (Requirement #3)
**Folder:** 2. Users → Get Users by Type with Pagination  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/type/PROVIDER?page=0&size=10&sortBy=name&direction=asc`

**Expected Response:**
```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalPages": 1,
  "totalElements": 3,
  "first": true,
  "last": true
}
```

---

### ✅ Test 7: Get Providers with Sorting (Requirement #3)
**Folder:** 3. Providers → Get All Providers with Pagination  
**Method:** GET  
**URL:** `http://localhost:8080/api/providers?page=0&size=10&sortBy=rating&direction=desc`

**Expected Response:** Paginated list of providers sorted by rating

---

### ✅ Test 8: Get Providers by Skill (Many-to-Many - Requirement #4)
**Folder:** 3. Providers → Get Providers by Skill  
**Method:** GET  
**URL:** `http://localhost:8080/api/providers/skill/Plumbing?page=0&size=10`

**Expected Response:** Providers who have "Plumbing" skill

---

### ✅ Test 9: Add Skill to Provider (Many-to-Many - Requirement #4)
**Folder:** 3. Providers → Add Skill to Provider  
**Method:** POST  
**URL:** `http://localhost:8080/api/providers/1/skills?skillName=Carpentry`

**Expected Response:** Updated provider profile with new skill

---

### ✅ Test 10: Create Location (Requirement #2)
**Folder:** 1. Locations → Create Location  
**Method:** POST  
**URL:** `http://localhost:8080/api/locations`  
**Body:**
```json
{
  "provinceCode": "TEST",
  "provinceName": "Test Province",
  "districtName": "Test District",
  "sectorName": "Test Sector",
  "cellName": "Test Cell",
  "villageName": "Test Village"
}
```

**Expected Response:**
```json
{
  "id": 6,
  "provinceCode": "TEST",
  "provinceName": "Test Province",
  "districtName": "Test District",
  "sectorName": "Test Sector",
  "cellName": "Test Cell",
  "villageName": "Test Village",
  "fullAddress": "Test Village, Test Cell, Test Sector, Test District, Test Province"
}
```

---

### ✅ Test 11: Create Partial Location (Requirement #2)
**Folder:** 1. Locations → Create Partial Location  
**Method:** POST  
**URL:** `http://localhost:8080/api/locations`  
**Body:**
```json
{
  "provinceCode": "TEST2",
  "provinceName": "Test Province 2",
  "districtName": "Test District 2"
}
```

**Expected Response:**
```json
{
  "id": 7,
  "provinceCode": "TEST2",
  "provinceName": "Test Province 2",
  "districtName": "Test District 2",
  "sectorName": null,
  "cellName": null,
  "villageName": null,
  "shortAddress": "Test District 2, Test Province 2"
}
```

---

### ✅ Test 12: Get Locations with Pagination (Requirement #3)
**Folder:** 1. Locations → Get Locations with Pagination  
**Method:** GET  
**URL:** `http://localhost:8080/api/locations?page=0&size=10&sortBy=provinceName&direction=asc`

**Expected Response:** Paginated list of locations sorted by province name

---

### ✅ Test 13: Get All Jobs with Pagination (Requirement #3)
**Folder:** 4. Jobs → Get All Jobs with Pagination  
**Method:** GET  
**URL:** `http://localhost:8080/api/jobs?page=0&size=10&sortBy=createdAt&direction=desc`

**Expected Response:** Paginated list of jobs sorted by creation date

---

### ✅ Test 14: Get Categories with Sorting (Requirement #3)
**Folder:** 5. Categories → Get All Categories with Sorting  
**Method:** GET  
**URL:** `http://localhost:8080/api/categories?sortBy=name&direction=asc`

**Expected Response:** List of categories sorted by name

---

## 🎯 Testing All Requirements

### Requirement #2: Saving Location ✅
- Test 10: Create Complete Location
- Test 11: Create Partial Location

### Requirement #3: Pagination & Sorting ✅
- Test 6: Users with Pagination
- Test 7: Providers with Sorting
- Test 12: Locations with Pagination
- Test 13: Jobs with Pagination
- Test 14: Categories with Sorting

### Requirement #4: Many-to-Many ✅
- Test 8: Get Providers by Skill
- Test 9: Add Skill to Provider

### Requirement #5: One-to-Many ✅
- Test 1: Get Locations (shows users/jobs relationship)
- Location → User (location_id in users table)
- Location → Job (location_id in jobs table)

### Requirement #6: One-to-One ✅
- Create Provider Profile (in collection)

### Requirement #7: existsBy() ✅
- Test 2: Check Location Exists by Province Code
- Test 5: Check Email Exists

### Requirement #8: Users by Province and Location Hierarchy ✅
- Test 3: Get Users by Province CODE
- Test 4: Get Users by Province NAME
- Test 4B: Get Users by District Name (NEW)
- Test 4C: Get Users by Village Name (NEW)
- Test 4D: Get Users by Location Hierarchy (NEW)

---

## 🔧 Troubleshooting

### Error: "Connection refused"
**Solution:** Make sure the Spring Boot application is running
```bash
cd backend
mvn spring-boot:run
```

### Error: "404 Not Found"
**Solution:** Check the URL is correct: `http://localhost:8080`

### Error: "500 Internal Server Error"
**Solution:** Check the console logs in your Spring Boot application

### Empty Response
**Solution:** Make sure data seeding completed successfully

---

## 📊 Expected Data

After seeding, you should have:
- 5 Locations (with complete hierarchy: Kigali-Gasabo-Kimisagara, Eastern-Rwamagana, etc.)
- 5 Users (2 clients, 3 providers) - each linked to a location
- 3 Provider Profiles (one-to-one with provider users)
- 5 Service Categories (Plumbing, Electrical, Cleaning, etc.)
- 4 Jobs (various statuses and budgets) - each linked to a location
- 5 Skills (linked to providers via many-to-many)

---

## 🎉 Success Indicators

✅ All GET requests return data  
✅ POST requests create new records  
✅ Pagination shows correct page info  
✅ Sorting orders results correctly  
✅ existsBy() returns true/false  
✅ Province queries return users  
✅ Many-to-Many queries work  

---

## 💡 Tips

1. **Test in order** - Some tests depend on previous data
2. **Check response status** - Should be 200 (OK) or 201 (Created)
3. **Verify pagination** - Check totalPages, totalElements
4. **Test sorting** - Verify results are in correct order
5. **Save responses** - Use Postman's save feature for examples

---

## 📚 Additional Tests

You can also test:
- Register new user
- Create new job
- Assign provider to job
- Complete job
- Search providers by skill and province
- Get verified providers

All these are included in the collection!

---

**Happy Testing!** 🚀

---

## 🏘️ Village-Based User Registration

### Why Village-Based Registration?
When users register by selecting their village, they automatically get linked to the complete location hierarchy:
- **Province** (e.g., "Kigali")
- **District** (e.g., "Gasabo") 
- **Sector** (e.g., "Kimisagara")
- **Cell** (e.g., "Rugenge")
- **Village** (e.g., "Kiyovu")

This means you can query users by any level of the hierarchy!

### Testing Village Registration Flow

1. **Get Available Villages** (Test 1 & 2)
   - Use `/api/locations/villages` to show users available villages
   - Use `/api/locations/villages/names` for a simple dropdown

2. **Register User by Village** (Test 3)
   - User selects "Kiyovu" from dropdown
   - System automatically links to complete hierarchy
   - User can now be found by province, district, sector, cell, or village queries

3. **Verify Location Linking** (Tests 7-9)
   - Test that user appears in province queries
   - Test that user appears in village queries
   - Test hierarchical location queries

### Sample Village Registration Test

```bash
# Step 1: Get available villages
curl http://localhost:8080/api/locations/villages/names

# Step 2: Register user by village
curl -X POST http://localhost:8080/api/users/register/village?villageName=Kiyovu \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Village User",
    "email": "village.user@example.com",
    "password": "password123",
    "phone": "0783456789",
    "userType": "CLIENT"
  }'

# Step 3: Verify user appears in province query
curl http://localhost:8080/api/users/province/code/KGL

# Step 4: Verify user appears in village query
curl http://localhost:8080/api/users/village/name/Kiyovu
```

---

**Village Registration Complete!** 🏘️✅