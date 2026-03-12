# Village-Based User Registration Guide

## 🏘️ Overview

The FixMatch system now supports **village-based user registration**. When users register by selecting their village, they are automatically linked to the complete location hierarchy:

**Province → District → Sector → Cell → Village**

This means users can be queried by any level of the hierarchy!

---

## 🚀 Quick Test Guide

### Step 1: Start the Application
```bash
mvn spring-boot:run
```

### Step 2: Get Available Villages
```bash
# Get all village names for dropdown
curl http://localhost:8080/api/locations/villages/names

# Get complete village information
curl http://localhost:8080/api/locations/villages
```

**Expected Response (village names):**
```json
[
  "Kiyovu",
  "Kimihurura", 
  "Kacyiru",
  "Nyakariro",
  "Bwishyura"
]
```

### Step 3: Register User by Village Name
```bash
curl -X POST "http://localhost:8080/api/users/register/village?villageName=Kiyovu" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Village Test User",
    "email": "village.test@example.com",
    "password": "password123",
    "phone": "0786789012",
    "userType": "CLIENT"
  }'
```

**Expected Response:**
```json
{
  "id": 6,
  "name": "Village Test User",
  "email": "village.test@example.com",
  "phone": "0786789012",
  "userType": "CLIENT",
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

### Step 4: Verify User Appears in Location Queries

**By Province:**
```bash
curl http://localhost:8080/api/users/province/code/KGL
```

**By District:**
```bash
curl http://localhost:8080/api/users/district/name/Gasabo
```

**By Village:**
```bash
curl http://localhost:8080/api/users/village/name/Kiyovu
```

**By Complete Hierarchy:**
```bash
curl "http://localhost:8080/api/users/location?provinceCode=KGL&districtName=Gasabo&sectorName=Kimisagara&cellName=Rugenge&villageName=Kiyovu"
```

All these queries should return the user you just registered!

---

## 📱 Postman Testing

### Import Collection
1. Open Postman
2. Import `FixMatch_Postman_Collection_Updated.json`
3. Look for **"2. Users - Location Hierarchy"** folder

### Test Requests
1. **"Get All Village Locations"** - See available villages
2. **"Register User by Village Name"** - Register with village selection
3. **"Get Users by Village Name"** - Verify user appears in village query
4. **"Get Users by Province CODE"** - Verify user appears in province query

---

## 🎯 Key Benefits

### For Frontend Development
- **Simple Dropdown**: Use `/api/locations/villages/names` for village selection
- **Complete Info**: Use `/api/locations/villages` for detailed village data
- **Automatic Linking**: No need to manage complex location relationships

### For Queries
- **Flexible Searching**: Find users by any location level
- **Hierarchical Queries**: Support province, district, sector, cell, village queries
- **Location Context**: Every user has complete location context

### For User Experience
- **Easy Registration**: Users just select their village
- **Accurate Location**: Automatic linking to complete hierarchy
- **Consistent Data**: No manual location entry errors

---

## 🔧 Available Endpoints

### Registration Endpoints
```http
# Basic registration (no location)
POST /api/users/register

# Registration with location ID
POST /api/users/register?locationId=1

# Registration by village name (RECOMMENDED)
POST /api/users/register/village?villageName=Kiyovu
```

### Location Helper Endpoints
```http
# Get all village locations
GET /api/locations/villages

# Get village names only
GET /api/locations/villages/names

# Search locations
GET /api/locations/search?keyword=Kiyovu
```

### User Query Endpoints
```http
# By province
GET /api/users/province/code/KGL
GET /api/users/province/name/Kigali

# By district
GET /api/users/district/name/Gasabo

# By sector
GET /api/users/sector/name/Kimisagara

# By cell
GET /api/users/cell/name/Rugenge

# By village
GET /api/users/village/name/Kiyovu

# By hierarchy
GET /api/users/location?provinceCode=KGL&districtName=Gasabo&villageName=Kiyovu
```

---

## ✅ Test Checklist

- [ ] Application starts successfully
- [ ] Village endpoints return data
- [ ] User registration by village works
- [ ] User appears in province queries
- [ ] User appears in village queries
- [ ] Location hierarchy is complete
- [ ] All query endpoints work

---

## 🐛 Troubleshooting

### "Village not found" Error
- Check available villages: `GET /api/locations/villages/names`
- Use exact village name from the list
- Village names are case-sensitive

### "Email already exists" Error
- Use a unique email address
- Check existing users: `GET /api/users`

### Empty Location Response
- Ensure DataSeeder ran successfully
- Check application logs for seeding messages
- Restart application if needed

---

**Village Registration is Ready!** 🏘️✅

Test it now and see how users get automatically linked to the complete location hierarchy!