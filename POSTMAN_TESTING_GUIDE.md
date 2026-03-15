# Postman Testing Guide for FixMatch API - Unified Hierarchical Location System

## 🚀 Quick Setup

### Step 1: Import Collection into Postman

1. Open Postman
2. Click **Import** button (top left)
3. Select **File** tab
4. Choose `FixMatch_Postman_Collection_Updated.json`
5. Click **Import**

You'll see a collection called **"FixMatch API - Unified Hierarchical Location System"** with organized folders!

---

## 📋 Test Sequence (Follow This Order)

### ✅ **Phase 1: Initialize & Explore Hierarchical System**

#### Test 1: Initialize Rwanda Administrative Hierarchy
**Folder:** 1. Hierarchical Locations → Initialize Rwanda Hierarchy  
**Method:** POST  
**URL:** `http://localhost:8080/api/locations/initialize`

**Expected Response:**
```json
"Rwanda location hierarchy initialized successfully!"
```

#### Test 2: Get All Provinces (Root Nodes)
**Folder:** 1. Hierarchical Locations → Get All Provinces (Root Nodes)  
**Method:** GET  
**URL:** `http://localhost:8080/api/locations/provinces`

**Expected Response:**
```json
[
  {
    "locationId": 1,
    "name": "Kigali City",
    "code": "KGL",
    "type": "PROVINCE",
    "fullPath": "Kigali City",
    "formattedAddress": "Kigali City",
    "depthLevel": 0,
    "root": true,
    "leaf": false
  },
  {
    "locationId": 2,
    "name": "Eastern Province",
    "code": "EST",
    "type": "PROVINCE",
    "fullPath": "Eastern Province",
    "formattedAddress": "Eastern Province",
    "depthLevel": 0,
    "root": true,
    "leaf": false
  }
]
```

#### Test 3: Get All Villages (Leaf Nodes)
**Folder:** 1. Hierarchical Locations → Get All Villages (Leaf Nodes)  
**Method:** GET  
**URL:** `http://localhost:8080/api/locations/villages`

**Expected Response:**
```json
[
  {
    "locationId": 12,
    "name": "Nyagatovu",
    "code": "NYG",
    "type": "VILLAGE",
    "fullPath": "Kigali City → Gasabo → Kimironko → Bibare → Nyagatovu",
    "formattedAddress": "Kigali City, Gasabo, Kimironko, Bibare, Nyagatovu",
    "depthLevel": 4,
    "parentLocationId": 10,
    "parentLocationName": "Bibare",
    "root": false,
    "leaf": true
  },
  {
    "locationId": 13,
    "name": "Kiyovu",
    "code": "KIY",
    "type": "VILLAGE",
    "fullPath": "Kigali City → Gasabo → Kimisagara → Rugenge → Kiyovu",
    "formattedAddress": "Kigali City, Gasabo, Kimisagara, Rugenge, Kiyovu",
    "depthLevel": 4,
    "parentLocationId": 11,
    "parentLocationName": "Rugenge",
    "root": false,
    "leaf": true
  }
]
```

#### Test 4: Get Location by ID with Full Hierarchy
**Folder:** 1. Hierarchical Locations → Get Location by ID with Full Hierarchy  
**Method:** GET  
**URL:** `http://localhost:8080/api/locations/12`

**Expected Response:**
```json
{
  "locationId": 12,
  "name": "Nyagatovu",
  "code": "NYG",
  "type": "VILLAGE",
  "fullPath": "Kigali City → Gasabo → Kimironko → Bibare → Nyagatovu",
  "formattedAddress": "Kigali City, Gasabo, Kimironko, Bibare, Nyagatovu",
  "depthLevel": 4,
  "parentLocationId": 10,
  "parentLocationName": "Bibare",
  "root": false,
  "leaf": true
}
```

