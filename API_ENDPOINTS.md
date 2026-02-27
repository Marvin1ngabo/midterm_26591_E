# FixMatch API Endpoints Documentation

Base URL: `http://localhost:8080`

## 📍 Location Endpoints

### Provinces

#### Create Province (Requirement #2)
```http
POST /api/locations/provinces
Content-Type: application/json

{
  "code": "KGL",
  "name": "Kigali"
}
```

#### Get All Provinces
```http
GET /api/locations/provinces
```

#### Get Province by Code
```http
GET /api/locations/provinces/code/KGL
```

#### Check if Province Exists (Requirement #7)
```http
GET /api/locations/provinces/exists/KGL
Response: true/false
```

### Districts

#### Create District (Requirement #2)
```http
POST /api/locations/districts?provinceId=1
Content-Type: application/json

{
  "name": "Gasabo"
}
```

#### Get Districts by Province
```http
GET /api/locations/provinces/1/districts
```

#### Get Districts with Pagination (Requirement #3)
```http
GET /api/locations/provinces/1/districts/paginated?page=0&size=10
```

---

## 👤 User Endpoints

#### Register User
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
POST /api/jobs?clientId=1&categoryId=1&districtId=1
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
# 1. Create Province
curl -X POST http://localhost:8080/api/locations/provinces \
  -H "Content-Type: application/json" \
  -d '{"code":"KGL","name":"Kigali"}'

# 2. Create District
curl -X POST http://localhost:8080/api/locations/districts?provinceId=1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Gasabo"}'
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

### Test Requirement #8: Users by Province
```bash
# By province code
curl http://localhost:8080/api/users/province/code/KGL

# By province name
curl http://localhost:8080/api/users/province/name/Kigali
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
- ✅ Requirement #5: One-to-Many (Province → District)
- ✅ Requirement #6: One-to-One (User → ProviderProfile)
- ✅ Requirement #7: existsBy() methods
- ✅ Requirement #8: Users by province (code & name)
