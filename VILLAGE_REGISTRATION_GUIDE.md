# Village-Based User Registration Guide - Unified Hierarchical Location System

## 🏘️ Overview

The FixMatch system features a **unified hierarchical location system** using the **Adjacency List Model**. When users register by selecting their village, they are automatically linked to the complete location hierarchy:

**Province → District → Sector → Cell → Village**

This demonstrates sophisticated tree data structures and self-referencing relationships in Spring Boot using a single Location table!

---

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
- ✅ **Hierarchical queries** at any level

---

## 🚀 Quick Test Guide

### Step 1: Start the Application (Auto-Initialization)
```bash
mvn spring-boot:run
```
**Note:** The hierarchical location system is automatically initialized during startup!

### Step 2: Get Available Villages for Registration
```bash
# Get all village locations
curl http://localhost:8080/api/locations/villages

# Get village names only (for dropdown)
curl http://localhost:8080/api/locations/villages/names
```

**Expected Response (hierarchical villages):**
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

### Step 3: Register User with Village Selection

#### Method 1: Village Name in Request Body (RECOMMENDED)
```bash
curl -X POST "http://localhost:8080/api/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Village User Test",
    "email": "village.user@example.com",
    "password": "password123",
    "phone": "0786789012",
    "userType": "CLIENT",
    "villageName": "Kiyovu"
  }'
```

#### Method 2: Village Name as Parameter
```bash
curl -X POST "http://localhost:8080/api/users/register/village?villageName=Nyagatovu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Village Param User",
    "email": "village.param@example.com",
    "password": "password123",
    "phone": "0786789013",
    "userType": "PROVIDER"
  }'
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

### Step 4: Verify Hierarchical Location Queries

**Get Users by Province (Hierarchical Traversal):**
```bash
curl "http://localhost:8080/api/users/province/name/Kigali%20City"
```

**Get Users by District (Includes Child Locations):**
```bash
curl "http://localhost:8080/api/users/district/name/Gasabo"
```

**Get Users by Village (Direct Match):**
```bash
curl "http://localhost:8080/api/users/village/name/Kiyovu"
```

**Get Full Location Hierarchy Path:**
```bash
curl http://localhost:8080/api/locations/12/path
# Response: "Kigali City → Gasabo → Kimisagara → Rugenge → Kiyovu"
```

**Get Location Children (Tree Navigation):**
```bash
# Get districts under Kigali City
curl http://localhost:8080/api/locations/1/children

# Get sectors under Gasabo
curl http://localhost:8080/api/locations/4/children
```

---

## 📱 Postman Testing

### Import Collection
1. Open Postman
2. Import `FixMatch_Postman_Collection_Updated.json`
3. Look for **"0. Hierarchical Locations (NEW)"** folder

### Test Requests (In Order)
1. **"Get All Provinces (Root Nodes)"** - See the hierarchical structure
2. **"Get All Villages (Leaf Nodes)"** - See available villages for registration
3. **"Register User with Village Name (Recommended)"** - Register with village in request body
4. **"Register User with Village (Separate Endpoint)"** - Register with village as parameter
5. **"Get Location by ID with Full Hierarchy"** - See complete hierarchy path
6. **"Get Users by Village Name"** - Verify users appear in location queries

---

## 🎯 Key Benefits

### For Computer Science Learning
- **Tree Data Structures**: Real-world implementation of hierarchical data
- **Adjacency List Model**: Database pattern for representing trees
- **Self-Referencing Relationships**: Advanced JPA relationship mapping
- **Recursive Operations**: Path building and tree traversal algorithms

### For Frontend Development
- **Simple Village Selection**: Use hierarchical villages endpoint for dropdown
- **Complete Hierarchy Info**: Get full path and formatted address
- **Flexible Navigation**: Navigate up/down the location tree
- **Automatic Linking**: No need to manage complex location relationships

### For Queries and Analytics
- **Multi-Level Queries**: Find users at any hierarchy level
- **Tree Navigation**: Move up/down the location hierarchy
- **Path Generation**: Get complete location paths
- **Scalable Design**: Add new hierarchy levels without schema changes

---

## 🔧 Available Hierarchical Endpoints

### Initialization and Setup
```http
# Get hierarchy statistics (auto-initialized)
GET /api/locations/statistics
```

### Tree Navigation
```http
# Get all provinces (root nodes)
GET /api/locations/provinces

# Get all villages (leaf nodes) 
GET /api/locations/villages

# Get children of a location
GET /api/locations/{id}/children

# Get full path of a location
GET /api/locations/{id}/path
```

### Location Queries
```http
# Get location by ID with full hierarchy info
GET /api/locations/{id}

# Get locations by type
GET /api/locations/type/VILLAGE
GET /api/locations/type/DISTRICT

# Search locations by name
GET /api/locations/search?name=Kigali

# Find village by name (for registration)
GET /api/locations/village/{villageName}
```

### User Registration with Hierarchical Locations
```http
# Register with village in request body (RECOMMENDED)
POST /api/users/register
Body: { "villageName": "Kiyovu", ... }

# Register with village as parameter
POST /api/users/register/village?villageName=Kiyovu

# Register with DTO
POST /api/users/register/dto
```

### Hierarchical User Queries
```http
# Get users by province (hierarchical traversal)
GET /api/users/province/name/{provinceName}

# Get users by district (includes child locations)
GET /api/users/district/name/{districtName}

# Get users by village (direct match)
GET /api/users/village/name/{villageName}

# Flexible location hierarchy query
GET /api/users/location?provinceName=X&districtName=Y&villageName=Z
```

---

## ✅ Test Checklist

- [ ] Application starts successfully with hierarchical location seeding
- [ ] Village endpoints return hierarchical data with full paths
- [ ] User registration with village works (both methods)
- [ ] Users appear in hierarchical location queries
- [ ] Full path generation works (Province → District → Sector → Cell → Village)
- [ ] Tree navigation (children/parent) works
- [ ] Hierarchical queries work at all levels (province, district, sector, cell, village)

---

## 🐛 Troubleshooting

### "Village not found" Error
- Check available villages: `GET /api/locations/villages`
- Use exact village name from the unified system
- Ensure DataSeeder ran successfully during startup

### "Location not set" Error
- Ensure village exists in hierarchical system
- Check application logs for "Database seeding completed successfully!"
- Verify location repository has data

### Empty Hierarchy Response
- Check application startup logs for seeding messages
- Restart application if needed
- Verify database connection is working

### Query Returns No Users
- Ensure users are registered with village names
- Check that hierarchical queries are using correct traversal logic
- Verify location relationships are properly established

---

## 🎓 **Computer Science Concepts Demonstrated**

### 1. **Tree Data Structure**
- **Nodes**: Each location is a node
- **Edges**: Parent-child relationships
- **Root**: Provinces (no parent)
- **Leaves**: Villages (no children)
- **Depth**: Distance from root (Province=0, Village=4)

### 2. **Adjacency List Model**
- Each node stores reference to its parent
- Enables efficient tree traversal
- Supports recursive operations
- Scalable for any tree depth

### 3. **Self-Referencing Relationships**
- Entity references itself via foreign key
- `@ManyToOne` for parent relationship
- `@OneToMany` for children relationship
- Demonstrates advanced JPA mapping

### 4. **Recursive Algorithms**
- **Path Building**: Traverse from node to root
- **Tree Navigation**: Find ancestors/descendants
- **Depth Calculation**: Count levels from root

---

**Unified Hierarchical Village Registration is Ready!** 🌳✅

Experience the power of tree data structures in a real Spring Boot application with a clean, single-table architecture!