#### Test 5: Get Children of Location (Tree Navigation)
**Folder:** 1. Hierarchical Locations → Get Children of Location  
**Method:** GET  
**URL:** `http://localhost:8080/api/locations/1/children`

**Expected Response:** Districts under Kigali City
```json
[
  {
    "locationId": 4,
    "name": "Gasabo",
    "code": "GAS",
    "type": "DISTRICT",
    "fullPath": "Kigali City → Gasabo",
    "formattedAddress": "Kigali City, Gasabo",
    "depthLevel": 1,
    "parentLocationId": 1,
    "parentLocationName": "Kigali City",
    "root": false,
    "leaf": false
  }
]
```

#### Test 6: Get Full Hierarchy Path
**Folder:** 1. Hierarchical Locations → Get Full Hierarchy Path  
**Method:** GET  
**URL:** `http://localhost:8080/api/locations/12/path`

**Expected Response:**
```
"Kigali City → Gasabo → Kimironko → Bibare → Nyagatovu"
```

#### Test 7: Get Hierarchy Statistics
**Folder:** 1. Hierarchical Locations → Get Hierarchy Statistics  
**Method:** GET  
**URL:** `http://localhost:8080/api/locations/statistics`

**Expected Response:**
```json
{
  "totalLocations": 13,
  "provinces": 3,
  "districts": 2,
  "sectors": 2,
  "cells": 2,
  "villages": 2
}
```

### ✅ **Phase 2: Village ID-Based User Registration**

#### Test 8: Register User with Village ID (RECOMMENDED)
**Folder:** 2. Users - Village ID-Based Registration → Register User with Village ID (Recommended)  
**Method:** POST  
**URL:** `http://localhost:8080/api/users/register`

**Request Body:**
```json
{
  "name": "Village User Test",
  "email": "village.user@example.com",
  "password": "password123",
  "phone": "0786789012",
  "userType": "CLIENT",
  "villageId": 13
}
```

**Expected Response:**
```json
{
  "id": 4,
  "name": "Village User Test",
  "email": "village.user@example.com",
  "phone": "0786789012",
  "userType": "CLIENT",
  "createdAt": "2026-03-13T11:45:54.728453",
  "updatedAt": "2026-03-13T11:45:54.728453",
  "provider": false,
  "fullLocation": "Kigali City, Gasabo, Kimisagara, Rugenge, Kiyovu",
  "client": true
}
```

**Note:** The user is automatically linked to the complete location hierarchy through the village ID!

#### Test 9: Register User with Village ID (Separate Endpoint)
**Folder:** 2. Users - Village ID-Based Registration → Register User with Village ID (Separate Endpoint)  
**Method:** POST  
**URL:** `http://localhost:8080/api/users/register/village?villageId=12`

**Request Body:**
```json
{
  "name": "Village Param User",
  "email": "village.param@example.com",
  "password": "password123",
  "phone": "0786789013",
  "userType": "PROVIDER"
}
```

#### Test 10: Get All Users (Shows Full Location Hierarchy)
**Folder:** 2. Users - Village-Based Registration → Get All Users (Shows Full Location Hierarchy)  
**Method:** GET  
**URL:** `http://localhost:8080/api/users`

**Expected Response:**
```json
[
  {
    "id": 1,
    "name": "Alice Uwimana",
    "email": "alice@example.com",
    "userType": "CLIENT",
    "fullLocation": "Kigali City, Gasabo, Kimisagara, Rugenge, Kiyovu",
    "provider": false,
    "client": true
  },
  {
    "id": 2,
    "name": "Bob Nkurunziza",
    "email": "bob@example.com",
    "userType": "CLIENT",
    "fullLocation": "Kigali City, Gasabo, Kimironko, Bibare, Nyagatovu",
    "provider": false,
    "client": true
  }
]
```

**Note:** Each user shows their complete location hierarchy in the `fullLocation` field!

### ✅ **Phase 3: Test Hierarchical Location Queries (Requirement #8)**

