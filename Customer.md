# Customer API Spec


# Search Hotel List
Endpoint : GET/search
Request Param : City, CheckinDate, Checkoutdate, Roomcount, GuestCount

Response Body(success):

```json
{
  "massages": "",
  "status": 200,
  "data": {
    "duration": "",
    "checkin-date": "",
    "checkout-date": "",
    "hotel": [
      {
        "Id": 1,
        "name": "hotel1",
        "rooms": [
          {
            "room-type": "STANDARD_ROOM",
            "room-price": 100000,
            "available-room": "DELUX_DOUBLE"
          }
        ]
      },
      {
        "Id": 2,
        "name": "hotel2",
        "rooms": [
          {
            "room-type": "STANDARD_ROOM",
            "room-price": 1000000,
            "available-room": "10"
          },
          {
            "room-type": "DELUX_ROOM",
            "room-price": 100000,
            "available-room": "20"
          }
        ]
      },
      {
        "Id": 3,
        "name": "hotel3",
        "rooms": [
          {
            "room-type": "STANDARD_ROOM",
            "room-price": 1000000,
            "available-room": "10"
          },
          {
            "room-type": "DELUX_ROOM",
            "room-price": 100000,
            "available-room": "20"
          }
        ]
      }
    ],
    "success": true,
    "timestamp": "2025-01-27T17:08:25.542+00:00"
  }
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
Endpoint : GET/hotel-details/{id}
Request Param : Checkindate, checkoutdate

Response Body(success):
```json
{
  "massages": "",
  "status": 200,
  "data": {
    "name": "hotel2",
    "rooms": [
      {
        "room-type": "STANDARD_ROOM",
        "room-price": 1000000,
        "available-room": "10"
      },
      {
        "room-type": "DELUX_ROOM",
        "room-price": 100000,
        "available-room": "20"
      }
    ]
  },
  "success": true,
  "timestamp": "2025-01-27T17:08:25.542+00:00"
}
```