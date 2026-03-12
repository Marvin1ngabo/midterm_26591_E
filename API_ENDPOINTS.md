# FixMatch API Endpoints Documentation - Hierarchical Location System

Base URL: `http://localhost:8080`

## 🌳 **Hierarchical Location Endpoints (NEW)**

### **Initialize Rwanda Location Hierarchy**
```http
POST /api/hierarchical-locations/initialize
```
**Description**: Creates the complete Rwanda administrative hierarchy using the Adjacency List Model.

### **Get All Provinces (Root Nodes)**
```http
GET /api/hierarchical-locations/provinces
```

### **Get All Villages (Leaf Nodes)**
```http
GET /api/hierarchical-locations/villages
```

### **Get Location with Full Path**
```http
GET /api/hierarchical-locations/1
```
**Response Example**:
```json
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
}
```

### **Get Children of a Location**
```http
GET /api/hierarchical-locations/1/children
```

### **Get Full Hierarchy Path**
```http
GET /api/hierarchical-locations/5/path
```
**Response**: `"Kigali City → Gasabo → Kimironko → Bibare → Nyagatovu"`

### **Get Locations by Type**
```http
GET /api/hierarchical-locations/type/DISTRICT
GET /api/hierarchical-locations/type/VILLAGE
```

### **Search Locations by Name**
```http
GET /api/hierarchical-locations/search?name=Kigali
```

### **Find Village by Name (For Registration)**
```http
GET /api/hierarchical-locations/village/Nyagatovu
```

### **Create New Location**
```http
POST /api/hierarchical-locations
Content-Type: application/json

{
  "name": "New Village",
  "code": "NV",
  "type": "VILLAGE",
  "parentId": 4
}
```

### **Get Hierarchy Statistics**
```http
GET /api/hierarchical-locations/statistics
```
**Response**:
```json
{
  "totalLocations": 15,
  "provinces": 5,
  "districts": 8,
  "sectors": 2,
  "cells": 2,
  "villages": 2
}
```

---

## 📍 Location Endpoints

### Single Location Table (Complete Hierarchy)

#### Create Location (Requirement #2)
```http
POST /api/locations
Content-Type: application/json

{
  "provinceCode": "KGL",
  "provinceName": "Kigali",
  "districtName": "Gasabo",
  "sectorName": "Kimisagara",
  "cellName": "Rugenge",
  "villageName": "Kiyovu"
}
```

#### Create Partial Location (District Level Only)
```http
POST /api/locations
Content-Type: application/json

{
  "provinceCode": "KGL",
  "provinceName": "Kigali",
  "districtName": "Gasabo"
}
```

#### Get All Locations
```http
GET /api/locations
```

#### Get Location by ID
```http
GET /api/locations/1
```

#### Get All Provinces
```http
GET /api/locations/provinces
```

#### Get Locations by Province Code
```http
GET /api/locations/province/KGL
```

#### Get Locations by Province with Pagination (Requirement #3)
```http
GET /api/locations/province/KGL/paginated?page=0&size=10
```

#### Get Districts by Province Code
```http
GET /api/locations/province/KGL/districts
```

#### Get Sectors by Province and District
```http
GET /api/locations/province/KGL/district/Gasabo/sectors
```

#### Check if Province Exists (Requirement #7)
```http
GET /api/locations/province/KGL/exists
Response: true/false
```

#### Check if District Exists in Province (Requirement #7)
```http
GET /api/locations/province/KGL/district/Gasabo/exists
Response: true/false
```

#### Get Locations by Level
```http
GET /api/locations/level/Village
```

#### Search Locations
```http
GET /api/locations/search?keyword=gasabo
```

#### Get Location Statistics
```http
GET /api/locations/statistics
```

#### Get Complete Hierarchy Locations
```http
GET /api/locations/complete
```

#### Get All Village Locations (For User Registration)
```http
GET /api/locations/villages
```

#### Get All Village Names Only
```http
GET /api/locations/villages/names
```

---

## 👤 User Endpoints