#### Test 11: Get Users by Province NAME
**Folder:** 2. Users - Village-Based Registration → Get Users by Province NAME  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/province/name/Kigali%20City`

**Expected Response:** All users in Kigali City province (uses hierarchical traversal)
```json
[
  {
    "id": 1,
    "name": "Alice Uwimana",
    "fullLocation": "Kigali City, Gasabo, Kimisagara, Rugenge, Kiyovu"
  },
  {
    "id": 2,
    "name": "Bob Nkurunziza", 
    "fullLocation": "Kigali City, Gasabo, Kimironko, Bibare, Nyagatovu"
  }
]
```

#### Test 12: Get Users by District Name
**Folder:** 2. Users - Village-Based Registration → Get Users by District Name  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/district/name/Gasabo`

**Expected Response:** Users in Gasabo district or any of its child locations

#### Test 13: Get Users by Village Name
**Folder:** 2. Users - Village-Based Registration → Get Users by Village Name  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/village/name/Kiyovu`

**Expected Response:** Users registered in Kiyovu village

#### Test 14: Get Users by Village ID (RECOMMENDED)
**Folder:** 2. Users - Village-Based Registration → Get Users by Village ID (RECOMMENDED)  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/village/id/13`

**Available Village IDs:**
- Nyagatovu: ID = 12
- Kiyovu: ID = 13

**Expected Response:** Users registered in the specified village (same as village name but more efficient)
```json
[
  {
    "id": 1,
    "name": "Alice Uwimana",
    "email": "alice@example.com",
    "fullLocation": "Kigali City, Gasabo, Kimisagara, Rugenge, Kiyovu"
  }
]
```

#### Test 14: Get Users by Location Hierarchy (Flexible)
**Folder:** 2. Users - Village-Based Registration → Get Users by Location Hierarchy (Flexible)  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/location?provinceName=Kigali%20City&districtName=Gasabo&villageName=Kiyovu`

**Expected Response:** Users matching the hierarchical location criteria

#### Test 15: Check Email Exists (Requirement #7)
**Folder:** 2. Users - Village-Based Registration → Check Email Exists  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/exists/email?email=village.user@example.com`

**Expected Response:**
```json
true
```

### ✅ **Phase 4: Test Other Core Features**

#### Test 16: Get Users with Pagination (Requirement #3)
**Folder:** 2. Users - Village-Based Registration → Get Users by Type with Pagination  
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

#### Test 17: Create Provider Profile (One-to-One - Requirement #6)
**Folder:** 3. Providers → Create Provider Profile  
**Method:** POST  
**URL:** `http://localhost:8080/api/providers?userId=3`

**Request Body:**
```json
{
  "bio": "Experienced plumber with 10 years",
  "hourlyRate": 5000,
  "yearsExperience": 10
}
```

#### Test 18: Add Skill to Provider (Many-to-Many - Requirement #4)
**Folder:** 3. Providers → Add Skill to Provider  
**Method:** POST  
**URL:** `http://localhost:8080/api/providers/1/skills?skillName=Plumbing`

**Expected Response:** Updated provider profile with new skill

#### Test 19: Create Job with Village Location
**Folder:** 4. Jobs - With Hierarchical Locations → Create Job (Use Village Location ID)  
**Method:** POST  
**URL:** `http://localhost:8080/api/jobs?clientId=1&categoryId=1&locationId=13`

**Request Body:**
```json
{
  "title": "Fix leaking sink in Kiyovu",
  "description": "Kitchen sink is leaking badly, need urgent repair",
  "budget": 15000
}
```

**Note:** Use village location ID (13 for Kiyovu) to link job to complete location hierarchy

#### Test 20: Get Job Statistics by Province
**Folder:** 4. Jobs - With Hierarchical Locations → Get Job Statistics by Province  
**Method:** GET  
**URL:** `http://localhost:8080/api/jobs/statistics/province`

