# Postman Testing Guide for FixMatch API - Hierarchical Location System

## 🚀 Quick Setup

### Step 1: Import Collection into Postman

1. Open Postman
2. Click **Import** button (top left)
3. Select **File** tab
4. Choose `FixMatch_Postman_Collection_Updated.json`
5. Click **Import**

You'll see a collection called **"FixMatch API - Complete with Hierarchical Location System"** with 6 folders!

---

## 📋 Test Sequence (Follow This Order)

### ✅ **Phase 1: Initialize Hierarchical System**

#### Test 1: Initialize Rwanda Administrative Hierarchy
**Folder:** 0. Hierarchical Locations (NEW) → Initialize Rwanda Hierarchy  
**Method:** POST  
**URL:** `http://localhost:8080/api/hierarchical-locations/initialize`

**Expected Response:**
```json
{
  "message": "Rwanda administrative hierarchy initialized successfully",
  "totalLocations": 12,
  "provinces": 3,
  "districts": 4,
  "sectors": 2,
  "cells": 2,
  "villages": 2
}
```

#### Test 2: Get Hierarchy Statistics
**Folder:** 0. Hierarchical Locations (NEW) → Get Hierarchy Statistics  
**Method:** GET  
**URL:** `http://localhost:8080/api/hierarchical-locations/statistics`

**Expected Response:**
```json
{
  "totalLocations": 12,
  "provinces": 3,
  "districts": 4,
  "sectors": 2,
  "cells": 2,
  "villages": 2
}
```

---

### ✅ **Phase 2: Explore Hierarchical Structure**

#### Test 3: Get All Provinces (Root Nodes)
**Folder:** 0. Hierarchical Locations (NEW) → Get All Provinces (Root Nodes)  
**Method:** GET  
**URL:** `http://localhost:8080/api/hierarchical-locations/provinces`

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
    "isRoot": true,
    "isLeaf": false
  },
  ...
]
```

#### Test 4: Get All Villages (Leaf Nodes)
**Folder:** 0. Hierarchical Locations (NEW) → Get All Villages (Leaf Nodes)  
**Method:** GET  
**URL:** `http://localhost:8080/api/hierarchical-locations/villages`

**Expected Response:**
```json
[
  {
    "locationId": 11,
    "name": "Nyagatovu",
    "code": "NYG",
    "type": "VILLAGE",
    "fullPath": "Kigali City → Gasabo → Kimironko → Bibare → Nyagatovu",
    "formattedAddress": "Kigali City, Gasabo, Kimironko, Bibare, Nyagatovu",
    "depthLevel": 4,
    "isRoot": false,
    "isLeaf": true
  },
  {
    "locationId": 12,
    "name": "Kiyovu",
    "code": "KIY",
    "type": "VILLAGE",
    "fullPath": "Kigali City → Gasabo → Kimisagara → Rugenge → Kiyovu",
    "formattedAddress": "Kigali City, Gasabo, Kimisagara, Rugenge, Kiyovu",
    "depthLevel": 4,
    "isRoot": false,
    "isLeaf": true
  }
]
```

#### Test 5: Get Children of Location (Tree Navigation)
**Folder:** 0. Hierarchical Locations (NEW) → Get Children of Location  
**Method:** GET  
**URL:** `http://localhost:8080/api/hierarchical-locations/1/children`

**Expected Response:** Districts under Kigali City
```json
[
  {
    "locationId": 4,
    "name": "Gasabo",
    "code": "GAS",
    "type": "DISTRICT",
    "fullPath": "Kigali City → Gasabo"
  },
  ...
]
```

#### Test 6: Get Full Hierarchy Path
**Folder:** 0. Hierarchical Locations (NEW) → Get Full Hierarchy Path  
**Method:** GET  
**URL:** `http://localhost:8080/api/hierarchical-locations/12/path`

**Expected Response:**
```
"Kigali City → Gasabo → Kimisagara → Rugenge → Kiyovu"
```

---

### ✅ **Phase 3: Hierarchical User Registration**

#### Test 7: Register User with Village in Request Body (RECOMMENDED)
**Folder:** 2. Users - Hierarchical Registration → Register User with Village (Body)  
**Method:** POST  
**URL:** `http://localhost:8080/api/users/register`

**Request Body:**
```json
{
  "name": "Hierarchical Test User",
  "email": "hierarchical.test@example.com",
  "password": "password123",
  "phone": "0786789012",
  "userType": "CLIENT",
  "villageName": "Kiyovu"
}
```

**Expected Response:**
```json
{
  "id": 5,
  "name": "Hierarchical Test User",
  "email": "hierarchical.test@example.com",
  "phone": "0786789012",
  "userType": "CLIENT",
  "hierarchicalLocation": {
    "locationId": 12,
    "name": "Kiyovu",
    "code": "KIY",
    "type": "VILLAGE",
    "fullPath": "Kigali City → Gasabo → Kimisagara → Rugenge → Kiyovu"
  },
  "fullLocation": "Kigali City, Gasabo, Kimisagara, Rugenge, Kiyovu"
}
```