#### Register User with Village in Request Body (RECOMMENDED)
```http
POST /api/users/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "0781234567",
  "userType": "CLIENT",
  "villageName": "Kiyovu"
}
```
**Note**: The villageName automatically links the user to the complete location hierarchy (Province → District → Sector → Cell → Village)

#### Register User without Village (Optional)
```http
POST /api/users/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "0781234567",
  "userType": "CLIENT"
}
```

#### Register User with Location ID (Legacy)
```http
POST /api/users/register?locationId=1
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "0781234567",
  "userType": "CLIENT"
}
```

#### Register User by Village Name Parameter (Alternative)
```http
POST /api/users/register/village?villageName=Kiyovu
Content-Type: application/json

{
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "password": "password123",
  "phone": "0782345678",
  "userType": "PROVIDER"
}
```

#### Get User by ID
```http
GET /api/users/1
```

#### Get All Users with Sorting (Requirement #3)
```http
GET /api/users?sortBy=name&direction=asc
```

#### Get Users by Type with Pagination (Requirement #3)
```http
GET /api/users/type/PROVIDER?page=0&size=10&sortBy=name&direction=asc
```

#### Get Users by Province Code (Requirement #8)
```http
GET /api/users/province/code/KGL
```

#### Get Users by Province Name (Requirement #8)
```http
GET /api/users/province/name/Kigali
```

#### Get Users by District Name
```http
GET /api/users/district/name/Gasabo
```

#### Get Users by Sector Name
```http
GET /api/users/sector/name/Kimisagara
```

#### Get Users by Cell Name
```http
GET /api/users/cell/name/Rugenge
```

#### Get Users by Village Name
```http
GET /api/users/village/name/Kiyovu
```

#### Get Users by Location Hierarchy
```http
GET /api/users/location?provinceCode=KGL&districtName=Gasabo&sectorName=Kimisagara&cellName=Rugenge&villageName=Kiyovu
```

#### Get Users by Location ID
```http
GET /api/users/location/1
```

#### Get Providers by Province with Pagination
```http
GET /api/users/providers/province/KGL?page=0&size=10
```

#### Check if Email Exists (Requirement #7)
```http
GET /api/users/exists/email?email=john@example.com
Response: true/false
```

#### Check if Phone Exists (Requirement #7)
```http
GET /api/users/exists/phone?phone=0781234567
Response: true/false
```

#### Update User
```http
PUT /api/users/1
Content-Type: application/json

{
  "name": "John Updated",
  "phone": "0789999999"
}
```

#### Delete User
```http
DELETE /api/users/1
```

---

## 🔧 Provider Endpoints

#### Create Provider Profile (One-to-One)
```http
POST /api/providers?userId=1
Content-Type: application/json

{
  "bio": "Experienced plumber with 10 years",
  "hourlyRate": 5000,
  "yearsExperience": 10
}
```

#### Add Skill to Provider (Many-to-Many - Requirement #4)
```http
POST /api/providers/1/skills?skillName=Plumbing
```

#### Get Provider by User ID
```http
GET /api/providers/user/1
```

#### Get All Providers with Pagination & Sorting (Requirement #3)
```http
GET /api/providers?page=0&size=10&sortBy=rating&direction=desc
```

#### Get Verified Providers
```http
GET /api/providers/verified?page=0&size=10
```

#### Get Providers by Skill (Many-to-Many Query)
```http
GET /api/providers/skill/Plumbing?page=0&size=10
```

#### Search Providers (Complex Query)
```http
GET /api/providers/search?skill=Plumbing&province=KGL&minRating=4.0&page=0&size=10
```

#### Update Provider
```http
PUT /api/providers/1
Content-Type: application/json

{
  "bio": "Updated bio",
  "hourlyRate": 6000
}
```

---

## 📋 Job Endpoints

#### Create Job
```http
POST /api/jobs?clientId=1&categoryId=1&locationId=1
Content-Type: application/json

{
  "title": "Fix leaking sink",
  "description": "Kitchen sink is leaking badly",
  "budget": 15000
}
```

#### Assign Provider to Job
```http
PUT /api/jobs/1/assign?providerId=2
```

#### Complete Job
```http
PUT /api/jobs/1/complete
```