**Expected Response:**
```json
[
  ["Kigali City", 1],
  ["Eastern Province", 0]
]
```

---

## 🎯 **Key Testing Scenarios**

### **Scenario 1: Complete Village-Based Registration Flow**
1. Get villages → Register user with village → Verify in hierarchical queries
2. **Expected:** User appears in all relevant hierarchy levels (province, district, sector, cell, village)

### **Scenario 2: Hierarchical Location Tree Navigation**
1. Get provinces → Get children → Get grandchildren → Get full path
2. **Expected:** Complete tree traversal works with parent-child relationships

### **Scenario 3: Location-Based User Queries**
1. Register users in different villages → Query by province/district/village
2. **Expected:** Hierarchical traversal finds users at all levels

### **Scenario 4: Job Creation with Location Hierarchy**
1. Create job with village location → Verify job shows complete location context
2. **Expected:** Job linked to complete location hierarchy

---

## 🔍 **Response Validation Checklist**

### ✅ **Hierarchical Location Response**
- [ ] `locationId` is present and numeric
- [ ] `name` matches expected location name
- [ ] `code` is present (may be null for some locations)
- [ ] `type` is valid enum (PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE)
- [ ] `fullPath` shows complete hierarchy with → separators
- [ ] `formattedAddress` shows comma-separated hierarchy
- [ ] `depthLevel` is correct (0=Province, 4=Village)
- [ ] `root` and `leaf` are correct booleans

### ✅ **User Registration Response**
- [ ] User has `fullLocation` field showing complete hierarchy
- [ ] Village name matches registered location
- [ ] User appears in location-based queries
- [ ] Location hierarchy is complete and accurate

### ✅ **Tree Navigation Response**
- [ ] Children endpoint returns direct children only
- [ ] Path endpoint returns string with → separators
- [ ] Statistics show correct counts by type
- [ ] Parent-child relationships are maintained

---

## 🐛 **Troubleshooting**

### **"Application not running" Error**
- **Solution:** Make sure the Spring Boot application is running
```bash
cd backend
mvn spring-boot:run
```

### **"Village not found" Error**
- **Solution:** Use exact village names from villages endpoint
- **Check:** Village names are case-sensitive ("Kiyovu" not "kiyovu")

### **Empty Hierarchical Response**
- **Solution:** Ensure DataSeeder ran successfully during startup
- **Check:** Application logs should show "Database seeding completed successfully!"

### **"500 Internal Server Error"**
- **Solution:** Check application console logs for specific error details
- **Common causes:** Database connection issues, query validation errors

---

## 📊 **Expected Data After Seeding**

After successful application startup, you should have:
- **13 Locations** (complete Rwanda hierarchy sample)
  - 3 Provinces (Kigali City, Eastern Province, Western Province)
  - 2 Districts (Gasabo, Rwamagana)
  - 2 Sectors (Kimironko, Kimisagara)
  - 2 Cells (Bibare, Rugenge)
  - 2 Villages (Nyagatovu, Kiyovu)
- **3 Users** (linked to villages with full hierarchy)
- **4 Service Categories** (Plumbing, Electrical, Cleaning, Carpentry)
- **0 Jobs** (ready for creation)

---

## 🎉 **Success Indicators**

✅ All GET requests return data with proper hierarchy  
✅ POST requests create new records with location links  
✅ Village registration shows complete location context  
✅ Hierarchical queries work at all levels  
✅ Tree navigation (children, path) functions correctly  
✅ Statistics show accurate location counts  
✅ Users appear in province/district/village queries  

---

## 💡 **Pro Testing Tips**

1. **Test in sequence** - Some tests depend on previous data creation
2. **Check response status** - Should be 200 (OK) or 201 (Created)
3. **Verify hierarchy** - Full paths should show complete Province → Village chain
4. **Test edge cases** - Try non-existent village names, invalid IDs
5. **Save examples** - Use Postman's save feature for response examples
6. **Monitor logs** - Watch application console for any error messages

