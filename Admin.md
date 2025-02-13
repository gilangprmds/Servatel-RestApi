# Admin API Spec

# List Managaer
Endpoint : GET /users/managers
Response Body(success) :
```json
{
  "massages": "",
  "status": 200,
  "data": [
    {
      "ID": 1,
      "username": "manager123",
      "first-name": "manager",
      "last-name": "manager"
    },
    {
      "ID": 2,
      "username": "manager123",
      "first-name": "manager",
      "last-name": "manager"
    },
    {
      "ID": 3,
      "username": "manager123",
      "first-name": "manager",
      "last-name": "manager"
    }
  ],
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
Response Body(error) :
```json
{
    "path": "",
    "data": "",
    "success": false,
    "message": "Data Tidak Ditemukan",
    "error-code": "########",
    "status": 400,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```

# List Customer
Endpoint : GET /users/customers

Response Body :
```json
{
  "massages": "",
  "status": 200,
  "data": [
    {
      "ID": 1,
      "username": "user123",
      "first-name": "user",
      "last-name": "user"
    },
    {
      "ID": 2,
      "username": "user123",
      "first-name": "user",
      "last-name": "user"
    },
    {
      "ID": 3,
      "username": "user123",
      "first-name": "user",
      "last-name": "user"
    }
  ],
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
Response Body(error) :
```json
{
    "path": "",
    "data": "",
    "success": false,
    "message": "Data Tidak Ditemukan",
    "error-code": "########",
    "status": 400,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
# User Details
Endpoint : GET/users/{id}

Response Body(Succes):
```json
{
  "massages": "",
  "status": 200,
  "data": {
      "username": "manager123",
      "email": "manager123@gmail.com",
      "first-name": "manager",
      "last-name": "manager",
      "phone-number":"081212122121"
    },
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
Response Body(error) :
```json
{
    "path": "",
    "data": "",
    "success": false,
    "message": "Data Tidak Ditemukan",
    "error-code": "########",
    "status": 400,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
# User Edit
Endpoint : POST/users/edit/{id}

Request Body:
```json
{
  "username": "manager123",
  "email": "manager123@gmail.com",
  "first-name": "manager",
  "last-name": "manager",
  "phone-number":"081212122121"
}
```
Response Body(Succes):
```json
{
  "massages": "Data Berhasil Diubah",
  "status": 200,
  "data": "",
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
Response Body(error) :
```json
{
    "path": "",
    "data": "",
    "success": false,
    "message": "Data Gagal Diedit",
    "error-code": "########",
    "status": 400,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```

# Delete User
Endpoint : DELETE/users/{id}

Response Body(Succes):
```json
{
  "massages": "Data Berhasil Dihapus",
  "status": 200,
  "data": "",
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
Response Body(error) :
```json
{
    "path": "",
    "data": "",
    "success": false,
    "message": "Data Gagal Dihapus",
    "error-code": "########",
    "status": 400,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```

# List Hotel
Endpoint : GET/admin/hotels

Response Body(success) :
```json
{
  "massages": "",
  "status": 200,
  "data": [
    {
      "Id": 1,
      "name": "hotel1",
      "hotel-manager": "manager1"
    },
    {
      "Id": 2,
      "name": "hotel2",
      "hotel-manager": "manager2"
    },
    {
      "Id": 3,
      "name": "hotel3",
      "hotel-manager": "manager3"
    }
  ],
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
Response Body(error) :
```json
{
    "path": "",
    "data": "",
    "success": false,
    "message": "Data Tidak Ditemukan",
    "error-code": "########",
    "status": 400,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```

# Hotel Details
Endpoint : GET/admin/hotels/{id}

Response Body(Succes):

```json
{
  "massages": "",
  "status": 200,
  "data": {
    "name": "hotel1",
    "address": {
      "id": 1,
      "street-name": "Jl. Makmur",
      "city": "Jakarta",
      "country": "Indonesia"
    },
    "rooms": [
      {
        "id": 1,
        "room-type": "STANDARD_ROOM",
        "room-count": 2,
        "price-per-night": 100000
      },
      {
        "id": 2,
        "room-type": "DELUX_DOUBLE",
        "room-count": 2,
        "price-per-night": 200000
      }
    ],
    "user": {
      "id": 1,
      "name": "manager1"
    },
    "description": "hotel asikkkk"
  },
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
Response Body(error) :
```json
{
    "path": "",
    "data": "",
    "success": false,
    "message": "Data Tidak Ditemukan",
    "error-code": "########",
    "status": 400,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```

# Edit Hotel
Endpoint : PUT/hotels/edit/{id}

Request Body :
```json
{
  "name": "hotel1",
  "address": {
    "id": 1,
    "street-name": "Jl. Makmur",
    "city": "Jakarta",
    "country": "Indonesia"
  },
  "rooms": [
    {
      "id": 1,
      "room-type": "STANDARD_ROOM",
      "room-count": 2,
      "price-per-night": 100000
    },
    {
      "id": 2,
      "room-type": "DELUX_DOUBLE",
      "room-count": 2,
      "price-per-night": 200000
    }
  ],
  "description": "hotel asikkkk"
}
```
Response Body(Succes):
```json
{
  "massages": "Data Berhasil Diubah",
  "status": 200,
  "data": "",
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
Response Body(error) :
```json
{
    "path": "",
    "data": "",
    "success": false,
    "message": "Data Gagal Diedit",
    "error-code": "########",
    "status": 400,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```

# Delete Hotel
Endpoint : DELETE/admin/hotels/{id}

Response Body(Succes):
```json
{
  "massages": "",
  "status": 200,
  "data": "",
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
Response Body(error) :
```json
{
    "path": "",
    "data": "",
    "success": false,
    "message": "Data Gagal Dihapus",
    "error-code": "########",
    "status": 400,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```

# List Bookings
Endpoint : GET/admin/bookings

Response Body(success) :

```json
{
  "massages": "",
  "status": 200,
  "data": [
    {
      "Id": 1,
      "booking-date": "2025-01-27T17:08:25.542+00:00",
      "hotel": {
        "id": 1,
        "hotel-name": "hotel1"
      },
      "user":{
        "id": 1,
        "user-name": "customer1"
      },
      "payment": {
        "id": 1,
        "total-price": 10000000
      }
    },
    {
      "Id": 2,
      "booking-date": "2025-01-27T17:08:25.542+00:00",
      "hotel": {
        "id": 2,
        "hotel-name": "hotel2"
      },
      "user":{
        "id": 2,
        "user-name": "customer2"
      },
      "payment": {
        "id": 2,
        "total-price": 10000000
      }
    },
    {
      "Id": 3,
      "booking-date": "2025-01-27T17:08:25.542+00:00",
      "hotel": {
        "id": 3,
        "hotel-name": "hotel3"
      },
      "user":{
        "id": 3,
        "user-name": "customer3"
      },
      "payment": {
        "id": 3,
        "total-price": 10000000
      }
    }
  ],
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
Response Body(error) :
```json
{
    "path": "",
    "data": "",
    "success": false,
    "message": "Data Tidak Ditemukan",
    "error-code": "########",
    "status": 400,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```

# Booking Details
Endpoint : GET/admin/bookings/{id}

Response Body(Success):

```json
{
  "massages": "",
  "status": 200,
  "data": {
    "confirmation-number": 12312421,
    "hotel": {
      "id": 1,
      "name": "hotel1",
      "address": {
        "street-name": "jl.makur",
        "city": "jakarta",
        "country": "indonesia"
      }
    },
    "checkin-date": "2025-01-15",
    "checkout-date": "2025-01-15",
    "payment": {
      "id": 1,
      "payment-methode": "CREDIT_CARD",
      "payment-status": "COMPLETED"
    },
    "user": {
      "name": "customer1",
      "email": "customer1@email.com"
    }
  },
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```
Response Body(error) :
```json
{
    "path": "",
    "data": "",
    "success": false,
    "message": "Data Tidak Ditemukan",
    "error-code": "########",
    "status": 400,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```