#### Get Job by ID
```http
GET /api/jobs/1
```

#### Get All Jobs with Pagination & Sorting (Requirement #3)
```http
GET /api/jobs?page=0&size=10&sortBy=createdAt&direction=desc
```

#### Get Jobs by Status
```http
GET /api/jobs/status/OPEN?page=0&size=10
```

#### Get Jobs by Client
```http
GET /api/jobs/client/1?page=0&size=10
```

#### Get Jobs by Provider
```http
GET /api/jobs/provider/2?page=0&size=10
```

#### Get Jobs by Province
```http
GET /api/jobs/province/KGL?page=0&size=10
```

#### Search Jobs
```http
GET /api/jobs/search?categoryId=1&province=KGL&page=0&size=10
```

---

## 🏷️ Service Category Endpoints

#### Create Category
```http
POST /api/categories
Content-Type: application/json

{
  "name": "Plumbing",
  "icon": "Wrench",
  "description": "Professional plumbing services"
}
```

#### Get All Categories with Sorting (Requirement #3)
```http
GET /api/categories?sortBy=name&direction=asc
```

#### Get Category by ID
```http
GET /api/categories/1
```

#### Get Category by Name
```http
GET /api/categories/name/Plumbing
```

---

## 📊 Sample Test Scenarios

### Test Requirement #2: Saving Location
```bash
# 1. Create Complete Location Hierarchy
curl -X POST http://localhost:8080/api/locations \
  -H "Content-Type: application/json" \
  -d '{
    "provinceCode":"KGL",
    "provinceName":"Kigali",
    "districtName":"Gasabo",
    "sectorName":"Kimisagara",
    "cellName":"Rugenge",
    "villageName":"Kiyovu"
  }'

# 2. Create Partial Location (District Level)
curl -X POST http://localhost:8080/api/locations \
  -H "Content-Type: application/json" \
  -d '{
    "provinceCode":"EST",
    "provinceName":"Eastern Province",
    "districtName":"Rwamagana"
  }'
```

### Test Requirement #3: Pagination & Sorting
```bash
# Get providers sorted by rating, page 0, size 10
curl http://localhost:8080/api/providers?page=0&size=10&sortBy=rating&direction=desc
```

### Test Requirement #4: Many-to-Many
```bash
# Add skill to provider
curl -X POST http://localhost:8080/api/providers/1/skills?skillName=Plumbing
```

### Test Requirement #7: existsBy()
```bash
# Check if email exists
curl http://localhost:8080/api/users/exists/email?email=john@example.com
```

### Test Requirement #8: Users by Province and Location Hierarchy
```bash
# By province code
curl http://localhost:8080/api/users/province/code/KGL

# By province name
curl http://localhost:8080/api/users/province/name/Kigali

# By district name
curl http://localhost:8080/api/users/district/name/Gasabo

# By village name
curl http://localhost:8080/api/users/village/name/Kiyovu

# By complete location hierarchy
curl "http://localhost:8080/api/users/location?provinceCode=KGL&districtName=Gasabo&sectorName=Kimisagara"
```

---

## 🎯 Response Formats

### Success Response (Single Entity)
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  ...
}
```

### Success Response (Paginated)
```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalPages": 5,
  "totalElements": 50,
  "last": false,
  "first": true
}
```

### Error Response
```json
{
  "timestamp": "2026-02-26T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email already exists"
}
```

---

## 🧪 Testing with Postman

1. Import this file into Postman
2. Set base URL: `http://localhost:8080`
3. Test each endpoint
4. Verify pagination, sorting, and relationships

---

## ✅ All Requirements Covered

- ✅ Requirement #2: Location saving endpoints
- ✅ Requirement #3: Pagination & Sorting on all list endpoints
- ✅ Requirement #4: Many-to-Many (Provider ↔ Skill)
- ✅ Requirement #5: One-to-Many (Location → User, Location → Job)
- ✅ Requirement #6: One-to-One (User → ProviderProfile)
- ✅ Requirement #7: existsBy() methods
- ✅ Requirement #8: Users by province and complete location hierarchy (code, name, district, sector, cell, village)
