# FixMatch API Endpoints Documentation - Unified Hierarchical Location System

Base URL: `http://localhost:8080`

## 🌳 **Hierarchical Location Endpoints**

### **Initialize Rwanda Location Hierarchy**
```http
POST /api/locations/initialize
```
**Description**: Creates the complete Rwanda administrative hierarchy using the Adjacency List Model with single Location table.

### **Get All Provinces (Root Nodes)**
```http
GET /api/locations/provinces
```
**Response Example**:
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
  }
]
```

### **Get All Villages (Leaf Nodes)**
```http
GET /api/locations/villages
```
**Response Example**:
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
  }
]
```

### **Get Location by ID with Full Hierarchy**
```http
GET /api/locations/{id}
```
**Example**: `GET /api/locations/12`

### **Get Children of a Location**
```http
GET /api/locations/{id}/children
```
**Example**: `GET /api/locations/1/children` (Get all districts in Kigali City)

### **Get Full Hierarchy Path**
```http
GET /api/locations/{id}/path
```
**Example**: `GET /api/locations/12/path`
**Response**: `"Kigali City → Gasabo → Kimironko → Bibare → Nyagatovu"`

### **Get Locations by Type**
```http
GET /api/locations/type/{type}
```
**Examples**:
- `GET /api/locations/type/PROVINCE`
- `GET /api/locations/type/DISTRICT`
- `GET /api/locations/type/VILLAGE`

### **Search Locations by Name**
```http
GET /api/locations/search?name={name}
```
**Example**: `GET /api/locations/search?name=Kigali`

### **Find Village by Name (For User Registration)**
```http
GET /api/locations/village/{villageName}
```
**Example**: `GET /api/locations/village/Nyagatovu`

### **Create New Location**
```http
POST /api/locations
Content-Type: application/json

{
  "name": "New Village",
  "code": "NV",
  "type": "VILLAGE",
  "parentId": 10
}
```

### **Delete Location (Only if No Children)**
```http
DELETE /api/locations/{id}
```

### **Get Hierarchy Statistics**
```http
GET /api/locations/statistics
```
**Response Example**:
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

---

## 👤 User Endpoints - Village-Based Registration

#### Register User with Village Name (RECOMMENDED)
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
**Note**: The villageName automatically links the user to the complete location hierarchy (Province → District → Sector → Cell → Village). The user's `fullLocation` will show: "Kigali City, Gasabo, Kimisagara, Rugenge, Kiyovu"

#### Register User with Village (Separate Endpoint)
```http
POST /api/users/register/village?villageName=Nyagatovu
Content-Type: application/json

{
  "name": "Jane Smith",
  "email": "jane.smith@example.com",
  "password": "password123",
  "phone": "0782345678",
  "userType": "PROVIDER"
}
```

#### Register User with DTO
```http
POST /api/users/register/dto
Content-Type: application/json

{
  "name": "DTO User",
  "email": "dto.user@example.com",
  "password": "password123",
  "phone": "0786789014",
  "userType": "BOTH",
  "villageName": "Kiyovu"
}
```

#### Get User by ID
```http
GET /api/users/{id}
```
**Response includes full location hierarchy**:
```json
{
  "id": 1,
  "name": "Alice Uwimana",
  "email": "alice@example.com",
  "userType": "CLIENT",
  "fullLocation": "Kigali City, Gasabo, Kimisagara, Rugenge, Kiyovu",
  "provider": false,
  "client": true
}
```

#### Get All Users with Sorting (Requirement #3)
```http
GET /api/users?sortBy=name&direction=asc
```

#### Get Users by Type with Pagination (Requirement #3)
```http
GET /api/users/type/PROVIDER?page=0&size=10&sortBy=name&direction=asc
```

### **Hierarchical Location Queries (Requirement #8)**

#### Get Users by Province Code
```http
GET /api/users/province/code/KGL
```

#### Get Users by Province Name
```http
GET /api/users/province/name/Kigali%20City
```
**Note**: Uses hierarchical traversal to find all users in any location within the province

#### Get Users by District Name
```http
GET /api/users/district/name/Gasabo
```
**Note**: Finds users in the district or any of its child locations (sectors, cells, villages)

#### Get Users by Sector Name
```http
GET /api/users/sector/name/Kimironko
```

#### Get Users by Cell Name
```http
GET /api/users/cell/name/Bibare
```

#### Get Users by Village Name
```http
GET /api/users/village/name/Kiyovu
```
**Note**: Direct match since users are typically registered at village level

#### Get Users by Location Hierarchy (Flexible)
```http
GET /api/users/location?provinceName=Kigali%20City&districtName=Gasabo&villageName=Kiyovu
```
**Note**: Searches from most specific (village) to least specific (province)

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

#### Get User Statistics
```http
GET /api/users/statistics
```

#### Update User
```http
PUT /api/users/{id}
Content-Type: application/json

{
  "name": "John Updated",
  "phone": "0789999999"
}
```

