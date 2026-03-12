# Hierarchical Location System Guide

## 🏗️ **Overview**

The new hierarchical location system implements the **Adjacency List Model** for representing geographical hierarchies. This is a much more flexible and scalable approach than the previous flat structure.

## 🌳 **Hierarchy Structure**

```
Province (Root)
└── District
    └── Sector
        └── Cell
            └── Village (Leaf)
```

**Example:**
```
Kigali City (Province)
└── Gasabo (District)
    └── Kimironko (Sector)
        └── Bibare (Cell)
            └── Nyagatovu (Village)
```

## 🚀 **Getting Started**

### **Step 1: Initialize the Hierarchy**
```bash
POST http://localhost:8080/api/hierarchical-locations/initialize
```
This creates the complete Rwanda location hierarchy.

### **Step 2: Test the Endpoints**

#### **Get All Provinces**
```bash
GET http://localhost:8080/api/hierarchical-locations/provinces
```

#### **Get All Villages**
```bash
GET http://localhost:8080/api/hierarchical-locations/villages
```

#### **Get Location with Full Path**
```bash
GET http://localhost:8080/api/hierarchical-locations/1
```

#### **Get Children of a Location**
```bash
GET http://localhost:8080/api/hierarchical-locations/1/children
```

#### **Search Locations**
```bash
GET http://localhost:8080/api/hierarchical-locations/search?name=Kigali
```

## 🎯 **Key Features**

### **1. Self-Referencing Relationship**
- Each location can have a parent location
- Each location can have multiple child locations
- Creates a tree structure

### **2. Full Path Generation**
- Automatically generates paths like: "Kigali City → Gasabo → Kimironko → Bibare → Nyagatovu"
- Shows complete hierarchy from root to current location

### **3. Tree Navigation**
- Get all ancestors (path to root)
- Get all descendants (all children recursively)
- Find specific location types in hierarchy

### **4. Flexible Queries**
- Find by location type (PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE)
- Search by name
- Get statistics by level

## 📊 **Database Design**

### **Table: hierarchical_locations**
```sql
CREATE TABLE hierarchical_locations (
    location_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10),
    type VARCHAR(20) NOT NULL,
    parent_location_id BIGINT REFERENCES hierarchical_locations(location_id)
);
```

### **Sample Data**
```
ID | Name        | Code | Type     | Parent_ID
1  | Kigali City | KGL  | PROVINCE | NULL
2  | Gasabo      | GAS  | DISTRICT | 1
3  | Kimironko   | KIM  | SECTOR   | 2
4  | Bibare      | BIB  | CELL     | 3
5  | Nyagatovu   | NYG  | VILLAGE  | 4
```

## 🔧 **Advantages**

### **1. Flexibility**
- Easy to add new location levels
- Can represent any geographical hierarchy
- No need to change database structure for new levels

### **2. Scalability**
- Single table handles all location data
- Efficient storage and querying
- Supports unlimited hierarchy depth

### **3. Data Integrity**
- Referential integrity through foreign keys
- Consistent location relationships
- Prevents orphaned locations

### **4. Query Efficiency**
- Tree traversal using recursive CTEs
- Fast parent-child lookups
- Indexed relationships

## 🧪 **Testing the System**

### **1. Initialize Data**
```bash
curl -X POST http://localhost:8080/api/hierarchical-locations/initialize
```

### **2. Get Statistics**
```bash
curl http://localhost:8080/api/hierarchical-locations/statistics
```

### **3. Find a Village**
```bash
curl http://localhost:8080/api/hierarchical-locations/village/Nyagatovu
```

### **4. Get Full Path**
```bash
curl http://localhost:8080/api/hierarchical-locations/5/path
```

## 🎯 **Integration with User Registration**

The system can be integrated with user registration to:
1. Allow users to select their village
2. Automatically link to complete hierarchy
3. Enable queries by any location level
4. Provide full address information

## 📈 **Future Enhancements**

1. **Caching**: Add Redis caching for frequently accessed paths
2. **Materialized Paths**: Store pre-computed paths for faster queries
3. **Nested Sets**: Alternative model for read-heavy workloads
4. **Geographic Coordinates**: Add latitude/longitude for mapping
5. **Localization**: Support multiple languages for location names

This hierarchical location system provides a solid foundation for scalable geographical data management! 🌍✅