---

## 🌟 **Key Features Demonstrated**

### **Single Location Table Architecture**
- All administrative levels in one table
- Self-referencing parent-child relationships
- Efficient tree data structure operations

### **Village ID-Based User Registration**
- Users select village ID from dropdown (more efficient than names)
- Automatic linking to complete location hierarchy
- Full location context available for queries and display
- Validation ensures the provided ID is actually a village

### **Hierarchical Location Queries**
- Query users by any administrative level
- Automatic traversal of parent-child relationships
- Efficient location-based filtering

### **Tree Data Structure Operations**
- Navigate up and down the location tree
- Get children of any location
- Calculate full hierarchy paths
- Location statistics and analytics

---

**Unified Hierarchical Location Testing Complete!** 🌳✅

You've successfully tested a sophisticated single-table hierarchical location system with village-based user registration!
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

### ✅ Test 11B: Create Complete Hierarchy (NEW)
**Folder:** 1. Locations → Create Complete Hierarchy  
**Method:** POST  
**URL:** `http://localhost:8080/api/locations/hierarchy`  

**Body:**
```json
{
  "provinceId": 1,
  "districtName": "Hierarchy District",
  "districtCode": "HD",
  "sectorName": "Hierarchy Sector",
  "sectorCode": "HS",
  "cellName": "Hierarchy Cell", 
  "cellCode": "HC",
  "villageName": "Hierarchy Village",
  "villageCode": "HV"
}
```

**Expected Response:** Village with complete hierarchy path

**Note:** Creates District → Sector → Cell → Village all in one request!

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

## 🏘️ Village ID-Based User Registration

### Why Village ID-Based Registration?
When users register by selecting their village ID, they automatically get linked to the complete location hierarchy:
- **Province** (e.g., "Kigali City")
- **District** (e.g., "Gasabo") 
- **Sector** (e.g., "Kimisagara")
- **Cell** (e.g., "Rugenge")
- **Village** (e.g., "Kiyovu")

This means you can query users by any level of the hierarchy!

### Available Villages for Registration
- **Nyagatovu**: ID = 12 (Kigali City → Gasabo → Kimironko → Bibare → Nyagatovu)
- **Kiyovu**: ID = 13 (Kigali City → Gasabo → Kimisagara → Rugenge → Kiyovu)

### Testing Village ID Registration Flow

1. **Get Available Villages** (Test 3)
   - Use `/api/locations/villages` to show users available villages with IDs
   - Frontend can display village names but submit village IDs

2. **Register User by Village ID** (Test 8)
   - User selects "Kiyovu" from dropdown (ID = 13)
   - System automatically links to complete hierarchy
   - User can now be found by province, district, sector, cell, or village queries

3. **Verify Location Linking** (Tests 11-14)
   - Test that user appears in province queries
   - Test that user appears in village queries
   - Test hierarchical location queries

### Sample Village ID Registration Test

```bash
# Step 1: Get available villages with IDs
curl http://localhost:8080/api/locations/villages

# Step 2: Register user by village ID
curl -X POST http://localhost:8080/api/users/register/village?villageId=13 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Village User",
    "email": "village.user@example.com",
    "password": "password123",
    "phone": "0783456789",
    "userType": "CLIENT"
  }'

# Step 3: Register user with village ID in body
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Another User",
    "email": "another@example.com",
    "password": "password123",
    "phone": "0783456790",
    "userType": "PROVIDER",
    "villageId": 12
  }'

# Step 4: Verify user appears in province query
curl http://localhost:8080/api/users/province/code/KGL

# Step 5: Verify user appears in village query
curl http://localhost:8080/api/users/village/name/Kiyovu

# Step 5B: Verify user appears in village ID query (RECOMMENDED)
curl http://localhost:8080/api/users/village/id/13
```

---

**Village Registration Complete!** 🏘️✅