#### Delete User
```http
DELETE /api/users/{id}
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

## 📋 Job Endpoints - With Hierarchical Locations

#### Create Job (Use Village Location ID)
```http
POST /api/jobs?clientId=1&categoryId=1&locationId=13
Content-Type: application/json

{
  "title": "Fix leaking sink in Kiyovu",
  "description": "Kitchen sink is leaking badly, need urgent repair",
  "budget": 15000
}
```
**Note**: Use village location ID (e.g., 13 for Kiyovu) to link job to complete location hierarchy

#### Get All Jobs (Shows Full Location Hierarchy)
```http
GET /api/jobs
```
**Response includes location hierarchy for each job**

#### Assign Provider to Job
```http
PUT /api/jobs/{id}/assign?providerId={providerId}
```

#### Complete Job
```http
PUT /api/jobs/{id}/complete
```

#### Get Job by ID
```http
GET /api/jobs/{id}
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
GET /api/jobs/client/{clientId}?page=0&size=10
```

#### Get Jobs by Provider
```http
GET /api/jobs/provider/{providerId}?page=0&size=10
```

#### Get Jobs by Province Code
```http
GET /api/jobs/province/{provinceCode}?page=0&size=10
```

#### Get Job Statistics by Province
```http
GET /api/jobs/statistics/province
```
**Response Example**:
```json
[
  ["Kigali City", 5],
  ["Eastern Province", 2],
  ["Western Province", 1]
]
```

#### Search Jobs by Category and Province
```http
GET /api/jobs/search?categoryId=1&provinceCode=KGL&page=0&size=10
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

### Test Hierarchical Location System
```bash
# 1. Get all provinces
curl http://localhost:8080/api/locations/provinces

# 2. Get all villages
curl http://localhost:8080/api/locations/villages

# 3. Find village by name
curl http://localhost:8080/api/locations/village/Kiyovu

# 4. Get location hierarchy path
curl http://localhost:8080/api/locations/12/path
```

### Test Village-Based User Registration
```bash
# 1. Register user with village name
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123",
    "phone": "0781234567",
    "userType": "CLIENT",
    "villageName": "Kiyovu"
  }'

# 2. Get all users (shows full location hierarchy)
curl http://localhost:8080/api/users
```

### Test Requirement #3: Pagination & Sorting
```bash
# Get providers sorted by rating, page 0, size 10
curl "http://localhost:8080/api/providers?page=0&size=10&sortBy=rating&direction=desc"
```

### Test Requirement #4: Many-to-Many
```bash
# Add skill to provider
curl -X POST "http://localhost:8080/api/providers/1/skills?skillName=Plumbing"
```

### Test Requirement #7: existsBy()
```bash
# Check if email exists
curl "http://localhost:8080/api/users/exists/email?email=test@example.com"
```

### Test Requirement #8: Hierarchical Location Queries
```bash
# By province name (hierarchical traversal)
curl "http://localhost:8080/api/users/province/name/Kigali%20City"

# By district name (includes all child locations)
curl "http://localhost:8080/api/users/district/name/Gasabo"

# By village name (direct match)
curl "http://localhost:8080/api/users/village/name/Kiyovu"

# By complete location hierarchy (flexible)
curl "http://localhost:8080/api/users/location?provinceName=Kigali%20City&districtName=Gasabo&villageName=Kiyovu"
```

### Test Job Creation with Hierarchical Locations
```bash
# Create job with village location ID
curl -X POST "http://localhost:8080/api/jobs?clientId=1&categoryId=1&locationId=13" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Fix sink in Kiyovu",
    "description": "Urgent plumbing repair needed",
    "budget": 15000
  }'
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

- ✅ **Requirement #2**: Hierarchical location system with single Location table
- ✅ **Requirement #3**: Pagination & Sorting on all list endpoints  
- ✅ **Requirement #4**: Many-to-Many (Provider ↔ Skill)
- ✅ **Requirement #5**: One-to-Many (Location → User, Location → Job) with hierarchical relationships
- ✅ **Requirement #6**: One-to-One (User → ProviderProfile)
- ✅ **Requirement #7**: existsBy() methods for email and phone validation
- ✅ **Requirement #8**: Users by province with hierarchical location traversal (code, name, district, sector, cell, village)

## 🌟 **Key Features of Unified Hierarchical System**

### **Single Location Table**
- All administrative levels in one table: `locations`
- Self-referencing parent-child relationships
- Complete hierarchy: Province → District → Sector → Cell → Village

### **Village-Based User Registration**
- Users register by selecting village name
- Automatically linked to complete location hierarchy
- Full location context available: "Kigali City, Gasabo, Kimironko, Bibare, Nyagatovu"

### **Hierarchical Location Queries**
- Query users by any administrative level
- Automatic traversal of parent-child relationships
- Efficient location-based filtering for jobs and providers

### **Tree Data Structure Operations**
- Get children of any location
- Get full hierarchy path
- Navigate up and down the location tree
- Location statistics and analytics
