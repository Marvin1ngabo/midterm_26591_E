# Village-Based User Registration Guide - Hierarchical Location System

## 🏘️ Overview

The FixMatch system now supports **advanced hierarchical location-based user registration** using the **Adjacency List Model**. When users register by selecting their village, they are automatically linked to the complete location hierarchy:

**Province → District → Sector → Cell → Village**

This demonstrates sophisticated tree data structures and self-referencing relationships in Spring Boot!

---

## 🌳 **Hierarchical Location Architecture**

### **Design Pattern: Adjacency List Model**
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
- ✅ **Self-referencing relationships** (parent-child)
- ✅ **Tree data structure** with recursive operations
- ✅ **Full path generation** and navigation
- ✅ **Village-based user registration** with automatic hierarchy mapping
- ✅ **Flexible queries** at any hierarchy level

---

## 🚀 Quick Test Guide

### Step 1: Initialize Hierarchical Location System
```bash
# Initialize Rwanda administrative hierarchy
curl -X POST http://localhost:8080/api/hierarchical-locations/initialize
```

### Step 2: Start the Application
```bash
mvn spring-boot:run
```

### Step 3: Get Available Villages for Registration
```bash
# Get all village locations (hierarchical system)
curl http://localhost:8080/api/hierarchical-locations/villages

# Get village names only
curl http://localhost:8080/api/hierarchical-locations/villages/names

# Legacy system (backward compatibility)
curl http://localhost:8080/api/locations/villages/names
```

**Expected Response (hierarchical villages):**
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

### Step 4: Register User with Hierarchical Village Selection

#### Method 1: Village Name in Request Body (RECOMMENDED)
```bash
curl -X POST "http://localhost:8080/api/users/register" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Hierarchical Test User",
    "email": "hierarchical.test@example.com",
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
  "id": 6,
  "name": "Hierarchical Test User",
  "email": "hierarchical.test@example.com",
  "phone": "0786789012",
  "userType": "CLIENT",
  "hierarchicalLocation": {
    "locationId": 12,
    "name": "Kiyovu",
    "code": "KIY",
    "type": "VILLAGE",
    "fullPath": "Kigali City → Gasabo → Kimisagara → Rugenge → Kiyovu",
    "formattedAddress": "Kigali City, Gasabo, Kimisagara, Rugenge, Kiyovu"
  },
  "fullLocation": "Kigali City, Gasabo, Kimisagara, Rugenge, Kiyovu"
}
```

### Step 5: Verify Hierarchical Location Queries

**Get Users by Province (Root Level):**
```bash
curl http://localhost:8080/api/hierarchical-locations/1/users
```

**Get Users by District:**
```bash
curl http://localhost:8080/api/hierarchical-locations/4/users
```

**Get Users by Village (Leaf Level):**
```bash
curl http://localhost:8080/api/hierarchical-locations/12/users
```

**Get Full Location Hierarchy Path:**
```bash
curl http://localhost:8080/api/hierarchical-locations/12/path
# Response: "Kigali City → Gasabo → Kimisagara → Rugenge → Kiyovu"
```

**Get Location Children (Tree Navigation):**
```bash
# Get districts under Kigali City
curl http://localhost:8080/api/hierarchical-locations/1/children

# Get sectors under Gasabo
curl http://localhost:8080/api/hierarchical-locations/4/children
```

---

## 📱 Postman Testing

### Import Collection
1. Open Postman
2. Import `FixMatch_Postman_Collection_Updated.json`
3. Look for **"0. Hierarchical Locations (NEW)"** folder

### Test Requests (In Order)
1. **"Initialize Rwanda Hierarchy"** - Set up the hierarchical system
2. **"Get All Villages (Leaf Nodes)"** - See available villages for registration
3. **"Register User with Village (Body)"** - Register with village in request body
4. **"Register User with Village (Param)"** - Register with village as parameter
5. **"Get Location with Full Path"** - See complete hierarchy path
6. **"Get Users by Location"** - Verify users appear in location queries

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
# Initialize Rwanda administrative hierarchy
POST /api/hierarchical-locations/initialize

# Get hierarchy statistics
GET /api/hierarchical-locations/statistics
```

### Tree Navigation
```http
# Get all provinces (root nodes)
GET /api/hierarchical-locations/provinces

# Get all villages (leaf nodes) 
GET /api/hierarchical-locations/villages

# Get children of a location
GET /api/hierarchical-locations/{id}/children

# Get full path of a location
GET /api/hierarchical-locations/{id}/path
```

### Location Queries
```http
# Get location by ID with full hierarchy info
GET /api/hierarchical-locations/{id}

# Get locations by type
GET /api/hierarchical-locations/type/VILLAGE
GET /api/hierarchical-locations/type/DISTRICT

# Search locations by name
GET /api/hierarchical-locations/search?name=Kigali

# Find village by name (for registration)
GET /api/hierarchical-locations/village/{villageName}
```

### User Registration with Hierarchical Locations
```http
# Register with village in request body (RECOMMENDED)
POST /api/users/register
Body: { "villageName": "Kiyovu", ... }

# Register with village as parameter
POST /api/users/register/village?villageName=Kiyovu

# Get users by hierarchical location
GET /api/hierarchical-locations/{locationId}/users
```

### Legacy Location Endpoints (Backward Compatibility)
```http
# Legacy flat location system
GET /api/locations/villages
GET /api/locations/villages/names
POST /api/users/register?locationId=1
```

---

## ✅ Test Checklist

- [ ] Application starts successfully
- [ ] Hierarchical locations are initialized
- [ ] Village endpoints return hierarchical data
- [ ] User registration with village works (both methods)
- [ ] Users appear in hierarchical location queries
- [ ] Full path generation works
- [ ] Tree navigation (children/parent) works
- [ ] Legacy system still works for backward compatibility

---

## 🐛 Troubleshooting

### "Village not found" Error
- Initialize hierarchy first: `POST /api/hierarchical-locations/initialize`
- Check available villages: `GET /api/hierarchical-locations/villages`
- Use exact village name from the hierarchical system

### "Hierarchical location not set" Error
- Ensure village exists in hierarchical system
- Check if DataSeeder ran successfully
- Verify hierarchical location repository has data

### Empty Hierarchy Response
- Run initialization endpoint first
- Check application logs for seeding messages
- Restart application if needed

### Legacy vs Hierarchical Confusion
- **Hierarchical System**: `/api/hierarchical-locations/*` (NEW)
- **Legacy System**: `/api/locations/*` (Backward compatibility)
- Use hierarchical system for new features

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

**Hierarchical Village Registration is Ready!** 🌳✅

Experience the power of tree data structures in a real Spring Boot application!