#### Test 8: Register User with Village as Parameter
**Folder:** 2. Users - Hierarchical Registration → Register User with Village (Param)  
**Method:** POST  
**URL:** `http://localhost:8080/api/users/register/village?villageName=Nyagatovu`

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

---

### ✅ **Phase 4: Verify Hierarchical Queries**

#### Test 9: Get Users by Province CODE
**Folder:** 2. Users - Hierarchical Registration → Get Users by Province CODE  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/province/code/KGL`

**Expected:** Should return all users registered in Kigali City province

#### Test 10: Get Users by Village Name
**Folder:** 2. Users - Hierarchical Registration → Get Users by Village Name  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/village/name/Kiyovu`

**Expected:** Should return users registered in Kiyovu village

#### Test 11: Get Users by Location Hierarchy
**Folder:** 2. Users - Hierarchical Registration → Get Users by Location Hierarchy  
**Method:** GET  
**URL:** `http://localhost:8080/api/users/location?provinceCode=KGL&districtName=Gasabo&villageName=Kiyovu`

**Expected:** Should return users matching the complete hierarchy

---

### ✅ **Phase 5: Legacy System Compatibility**

#### Test 12: Get All Village Locations (Legacy)
**Folder:** 1. Locations (Legacy System) → Get All Village Locations  
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

#### Test 13: Get All Village Names Only (Legacy)
**Folder:** 1. Locations (Legacy System) → Get All Village Names Only  
**Method:** GET  
**URL:** `http://localhost:8080/api/locations/villages/names`

**Expected Response:**
```json
[
  "Kiyovu",
  "Kimihurura",
  "Kacyiru",
  "Nyakariro"
]
```

---

### ✅ **Phase 6: Advanced Features**

#### Test 14: Search Locations by Name
**Folder:** 0. Hierarchical Locations (NEW) → Search Locations by Name  
**Method:** GET  
**URL:** `http://localhost:8080/api/hierarchical-locations/search?name=Kigali`

#### Test 15: Find Village by Name
**Folder:** 0. Hierarchical Locations (NEW) → Find Village by Name  
**Method:** GET  
**URL:** `http://localhost:8080/api/hierarchical-locations/village/Kiyovu`

#### Test 16: Get Locations by Type
**Folder:** 0. Hierarchical Locations (NEW) → Get Locations by Type (Villages)  
**Method:** GET  
**URL:** `http://localhost:8080/api/hierarchical-locations/type/VILLAGE`

---

## 🎯 **Key Testing Scenarios**

### **Scenario 1: Complete Hierarchical Registration Flow**
1. Initialize hierarchy → Get villages → Register user → Verify in queries
2. **Expected:** User appears in all relevant hierarchy levels

### **Scenario 2: Tree Navigation**
1. Get provinces → Get children → Get grandchildren → Get full path
2. **Expected:** Complete tree traversal works

### **Scenario 3: Backward Compatibility**
1. Test both hierarchical and legacy endpoints
2. **Expected:** Both systems work independently

### **Scenario 4: Village-Based Registration**
1. Register with village name → Check hierarchical location assignment
2. **Expected:** User gets complete location hierarchy automatically

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
- [ ] `isRoot` and `isLeaf` are correct booleans

### ✅ **User Registration Response**
- [ ] User has `hierarchicalLocation` object
- [ ] `fullLocation` shows formatted address
- [ ] User appears in location-based queries
- [ ] Village name matches registered location

### ✅ **Tree Navigation Response**
- [ ] Children endpoint returns direct children only
- [ ] Path endpoint returns string with → separators
- [ ] Statistics show correct counts by type

---

## 🐛 **Troubleshooting**

### **"Hierarchy not initialized" Error**
- **Solution:** Run "Initialize Rwanda Hierarchy" first
- **Check:** Statistics endpoint should show > 0 locations

### **"Village not found" Error**
- **Solution:** Use exact village names from villages endpoint
- **Check:** Village names are case-sensitive

### **Empty Hierarchical Response**
- **Solution:** Ensure DataSeeder ran successfully
- **Check:** Application logs for seeding messages

### **Legacy vs Hierarchical Confusion**
- **Hierarchical:** `/api/hierarchical-locations/*` (NEW)
- **Legacy:** `/api/locations/*` (Backward compatibility)
- **Use:** Hierarchical for new features, legacy for compatibility

---

## 🎓 **Computer Science Learning Points**

### **Tree Data Structure Concepts**
- **Nodes:** Each location is a node in the tree
- **Edges:** Parent-child relationships between locations
- **Root:** Provinces (no parent)
- **Leaves:** Villages (no children)
- **Depth:** Distance from root (Province=0, Village=4)
- **Path:** Route from root to any node

### **Adjacency List Model**
- Each node stores reference to its parent
- Enables efficient tree traversal
- Supports recursive operations
- Scalable for any tree depth

### **Self-Referencing Relationships**
- Entity references itself via foreign key
- `@ManyToOne` for parent relationship
- `@OneToMany` for children relationship
- Demonstrates advanced JPA mapping

---

**Hierarchical Location Testing Complete!** 🌳✅

You've successfully tested a sophisticated tree data structure implementation in Spring Boot!
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